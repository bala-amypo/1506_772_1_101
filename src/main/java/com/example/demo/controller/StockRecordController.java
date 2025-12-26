/*package com.example.demo.controller;

import com.example.demo.model.StockRecord;
import com.example.demo.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockRecordController {

    private final StockRecordService stockRecordService;

    @PostMapping("/{productId}/{warehouseId}")
    public StockRecord createStockRecord(
            @PathVariable Long productId,
            @PathVariable Long warehouseId,
            @RequestBody StockRecord record
    ) {
        return stockRecordService.createStockRecord(productId, warehouseId, record);
    }

    @GetMapping("/{id}")
    public StockRecord getStockRecord(@PathVariable Long id) {
        return stockRecordService.getStockRecord(id);
    }

    @GetMapping("/product/{productId}")
    public List<StockRecord> getByProduct(@PathVariable Long productId) {
        return stockRecordService.getRecordsBy_product(productId);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<StockRecord> getByWarehouse(@PathVariable Long warehouseId) {
        return stockRecordService.getRecordsByWarehouse(warehouseId);
    }
}*/
package com.example.demo.controller;

import com.example.demo.model.StockRecord;
import com.example.demo.service.StockRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Tag(name = "Stock Records", description = "Stock record management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class StockRecordController {
    
    private final StockRecordService stockRecordService;
    
    @PostMapping("/{productId}/{warehouseId}")
    @Operation(summary = "Create a new stock record", 
               description = "Creates a stock record for a product in a warehouse")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Stock record created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid stock record data"),
        @ApiResponse(responseCode = "404", description = "Product or warehouse not found"),
        @ApiResponse(responseCode = "409", description = "Stock record already exists for this product-warehouse pair")
    })
    public ResponseEntity<StockRecord> createStockRecord(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId,
            @Parameter(description = "ID of the warehouse", required = true)
            @PathVariable Long warehouseId,
            @RequestBody StockRecord record) {
        
        StockRecord created = stockRecordService.createStockRecord(productId, warehouseId, record);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get stock record by ID", 
               description = "Returns a single stock record by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock record found"),
        @ApiResponse(responseCode = "404", description = "Stock record not found")
    })
    public ResponseEntity<StockRecord> getStockRecord(
            @Parameter(description = "ID of the stock record to retrieve", required = true)
            @PathVariable Long id) {
        StockRecord record = stockRecordService.getStockRecord(id);
        return ResponseEntity.ok(record);
    }
    
    @GetMapping("/product/{productId}")
    @Operation(summary = "Get stock records by product ID", 
               description = "Returns all stock records for a specific product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock records found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<List<StockRecord>> getRecordsByProduct(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId) {
        List<StockRecord> records = stockRecordService.getRecordsByProduct(productId);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    @Operation(summary = "Get stock records by warehouse ID", 
               description = "Returns all stock records in a specific warehouse")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock records found"),
        @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    public ResponseEntity<List<StockRecord>> getRecordsByWarehouse(
            @Parameter(description = "ID of the warehouse", required = true)
            @PathVariable Long warehouseId) {
        List<StockRecord> records = stockRecordService.getRecordsByWarehouse(warehouseId);
        return ResponseEntity.ok(records);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update stock record", 
               description = "Updates an existing stock record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock record updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid update data"),
        @ApiResponse(responseCode = "404", description = "Stock record not found")
    })
    public ResponseEntity<StockRecord> updateStockRecord(
            @Parameter(description = "ID of the stock record to update", required = true)
            @PathVariable Long id,
            @RequestBody StockRecord record) {
        
        StockRecord updated = stockRecordService.updateStockRecord(id, record);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete stock record", 
               description = "Deletes a stock record from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Stock record deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Stock record not found")
    })
    public ResponseEntity<Void> deleteStockRecord(
            @Parameter(description = "ID of the stock record to delete", required = true)
            @PathVariable Long id) {
        stockRecordService.deleteStockRecord(id);
        return ResponseEntity.noContent().build();
    }
}