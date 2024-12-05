package com.example.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.model.Train;
import com.example.service.TrainService;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    @Autowired
    private TrainService trainService;

    // API Key for Admin endpoints
    private static final String ADMIN_API_KEY = "your_secure_admin_key";

    @PostMapping("/add")
    public ResponseEntity<String> addTrain(
            @RequestHeader("X-API-KEY") String apiKey,
            @RequestBody Train train) {
        if (!ADMIN_API_KEY.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }
        trainService.addTrain(train);
        return ResponseEntity.ok("Train added successfully!");
    }

    @GetMapping("/availability")
    public ResponseEntity<List<Train>> getAvailableTrains(
            @RequestParam String source,
            @RequestParam String destination) {
        return ResponseEntity.ok(trainService.getAvailableTrains(source, destination));
    }
}
