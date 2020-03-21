package foo.bar.roche_cc.controller;

import foo.bar.roche_cc.usecase.createProduct.CreateProductInput;
import foo.bar.roche_cc.usecase.createProduct.ProductSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductSaver productSaver;

    @PostMapping()
    public ResponseEntity<?> createProduct(@RequestBody CreateProductInput input, UriComponentsBuilder b) {

        String productId = productSaver.saveProduct(input, Instant.now());

        UriComponents uriComponents =
                b.path("/products/{productId}").buildAndExpand(productId);
        return ResponseEntity.created(uriComponents.toUri()).build();
    }
}
