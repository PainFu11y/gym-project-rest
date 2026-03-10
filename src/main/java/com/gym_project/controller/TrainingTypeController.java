package com.gym_project.controller;

import com.gym_project.constants.RoutConstants;
import com.gym_project.dto.response.TrainingTypeResponseDto;
import com.gym_project.service.TrainingTypeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RoutConstants.BASE_URL + RoutConstants.TRAINING_TYPES)
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @GetMapping
    public ResponseEntity<List<TrainingTypeResponseDto>> findAll() {
        List<TrainingTypeResponseDto> response = trainingTypeService.findAll();
        return ResponseEntity.ok(response);
    }
}