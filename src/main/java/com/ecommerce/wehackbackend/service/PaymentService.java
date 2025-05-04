package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.exception.ResourceNotFoundException;
import com.ecommerce.wehackbackend.exception.StripeOperationException;
import com.ecommerce.wehackbackend.model.dto.request.PaymentRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.PaymentResponseDto;
import com.ecommerce.wehackbackend.model.dto.response.PaymentStatusResponseDto;
import com.ecommerce.wehackbackend.model.entity.Order;
import com.ecommerce.wehackbackend.model.entity.Payment;
import com.ecommerce.wehackbackend.model.entity.User;
import com.ecommerce.wehackbackend.repository.OrderRepository;
import com.ecommerce.wehackbackend.repository.PaymentRepository;
import com.ecommerce.wehackbackend.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Log4j2
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;


    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto paymentRequest) {
        log.info("Creating payment for Order ID: {}", paymentRequest.getOrderId());
        if (!"kzt".equalsIgnoreCase(paymentRequest.getCurrency())) {
            throw new IllegalArgumentException("Only KZT (Kazakhstani Tenge) payments are accepted");
        }

        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + paymentRequest.getOrderId()));
        log.info("Found order with ID: {}", order.getId());

        User user = userRepository.findById(paymentRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + paymentRequest.getUserId()));
        log.info("Found user with ID: {}", user.getId());

        try {
            // Create Stripe PaymentIntent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(convertToKZT(paymentRequest.getAmount()))  // No need to multiply by 100 for KZT
                    .setCurrency("kzt") // Ensure the currency is "kzt"
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .putMetadata("order_id", order.getId().toString())
                    .putMetadata("user_id", user.getId().toString())
                    .build();
            log.info("Creating PaymentIntent with amount: {}, currency: {}", paymentRequest.getAmount(), paymentRequest.getCurrency());

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            log.info("Created PaymentIntent with ID: {}", paymentIntent.getId());

            // Save payment to database
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setUser(user);
            payment.setAmount(paymentRequest.getAmount());
            payment.setCurrency(paymentRequest.getCurrency().toUpperCase());
            payment.setStatus(paymentIntent.getStatus());
            payment.setTransactionReference(paymentIntent.getId());
            payment.setPaidAt(LocalDateTime.now());

            Payment savedPayment = paymentRepository.save(payment);

            return new PaymentResponseDto(
                    savedPayment.getId(),
                    paymentIntent.getClientSecret(),
                    savedPayment.getStatus(),
                    savedPayment.getTransactionReference(),
                    Instant.now()
            );
        } catch (StripeException e) {
            log.error("Error creating payment intent: ", e);
            throw new StripeOperationException("Failed to create payment intent", e);
        }
    }


    @Transactional(readOnly = true)
    public PaymentStatusResponseDto getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("payment", "paymentId", paymentId.toString()));

        return new PaymentStatusResponseDto(
                payment.getId(),
                payment.getStatus(),
                payment.getPaidAt().toInstant(ZoneOffset.UTC),
                payment.getTransactionReference(),
                payment.getPaymentMethod()
        );
    }

    @Transactional
    public void handleStripeWebhook(String payload, String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            if (event.getDataObjectDeserializer().getObject().isEmpty()) {
                throw new StripeOperationException("Failed to deserialize event data", null);
            }

            StripeObject stripeObject = event.getDataObjectDeserializer().getObject().get();

            switch (event.getType()) {
                case "payment_intent.succeeded":
                    handlePaymentIntentSucceeded((PaymentIntent) stripeObject);
                    break;
                case "payment_intent.payment_failed":
                    handlePaymentIntentFailed((PaymentIntent) stripeObject);
                    break;
                case "payment_intent.processing":
                    handlePaymentIntentProcessing((PaymentIntent) stripeObject);
                    break;
                default:
                    log.info("Unhandled event type: {}", event.getType());
            }
        } catch (Exception e) {
            throw new StripeOperationException("Failed to process webhook", e);
        }
    }

    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        String paymentMethodType = paymentIntent.getPaymentMethod() != null ?
                paymentIntent.getPaymentMethod() : null;
        updatePaymentStatus(
                paymentIntent.getId(),
                paymentIntent.getStatus(),
                paymentMethodType
        );
    }

    private void handlePaymentIntentFailed(PaymentIntent paymentIntent) {
        updatePaymentStatus(paymentIntent.getId(), paymentIntent.getStatus(), null);
    }

    private void handlePaymentIntentProcessing(PaymentIntent paymentIntent) {
        updatePaymentStatus(paymentIntent.getId(), paymentIntent.getStatus(), null);
    }

    private void updatePaymentStatus(String transactionReference, String status, String paymentMethod) {
        paymentRepository.findByTransactionReference(transactionReference)
                .ifPresent(payment -> {
                    payment.setStatus(status);
                    if (paymentMethod != null) {
                        payment.setPaymentMethod(paymentMethod);
                    }
                    if ("succeeded".equals(status)) {
                        payment.setPaidAt(LocalDateTime.now());
                    }
                    paymentRepository.save(payment);
                });
    }

    private long convertToKZT(Double amount) {
        amount = amount * 100; // Convert to KZT by multiplying by 100
        if (amount < 50) {
            throw new IllegalArgumentException("Amount must be at least 50 KZT.");
        }

        return (long) amount.doubleValue();
    }
}
