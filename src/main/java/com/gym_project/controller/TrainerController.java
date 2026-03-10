package com.gym_project.controller;


import com.gym_project.constants.RoutConstants;
import com.gym_project.dto.create.request.TrainerCreateRequestDto;
import com.gym_project.dto.create.response.TrainerCreateResponseDto;
import com.gym_project.dto.filter.TrainerTrainingFilterDto;
import com.gym_project.dto.response.TrainerResponseDto;
import com.gym_project.dto.response.TrainerSummaryDto;
import com.gym_project.dto.response.TrainingResponseDto;
import com.gym_project.dto.update.request.TrainerUpdateRequestDto;
import com.gym_project.dto.update.response.TrainerUpdateResponseDto;
import com.gym_project.service.TrainerService;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RoutConstants.BASE_URL + RoutConstants.TRAINERS)
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    public ResponseEntity<TrainerCreateResponseDto> create(
            @Valid @RequestBody TrainerCreateRequestDto dto) {

        TrainerCreateResponseDto response = trainerService.create(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerResponseDto> getByUsername(
            @PathVariable String username) {

        TrainerResponseDto response = trainerService.getByUsername(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<TrainerUpdateResponseDto> update(
            @Valid @RequestBody TrainerUpdateRequestDto dto) {

        TrainerUpdateResponseDto response = trainerService.update(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unassigned/{username}")
    public ResponseEntity<List<TrainerSummaryDto>> getUnassignedActiveTrainers(
            @PathVariable String username) {

        List<TrainerSummaryDto> trainers =
                trainerService.getUnassignedActiveTrainersByTraineeUsername(username);

        return ResponseEntity.ok(trainers);
    }

    @PostMapping("/trainings/filter")
    public ResponseEntity<List<TrainingResponseDto>> getTrainerTrainings(
            @RequestBody TrainerTrainingFilterDto dto) {

        List<TrainingResponseDto> trainings =
                trainerService.getTrainerTrainingsByFilter(dto);

        return ResponseEntity.ok(trainings);
    }

    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> toggleStatus(
            @PathVariable String username) {

        trainerService.toggleStatus(username);
        return ResponseEntity.ok().build();
    }
}