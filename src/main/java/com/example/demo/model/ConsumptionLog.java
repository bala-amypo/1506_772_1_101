@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumptionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int consumedQuantity;
    private LocalDate consumedDate;

    @ManyToOne
    private StockRecord stockRecord;
}
