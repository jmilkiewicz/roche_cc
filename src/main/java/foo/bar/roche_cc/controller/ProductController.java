package foo.bar.roche_cc.controller;

import foo.bar.roche_cc.model.Product;
import foo.bar.roche_cc.usecase.createProduct.CreateProductInput;
import foo.bar.roche_cc.usecase.createProduct.CreateProductUseCase;
import foo.bar.roche_cc.usecase.getAllProducts.GetAllProductsUseCase;
import foo.bar.roche_cc.usecase.updateProduct.UpdateProductInput;
import foo.bar.roche_cc.usecase.updateProduct.UpdateProductUseCase;
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

    public ProductController(CreateProductUseCase createProductUseCase, UpdateProductUseCase updateProductUseCase, GetAllProductsUseCase getAllProductsUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.getAllProductsUseCase = getAllProductsUseCase;
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
        if (updated) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<?> getAllProducts() {
        List<Product> allProducts = getAllProductsUseCase.execute();
        return ResponseEntity.ok(allProducts);
    }
}
