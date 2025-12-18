import java.util.List;

public interface ProductService {
    
    Product createProduct(Product product);
    
    Product getProduct(Long id);
    
    List<Product> getAllProducts();
}
