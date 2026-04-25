# Campus Resource Booking & Conflict Resolution System

A Spring Boot web application for booking campus resources (labs, rooms, auditoriums) with built-in conflict detection, waitlist management, and admin arbitration.

**Tech Stack:** Java 21 · Spring Boot 3.5 · Spring MVC · Spring Data JPA · Thymeleaf · MySQL · Lombok · Maven

---

## MVC Architecture

**Yes** — Spring MVC is used throughout.

| Layer | Implementation |
|---|---|
| **Model** | `Booking`, `Resource`, `User`, `WaitlistEntry`, `Notification`, `Rating` (JPA entities) |
| **View** | Thymeleaf HTML templates (`search.html`, `booking-form.html`, `conflict.html`, `admin-dashboard.html`, etc.) |
| **Controller** | `BookingController`, `AdminController`, `WaitlistController`, `UserController`, `ResourceController`, `RatingController`, `NotificationController` |

---

## Design Principles (GRASP)

### Information Expert → `BookingService`
`BookingService` owns all the information needed to create and validate a booking — it holds references to `BookingRepository`, `ResourceRepository`, `UserRepository`, and `ConflictDetector`. Because it has the data, it is assigned the responsibility of orchestrating booking creation, conflict checking, and waitlist entry. No other class needs to coordinate this logic.

### Low Coupling → `WaitlistService`
`WaitlistService` depends only on repositories and the `BookingEventObserver` interface — never on concrete controller or service classes. This means cancellation, promotion, and resolution logic can change independently without rippling across the system.

### High Cohesion → `ReportService`
`ReportService` has exactly one responsibility: generating admin-facing report data (total bookings, conflict counts, most-booked resource, bookings per resource). It does not touch booking creation, conflict detection, or notifications. `AdminController` delegates all data-gathering to it rather than querying repositories directly.

---

## Design Patterns

### Factory Method → `ResourceFactory` / `ResourceCreator`
**Where:** `factory/` package

`ResourceCreator` is the abstract creator defining the factory method `createResource(name)`. Concrete subclasses `LabResourceCreator`, `RoomResourceCreator`, and `AuditoriumResourceCreator` each produce a `Resource` with the correct type and default capacity. `ResourceFactory` picks the right creator by resource type string and delegates object creation. Adding a new resource type requires only a new subclass with zero changes to existing code.

### Observer → `BookingEventObserver` / `NotificationObserver`
**Where:** `observer/` package

`BookingEventObserver` defines the subject interface with four events: `onBookingConfirmed`, `onBookingCancelled`, `onBookingPromoted`, `onBookingConflict`. `NotificationObserver` is the concrete observer — it persists a `Notification` record to the database for every event. `BookingService` and `WaitlistService` fire events on state changes without knowing anything about how notifications are delivered.

### Strategy → `ConflictResolutionStrategy`
**Where:** `strategy/` package

`ConflictResolutionStrategy` is the interface with a single method `selectCandidate(queue)`. Three concrete strategies are implemented:
- `FcfsResolutionStrategy` — picks the earliest waitlist entry (First Come First Served)
- `PriorityResolutionStrategy` — picks by role priority (Faculty > Admin > Student)
- `AlternateRoomResolutionStrategy` — finds a free resource of the same type and reassigns the booking

`WaitlistService.resolveWithStrategy()` selects the appropriate strategy at runtime based on the user's choice, with no conditional branching in the caller.

### Decorator → `RecurringBookingDecorator`
**Where:** `decorator/` package

`RecurringBookingDecorator` wraps `BookingService` without modifying it. When an admin creates a recurring booking, the decorator calls `bookingService.createBooking()` in a loop — once per week for the requested number of weeks — shifting start/end times by 7 days each iteration. Conflict detection and notification still fire normally for each individual booking because the base service is unchanged.

---

## Team Contributions

| Member | Feature | Description | Key Classes |
|---|---|---|---|
| **Poojitha CV** | Resource Booking | Search & availability screen, calendar view, booking form, confirmation flow | `BookingController`, `search.html`, `booking-form.html`, `confirmation.html` |
| **Poojitha CV** | Conflict Detection Engine *(shared)* | Overlap query logic, fires conflict response on booking creation | `ConflictDetector`, `ConflictResponse`, `BookingService` |
| **Poojitha CV** | Registration & Login | User registration, login, session management, role-based redirect | `UserController`, `UserService`, `login.html`, `register.html` |
| **Poojitha CV** | Conflict UI Screen *(shared)* | Displays both conflicting bookings side by side with winner/loser outcome | `conflict.html` |
| **Poojitha CV** | GRASP & Pattern | Information Expert → `BookingService` · Factory Method → `ResourceFactory` | `ResourceFactory`, `ResourceCreator`, `LabResourceCreator`, `RoomResourceCreator`, `AuditoriumResourceCreator` |
| **Podamala Pragna** | Cancellation & Waitlist | Cancel confirmed bookings, auto-promote next waitlisted user (FCFS), queue management | `WaitlistService`, `WaitlistController`, `WaitlistEntry`, `waitlist.html`, `cancel-result.html` |
| **Podamala Pragna** | Resolution Strategies *(shared)* | Three pluggable conflict resolution strategies selectable at runtime | `FcfsResolutionStrategy`, `PriorityResolutionStrategy`, `AlternateRoomResolutionStrategy`, `resolve-strategy.html` |
| **Podamala Pragna** | Rating & Feedback | Submit and view ratings for resources after booking | `RatingController`, `Rating`, `RatingRepository`, `rating-form.html`, `resource-ratings.html` |
| **Podamala Pragna** | Notification System *(shared)* | Observer-based alerts persisted to DB for all booking events (confirmed, cancelled, promoted, conflict) | `NotificationObserver`, `BookingEventObserver`, `Notification`, `notifications.html` |
| **Podamala Pragna** | GRASP & Pattern | Low Coupling → `WaitlistService` · Observer → `NotificationObserver` | `BookingEventObserver`, `NotificationObserver`, `NotificationRepository` |
| **Nithya K** | Admin Dashboard + Resource CRUD + Reports | Full admin panel with booking stats, resource add/edit/delete, reports table | `AdminController`, `ReportService`, `admin-dashboard.html`, `admin-reports.html` |
| **Nithya K** | Winner/Loser Outcome + Admin Arbitration *(shared)* | Admin force-resolves conflicts manually; displays winner and loser outcome | `admin-arbitration.html`, `admin-arbitration-result.html`, `WaitlistService.forceResolve()` |
| **Nithya K** | Recurring Booking | Decorator-based bulk booking creation — repeats a booking weekly for N weeks | `RecurringBookingDecorator`, `admin-recurring.html`, `admin-recurring-result.html` |
| **Nithya K** | Admin Arbitration Screen *(shared)* | UI for admin to manually pick winner from conflicting bookings | `admin-arbitration.html` |
| **Nithya K** | GRASP & Patterns | High Cohesion → `ReportService` · Strategy → `ConflictResolutionStrategy` · Decorator → `RecurringBookingDecorator` | `ReportService`, `ConflictResolutionStrategy`, `RecurringBookingDecorator` |

---

## Project Structure

```
src/main/java/com/campus/booking/
├── controller/       # MVC Controllers
├── service/          # Business logic (BookingService, WaitlistService, ReportService, ...)
├── model/            # JPA Entities
├── repository/       # Spring Data JPA Repositories
├── factory/          # Factory Method pattern
├── observer/         # Observer pattern
├── strategy/         # Strategy pattern
├── decorator/        # Decorator pattern
└── config/           # DataLoader, WebMvcConfig
src/main/resources/
├── templates/        # Thymeleaf HTML views
└── application.properties
```

---

## Setup & Run

**Prerequisites:** Java 21, Maven, MySQL

```sql
CREATE DATABASE campus_booking;
```

Update `src/main/resources/application.properties` with your MySQL credentials, then:

```bash
mvn spring-boot:run
```

Access at `http://localhost:8080`. Default admin credentials are seeded by `DataLoader` on first run.
