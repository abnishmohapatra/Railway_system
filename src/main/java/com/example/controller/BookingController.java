package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.model.Booking;
import com.example.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Endpoint to book a seat on a train.
     * 
     * @param token   Authorization token from the user
     * @param trainId ID of the train to book a seat
     * @return Response indicating booking success or failure
     */
    @PostMapping("/book")
    public ResponseEntity<String> bookSeat(
            @RequestHeader("Authorization") String token,
            @RequestParam Long trainId) {
        try {
            // Book the seat
            bookingService.bookSeat(token, trainId);
            return ResponseEntity.ok("Booking successful!");
        } catch (IllegalArgumentException e) {
            // Handle specific errors for better feedback
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Booking failed: " + e.getMessage());
        } catch (Exception e) {
            // General exception fallback
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Endpoint to fetch all bookings for a logged-in user.
     * 
     * @param token Authorization token from the user
     * @return List of user bookings
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserBookings(
            @RequestHeader("Authorization") String token) {
        try {
            // Fetch user bookings
            List<Booking> bookings = bookingService.getUserBookings(token);
            return ResponseEntity.ok(bookings);
        } catch (IllegalArgumentException e) {
            // Handle specific errors for better feedback
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch bookings: " + e.getMessage());
        } catch (Exception e) {
            // General exception fallback
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
