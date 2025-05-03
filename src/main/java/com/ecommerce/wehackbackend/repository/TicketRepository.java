package com.ecommerce.wehackbackend.repository;

import com.ecommerce.wehackbackend.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByQrCode(String qrCode);

    List<Ticket> findByUserId(Long id);
}
