package com.gym_project.mapper;

import com.gym_project.dto.create.response.TrainingCreateResponseDto;
import com.gym_project.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    TrainingMapper INSTANCE = Mappers.getMapper(TrainingMapper.class);


    @Mapping(target = "traineeUsername", source = "trainee.username")
    @Mapping(target = "trainerUsername", source = "trainer.username")
    @Mapping(target = "trainingTypeName", source = "trainingType.trainingTypeName")
    TrainingCreateResponseDto toCreateResponseDto(Training training);

    default java.util.List<TrainingCreateResponseDto> toCreateResponseDtoList(List<Training> trainings) {
        if (trainings == null) return null;
        return trainings.stream()
                .map(this::toCreateResponseDto)
                .toList();
    }
}