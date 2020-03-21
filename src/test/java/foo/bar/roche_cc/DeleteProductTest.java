package foo.bar.roche_cc;

import foo.bar.roche_cc.repository.ProductRepository;
import foo.bar.roche_cc.usecase.createProduct.CreateProductInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DeleteProductTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;
    private String idOfExistingProduct;


    @BeforeEach
    void createSampleProduct() {
        idOfExistingProduct = productRepository.saveProduct(CreateProductInput.builder().name("productName").price(BigDecimal.ZERO).build(), Instant.now());
    }

    @Test
    void shallReturnSuccessCodeAndMarkAsDeleted() throws Exception {
        Map<String,Object> beforeDelete = productRepository.getById2(idOfExistingProduct);

        mockMvc.perform(delete("/products/{productId}", idOfExistingProduct))
                .andExpect(status().is2xxSuccessful());

        Map<String, Object> expected = new HashMap<>(beforeDelete);
        expected.put("deleted", Boolean.TRUE);
        assertThat(productRepository.getById2(idOfExistingProduct), is(expected));
    }


}
