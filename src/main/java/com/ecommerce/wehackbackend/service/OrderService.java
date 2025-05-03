package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.exception.NotEnoughTicketsException;
import com.ecommerce.wehackbackend.exception.ResourceNotFoundException;
import com.ecommerce.wehackbackend.mapper.OrderMapper;
import com.ecommerce.wehackbackend.model.dto.request.OrderRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.OrderResponseDto;
import com.ecommerce.wehackbackend.model.entity.Event;
import com.ecommerce.wehackbackend.model.entity.Order;
import com.ecommerce.wehackbackend.model.entity.User;
import com.ecommerce.wehackbackend.repository.EventRepository;
import com.ecommerce.wehackbackend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private static final String ORDER_STATUS_PAID = "PAID";

    private final OrderRepository orderRepository;
    private final EventRepository eventRepository;
    private final OrderMapper orderMapper;
    private final TicketService ticketService;
    private final UserService userService;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto request) {
        User currentUser = userService.getCurrentUser();
        Event event = getEvent(request.getEventId());

        validateTicketAvailability(event, request.getQuantity());

        Order order = createNewOrder(currentUser, event, request);
        updateEventCapacity(event, request.getQuantity());

        Order savedOrder = orderRepository.save(order);
        ticketService.generateTickets(savedOrder, request.getQuantity());

        log.info("Order created. Order ID: {}, User ID: {}", savedOrder.getId(), currentUser.getId());
        return orderMapper.toDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getUserOrders() {
        User currentUser = userService.getCurrentUser();
        List<Order> orders = orderRepository.findByUserId(currentUser.getId());
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderDetails(Long orderId) {
        Order order = getOrderById(orderId);
        return orderMapper.toDto(order);
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId.toString()));
    }

    private void validateTicketAvailability(Event event, int requestedQuantity) {
        if (event.getCapacity() == null || event.getCapacity() < requestedQuantity) {
            throw new NotEnoughTicketsException(
                    String.format("Not enough tickets available. Requested: %d, Available: %d",
                            requestedQuantity,
                            event.getCapacity() != null ? event.getCapacity() : 0)
            );
        }
    }

    private Order createNewOrder(User user, Event event, OrderRequestDto request) {
        return Order.builder()
                .user(user)
                .event(event)
                .quantity(request.getQuantity())
                .currency(request.getCurrency())
                .totalPrice(calculateTotalPrice(request.getQuantity(), event.getPrice()))
                .status(ORDER_STATUS_PAID)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private double calculateTotalPrice(int quantity, double price) {
        return quantity * price;
    }

    private void updateEventCapacity(Event event, int soldQuantity) {
        event.setCapacity(event.getCapacity() - soldQuantity);
        eventRepository.save(event);
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId.toString()));
    }
}