package foo.bar.roche_cc.usecase.createProduct;

import java.time.Instant;
import java.time.ZonedDateTime;

public interface ProductSaver {
    void saveProduct(CreateProductInput productInput, Instant now);
}
