package foo.bar.roche_cc;

import com.fasterxml.jackson.databind.ObjectMapper;
import foo.bar.roche_cc.repository.ProductRepository;
import foo.bar.roche_cc.usecase.createProduct.CreateProductInput;
import foo.bar.roche_cc.usecase.updateProduct.UpdateProductInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UpdateProductTest {
    private static UpdateProductInput sutInput = new UpdateProductInput("new Name", BigDecimal.ONE);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;


    @Test
    void shallReturnSuccessCodeAndUpdateProduct() throws Exception {
        String productId = productRepository.saveProduct(CreateProductInput.builder().name(sutInput.getName() + "!").price(sutInput.getPrice().add(BigDecimal.ONE)).build(), Instant.now().minus(3, ChronoUnit.HOURS));

        mockMvc.perform(put("/products/{productId}", productId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(sutInput)))
                .andExpect(status().is2xxSuccessful());
    }


}
