package org.sidequest.parley.controller;

import org.sidequest.parley.api.UsersApi;
import org.sidequest.parley.model.User;
import org.sidequest.parley.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * A RESTful web service controller for managing users.
 */
//@Service
@RestController
public class UserController implements UsersApi {
    /**
     * Retrieves a list of all users in JSON format.
     *
     * @return A JSON-formatted string containing an array of users.
     */
    @Override
//    public ResponseEntity<List<org.sidequest.parley.model.User>> getUsers() {
    public ResponseEntity<List<User>> getUsers() {
        try {
            UserService us = new UserService();
            List<User> users = us.getUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}


//@Path("/users")
//public class UserController implements UsersApi {
////	/**
////	 * Retrieves a list of all users in JSON format.
////	 *
////	 * @return A JSON-formatted string containing an array of users.
////	 */
////	@GET
////	@Produces(MediaType.APPLICATION_JSON)
////	public ResponseEntity<org.sidequest.parley.model.User> getUsers() throws IOException {
////		UserService us = new UserService();
////		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
////		for (User u : us.getUsers()) {
////			JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
////			objectBuilder.add("id", u.getId());
////			objectBuilder.add("name", u.getName());
////			arrayBuilder.add(objectBuilder.build());
////		}
////		return Json.createObjectBuilder().add("users", arrayBuilder.build()).build().toString();
////	}
//
//
//	/**
//	 * Retrieves a user by their ID.
//	 *
//	 * @param id The ID of the user to retrieve.
//	 * @return A User object representing the found user.
//	 * @throws FileNotFoundException If the file is not found.
//	 * @throws IOException           If an I/O error occurs.
//	 */
//	@GET
//	@Path("/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public User getUserById(@PathParam("id") int id) throws FileNotFoundException, IOException {
//		UserService us = new UserService();
//		User user = us.getUser(id);
//		if (user != null) {
//			return user;
//		} else {
//			// Return a 404 response if the user isn't found
//			throw new NotFoundException();
//		}
//	}
//
//	/**
//	 * Creates a new user using the provided input data.
//	 *
//	 * @param userInput A UserInput object containing the data for the new user.
//	 * @return A User object representing the created user.
//	 * @throws IOException If an I/O error occurs.
//	 */
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public User createUser(UserInput userInput) throws IOException {
//		UserService us = new UserService();
//		return us.createUser(userInput.getName());
//	}
//}