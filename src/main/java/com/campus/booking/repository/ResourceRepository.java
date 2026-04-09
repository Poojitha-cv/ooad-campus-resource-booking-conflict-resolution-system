package com.campus.booking.repository;

import com.campus.booking.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    // 🔍 Search by type (LAB / ROOM / AUDITORIUM)
    List<Resource> findByType(String type);

    // 🔍 Search by name (optional, improves UI)
    List<Resource> findByNameContainingIgnoreCase(String name);

    // 🔍 Combined filter (better for viva demo)
    List<Resource> findByTypeAndNameContainingIgnoreCase(String type, String name);
}