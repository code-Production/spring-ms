package com.geekbrains.springms.product.services;

import com.geekbrains.springms.product.entities.Product;
import com.geekbrains.springms.product.repositories.ProductRepository;
import com.geekbrains.springms.product.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final static int PAGE_SIZE = 5;

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> findAllFilteredProducts(Double minPrice, Double maxPrice, Integer pageNum) {

        Specification<Product> spec = Specification.where(null);
        Sort sort = Sort.sort(Product.class).descending();
        Pageable pageable = null;

        if (minPrice != null) {
            spec = spec.and(ProductSpecification.priceIsGreaterThanOrEqualTo(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductSpecification.priceIsLessThanOrEqualTo(maxPrice));
        }
        if (pageNum != null) {
            pageable = PageRequest.of(pageNum, PAGE_SIZE, sort);
        } else {
            pageable = Pageable.unpaged();
        }

        return productRepository.findAll(spec, pageable);

    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public Product saveProduct(Product product) {
        product.setId(null);
        return productRepository.save(product);
    }
}
