package com.sidequest.parley.config;


//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Info;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api") // Set the base path for your API
//@OpenAPIDefinition(info = @Info(title = "Parley API", version = "1.0", description = "API for Parley application")) // Provide API information
public class parleyConfig extends Application {
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

        // Register your JAX-RS resource classes here
        resources.add(com.sidequest.parley.controller.ChatMessageController.class);
        resources.add(com.sidequest.parley.controller.UserController.class);
        resources.add(com.sidequest.parley.controller.ChatRoomController.class);

        // Register your Swagger classes here
        //      resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        //      resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        return resources;
    }

}