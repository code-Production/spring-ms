package com.geekbrains.springms.user.mappers;

import com.geekbrains.springms.api.UserDetailsDto;
import com.geekbrains.springms.user.entities.UserDetails;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserDetailsMapper {

    UserDetailsMapper MAPPER = Mappers.getMapper(UserDetailsMapper.class);


    UserDetailsDto toDto(UserDetails userDetails);

    UserDetails toEntity(UserDetailsDto userDetailsDto);

    List<UserDetailsDto> toDtoList(List<UserDetails> userDetailsList);

    List<UserDetails> toEntityList(List<UserDetailsDto> userDetailsDtoList);
}
