package foo.bar.roche_cc.repository;

import foo.bar.roche_cc.model.Product;

import java.util.Optional;

public interface ProductRepository {
    Integer countAll();

    Optional<Product> getById(String idOfCreatedProduct);
}
