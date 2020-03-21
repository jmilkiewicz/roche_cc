package foo.bar.roche.repository;

import foo.bar.roche.model.Product;
import foo.bar.roche.usecase.createProduct.ProductSaver;

import java.util.Map;
import java.util.Optional;

//probably we shall create our own method for saving product to DB instead of reusing ProductSaver
//it would allow more flexibility regarding saving
public interface ProductRepository extends ProductSaver {
    Integer countAll();

    Optional<Product> getById(String productId);

    Map<String, Object> getByIdRaw(String productId);

    void deleteAll();

    void markAsDeleted(String productId);
}
