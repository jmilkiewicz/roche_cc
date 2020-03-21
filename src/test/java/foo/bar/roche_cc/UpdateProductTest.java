package foo.bar.roche_cc;

import com.fasterxml.jackson.databind.ObjectMapper;
import foo.bar.roche_cc.model.Product;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
        Product oldProduct = productRepository.getById(productId).get();

        mockMvc.perform(put("/products/{productId}", productId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(sutInput)))
                .andExpect(status().is2xxSuccessful());


        Product newProduct = productRepository.getById(productId).get();
        Product expected = Product.builder()
                .createdAt(oldProduct.getCreatedAt())
                .id(productId)
                .name(sutInput.getName())
                .price(sutInput.getPrice().setScale(2))
                .build();


        assertThat(newProduct, is(expected));
    }


}
