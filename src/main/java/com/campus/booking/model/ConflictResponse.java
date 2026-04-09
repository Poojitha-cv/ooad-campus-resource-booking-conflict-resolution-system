package com.campus.booking.model;

public class ConflictResponse {

    private Booking existingBooking;
    private Booking newBooking;
    private String winner;

    public Booking getExistingBooking() { return existingBooking; }
    public void setExistingBooking(Booking existingBooking) { this.existingBooking = existingBooking; }

    public Booking getNewBooking() { return newBooking; }
    public void setNewBooking(Booking newBooking) { this.newBooking = newBooking; }

    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
}