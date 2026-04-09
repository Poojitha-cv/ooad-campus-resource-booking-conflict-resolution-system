package com.campus.booking.service;

import com.campus.booking.model.*;
import com.campus.booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * GRASP: Information Expert
 * BookingService owns all data needed to create and validate bookings
 * (BookingRepository, ResourceRepository, UserRepository, ConflictDetector),
 * so it is the natural expert assigned booking responsibilities.
 */

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConflictDetector conflictDetector;

    public Object createBooking(BookingRequest request) {

        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking newBooking = new Booking();
        newBooking.setResource(resource);
        newBooking.setUser(user);
        newBooking.setStartTime(request.getStartTime());
        newBooking.setEndTime(request.getEndTime());
        newBooking.setPurpose(request.getPurpose());  // ✅ Set purpose
        newBooking.setStatus("PENDING");

        if (request.getStartTime().isAfter(request.getEndTime())) {
            return "Invalid time range!";
        }

        List<Booking> existingBookings = bookingRepository.findByResourceId(resource.getId());

        Booking conflict = conflictDetector.detectConflict(newBooking, existingBookings);

        if (conflict != null) {
            newBooking.setStatus("CONFLICT");
            bookingRepository.save(newBooking);

            ConflictResponse response = new ConflictResponse();
            response.setExistingBooking(conflict);
            response.setNewBooking(newBooking);
            response.setWinner("EXISTING BOOKING WINS");  // ✅ FCFS: earlier booking wins
            return response;
        }

        newBooking.setStatus("CONFIRMED");
        bookingRepository.save(newBooking);

        return "Booking Confirmed!";
    }

    public List<Booking> getBookingsByResource(Long resourceId) {
        return bookingRepository.findByResourceId(resourceId);
    }
}