package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.model.dto.request.OrganizerRequestDto;
import com.ecommerce.wehackbackend.model.entity.Club;
import com.ecommerce.wehackbackend.model.entity.OrganizerRequest;
import com.ecommerce.wehackbackend.model.entity.User;
import com.ecommerce.wehackbackend.repository.ClubRepository;
import com.ecommerce.wehackbackend.repository.OrganizerRequestRepository;
import com.ecommerce.wehackbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizerRequestService {

    private final OrganizerRequestRepository organizerRequestRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    @Transactional
    public void submitRequest(OrganizerRequestDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Club club = clubRepository.findById(dto.clubId())
                .orElseThrow(() -> new EntityNotFoundException("Club not found"));

        boolean exists = organizerRequestRepository.existsByUserIdAndClubId(user.getId(), club.getId());
        if (exists) throw new IllegalStateException("You already have a pending request for this club");

        OrganizerRequest request = new OrganizerRequest();
        request.setUser(user);
        request.setClub(club);
        request.setStatus(OrganizerRequest.Status.PENDING);
        request.setCreatedAt(LocalDateTime.now());

        organizerRequestRepository.save(request);
    }

    public List<OrganizerRequest> getAll() {
        return organizerRequestRepository.findAll();
    }

    @Transactional
    public void updateStatus(Long requestId, OrganizerRequest.Status newStatus) {
        OrganizerRequest request = organizerRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        request.setStatus(newStatus);
        request.setReviewedAt(LocalDateTime.now());
        organizerRequestRepository.save(request);
    }
}