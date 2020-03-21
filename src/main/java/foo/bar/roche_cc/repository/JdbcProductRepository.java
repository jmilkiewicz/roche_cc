package foo.bar.roche_cc.repository;

import foo.bar.roche_cc.model.Product;
import foo.bar.roche_cc.usecase.createProduct.CreateProductInput;
import foo.bar.roche_cc.usecase.createProduct.ProductSaver;
import foo.bar.roche_cc.usecase.deleteProduct.updateProduct.ProductDeleter;
import foo.bar.roche_cc.usecase.getAllProducts.AllProductsFetcher;
import foo.bar.roche_cc.usecase.updateProduct.ProductUpdater;
import foo.bar.roche_cc.usecase.updateProduct.UpdateProductInput;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcProductRepository implements ProductRepository, ProductSaver, ProductUpdater, AllProductsFetcher, ProductDeleter {
    private static final RowMapper<Product> productRowMapper = (rs, __) ->
            Product.builder()
                    .id(rs.getString("sku"))
                    .createdAt(rs.getTimestamp("createdAt").toInstant())
                    .name(rs.getString("name"))
                    .price(rs.getBigDecimal("price"))
                    .build();
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
    public Optional<Product> getById(String productId) {
        return jdbcTemplate.query("select * from Products where sku = ?", new Object[]{productId}, productRowMapper)
                .stream()
                .findFirst();

    }

    @Override
    public Map<String, Object> getById2(String productId) {
        return jdbcTemplate.queryForMap("select * from Products where sku = ?", new Object[]{productId});
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from Products");
    }

    @Override
    public void markAsDeleted(String productId) {
        deleteProduct(productId);
    }

    @Override
    public String saveProduct(CreateProductInput productInput, Instant now) {
        String id = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into Products(sku, createdAt, name, price) values(?,?, ?,?) ",
                id, Timestamp.from(now), productInput.getName(), productInput.getPrice());
        return id;
    }

    @Override
    public boolean updateProduct(String productId, UpdateProductInput updateProductInput) {
        int rowsAffected = jdbcTemplate.update("update Products set name = ?, price =? where sku = ?", updateProductInput.getName(), updateProductInput.getPrice(), productId);
        return rowsAffected > 0;
    }

    @Override
    public List<Product> getAllProducts() {
        return jdbcTemplate.query("select * from Products where deleted=false", productRowMapper);
    }

    @Override
    public boolean deleteProduct(String productId) {
        int rowsAffected = jdbcTemplate.update("update Products set deleted=true where sku = ?", productId);
        return rowsAffected > 0;
    }
}
