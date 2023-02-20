package com.geekbrains.springms.cart.mapper;

import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.cart.models.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CartItemMapper.class)
public interface CartMapper {

    CartMapper MAPPER = Mappers.getMapper(CartMapper.class);

    CartDto toDto(Cart cart);

    Cart toEntity(CartDto cartDto);
}
