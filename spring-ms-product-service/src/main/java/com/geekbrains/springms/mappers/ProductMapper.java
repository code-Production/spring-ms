package com.geekbrains.springms.mappers;

import com.geekbrains.springms.dtos.ProductDto;
import com.geekbrains.springms.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);

    List<ProductDto> toDtoList(List<Product> productList);

    List<Product> toEntityList(List<ProductDto> productDtoList);

}
