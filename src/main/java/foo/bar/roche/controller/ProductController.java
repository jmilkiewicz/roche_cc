package foo.bar.roche.controller;

import foo.bar.roche.model.Product;
import foo.bar.roche.usecase.createProduct.CreateProductInput;
import foo.bar.roche.usecase.createProduct.CreateProductUseCase;
import foo.bar.roche.usecase.deleteProduct.updateProduct.DeleteProductUseCase;
import foo.bar.roche.usecase.getAllProducts.GetAllProductsUseCase;
import foo.bar.roche.usecase.updateProduct.UpdateProductInput;
import foo.bar.roche.usecase.updateProduct.UpdateProductUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public ProductController(CreateProductUseCase createProductUseCase, UpdateProductUseCase updateProductUseCase, GetAllProductsUseCase getAllProductsUseCase, DeleteProductUseCase deleteProductUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.getAllProductsUseCase = getAllProductsUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @PostMapping()
    public ResponseEntity<?> createProduct(@RequestBody CreateProductInput input, UriComponentsBuilder b) {
        String productId = createProductUseCase.execute(input, Instant.now());

        UriComponents uriComponents =
                b.path("/products/{productId}").buildAndExpand(productId);
        return ResponseEntity.created(uriComponents.toUri()).build();
    }


    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody UpdateProductInput input, @PathVariable String productId) {
        boolean updated = updateProductUseCase.execute(productId, input);
        return noContentOrNotFound(updated);
    }

    private ResponseEntity<?> noContentOrNotFound(boolean updated) {
        if (updated) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        boolean updated = deleteProductUseCase.execute(productId);
        return noContentOrNotFound(updated);
    }

    @GetMapping()
    public ResponseEntity<?> getAllProducts() {
        List<Product> allProducts = getAllProductsUseCase.execute();
        return ResponseEntity.ok(allProducts);
    }
}