# OOAD Mini Project  
## Campus Resource Booking & Conflict Resolution System

---

## 👤 Team Member Details

**Member 1:** Poojitha CV  
**SRN:** PES2UG23CS415  

---

## 📌 My Contribution (Member 1)

### 🔹 Resource Booking System
Implemented complete booking functionality including:
- Resource search
- Booking form
- Time slot selection
- Booking confirmation

---

### 🔹 Conflict Detection Engine
Implemented logic to detect overlapping bookings for the same resource.

**Logic used:**
(newStart < existingEnd) AND (newEnd > existingStart)

- Prevents double booking
- Ensures only valid time slots are booked

---

### 🔹 Conflict UI Screen
- Displays conflicting bookings side-by-side
- Shows:
  - Existing booking
  - New booking request
- Helps identify which booking should be accepted

---

### 🔹 User Authentication
- User Registration
- Login functionality

---

## 🧠 Design Used

### GRASP Principle
- **Information Expert**
- BookingService handles booking and conflict logic

---

### Design Pattern

**Factory Pattern**
- ResourceFactory is used to create different types of resources dynamically

---

## 🏗️ Architecture (MVC)

**Model**
- Booking
- User
- Resource

**View**
- HTML pages using Thymeleaf

**Controller**
- Handles user requests and application flow

---

## 🗄️ Database

Tables used:
- Users
- Resources
- Bookings

---

## ⚙️ Technologies Used
- Java  
- Spring Boot  
- Spring Data JPA  
- MySQL  
- Thymeleaf  

---

## 📂 Project Structure

src/main/java/com/campus/booking/
- controller/
- service/
- repository/
- model/
- factory/

src/main/resources/templates/
- login.html
- register.html
- search.html
- booking-form.html
- confirmation.html
- conflict.html
- admin-dashboard.html

---

## 📌 Summary
This module focuses on implementing a robust booking system with conflict detection using proper MVC architecture, GRASP principles, and design patterns.
