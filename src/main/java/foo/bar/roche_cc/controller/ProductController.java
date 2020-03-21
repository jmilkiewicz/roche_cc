package foo.bar.roche_cc.controller;

import foo.bar.roche_cc.usecase.createProduct.CreateProductInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/products")
public class ProductController {

    @PostMapping()
    public ResponseEntity<?> createProduct(@RequestBody CreateProductInput input, UriComponentsBuilder b) {

        UriComponents uriComponents =
                b.path("/products/{id}").buildAndExpand(123);
        return ResponseEntity.created(uriComponents.toUri()).build();
    }
}
