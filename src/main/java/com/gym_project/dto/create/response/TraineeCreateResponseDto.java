package com.gym_project.dto.create.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@ToString
public class TraineeCreateResponseDto {

    private String username;
    private String password;
}