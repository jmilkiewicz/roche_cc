package foo.bar.roche.usecase.getAllProducts;

import foo.bar.roche.model.Product;

import java.util.List;

public interface AllProductsFetcher {
    List<Product> getAllProducts();
}
