package foo.bar.roche_cc;

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

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CreateProductTest {
    public static final Pattern JUST_CREATED_PRODUCT_PATTERN = Pattern.compile("^http://localhost/products/(.+)$");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;


    @Test
    void shallReturnCreated() throws Exception {
        CreateProductInput input = new CreateProductInput("sample name", BigDecimal.TEN);

        mockMvc.perform(post("/products")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.matchesPattern(JUST_CREATED_PRODUCT_PATTERN)));
    }

    @Test
    void shallAddNewProductToDB() throws Exception {
        Integer numOfProductsBefore = productRepository.countAll();

        CreateProductInput input = new CreateProductInput("sample name", BigDecimal.TEN);

        MvcResult mvcResult = mockMvc.perform(post("/products")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input)))
                .andReturn();

        Integer numOfProductsAfter = productRepository.countAll();
        assertThat(numOfProductsAfter, is(numOfProductsBefore + 1));

        String idOfCreatedProduct = getIdOfCreatedProduct(mvcResult);

        Optional<Product> newProductMaybe = productRepository.getById(idOfCreatedProduct);
        assertThat(newProductMaybe, isPresent());
        Product newProduct = newProductMaybe.get();
        assertThat(newProduct, hasProperty("name", is(input.getName())));
        assertThat(newProduct, hasProperty("price", is(input.getPrice())));
        assertThat(newProduct, hasProperty("createdAt", notNullValue()));

    }

    private String getIdOfCreatedProduct(MvcResult mvcResult) {
        return Optional.ofNullable(mvcResult.getResponse().getHeader("Location"))
                .map(headerValue -> {
                    Matcher matcher = JUST_CREATED_PRODUCT_PATTERN.matcher(headerValue);
                    matcher.find();
                    return matcher.group(1);
                }).orElse(null);
    }

}
