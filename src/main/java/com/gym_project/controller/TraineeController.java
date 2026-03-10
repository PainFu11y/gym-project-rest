package com.gym_project.controller;

import com.gym_project.constants.RoutConstants;
import com.gym_project.dto.create.request.TraineeCreateRequestDto;
import com.gym_project.dto.create.response.TraineeCreateResponseDto;
import com.gym_project.dto.request.TraineeTrainingsFilterRequestDto;
import com.gym_project.dto.response.TraineeResponseDto;
import com.gym_project.dto.response.TrainerSummaryDto;
import com.gym_project.dto.response.TrainingResponseDto;
import com.gym_project.dto.update.request.TraineeUpdateRequestDto;
import com.gym_project.dto.update.request.UpdateTraineeTrainerListRequestDto;
import com.gym_project.service.TraineeService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RoutConstants.BASE_URL + RoutConstants.TRAINEES)
public class TraineeController {

    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping
    public ResponseEntity<TraineeCreateResponseDto> create(
            @Valid @RequestBody TraineeCreateRequestDto dto
    ) {
        return ResponseEntity.ok(traineeService.create(dto));
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeResponseDto> getByUsername(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(traineeService.getByUsername(username));
    }

    @PutMapping
    public ResponseEntity<TraineeResponseDto> update(
            @Valid @RequestBody TraineeUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(traineeService.update(dto));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(
            @PathVariable String username
    ) {
        traineeService.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/trainers")
    public ResponseEntity<List<TrainerSummaryDto>> updateTrainerList(
            @Valid @RequestBody UpdateTraineeTrainerListRequestDto dto
    ) {
        return ResponseEntity.ok(traineeService.updateTrainerList(dto));
    }

    @PostMapping("/trainings/filter")
    public ResponseEntity<List<TrainingResponseDto>> getTrainings(
            @Valid @RequestBody TraineeTrainingsFilterRequestDto filter
    ) {
        return ResponseEntity.ok(
                traineeService.getTraineeTrainings(filter)
        );
    }

    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> toggleStatus(
            @PathVariable String username
    ) {
        traineeService.toggleStatus(username);
        return ResponseEntity.ok().build();
    }
}