package foo.bar.roche.usecase.updateProduct;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateProductUseCase {
    private final ProductUpdater productUpdater;

    public UpdateProductUseCase(ProductUpdater productUpdater) {
        this.productUpdater = productUpdater;
    }

    @Transactional
    public boolean execute(String productId, UpdateProductInput updateProductInput) {
        return productUpdater.updateProduct(productId, updateProductInput);
    }
}
