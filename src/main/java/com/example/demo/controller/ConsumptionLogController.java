/*package com.example.demo.controller;

import com.example.demo.model.ConsumptionLog;
import com.example.demo.service.ConsumptionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumption")
@RequiredArgsConstructor
public class ConsumptionLogController {

    private final ConsumptionLogService consumptionLogService;

    @PostMapping("/{stockRecordId}")
    public ConsumptionLog logConsumption(
            @PathVariable Long stockRecordId,
            @RequestBody ConsumptionLog log
    ) {
        return consumptionLogService.logConsumption(stockRecordId, log);
    }

    @GetMapping("/record/{stockRecordId}")
    public List<ConsumptionLog> getLogsByStockRecord(
            @PathVariable Long stockRecordId
    ) {
        return consumptionLogService.getLogsByStockRecord(stockRecordId);
    }

    @GetMapping("/{id}")
    public ConsumptionLog getLog(@PathVariable Long id) {
        return consumptionLogService.getLog(id);
    }
}*/
package com.example.demo.controller;

import com.example.demo.model.ConsumptionLog;
import com.example.demo.service.ConsumptionLogService;
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
@RequestMapping("/api/consumption")
@RequiredArgsConstructor
@Tag(name = "Consumption Logs", description = "Consumption log management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ConsumptionLogController {
    
    private final ConsumptionLogService consumptionLogService;
    
    @PostMapping("/{stockRecordId}")
    @Operation(summary = "Log consumption", 
               description = "Creates a consumption log entry for a stock record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Consumption logged successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid consumption data"),
        @ApiResponse(responseCode = "404", description = "Stock record not found")
    })
    public ResponseEntity<ConsumptionLog> logConsumption(
            @Parameter(description = "ID of the stock record", required = true)
            @PathVariable Long stockRecordId,
            @RequestBody ConsumptionLog log) {
        
        ConsumptionLog created = consumptionLogService.logConsumption(stockRecordId, log);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get consumption log by ID", 
               description = "Returns a single consumption log by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consumption log found"),
        @ApiResponse(responseCode = "404", description = "Consumption log not found")
    })
    public ResponseEntity<ConsumptionLog> getLog(
            @Parameter(description = "ID of the consumption log to retrieve", required = true)
            @PathVariable Long id) {
        ConsumptionLog log = consumptionLogService.getLog(id);
        return ResponseEntity.ok(log);
    }
    
    @GetMapping("/record/{stockRecordId}")
    @Operation(summary = "Get consumption logs by stock record ID", 
               description = "Returns all consumption logs for a specific stock record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consumption logs found"),
        @ApiResponse(responseCode = "404", description = "Stock record not found")
    })
    public ResponseEntity<List<ConsumptionLog>> getLogsByStockRecord(
            @Parameter(description = "ID of the stock record", required = true)
            @PathVariable Long stockRecordId) {
        List<ConsumptionLog> logs = consumptionLogService.getLogsByStockRecord(stockRecordId);
        return ResponseEntity.ok(logs);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete consumption log", 
               description = "Deletes a consumption log from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Consumption log deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Consumption log not found")
    })
    public ResponseEntity<Void> deleteLog(
            @Parameter(description = "ID of the consumption log to delete", required = true)
            @PathVariable Long id) {
        consumptionLogService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }
}