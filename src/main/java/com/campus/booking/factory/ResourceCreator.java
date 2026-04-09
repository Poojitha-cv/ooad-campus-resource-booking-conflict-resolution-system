package com.campus.booking.factory;

import com.campus.booking.model.Resource;

// Factory Method Pattern — Abstract Creator
public abstract class ResourceCreator {

    // The Factory Method — subclasses implement this
    public abstract Resource createResource(String name);
}