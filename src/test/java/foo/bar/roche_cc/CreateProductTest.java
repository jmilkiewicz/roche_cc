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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
class CreateProductTest {
    public static final Pattern JUST_CREATED_PRODUCT_PATTERN = Pattern.compile("^http://localhost/products/(.+)$");
    private static CreateProductInput sutInput = new CreateProductInput("sample name", BigDecimal.TEN);


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;


    private ResultActions executeSut(CreateProductInput input) throws Exception {
        return mockMvc.perform(post("/products")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input)));
    }

    @Test
    void shallReturnHTTPCreated() throws Exception {
        executeSut(sutInput)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.matchesPattern(JUST_CREATED_PRODUCT_PATTERN)));
    }

    @Test
    void shallAddNewProductToDB() throws Exception {
        Integer numOfProductsBefore = productRepository.countAll();

        MvcResult mvcResult = executeSut(sutInput).andReturn();

        assertThat(productRepository.countAll(), is(numOfProductsBefore + 1));

        String idOfCreatedProduct = getIdOfCreatedProduct(mvcResult);

        assertProductPropertiesSaved(idOfCreatedProduct, sutInput.getPrice().setScale(2), sutInput.getName());

    }

    private void assertProductPropertiesSaved(String idOfCreatedProduct, BigDecimal expectedPrice, String expectedName) {
        Optional<Product> newProductMaybe = productRepository.getById(idOfCreatedProduct);
        assertThat(newProductMaybe, isPresent());
        Product newProduct = newProductMaybe.get();
        assertThat(newProduct, hasProperty("id", is(idOfCreatedProduct)));
        assertThat(newProduct, hasProperty("name", is(expectedName)));
        assertThat(newProduct, hasProperty("price", is(expectedPrice)));
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
