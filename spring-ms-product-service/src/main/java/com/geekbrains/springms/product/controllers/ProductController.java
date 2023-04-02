package com.geekbrains.springms.product.controllers;

import com.geekbrains.springms.api.ProductDto;
import com.geekbrains.springms.product.mappers.ProductMapper;
import com.geekbrains.springms.product.entities.Product;
import com.geekbrains.springms.product.services.ProductService;
import com.geekbrains.springms.product.services.ProductServiceCached;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class ProductController {

    private ProductService productService;

    private ProductServiceCached productServiceCached;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setProductServiceCached(ProductServiceCached productServiceCached) {
        this.productServiceCached = productServiceCached;
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
        return productServiceCached.findProductById(id);
//        return productService.findProductById(id);
//                .map(ProductMapper.MAPPER::toDto)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND,
//                        String.format("Product with id '%s' cannot be found.", id)
//                ));
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id, HttpServletResponse response) {
        productService.deleteProductById(id);
    }

    @PutMapping("/")
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return productServiceCached.updateProduct(productDto);
//        return productService.updateProduct(productDto);
    }

    @PostMapping("/")
    public ProductDto saveProduct(@RequestBody ProductDto productDto) {
        Product product = ProductMapper.MAPPER.toEntity(productDto);
        return ProductMapper.MAPPER.toDto(productService.saveProduct(product));
    }

}
