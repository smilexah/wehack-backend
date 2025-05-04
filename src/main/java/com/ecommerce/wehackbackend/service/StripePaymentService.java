package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.exception.PaymentException;
import com.ecommerce.wehackbackend.model.dto.response.PaymentGatewayResponseDto;
import com.ecommerce.wehackbackend.model.dto.response.PaymentGatewayStatusDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class StripePaymentService {
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public PaymentGatewayResponseDto initiatePayment(
            Long orderId,
            Double amount,
            String currency,
            String paymentMethod) {

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(cancelUrl + "?order_id=" + orderId)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(currency.toLowerCase())
                                                    .setUnitAmount((long)(amount * 100)) // cents
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Order #" + orderId)
                                                                    .build())
                                                    .build())
                                    .build())
                    .build();

            Session session = Session.create(params);

            return new PaymentGatewayResponseDto(
                    session.getId(),
                    session.getUrl(),
                    "pending"
            );

        } catch (StripeException e) {
            throw new PaymentException(
                    "Failed to initiate payment: " + e.getMessage(),
                    e
            );
        }
    }

    public PaymentGatewayStatusDto checkPaymentStatus(String transactionId) {
        try {
            Session session = Session.retrieve(transactionId);

            return new PaymentGatewayStatusDto(
                    session.getId(),
                    session.getPaymentStatus(),
                    session.getSuccessUrl()
            );

        } catch (StripeException e) {
            throw new PaymentException(
                    "Failed to initiate payment: " + e.getMessage(),
                    e
            );
        }
    }
}