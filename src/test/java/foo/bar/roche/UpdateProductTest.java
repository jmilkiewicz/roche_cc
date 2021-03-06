package foo.bar.roche;

import com.fasterxml.jackson.databind.ObjectMapper;
import foo.bar.roche.repository.ProductRepository;
import foo.bar.roche.usecase.createProduct.CreateProductInput;
import foo.bar.roche.usecase.updateProduct.UpdateProductInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UpdateProductTest {
    private static UpdateProductInput sutInput = new UpdateProductInput("new Name", BigDecimal.ONE);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;
    private String idOfExistingProduct;


    @BeforeEach
    void createSampleProduct() {
        idOfExistingProduct = productRepository.saveProduct(CreateProductInput.builder().name(sutInput.getName() + "!").price(sutInput.getPrice().add(BigDecimal.ONE)).build(), Instant.now().minus(3, ChronoUnit.HOURS));
    }

    @Test
    void shallReturnSuccessCodeAndUpdateProduct() throws Exception {
        Map<String, Object> beforeUpdate = productRepository.getByIdRaw(idOfExistingProduct);

        executeSut(idOfExistingProduct)
                .andExpect(status().is2xxSuccessful());

        Map<String, Object> afterUpdate = productRepository.getByIdRaw(idOfExistingProduct);

        Map<String, Object> expected = new HashMap<>(beforeUpdate);
        expected.put("name", sutInput.getName());
        expected.put("price", sutInput.getPrice().setScale(2));
        assertThat(afterUpdate, is(expected));
    }

    @Test
    void shallReturnNotFoundWhenProductDoesNotExists() throws Exception {
        String notExistingProductId = idOfExistingProduct + "XXX";

        executeSut(notExistingProductId)
                .andExpect(status().isNotFound());
    }

    private ResultActions executeSut(String productId) throws Exception {
        return mockMvc.perform(put("/products/{productId}", productId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(sutInput)));
    }

    @Test
    void shallReturnNotFoundWhenProductIsMarkedForDeletion() throws Exception {
        productRepository.markAsDeleted(idOfExistingProduct);

        executeSut(idOfExistingProduct)
                .andExpect(status().isNotFound());
    }


}
