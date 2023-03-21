package com.geekbrains.springms.product.controllers;

import com.geekbrains.springms.api.ProductDto;
import com.geekbrains.springms.product.mappers.ProductMapper;
import com.geekbrains.springms.product.entities.Product;
import com.geekbrains.springms.product.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Enumeration;


@RestController
public class ProductController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public Page<ProductDto> findAllFilteredProducts(
            @RequestParam(required = false, name = "min_price") Double minPrice,
            @RequestParam(required = false, name = "max_price") Double maxPrice,
            @RequestParam(required = false, name = "page_num") Integer pageNum
    ){
        return productService.findAllFilteredProducts(minPrice, maxPrice, pageNum)
                .map(ProductMapper.MAPPER::toDto);
    }

    @GetMapping("/{id}")
    public ProductDto findProductById(@PathVariable Long id) {
        return productService.findProductById(id)
                .map(ProductMapper.MAPPER::toDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Product with id '%s' cannot be found.", id)
                ));
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id, HttpServletRequest request) {
        checkAuthorizationHeaderOrThrowException(request);
        hasSpecialAuthorityOrThrowException(request);
        productService.deleteProductById(id);
    }

    @PutMapping("/")
    public ProductDto updateProduct(@RequestBody ProductDto productDto, HttpServletRequest request) {
        checkAuthorizationHeaderOrThrowException(request);
        hasSpecialAuthorityOrThrowException(request);
        Product product = ProductMapper.MAPPER.toEntity(productDto);
        return ProductMapper.MAPPER.toDto(productService.updateProduct(product));
    }

    @PostMapping("/")
    public ProductDto saveProduct(@RequestBody ProductDto productDto, HttpServletRequest request) {
        checkAuthorizationHeaderOrThrowException(request);
        hasSpecialAuthorityOrThrowException(request);
        Product product = ProductMapper.MAPPER.toEntity(productDto);
        return ProductMapper.MAPPER.toDto(productService.saveProduct(product));
    }


    private void hasSpecialAuthorityOrThrowException(HttpServletRequest request) {
        if (request.getHeaders("roles").hasMoreElements()) {
            Enumeration<String> roles = request.getHeaders("roles");
            while (roles.hasMoreElements()) {
                if (roles.nextElement().equalsIgnoreCase("ROLE_ADMIN")) {
                    return;
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not enough privilege.");
    }

    private String checkAuthorizationHeaderOrThrowException(HttpServletRequest request) {
        String username = request.getHeader("username");
        if (username != null && !username.isBlank()) {
            return username;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access.");
    }

}
