package foo.bar.roche_cc.usecase.updateProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductInput {
    private String name;
    private BigDecimal price;
}
