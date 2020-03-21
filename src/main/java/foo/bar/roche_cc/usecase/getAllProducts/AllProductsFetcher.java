package foo.bar.roche_cc.usecase.getAllProducts;

import foo.bar.roche_cc.model.Product;

import java.util.List;

public interface AllProductsFetcher {
    List<Product> getAllProducts();
}
