package com.gym_project.mapper;

import com.gym_project.dto.create.response.TrainerCreateResponseDto;
import com.gym_project.dto.response.*;
import com.gym_project.dto.update.response.TrainerUpdateResponseDto;
import com.gym_project.entity.Trainer;
import com.gym_project.entity.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    TrainerMapper INSTANCE = Mappers.getMapper(TrainerMapper.class);

    @Mapping(target = "active", source = "isActive")
    @Mapping(target = "specialization", source = "specialization.trainingTypeName")
    @Mapping(target = "traineeUsernames", source = "trainees", qualifiedByName = "mapTraineeUsernames")
    TrainerResponseDto toResponseDto(Trainer trainer);

    List<TrainerResponseDto> toResponseDtoList(List<Trainer> trainers);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    TrainerCreateResponseDto toCreateResponseDto(Trainer trainer);

    @Mapping(target = "specialization", source = "specialization.trainingTypeName")
    @Mapping(target = "active", source = "isActive")
    @Mapping(target = "trainees", source = "trainees", qualifiedByName = "mapTraineeSummary")
    TrainerUpdateResponseDto toUpdateResponseDto(Trainer trainer);

    List<TrainerUpdateResponseDto> toUpdateResponseDtoList(List<Trainer> trainers);


    @Named("mapTraineeUsernames")
    default Set<String> mapTraineeUsernames(Set<Trainee> trainees) {
        if (trainees == null) return null;
        return trainees.stream()
                .map(Trainee::getUsername)
                .collect(Collectors.toSet());
    }

    @Named("mapTraineeSummary")
    default List<TraineeSummaryResponseDto> mapTraineeSummary(Set<Trainee> trainees) {
        if (trainees == null) return null;
        return trainees.stream()
                .map(trainee -> {
                    TraineeSummaryResponseDto dto = new TraineeSummaryResponseDto();
                    dto.setUsername(trainee.getUsername());
                    dto.setFirstName(trainee.getFirstName());
                    dto.setLastName(trainee.getLastName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}