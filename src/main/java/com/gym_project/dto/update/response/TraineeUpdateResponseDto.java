package com.gym_project.dto.update.response;

import com.gym_project.dto.response.TrainerSummaryDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@ToString
public class TraineeUpdateResponseDto {

    private String username;
    private String firstName;
    private String lastName;

    private LocalDate dateOfBirth;
    private String address;
    private boolean active;

    private Set<TrainerSummaryDto> trainers;
}