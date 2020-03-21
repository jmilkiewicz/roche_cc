package foo.bar.roche_cc.usecase.createProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductInput {
    private String name;
    private BigDecimal price;
}
