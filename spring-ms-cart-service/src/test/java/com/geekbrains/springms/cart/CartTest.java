//package com.geekbrains.springms.cart;
//
//import com.geekbrains.springms.api.ProductDto;
//import com.geekbrains.springms.cart.integrations.ProductServiceIntegration;
//import com.geekbrains.springms.cart.models.Cart;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.mockito.verification.VerificationMode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.math.BigDecimal;
//
//@SpringBootTest(classes = Cart.class)
//@ActiveProfiles("tests")
//public class CartTest {
//
//    @Autowired
//    private Cart cart;
//
//    @MockBean
//    private ProductServiceIntegration productServiceIntegration;
//
//    @Test
//    public void addProductToCartTest() {
//        ProductDto productDto = new ProductDto(17L, "test_title", BigDecimal.valueOf(111));
//        Mockito.doReturn(productDto).when(productServiceIntegration).getProductById(17L);
//        cart.addProductToCartById(17L, 10);
//        cart.addProductToCartById(17L, 10);
//        cart.addProductToCartById(17L, null);
//
//        Mockito.verify(productServiceIntegration, Mockito.times(1)).getProductById(17L);
//        Assertions.assertEquals(1, cart.getItems().size());
//        Assertions.assertEquals(17L, cart.getItems().get(0).getProductDto().getId());
//        Assertions.assertEquals(21, cart.getItems().get(0).getAmount());
//        Assertions.assertEquals(2331, cart.getTotalPrice());
//    }
//
//    @Test
//    @Order(Integer.MAX_VALUE)
//    public void removeProductFromCart() {
//        //addition should be tested previously
//        ProductDto productDto = new ProductDto(17L, "test_title", BigDecimal.valueOf(111));
//        Mockito.doReturn(productDto).when(productServiceIntegration).getProductById(17L);
//        cart.addProductToCartById(17L, 10);
//        cart.addProductToCartById(17L, null);
//
//        cart.removeProductFromCartById(17L, 2);
//        Assertions.assertEquals(1, cart.getItems().size());
//        Assertions.assertEquals(9, cart.getItems().get(0).getAmount());
//        Assertions.assertEquals(9 * 111.0, cart.getItems().get(0).getSum());
//        cart.removeProductFromCartById(17L, null);
//        Assertions.assertEquals(0, cart.getItems().size());
//        Assertions.assertEquals(0.0, cart.getTotalPrice());
//    }
//}
