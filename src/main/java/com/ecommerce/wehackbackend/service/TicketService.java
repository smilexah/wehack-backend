package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.exception.ResourceNotFoundException;
import com.ecommerce.wehackbackend.mapper.TicketMapper;
import com.ecommerce.wehackbackend.model.dto.response.TicketResponseDto;
import com.ecommerce.wehackbackend.model.entity.*;
import com.ecommerce.wehackbackend.repository.TicketRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
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
        String qrText = generateQrCodeText(order); // текст QR для сохранения в БД
        return Ticket.builder()
                .order(order)
                .user(order.getUser())
                .event(order.getEvent())
                .qrCode(qrText) // сохраняем только текст!
                .createdAt(LocalDateTime.now())
                .status("ACTIVE")
                .build();
    }

    private String generateQrCodeText(Order order) {
        // Генерируем читаемый уникальный идентификатор
        return String.format(
                "EVENT:%d|USER:%d|TICKET:%s",
                order.getEvent().getId(),
                order.getUser().getId(),
                UUID.randomUUID().toString().replace("-", "").substring(0, 12)
        );
    }

    public byte[] getQrCodeImage(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", ticketId.toString()));

        validateTicketAccess(ticket);

        return generateQrCodeImage(ticket.getQrCode());
    }

    private byte[] generateQrCodeImage(String content) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

            return pngOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    private byte[] generateQrCodePng(String content) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
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

    private void validateTicketAccess(Ticket ticket) {
        User currentUser = userService.getCurrentUser();
        if (!currentUser.getId().equals(ticket.getUser().getId())) {
            throw new AccessDeniedException("Access denied");
        }
    }
}
