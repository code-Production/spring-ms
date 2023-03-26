package com.geekbrains.springms.user.mappers;

import com.geekbrains.springms.api.UserDto;
import com.geekbrains.springms.user.entities.Role;
import com.geekbrains.springms.user.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {
        UserDetailsMapper.class,
        UserBillingMapper.class
})
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesListToStringList")
    UserDto toDto(User user);

    @Named("rolesListToStringList")
    default List<String> rolesListToStringList(List<Role> roles) {
        return roles.stream().map(Role::getName).toList();
    }

//    @Mapping(target = "roles", ignore = true)
//    User toEntity(UserDto userDto);

}
