package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.exception.ResourceNotFoundException;
import com.ecommerce.wehackbackend.mapper.EventMapper;
import com.ecommerce.wehackbackend.model.dto.request.EventRequestDto;
import com.ecommerce.wehackbackend.model.dto.request.EventReviewRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.EventResponseDto;
import com.ecommerce.wehackbackend.model.entity.*;
import com.ecommerce.wehackbackend.repository.*;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ClubRepository clubRepository;
    private final VenueRepository venueRepository;
    private final EventReviewRepository eventReviewRepository;
    private final EventAttendanceRepository eventAttendanceRepository;
    private final TicketRepository ticketRepository;
    private final EventMapper eventMapper;

    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("event", "id", id.toString()));
        return eventMapper.toDto(event);
    }

    public EventResponseDto createEvent(EventRequestDto eventRequestDTO) {
        Event event = eventMapper.toEntity(eventRequestDTO);

        Club club = clubRepository.findById(eventRequestDTO.getClubId())
                .orElseThrow(() -> new ResourceNotFoundException("club", "clubId", eventRequestDTO.getClubId().toString()));
        event.setClub(club);

        if (eventRequestDTO.getVenueId() != null) {
            Venue venue = venueRepository.findById(eventRequestDTO.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("venue", "venueId", eventRequestDTO.getVenueId().toString()));
            event.setVenue(venue);
        }

        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }

    public EventResponseDto updateEvent(Long id, EventRequestDto eventRequestDTO) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("event", "id", id.toString()));

        eventMapper.updateEntity(eventRequestDTO, existingEvent);

        if (eventRequestDTO.getClubId() != null) {
            Club club = clubRepository.findById(eventRequestDTO.getClubId())
                    .orElseThrow(() -> new ResourceNotFoundException("club", "clubId", eventRequestDTO.getClubId().toString()));
            existingEvent.setClub(club);
        }

        if (eventRequestDTO.getVenueId() != null) {
            Venue venue = venueRepository.findById(eventRequestDTO.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("venue", "venueId", eventRequestDTO.getVenueId().toString()));
            existingEvent.setVenue(venue);
        }

        Event updatedEvent = eventRepository.save(existingEvent);
        return eventMapper.toDto(updatedEvent);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("event", "id", id.toString()));
        eventRepository.delete(event);
    }

    public List<EventResponseDto.TicketDTO> getEventTickets(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("event", "id", eventId.toString()));
        return eventRepository.findTicketsByEventId(eventId)
                .stream()
                .map(eventMapper::ticketToDto)
                .collect(Collectors.toList());
    }

    public List<EventResponseDto.ReviewDTO> getEventReviews(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("event", "id", eventId.toString()));
        return eventRepository.findReviewsByEventId(eventId)
                .stream()
                .map(eventMapper::reviewToDto)
                .collect(Collectors.toList());
    }

    public EventResponseDto.ReviewDTO addEventReview(
            Long eventId,
            EventReviewRequestDto reviewRequestDTO,
            User currentUser) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("event", "id", eventId.toString()));

        boolean hasAlreadyReviewed = eventReviewRepository.existsByEventIdAndUserId(eventId, currentUser.getId());
        if (hasAlreadyReviewed) {
            throw new BadRequestException("You have already reviewed this event");
        }

        boolean hasAttended = eventAttendanceRepository.existsByEventIdAndUserId(eventId, currentUser.getId());
        if (!hasAttended) {
            throw new BadRequestException("You must attend the event before reviewing it");
        }

        EventReview review = new EventReview();
        review.setEvent(event);
        review.setUser(currentUser);
        review.setRating(reviewRequestDTO.getRating());
        review.setComment(reviewRequestDTO.getComment());
        review.setCreatedAt(LocalDateTime.now());

        EventReview savedReview = eventReviewRepository.save(review);

        updateEventAverageRating(eventId);

        return eventMapper.reviewToDto(savedReview);
    }

    private void updateEventAverageRating(Long eventId) {
        Double averageRating = eventReviewRepository.calculateAverageRatingByEventId(eventId);
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            event.setAverageRating(averageRating);
            eventRepository.save(event);
        }
    }

//    public void checkInToEvent(Long eventId, String qrCode, User checkedInBy) {
//        Event event = eventRepository.findById(eventId)
//                .orElseThrow(() -> new ResourceNotFoundException("event", "id", eventId.toString()));
//
//        Ticket ticket = ticketRepository.findByQrCode(qrCode)
//                .orElseThrow(() -> new BadRequestException("Invalid QR code"));
//
//        if (!ticket.getEvent().getId().equals(eventId)) {
//            throw new BadRequestException("This ticket is not valid for this event");
//        }
//
//        // Check if ticket is already used
//        if ("USED".equals(ticket.getStatus())) {
//            throw new BadRequestException("This ticket has already been used");
//        }
//
//        // Check if ticket is valid (not cancelled)
//        if ("CANCELLED".equals(ticket.getStatus())) {
//            throw new BadRequestException("This ticket has been cancelled");
//        }
//
//        // Get the user who owns the ticket
//        User attendee = ticket.getUser();
//
//        // Check if user is already checked in (optional)
//        boolean alreadyCheckedIn = eventAttendanceRepository.existsByEventIdAndUserId(eventId, attendee.getId());
//        if (alreadyCheckedIn) {
//            throw new BadRequestException("This user is already checked in");
//        }
//
//        // Update ticket status
//        ticket.setStatus("USED");
//        ticket.setUsedAt(LocalDateTime.now());
//        ticketRepository.save(ticket);
//
//        // Record attendance
//        EventAttendance attendance = new EventAttendance();
//        attendance.setEvent(event);
//        attendance.setUser(attendee);
//        attendance.setCheckedInBy(checkedInBy);
//        attendance.setAttendedAt(LocalDateTime.now());
//        attendance.setPointsAwarded(calculateAttendancePoints(event));
//        eventAttendanceRepository.save(attendance);
//
//        // Update user's loyalty points
//        updateUserLoyaltyPoints(attendee, attendance.getPointsAwarded());
//    }
//
//    private int calculateAttendancePoints(Event event) {
//        // Basic implementation - you might want to make this configurable
//        return 10; // 10 points per event attendance
//    }
//
//    private void updateUserLoyaltyPoints(User user, int points) {
//        UserLoyaltyPoints loyaltyPoints = userLoyaltyPointsRepository.findByUserId(user.getId())
//                .orElseGet(() -> {
//                    UserLoyaltyPoints newPoints = new UserLoyaltyPoints();
//                    newPoints.setUser(user);
//                    newPoints.setPoints(0);
//                    return newPoints;
//                });
//
//        loyaltyPoints.setPoints(loyaltyPoints.getPoints() + points);
//        loyaltyPoints.setUpdatedAt(LocalDateTime.now());
//        userLoyaltyPointsRepository.save(loyaltyPoints);
//    }
}
