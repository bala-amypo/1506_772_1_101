@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository repo;

    public Product createProduct(Product product) {
        return repo.save(product);
    }

    public Product getProduct(Long id) {
        return repo.findById(id).orElse(null);
    }

    public List<Product> getAllProducts() {
        return repo.findAll();
    }
}
