package foo.bar.roche_cc.usecase.getAllProducts;

import foo.bar.roche_cc.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllProductsUseCase {
    private final AllProductsFetcher allProductsFetcher;

    public GetAllProductsUseCase(AllProductsFetcher allProductsFetcher) {
        this.allProductsFetcher = allProductsFetcher;
    }

    public List<Product> execute(){
        return allProductsFetcher.getAllProducts();
    }
}
