package foo.bar.roche_cc.repository;

import foo.bar.roche_cc.usecase.createProduct.CreateProductInput;
import foo.bar.roche_cc.usecase.createProduct.ProductSaver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Repository
public class JdbcProductRepository implements ProductRepository, ProductSaver {
    private final JdbcTemplate jdbcTemplate;

    public JdbcProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer countAll() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Products", Integer.class);
    }

    @Override
    public void saveProduct(CreateProductInput productInput, Instant now) {
        jdbcTemplate.update("insert into Products(sku, createdAt) values(?,?) ",
                UUID.randomUUID().toString(), Timestamp.from(now));
    }
}
