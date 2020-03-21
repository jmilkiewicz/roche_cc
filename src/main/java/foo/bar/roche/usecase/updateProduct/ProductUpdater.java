package foo.bar.roche.usecase.updateProduct;

public interface ProductUpdater {
    boolean updateProduct(String productId, UpdateProductInput updateProductInput);
}
