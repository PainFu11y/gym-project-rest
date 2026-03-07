package com.gym_project.repository;

import com.gym_project.entity.Training;
import com.gym_project.entity.Trainee;
import com.gym_project.entity.Trainer;
import com.gym_project.entity.TrainingType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingRepository {

    void save(Training training);

    Training update(Training training);

    List<Training> findByTrainee(Trainee trainee);

    List<Training> findByTrainer(Trainer trainer);
}