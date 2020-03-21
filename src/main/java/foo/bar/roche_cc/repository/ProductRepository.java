package foo.bar.roche_cc.repository;

import foo.bar.roche_cc.model.Product;
import foo.bar.roche_cc.usecase.createProduct.ProductSaver;

import java.util.Map;
import java.util.Optional;

public interface ProductRepository extends ProductSaver {
    Integer countAll();

    Optional<Product> getById(String idOfCreatedProduct);

    Map<String,Object> getById2(String idOfCreatedProduct);

    void deleteAll();
}
