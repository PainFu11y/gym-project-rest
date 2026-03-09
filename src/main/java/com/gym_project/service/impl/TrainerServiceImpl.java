package com.gym_project.service.impl;

import com.gym_project.dto.create.request.TrainerCreateRequestDto;
import com.gym_project.dto.create.response.TrainerCreateResponseDto;
import com.gym_project.dto.filter.TrainerTrainingFilterDto;
import com.gym_project.dto.request.TrainerTrainingsRequestDto;
import com.gym_project.dto.response.TrainerResponseDto;
import com.gym_project.dto.response.TrainerSummaryDto;
import com.gym_project.dto.response.TrainingResponseDto;
import com.gym_project.dto.update.request.TrainerUpdateRequestDto;
import com.gym_project.dto.update.response.TrainerUpdateResponseDto;
import com.gym_project.entity.Trainer;
import com.gym_project.entity.Training;
import com.gym_project.entity.TrainingType;
import com.gym_project.repository.TrainerRepository;
import com.gym_project.repository.TrainingRepository;
import com.gym_project.repository.TrainingTypeRepository;
import com.gym_project.service.TrainerService;
import com.gym_project.mapper.TrainerMapper;
import com.gym_project.mapper.TrainingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;

    public TrainerServiceImpl(TrainerRepository trainerRepository,
                              TrainerMapper trainerMapper,
                              TrainingMapper trainingMapper,
                              TrainingTypeRepository trainingTypeRepository,
                              TrainingRepository trainingRepository) {
        this.trainerRepository = trainerRepository;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingRepository = trainingRepository;
    }

    @Override
    @Transactional
    public TrainerCreateResponseDto create(TrainerCreateRequestDto dto) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(dto.getFirstName());
        trainer.setLastName(dto.getLastName());
        trainer.setActive(true);

        TrainingType trainingType = trainingTypeRepository.findById(dto.getTrainingTypeId())
                .orElseThrow(() -> new RuntimeException(
                        "Training type not found with id: " + dto.getTrainingTypeId()
                ));
        trainer.setSpecialization(trainingType);

        trainerRepository.save(trainer);
        return trainerMapper.toCreateResponseDto(trainer);
    }

    @Override
    @Transactional(readOnly = true)
    public TrainerResponseDto getByUsername(String username) {
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
        return trainerMapper.toResponseDto(trainer);
    }

    @Override
    @Transactional
    public TrainerUpdateResponseDto update(TrainerUpdateRequestDto dto) {
        Trainer trainer = trainerRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
        trainer.setFirstName(dto.getFirstName());
        trainer.setLastName(dto.getLastName());
        trainerRepository.update(trainer);
        return trainerMapper.toUpdateResponseDto(trainer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerSummaryDto> getUnassignedActiveTrainersByTraineeUsername(String username) {
        List<Trainer> trainers = trainerRepository.findUnassignedActiveTrainersByTraineeUsername(username);
        return trainerMapper.toUpdateResponseDtoList(trainers)
                .stream()
                .map(t -> {
                    TrainerSummaryDto dto = new TrainerSummaryDto();
                    dto.setUsername(t.getUsername());
                    dto.setFirstName(t.getFirstName());
                    dto.setLastName(t.getLastName());
                    dto.setSpecialization(t.getSpecialization());
                    return dto;
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingResponseDto> getTrainerTrainingsByFilter(TrainerTrainingFilterDto dto) {
        TrainerTrainingsRequestDto repoFilter = new TrainerTrainingsRequestDto();
        repoFilter.setUsername(dto.getUsername());
        repoFilter.setPeriodFrom(dto.getFromDate());
        repoFilter.setPeriodTo(dto.getToDate());
        repoFilter.setTraineeName(dto.getTraineeName());

        List<Training> trainings = trainingRepository.findByTrainerFilter(repoFilter);

        return trainingMapper.toResponseDtoList(trainings);
    }

    @Override
    @Transactional
    public void toggleStatus(String username) {
        trainerRepository.toggleStatus(username);
    }
}