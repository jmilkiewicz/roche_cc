package foo.bar.roche_cc.usecase.createProduct;

import java.time.Instant;

public interface ProductSaver {
    String saveProduct(CreateProductInput productInput, Instant now);
}
