package com.gym_project.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class TraineeTrainingsRequestDto {

    @NotBlank(message = "Username is required")
    private String username;

    private LocalDate periodFrom;

    private LocalDate periodTo;

    private String trainerName;

    private String trainingType;
}