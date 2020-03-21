package foo.bar.roche_cc.usecase.createProduct;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CreateProductUseCase {
    private final ProductSaver productSaver;

    public CreateProductUseCase(ProductSaver productSaver) {
        this.productSaver = productSaver;
    }

    public String execute(CreateProductInput input, Instant now) {
        return productSaver.saveProduct(input, now);
    }
}
