package com.gym_project.controller;

import com.gym_project.constants.RoutConstants;
import com.gym_project.dto.create.request.TrainingCreateRequestDto;
import com.gym_project.service.TrainingService;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RoutConstants.BASE_URL + RoutConstants.TRAININGS)
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @Valid @RequestBody TrainingCreateRequestDto dto) {

        trainingService.create(dto);
        return ResponseEntity.ok().build();
    }
}