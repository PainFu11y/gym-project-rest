package com.gym_project.dto.create.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class TrainerCreateRequestDto {

    @NotBlank(message = "First Name is required")
    @Size(max = 50, message = "First Name cannot be longer than 50 characters")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(max = 50, message = "Last Name cannot be longer than 50 characters")
    private String lastName;

    @NotBlank(message = "Specialization is required")
    @Size(max = 100, message = "Specialization cannot be longer than 100 characters")
    private String specialization;
}