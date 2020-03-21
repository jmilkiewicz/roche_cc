package foo.bar.roche.usecase.createProduct;

import java.time.Instant;

public interface ProductSaver {
    String saveProduct(CreateProductInput productInput, Instant now);
}
