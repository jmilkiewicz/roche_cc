package foo.bar.roche_cc.usecase.updateProduct;

import org.springframework.stereotype.Component;

@Component
public class UpdateProductUseCase {
    private final ProductUpdater productUpdater;

    public UpdateProductUseCase(ProductUpdater productUpdater) {
        this.productUpdater = productUpdater;
    }


    public void execute(String productId, UpdateProductInput updateProductInput) {
        productUpdater.updateProduct(productId, updateProductInput);
    }
}
