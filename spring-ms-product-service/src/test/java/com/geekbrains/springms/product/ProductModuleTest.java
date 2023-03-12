//package com.geekbrains.springms.product;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.geekbrains.springms.api.ProductDto;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
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
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles(value = "tests")
//public class ProductModuleTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void getAllProductsTest() throws Exception {
//        mockMvc.perform(
//                MockMvcRequestBuilders
//                        .get("/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("content").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("content[0]").isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("content[0]").isMap())
//                .andExpect(MockMvcResultMatchers.jsonPath("content[0].id", Matchers.is(1)));
//    }
//
//    @Test
//    public void getProductByIdTest() throws Exception {
//        mockMvc.perform(
//                MockMvcRequestBuilders
//                        .get("/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
//    }
//
//    @Test
//    public void addAndDeleteProductTest() throws Exception {
//
//        ProductDto productDto = new ProductDto(null, "s7dfsd7sd9f7s9d87f", BigDecimal.ZERO);
//        MvcResult mvcResult = mockMvc.perform(
//                        MockMvcRequestBuilders
//                                .post("/")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(new ObjectMapper().writeValueAsString(productDto))
//
//                )
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("s7dfsd7sd9f7s9d87f")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(BigDecimal.ZERO)))
//                .andReturn();
//
//        Long createdProductId = new ObjectMapper().readValue(
//                mvcResult.getResponse().getContentAsString().getBytes(StandardCharsets.UTF_8),
//                ProductDto.class
//        ).getId();
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/" + createdProductId))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//
//
//
//}
