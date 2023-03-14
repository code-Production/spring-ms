//package com.geekbrains.springms.cart;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.geekbrains.springms.api.CartDto;
//import com.geekbrains.springms.api.OrderDto;
//import com.geekbrains.springms.api.ProductDto;
//import com.geekbrains.springms.cart.integrations.ProductServiceIntegration;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.math.BigDecimal;
//import java.nio.charset.StandardCharsets;
//
//import static reactor.core.publisher.Mono.when;
//
//
//@SpringBootTest
//@Order(Integer.MAX_VALUE) // after cart unit test
//@ActiveProfiles("tests")
//@AutoConfigureMockMvc
//public class CartModuleIntegrationWithProductAndOrderServicesTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ProductServiceIntegration productServiceIntegration;
//
//    @Test
//    public void createCartAndSaveAsOrderTest() throws Exception {
//        //find products that exist
//        Long id1 = 1L;
//        Long id2 = 2L;
//
//        ProductDto productDto1 = productServiceIntegration.getProductById(id1);
//        ProductDto productDto2 = productServiceIntegration.getProductById(id2);
//
//        MvcResult mvcResult1 = mockMvc.perform(
//                        MockMvcRequestBuilders.post("/?id=" + id1)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//        CartDto cartDto1 = new ObjectMapper().readValue(
//                mvcResult1.getResponse().getContentAsString().getBytes(StandardCharsets.UTF_8),
//                CartDto.class);
//        Assertions.assertNotNull(cartDto1);
//        Assertions.assertEquals(id1, cartDto1.getItems().get(0).getProductDto().getId());
//
//        MockHttpSession session = (MockHttpSession) mvcResult1.getRequest().getSession();
//        Assertions.assertNotNull(session);
//
//        MvcResult mvcResult2 = mockMvc.perform(
//                        MockMvcRequestBuilders.post("/?id=" + id2 + "&amount=10").session(session)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//        CartDto cartDto2 = new ObjectMapper().readValue(
//                mvcResult2.getResponse().getContentAsString().getBytes(StandardCharsets.UTF_8),
//                CartDto.class);
//        Assertions.assertNotNull(cartDto2);
//        Assertions.assertEquals(id2, cartDto2.getItems().get(1).getProductDto().getId());
//
//        MvcResult mvcResult3 = mockMvc.perform(
//                MockMvcRequestBuilders.get("/checkout").session(session)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//        OrderDto orderDto = new ObjectMapper().readValue(
//                mvcResult3.getResponse().getContentAsString().getBytes(StandardCharsets.UTF_8),
//                OrderDto.class
//        );
//
//        Assertions.assertEquals(2, orderDto.getOrderItems().size());
//
//        Assertions.assertEquals(productDto1.getId(), orderDto.getOrderItems().get(0).getProductId());
//        Assertions.assertEquals(productDto1.getPrice(), orderDto.getOrderItems().get(0).getPrice());
//        Assertions.assertEquals(1, orderDto.getOrderItems().get(0).getAmount());
//        Assertions.assertEquals(
//                productDto1.getPrice().multiply(BigDecimal.valueOf(1)),
//                orderDto.getOrderItems().get(0).getSum()
//        );
//        Assertions.assertEquals(productDto2.getId(), orderDto.getOrderItems().get(1).getProductId());
//        Assertions.assertEquals(productDto2.getPrice(), orderDto.getOrderItems().get(1).getPrice());
//        Assertions.assertEquals(10, orderDto.getOrderItems().get(1).getAmount());
//        Assertions.assertEquals(productDto2.getPrice().multiply(BigDecimal.valueOf(10)), orderDto.getOrderItems().get(1).getSum());
//
//
//        Assertions.assertEquals(
//                productDto1.getPrice().multiply(BigDecimal.valueOf(1))
//                        .add(productDto2.getPrice().multiply(BigDecimal.valueOf(10))),
//                orderDto.getOrderTotal());
//
//    }
//}
