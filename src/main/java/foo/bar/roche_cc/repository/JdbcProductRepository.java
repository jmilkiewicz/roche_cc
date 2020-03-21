package foo.bar.roche_cc.repository;

import foo.bar.roche_cc.model.Product;
import foo.bar.roche_cc.usecase.createProduct.CreateProductInput;
import foo.bar.roche_cc.usecase.createProduct.ProductSaver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcProductRepository implements ProductRepository, ProductSaver {
    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Product> productRowMapper = (rs, __) ->
            Product.builder()
                    .id(rs.getString("sku"))
                    .createdAt(rs.getTimestamp("createdAt").toInstant())
                    .name(rs.getString("name"))
                    .price(rs.getBigDecimal("price"))
                    .build();



    public JdbcProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer countAll() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Products", Integer.class);
    }

    @Override
    public Optional<Product> getById(String productId) {
        return jdbcTemplate.query("select * from Products where sku = ?", new Object[]{productId}, productRowMapper)
                .stream()
                .findFirst();

    }

    @Override
    public String saveProduct(CreateProductInput productInput, Instant now) {
        String id = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into Products(sku, createdAt, name, price) values(?,?, ?,?) ",
                id, Timestamp.from(now), productInput.getName(), productInput.getPrice());
        return id;
    }
}
