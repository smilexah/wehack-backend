package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.exception.BadRequestException;
import com.ecommerce.wehackbackend.exception.ResourceNotFoundException;
import com.ecommerce.wehackbackend.model.entity.Club;
import com.ecommerce.wehackbackend.model.entity.Subscription;
import com.ecommerce.wehackbackend.model.entity.SubscriptionId;
import com.ecommerce.wehackbackend.model.entity.User;
import com.ecommerce.wehackbackend.repository.ClubRepository;
import com.ecommerce.wehackbackend.repository.SubscriptionRepository;
import com.ecommerce.wehackbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    @Transactional
    public Subscription subscribe(Long userId, Long clubId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ResourceNotFoundException("Club", "id", clubId.toString()));

        if (subscriptionRepository.existsById_UserIdAndId_ClubId(userId, clubId)) {
            throw new BadRequestException("Subscription already exists");
        }

        Subscription subscription = Subscription.builder()
                .id(new SubscriptionId(userId, clubId))
                .user(user)
                .club(club)
                .subscribedAt(LocalDateTime.now())
                .build();

        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public void unsubscribe(Long userId, Long clubId) {
        Subscription subscription = subscriptionRepository
                .findById(new SubscriptionId(userId, clubId))
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "clubId", clubId.toString()));
        subscriptionRepository.delete(subscription);
    }

    @Transactional(readOnly = true)
    public List<Subscription> getUserSubscriptions(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    public List<Long> getUserIds(Long clubId) {
        return subscriptionRepository.findByClubId(clubId).stream().map(c -> c.getUser().getId()).toList();
    }
}