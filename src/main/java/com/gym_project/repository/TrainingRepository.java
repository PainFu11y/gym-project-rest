package com.gym_project.repository;

import com.gym_project.dto.request.TraineeTrainingsFilterRequestDto;
import com.gym_project.entity.Training;
import com.gym_project.entity.Trainee;
import com.gym_project.entity.Trainer;

import java.util.List;

public interface TrainingRepository {

    void save(Training training);

    Training update(Training training);

    List<Training> findByTraineeFilter(TraineeTrainingsFilterRequestDto trainee);

    List<Training> findByTrainer(Trainer trainer);
}