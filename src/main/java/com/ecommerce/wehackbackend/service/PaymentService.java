package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.exception.ResourceNotFoundException;
import com.ecommerce.wehackbackend.mapper.PaymentMapper;
import com.ecommerce.wehackbackend.model.dto.request.PaymentRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.PaymentGatewayResponseDto;
import com.ecommerce.wehackbackend.model.dto.response.PaymentGatewayStatusDto;
import com.ecommerce.wehackbackend.model.dto.response.PaymentResponseDto;
import com.ecommerce.wehackbackend.model.dto.response.PaymentStatusResponseDto;
import com.ecommerce.wehackbackend.model.entity.Order;
import com.ecommerce.wehackbackend.model.entity.Payment;
import com.ecommerce.wehackbackend.model.entity.User;
import com.ecommerce.wehackbackend.repository.OrderRepository;
import com.ecommerce.wehackbackend.repository.PaymentRepository;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final StripePaymentService paymentGatewayService;
    private final PaymentMapper paymentMapper;


    @Transactional
    public PaymentResponseDto initiatePayment(PaymentRequestDto paymentRequest, User user) {
        if (user == null) {
            throw new BadRequestException("User is not authenticated");
        }

        if (!user.getId().equals(paymentRequest.getUserId())) {
            throw new BadRequestException("User ID in request does not match authenticated user");
        }

        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("order", "orderId", paymentRequest.getOrderId().toString()));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Order does not belong to this user");
        }

        if (Math.abs(order.getTotalPrice() - paymentRequest.getAmount()) > 0.01) {
            throw new BadRequestException("Payment amount does not match order total");
        }

        if (paymentRepository.existsByOrderId(order.getId())) {
            throw new BadRequestException("Payment already initiated for this order");
        }

        // Create payment record
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setUser(user);
        payment.setAmount(paymentRequest.getAmount());  // Use amount from request
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setStatus("pending");

        Payment savedPayment = paymentRepository.save(payment);

        // Initiate payment with gateway
        PaymentGatewayResponseDto gatewayResponse = paymentGatewayService.initiatePayment(
                order.getId(),
                paymentRequest.getAmount(),  // Use amount from request
                paymentRequest.getCurrency(),
                paymentRequest.getPaymentMethod()
        );

        // Update payment with gateway reference
        payment.setTransactionReference(gatewayResponse.getTransactionId());
        payment.setPaymentUrl(gatewayResponse.getPaymentUrl());
        paymentRepository.save(payment);

        // Prepare response
        PaymentResponseDto response = paymentMapper.toDto(savedPayment);
        response.setPaymentUrl(gatewayResponse.getPaymentUrl());
        response.setStatus("pending");

        return response;
    }


    @Transactional(readOnly = true)
    public PaymentStatusResponseDto getPaymentStatus(Long paymentId, User user) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("paymenyt", "paymentId", paymentId.toString()));

        // Verify payment belongs to user
        if (!payment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Payment does not belong to this user");
        }

        // Check with payment gateway for latest status
        PaymentGatewayStatusDto gatewayStatus = paymentGatewayService.checkPaymentStatus(
                payment.getTransactionReference()
        );

        // Update payment status if changed
        if (!payment.getStatus().equals(gatewayStatus.getStatus())) {
            payment.setStatus(gatewayStatus.getStatus());
            if ("success".equals(gatewayStatus.getStatus())) {
                payment.setPaidAt(LocalDateTime.now());
            }
            paymentRepository.save(payment);
        }

        // Prepare response
        return PaymentStatusResponseDto.builder()
                .status(payment.getStatus())
                .transactionId(payment.getTransactionReference())
                .processedAt(payment.getPaidAt())
                .receiptUrl(gatewayStatus.getReceiptUrl())
                .build();
    }

//    @Transactional
//    public void handleStripeWebhook(String payload, String sigHeader) {
//        try {
//            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
//
//            if (event.getDataObjectDeserializer().getObject().isEmpty()) {
//                throw new StripeOperationException("Failed to deserialize event data", null);
//            }
//
//            StripeObject stripeObject = event.getDataObjectDeserializer().getObject().get();
//
//            switch (event.getType()) {
//                case "payment_intent.succeeded":
//                    handlePaymentIntentSucceeded((PaymentIntent) stripeObject);
//                    break;
//                case "payment_intent.payment_failed":
//                    handlePaymentIntentFailed((PaymentIntent) stripeObject);
//                    break;
//                case "payment_intent.processing":
//                    handlePaymentIntentProcessing((PaymentIntent) stripeObject);
//                    break;
//                default:
//                    log.info("Unhandled event type: {}", event.getType());
//            }
//        } catch (Exception e) {
//            throw new StripeOperationException("Failed to process webhook", e);
//        }
//    }
//
//    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
//        String paymentMethodType = paymentIntent.getPaymentMethod() != null ?
//                paymentIntent.getPaymentMethod() : null;
//        updatePaymentStatus(
//                paymentIntent.getId(),
//                paymentIntent.getStatus(),
//                paymentMethodType
//        );
//    }
//
//    private void handlePaymentIntentFailed(PaymentIntent paymentIntent) {
//        updatePaymentStatus(paymentIntent.getId(), paymentIntent.getStatus(), null);
//    }
//
//    private void handlePaymentIntentProcessing(PaymentIntent paymentIntent) {
//        updatePaymentStatus(paymentIntent.getId(), paymentIntent.getStatus(), null);
//    }
//
//    private void updatePaymentStatus(String transactionReference, String status, String paymentMethod) {
//        paymentRepository.findByTransactionReference(transactionReference)
//                .ifPresent(payment -> {
//                    payment.setStatus(status);
//                    if (paymentMethod != null) {
//                        payment.setPaymentMethod(paymentMethod);
//                    }
//                    if ("succeeded".equals(status)) {
//                        payment.setPaidAt(LocalDateTime.now());
//                    }
//                    paymentRepository.save(payment);
//                });
//    }
//
//    private long convertToKZT(Double amount) {
//        amount = amount * 100; // Convert to KZT by multiplying by 100
//        if (amount < 50) {
//            throw new IllegalArgumentException("Amount must be at least 50 KZT.");
//        }
//
//        return (long) amount.doubleValue();
//    }
}
