package foo.bar.roche_cc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import foo.bar.roche_cc.model.Product;
import foo.bar.roche_cc.repository.ProductRepository;
import foo.bar.roche_cc.usecase.createProduct.CreateProductInput;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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
    private String productId1;
    private String productId2;


    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        productId1 = productRepository.saveProduct(CreateProductInput.builder().name("prod1").price(BigDecimal.ONE).build(), Instant.now().minus(3, ChronoUnit.HOURS));
        productId2 = productRepository.saveProduct(CreateProductInput.builder().name("prod2").price(BigDecimal.TEN).build(), Instant.now().minus(2, ChronoUnit.MINUTES));
    }

    @Test
    void shallReturnAllProducts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/products")
                .accept(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        List<Product> returnedProducts = readFromBody(mvcResult);

        Product[] expectedProducts = getProductsByIds(productId1, productId2);
        assertThat(returnedProducts, Matchers.containsInAnyOrder(expectedProducts));
    }

    @Test
    void shallOmitMarkedAsDeleted() throws Exception {
        productRepository.markAsDeleted(productId1);

        MvcResult mvcResult = mockMvc.perform(get("/products")
                .accept(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        List<Product> returnedProducts = readFromBody(mvcResult);

        Product[] expectedProducts = getProductsByIds(productId2);
        assertThat(returnedProducts, Matchers.containsInAnyOrder(expectedProducts));
    }

    private List<Product> readFromBody(MvcResult mvcResult) throws java.io.IOException {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<ArrayList<Product>>() {});
    }

    private Product[] getProductsByIds(String... productIdIds) {
        return Arrays.asList(productIdIds).stream()
                .map(productRepository::getById)
                .map(Optional::get)
                .collect(Collectors.toList())
                .toArray(Product[]::new);
    }
}