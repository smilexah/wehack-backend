package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.exception.ResourceNotFoundException;
import com.ecommerce.wehackbackend.mapper.ClubMapper;
import com.ecommerce.wehackbackend.model.dto.request.ClubRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.ClubResponseDto;
import com.ecommerce.wehackbackend.model.entity.Club;
import com.ecommerce.wehackbackend.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;

    public Page<ClubResponseDto> getAllClubs(Pageable pageable) {
        return clubRepository.findAll(pageable)
                .map(clubMapper::toDto);
    }

    public ClubResponseDto getClubById(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("club", "id", id.toString()));
        return clubMapper.toDto(club);
    }

    public ClubResponseDto createClub(ClubRequestDto clubRequestDTO) {
        Club club = clubMapper.toEntity(clubRequestDTO);
        Club savedClub = clubRepository.save(club);
        return clubMapper.toDto(savedClub);
    }

    public ClubResponseDto updateClub(Long id, ClubRequestDto clubRequestDTO) {
        Club existingClub = clubRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("club", "id", id.toString()));

        clubMapper.updateEntity(clubRequestDTO, existingClub);
        Club updatedClub = clubRepository.save(existingClub);
        return clubMapper.toDto(updatedClub);
    }

    public void deleteClub(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("club", "id", id.toString()));
        clubRepository.delete(club);
    }

    public Page<ClubResponseDto.EventDTO> getClubEvents(Long id, Pageable pageable) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("club", "id", id.toString()));
        return clubRepository.findEventsByClubId(club.getId(), pageable)
                .map(clubMapper::eventToDto);
    }
}
