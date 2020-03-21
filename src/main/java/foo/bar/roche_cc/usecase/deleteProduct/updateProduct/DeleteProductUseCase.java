package foo.bar.roche_cc.usecase.deleteProduct.updateProduct;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteProductUseCase {
    private final ProductDeleter productDeleter;

    public DeleteProductUseCase(ProductDeleter productDeleter) {
        this.productDeleter = productDeleter;
    }

    @Transactional
    public boolean execute(String productId) {
        return productDeleter.deleteProduct(productId);
    }
}
