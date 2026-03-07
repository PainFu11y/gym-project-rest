package com.gym_project.mapper;

import com.gym_project.dto.create.response.TraineeCreateResponseDto;
import com.gym_project.dto.response.*;
import com.gym_project.dto.update.response.TraineeUpdateResponseDto;
import com.gym_project.entity.Trainee;
import com.gym_project.entity.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TraineeMapper {

    TraineeMapper INSTANCE = Mappers.getMapper(TraineeMapper.class);

    @Mapping(target = "active", source = "isActive")
    @Mapping(target = "trainerUsernames", source = "trainers", qualifiedByName = "mapTrainerUsernames")
    TraineeResponseDto toResponseDto(Trainee trainee);

    List<TraineeResponseDto> toResponseDtoList(List<Trainee> trainees);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    TraineeCreateResponseDto toCreateResponseDto(Trainee trainee);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    TraineeSummaryResponseDto toSummaryDto(Trainee trainee);

    List<TraineeSummaryResponseDto> toSummaryDtoList(List<Trainee> trainees);

    @Mapping(target = "active", source = "isActive")
    @Mapping(target = "trainers", source = "trainers", qualifiedByName = "mapTrainerSummary")
    TraineeUpdateResponseDto toUpdateResponseDto(Trainee trainee);

    List<TraineeUpdateResponseDto> toUpdateResponseDtoList(List<Trainee> trainees);

    @Named("mapTrainerUsernames")
    default Set<String> mapTrainerUsernames(Set<Trainer> trainers) {
        if (trainers == null) return null;
        return trainers.stream()
                .map(Trainer::getUsername)
                .collect(Collectors.toSet());
    }

    @Named("mapTrainerSummary")
    default Set<TrainerSummaryDto> mapTrainerSummary(Set<Trainer> trainers) {
        if (trainers == null) return null;
        return trainers.stream()
                .map(trainer -> {
                    TrainerSummaryDto dto = new TrainerSummaryDto();
                    dto.setUsername(trainer.getUsername());
                    dto.setFirstName(trainer.getFirstName());
                    dto.setLastName(trainer.getLastName());
                    dto.setSpecialization(trainer.getSpecialization().getTrainingTypeName());
                    return dto;
                })
                .collect(Collectors.toSet());
    }
}