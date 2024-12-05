package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.model.Train;
import com.example.repo.TrainRepository;

import java.util.List;

@Service
public class TrainService {

    @Value("${admin.api.key}")
    private String adminApiKey;

    private final TrainRepository trainRepository;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public void validateAdminApiKey(String apiKey) {
        if (!adminApiKey.equals(apiKey)) {
            throw new IllegalArgumentException("Invalid Admin API Key");
        }
    }

    public void addTrain(Train train) {
        trainRepository.save(train);
    }

    public List<Train> getAvailableTrains(String source, String destination) {
        return trainRepository.findBySourceAndDestination(source, destination);
    }
}
