public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {
    List<StockRecord> findByProductId(Long productId);
    List<StockRecord> findByWarehouseId(Long warehouseId);
    boolean existsByProductIdAndWarehouseId(Long productId, Long warehouseId);
}
