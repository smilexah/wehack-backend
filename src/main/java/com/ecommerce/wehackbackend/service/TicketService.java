package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.exception.ResourceNotFoundException;
import com.ecommerce.wehackbackend.mapper.TicketMapper;
import com.ecommerce.wehackbackend.model.dto.response.TicketResponseDto;
import com.ecommerce.wehackbackend.model.entity.*;
import com.ecommerce.wehackbackend.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final TicketMapper ticketMapper;

    @Transactional
    public List<TicketResponseDto> generateTickets(Order order, int quantity) {
        List<Ticket> tickets = IntStream.range(0, quantity)
                .mapToObj(i -> createTicket(order))
                .toList();

        List<Ticket> savedTickets = ticketRepository.saveAll(tickets);
        return savedTickets.stream()
                .map(ticketMapper::toDto)
                .toList();
    }

    private Ticket createTicket(Order order) {
        return Ticket.builder()
                .order(order)
                .user(order.getUser())
                .event(order.getEvent())
                .qrCode(generateSecureQrCode(order))
                .status("ACTIVE")
                .build();
    }

    private String generateSecureQrCode(Order order) {
        String base = order.getId() + "-" + order.getUser().getId() + "-" + System.currentTimeMillis();
        return Base64.getEncoder().encodeToString(
                DigestUtils.sha256(base + UUID.randomUUID())
        ).substring(0, 20);
    }

    private Ticket createTicket(Order order, User user, Event event) {
        Ticket ticket = new Ticket();
        ticket.setOrder(order);
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setQrCode(generateQrCode(order));
        ticket.setStatus("ACTIVE");
        return ticket;
    }

    private String generateQrCode(Order order) {
        return "TICKET-" + order.getId() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public List<TicketResponseDto> getUserTickets() {
        User user = userService.getCurrentUser();
        return ticketRepository.findByUserId(user.getId()).stream()
                .map(ticketMapper::toDto)
                .toList();
    }

    public TicketResponseDto getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id.toString()));
        validateTicketAccess(ticket);
        return ticketMapper.toDto(ticket);
    }

    public String getQrCode(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", ticketId.toString()));

        validateTicketAccess(ticket);
        return ticket.getQrCode();
    }

    private void validateTicketAccess(Ticket ticket) {
        User currentUser = userService.getCurrentUser();
        if (!currentUser.getId().equals(ticket.getUser().getId())) {
            throw new AccessDeniedException("Access denied");
        }
    }
}