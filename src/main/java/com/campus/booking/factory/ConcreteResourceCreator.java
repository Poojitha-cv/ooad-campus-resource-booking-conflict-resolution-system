package com.campus.booking.factory;

import com.campus.booking.model.Resource;

// Concrete Creator — handles all resource types, type is injected via constructor
public class ConcreteResourceCreator extends ResourceCreator {

    private final String type;

    public ConcreteResourceCreator(String type) {
        this.type = type;
    }

    @Override
    public Resource createResource(String name) {
        Resource resource = new Resource();
        resource.setName(name);
        resource.setType(type);
        return resource;
    }
}