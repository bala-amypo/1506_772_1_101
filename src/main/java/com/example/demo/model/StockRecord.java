@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int currentQuantity;
    private int reorderThreshold;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Warehouse warehouse;

    private LocalDateTime lastUpdated;
}
