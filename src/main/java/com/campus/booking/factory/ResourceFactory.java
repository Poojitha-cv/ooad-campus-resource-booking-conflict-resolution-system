package com.campus.booking.factory;

import com.campus.booking.model.Resource;

// Factory Method Pattern entry point
public class ResourceFactory {

    private static final java.util.Set<String> VALID_TYPES =
        java.util.Set.of("LAB", "ROOM", "AUDITORIUM", "SEMINAR_HALL", "SPORTS");

    public static Resource createResource(String name, String type) {
        String upperType = type.toUpperCase();
        if (!VALID_TYPES.contains(upperType)) {
            throw new IllegalArgumentException("Invalid resource type: " + type);
        }
        ResourceCreator creator = new ConcreteResourceCreator(upperType);
        return creator.createResource(name);
    }
}