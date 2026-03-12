package com.gym_project.security;

import com.gym_project.entity.Trainee;
import com.gym_project.entity.Trainer;
import com.gym_project.exception.InvalidCredentialsException;
import com.gym_project.repository.TraineeRepository;
import com.gym_project.repository.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final AuthService authService;

    public LoginService(
            TrainerRepository trainerRepository,
            TraineeRepository traineeRepository,
            AuthService authService
    ) {
        this.trainerRepository = trainerRepository;
        this.traineeRepository = traineeRepository;
        this.authService = authService;
    }

    public void login(String username, String password) {
        Optional<Trainer> trainer =
                trainerRepository.findByUsernameAndPassword(username, password);

        if (trainer.isPresent()) {
            authService.authenticate(username, Role.TRAINER);
            return;
        }

        Optional<Trainee> trainee = traineeRepository.findByUsername(username);

        if (trainee.isPresent() && trainee.get().getPassword().equals(password)) {
            authService.authenticate(username, Role.TRAINEE);
            return;
        }

        throw new InvalidCredentialsException();
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        Optional<Trainer> trainer =
                trainerRepository.findByUsernameAndPassword(username, oldPassword);

        if (trainer.isPresent()) {
            trainerRepository.changePassword(username, newPassword);
            return;
        }

        Optional<Trainee> trainee = traineeRepository.findByUsername(username);

        if (trainee.isPresent() && trainee.get().getPassword().equals(oldPassword)) {
            traineeRepository.changePassword(username, newPassword);
            return;
        }

        throw new InvalidCredentialsException();
    }
}