package com.gym_project.service.impl;

import com.gym_project.dto.create.request.TraineeCreateRequestDto;
import com.gym_project.dto.create.response.TraineeCreateResponseDto;
import com.gym_project.dto.request.LoginRequestDto;
import com.gym_project.dto.request.TraineeTrainingsFilterRequestDto;
import com.gym_project.dto.response.TraineeResponseDto;
import com.gym_project.dto.response.TrainerResponseDto;
import com.gym_project.dto.response.TrainerSummaryDto;
import com.gym_project.dto.response.TrainingResponseDto;
import com.gym_project.dto.update.request.TraineeUpdateRequestDto;
import com.gym_project.dto.update.request.UpdateTraineeTrainerListRequestDto;
import com.gym_project.entity.Trainee;
import com.gym_project.entity.Trainer;
import com.gym_project.entity.Training;
import com.gym_project.mapper.TraineeMapper;
import com.gym_project.mapper.TrainerMapper;
import com.gym_project.mapper.TrainingMapper;
import com.gym_project.repository.TraineeRepository;
import com.gym_project.repository.TrainerRepository;
import com.gym_project.repository.TrainingRepository;
import com.gym_project.service.TraineeService;
import com.gym_project.utils.PasswordGenerator;
import com.gym_project.utils.UsernameGenerator;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;

    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;

    public TraineeServiceImpl(
            TraineeRepository traineeRepository,
            TrainerRepository trainerRepository,
            TrainingRepository trainingRepository,
            TraineeMapper traineeMapper,
            TrainerMapper trainerMapper,
            TrainingMapper trainingMapper
    ) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
    }


    @Override
    @Transactional
    public TraineeCreateResponseDto create(TraineeCreateRequestDto dto) {
        String base = dto.getFirstName() + "." + dto.getLastName();
        List<String> existingUsernames = traineeRepository.findUsernamesStartingWith(base);
        String generatedUsername =
                UsernameGenerator.generate(dto.getFirstName(), dto.getLastName(), existingUsernames);

        Trainee trainee = traineeMapper.toEntity(dto);
        trainee.setUsername(generatedUsername);
        trainee.setPassword(PasswordGenerator.generate());
        traineeRepository.save(trainee);

        return traineeMapper.toCreateResponseDto(trainee);
    }

    @Override
    @PreAuthorize("#username == authentication.name")
    public TraineeResponseDto getByUsername(String username) {

        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainee not found"));

        return traineeMapper.toResponseDto(trainee);
    }

    @Override
    @Transactional
    @PreAuthorize("#username == authentication.name")
    public TraineeResponseDto update(TraineeUpdateRequestDto dto) {

        Trainee trainee = traineeRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Trainee not found"));

        traineeMapper.updateEntity(dto, trainee);

        Trainee updated = traineeRepository.update(trainee);

        return traineeMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    @PreAuthorize("#username == authentication.name")
    public void deleteByUsername(String username) {
        traineeRepository.deleteByUsername(username);
    }

    @Override
    @Transactional
    @PreAuthorize("#username == authentication.name")
    public void toggleStatus(String username) {
        traineeRepository.toggleStatus(username);
    }

    @Override
    public List<TrainingResponseDto> getTraineeTrainings(TraineeTrainingsFilterRequestDto filter) {

        List<Training> trainings = trainingRepository.findByTraineeFilter(filter);

        return trainingMapper.toResponseDtoList(trainings);
    }


    @Override
    @PreAuthorize("#username == authentication.name")
    public TraineeResponseDto validateCredentials(LoginRequestDto dto) {

        Trainee trainee = traineeRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!trainee.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return traineeMapper.toResponseDto(trainee);
    }

    @Override
    @PreAuthorize("#username == authentication.name")
    public List<TrainerResponseDto> getTrainers(String username) {

        List<Trainer> trainers = trainerRepository
                .findTraineesByTrainerUsername(username)
                .stream()
                .flatMap(t -> t.getTrainers().stream())
                .collect(Collectors.toList());

        return trainerMapper.toResponseDtoList(trainers);
    }

    @Override
    @Transactional
    @PreAuthorize("#username == authentication.name")
    public List<TrainerSummaryDto> updateTrainerList(UpdateTraineeTrainerListRequestDto dto) {

        Trainee trainee = traineeRepository.findByUsername(dto.getTraineeUsername())
                .orElseThrow(() -> new RuntimeException("Trainee not found"));

        List<String> trainerUsernames = dto.getTrainers()
                .stream()
                .map(t -> t.getUsername())
                .toList();

        List<Trainer> trainers = trainerRepository.findTrainersByUsernames(trainerUsernames);

        trainee.setTrainers(Set.copyOf(trainers));

        traineeRepository.update(trainee);

        return trainers.stream()
                .map(tr -> new TrainerSummaryDto(
                        tr.getUsername(),
                        tr.getFirstName(),
                        tr.getLastName(),
                        tr.getSpecialization().getTrainingTypeName()
                ))
                .toList();
    }
}
