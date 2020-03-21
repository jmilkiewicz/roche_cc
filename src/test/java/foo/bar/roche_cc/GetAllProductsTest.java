package foo.bar.roche_cc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import foo.bar.roche_cc.model.Product;
import foo.bar.roche_cc.repository.ProductRepository;
import foo.bar.roche_cc.usecase.createProduct.CreateProductInput;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GetAllProductsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shallReturnAllProducts() throws Exception {
        productRepository.deleteAll();

        String productId1 = productRepository.saveProduct(CreateProductInput.builder().name("prod1").price(BigDecimal.ONE).build(), Instant.now().minus(3, ChronoUnit.HOURS));
        String productId2 = productRepository.saveProduct(CreateProductInput.builder().name("prod2").price(BigDecimal.TEN).build(), Instant.now().minus(2, ChronoUnit.MINUTES));


        MvcResult mvcResult = mockMvc.perform(get("/products")
                .accept(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        List<Product> responseBody = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<ArrayList<Product>>() {});

        Product[] expectedProducts = getProductsByIds(productId1, productId2);
        assertThat(responseBody, Matchers.containsInAnyOrder(expectedProducts));
    }

    private Product[] getProductsByIds(String productId1, String productId2) {
        return Arrays.asList(productId1, productId2).stream()
                .map(productRepository::getById)
                .map(Optional::get)
                .collect(Collectors.toList())
                .toArray(Product[]::new);
    }
}