package com.geekbrains.springms.user.mappers;

import com.geekbrains.springms.api.UserBillingDto;
import com.geekbrains.springms.user.entities.UserBilling;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserBillingMapper {

    UserBillingMapper MAPPER = Mappers.getMapper(UserBillingMapper.class);

    UserBillingDto toDto(UserBilling userBilling);

    UserBilling toEntity(UserBillingDto userBillingDto);

    List<UserBillingDto> toDtoList(List<UserBilling> userBillingList);
    List<UserBilling> toEntityList(List<UserBillingDto> userBillingDtoList);

}
