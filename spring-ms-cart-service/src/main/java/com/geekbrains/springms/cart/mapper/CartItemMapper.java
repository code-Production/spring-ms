package com.geekbrains.springms.cart.mapper;

import com.geekbrains.springms.api.CartItemDto;
import com.geekbrains.springms.cart.models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartItemMapper {
    CartItemMapper MAPPER = Mappers.getMapper(CartItemMapper.class);

    CartItemDto toDto(CartItem cartItem);

    CartItem toEntity(CartItemDto cartItemDto);

    List<CartItemDto> toDtoList(List<CartItem> cartItems);

    List<CartItem> toEntityList(List<CartItemDto> cartItemDtos);

}
