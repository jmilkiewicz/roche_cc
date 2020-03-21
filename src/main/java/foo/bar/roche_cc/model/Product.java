package foo.bar.roche_cc.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class Product {
    private final String id;
    private final Instant createdAt;
    private String name;
    private BigDecimal price;

}
