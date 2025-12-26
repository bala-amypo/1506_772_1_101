/*package com.example.demo.controller;

import com.example.demo.model.PredictionRule;
import com.example.demo.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/predict")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;

    @GetMapping("/restock-date/{stockRecordId}")
    public LocalDate predictRestockDate(@PathVariable Long stockRecordId) {
        return predictionService.predictRestockDate(stockRecordId);
    }

    @PostMapping("/rules")
    public PredictionRule createRule(@RequestBody PredictionRule rule) {
        return predictionService.createRule(rule);
    }

    @GetMapping("/rules")
    public List<PredictionRule> getAllRules() {
        return predictionService.getAllRules();
    }
}
*/
package com.example.demo.controller;

import com.example.demo.model.PredictionRule;
import com.example.demo.service.PredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/predict")
@RequiredArgsConstructor
@Tag(name = "Predictions", description = "Prediction management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class PredictionController {
    
    private final PredictionService predictionService;
    
    @GetMapping("/restock-date/{stockRecordId}")
    @Operation(summary = "Predict restock date", 
               description = "Predicts when a product needs to be restocked based on consumption patterns")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restock date predicted successfully"),
        @ApiResponse(responseCode = "404", description = "Stock record or prediction rules not found")
    })
    public ResponseEntity<LocalDate> predictRestockDate(
            @Parameter(description = "ID of the stock record", required = true)
            @PathVariable Long stockRecordId) {
        LocalDate restockDate = predictionService.predictRestockDate(stockRecordId);
        return ResponseEntity.ok(restockDate);
    }
    
    @PostMapping("/rules")
    @Operation(summary = "Create a new prediction rule", 
               description = "Creates a new prediction rule for restock calculations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Prediction rule created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid rule data"),
        @ApiResponse(responseCode = "409", description = "Rule name already exists")
    })
    public ResponseEntity<PredictionRule> createRule(@RequestBody PredictionRule rule) {
        PredictionRule created = predictionService.createRule(rule);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    @GetMapping("/rules")
    @Operation(summary = "Get all prediction rules", 
               description = "Returns a list of all prediction rules in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved prediction rules")
    public ResponseEntity<List<PredictionRule>> getAllRules() {
        List<PredictionRule> rules = predictionService.getAllRules();
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/rules/{id}")
    @Operation(summary = "Get prediction rule by ID", 
               description = "Returns a single prediction rule by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prediction rule found"),
        @ApiResponse(responseCode = "404", description = "Prediction rule not found")
    })
    public ResponseEntity<PredictionRule> getRule(
            @Parameter(description = "ID of the prediction rule to retrieve", required = true)
            @PathVariable Long id) {
        PredictionRule rule = predictionService.getRule(id);
        return ResponseEntity.ok(rule);
    }
    
    @DeleteMapping("/rules/{id}")
    @Operation(summary = "Delete prediction rule", 
               description = "Deletes a prediction rule from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Prediction rule deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Prediction rule not found")
    })
    public ResponseEntity<Void> deleteRule(
            @Parameter(description = "ID of the prediction rule to delete", required = true)
            @PathVariable Long id) {
        predictionService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
}