package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Booking;
import com.example.model.Train;
import com.example.model.User;
import com.example.repo.BookingRepository;
import com.example.repo.TrainRepository;
import com.example.repo.UserRepository;
import com.example.security.JwtTokenProvider;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Validates the token and fetches the user
    private User validateTokenAndGetUser(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }
        String username = jwtTokenProvider.getUsernameFromToken(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public void bookSeat(String token, Long trainId) {
        User user = validateTokenAndGetUser(token);

        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new IllegalArgumentException("Train not found"));

        if (train.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("No seats available");
        }

        // Update train's available seats atomically
        train.setAvailableSeats(train.getAvailableSeats() - 1);
        trainRepository.save(train);

        // Save booking details
        Booking booking = new Booking();
        booking.setUserId(user.getId());
        booking.setTrainId(trainId);
        booking.setBookingTime(LocalDateTime.now());
        bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(String token) {
        User user = validateTokenAndGetUser(token);
        return bookingRepository.findByUserId(user.getId());
    }
}
