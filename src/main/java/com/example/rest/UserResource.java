package com.example.rest;

import com.example.entity.UserRepository;
import com.example.entity.model.User;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Logger;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    private UserRepository userRepository;

    @GET
    public List<User> getAllUsers() {
        logger.info("Getting all users");
        return userRepository.getAllUsers();
    }

    @GET
    @Path("/{userId}")
    public User getUserById(@PathParam("userId") String email) {
        logger.info("Getting user by id");
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @POST
    @Path("/create")
    public User addUser(User user) {
        logger.info("Creating user " + user.getFirstname() + " " + user.getLastname());
        userRepository.addUser(user);
        return user;
    }

    @PUT
    @Path("/{email}")
    public String updateUser(@PathParam("email") String email, User user) {
        try {
            logger.info("Updating user " + email);
            user.setEmail(email);
            userRepository.updateUser(user);
            return "Success";
        } catch (PersistenceException ex) {
            logger.info("Error updating user " + email);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }
}
