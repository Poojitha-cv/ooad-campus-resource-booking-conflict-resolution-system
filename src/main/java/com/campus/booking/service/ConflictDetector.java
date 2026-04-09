package com.campus.booking.service;

import com.campus.booking.model.Booking;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConflictDetector {

    public Booking detectConflict(Booking newBooking, List<Booking> existingBookings) {

        for (Booking b : existingBookings) {

            boolean overlap =
                    newBooking.getStartTime().isBefore(b.getEndTime()) &&
                    newBooking.getEndTime().isAfter(b.getStartTime());

            if (overlap) {
                return b;
            }
        }

        return null;
    }
}