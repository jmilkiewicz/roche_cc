package foo.bar.roche_cc;

import com.fasterxml.jackson.databind.ObjectMapper;
import foo.bar.roche_cc.usecase.createProduct.CreateProductInput;
import foo.bar.roche_cc.usecase.createProduct.ProductSaver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CreateProductTest {

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
				.andExpect(status().isCreated());
	}

	@Test
	void shallAddNewProductToDB() throws Exception {
		Integer numOfProductsBefore = productRepository.countAll();

		CreateProductInput input = new CreateProductInput("sample name", BigDecimal.TEN);

		mockMvc.perform(post("/products")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(input)))
				.andExpect(status().isCreated());


		Integer numOfProductsAfter = productRepository.countAll();


	}

}
