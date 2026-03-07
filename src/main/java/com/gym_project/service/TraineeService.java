package com.gym_project.service;

import com.gym_project.dto.create.request.TraineeCreateRequestDto;
import com.gym_project.dto.filter.TraineeTrainingFilterResponseDto;
import com.gym_project.dto.response.TraineeResponseDto;
import com.gym_project.dto.response.TrainerResponseDto;
import com.gym_project.dto.response.TrainingResponseDto;
import com.gym_project.dto.update.request.TraineeUpdateRequestDto;

import java.util.List;

public interface TraineeService {

    TraineeCreateRequestDto create(TraineeCreateRequestDto dto);

    TraineeResponseDto getByUsername(String username);

    TraineeResponseDto update(TraineeUpdateRequestDto dto);

    void deleteByUsername(String username);

    void activateStatus(String username, Boolean isActive);

    void deactivateStatus(String username, Boolean isActive);

    List<TrainingResponseDto> getTrainings(TraineeTrainingFilterResponseDto filter);

    TraineeResponseDto validateCredentials(String username, String password);

    List<TrainerResponseDto> getTrainers(String username);
}