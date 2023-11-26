package com.sidequest.parley.controller;


import com.sidequest.parley.service.ChatRoomService;
import org.sidequest.parley.api.ChatroomsApi;
import org.sidequest.parley.model.ChatRoom;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatRoomController implements ChatroomsApi {

    @Override
    public ResponseEntity<List<ChatRoom>> getChatRooms() {

        try {
            ChatRoomService crs = new ChatRoomService();
            List<ChatRoom> chatRooms = crs.getChatRooms();
            return ResponseEntity.ok(chatRooms);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}

//@Path("/chatrooms")
//public class ChatRoomController {
//
//    /**
//     * Retrieves a list of chat room IDs in JSON format.
//     *
//     * @return A JSON-formatted integers containing an array of chat ids.
//     * @throws IOException If an I/O error occurs.
//     */
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getChatRooms() throws IOException {
//        ChatRoomService crs = new ChatRoomService();
//        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
//        for (ChatRoom c : crs.getChatRooms()) {
//            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
//            objectBuilder.add("chatRoomId", c.getChatRoomId());
//            objectBuilder.add("name", c.getName());
//            objectBuilder.add("moderatorId", c.getModeratorId());
//            objectBuilder.add("userIds", c.getUserIds().toString());
//            byte[] icon = c.getIcon();
//            if (icon != null && icon.length > 0) {
//                String base64Icon = Base64.getEncoder().encodeToString(icon);
//                objectBuilder.add("icon", base64Icon);
//            }
//            arrayBuilder.add(objectBuilder.build());
//
//        }
//        return Json.createObjectBuilder().add("chatrooms", arrayBuilder.build()).build().toString();
//    }
//
//    /**
//     * Retrieves a chat room by its ID.
//     *
//     * @param id The ID of the chat room to retrieve.
//     * @return A ChatRoom object representing the found chat room.
//     * @throws IOException If an I/O error occurs.
//     */
//    @GET
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public ChatRoom getChatRoomById(@PathParam("id") int id) throws IOException {
//        ChatRoomService crs = new ChatRoomService();
//        ChatRoom c = crs.getChatRoom(id);
//        if (c == null) {
//            throw new NotFoundException();
//        }
//        return c;
//    }
//
//    /**
//     * Creates a new chat room.
//     *
//     * @param chatRoomInput The chat room to create.
//     * @return The created chat room.
//     * @throws IOException If an I/O error occurs.
//     */
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public ChatRoom createChatRoom(ChatRoomInput chatRoomInput) throws IOException, SQLException {
//        ChatRoomService crs = new ChatRoomService();
//        try {
//            return crs.createChatRoom(chatRoomInput.getName(), chatRoomInput.getModeratorId(), chatRoomInput.getUserIds(), chatRoomInput.getIcon());
//        } catch (ForeignKeyConstraintException e) {
//            throw new ForeignKeyConstraintException();
//        } catch (SQLException e) {
//            throw new SQLException();
//        }
//    }
//
//    /**
//     * Deletes a chat room.
//     *
//     * @param id The ID of the chat room to delete.
//     * @throws IOException If an I/O error occurs.
//     */
//    @DELETE
//    @Path("/{id}")
//    public void deleteChatRoom(@PathParam("id") int id) throws IOException {
//        ChatRoomService crs = new ChatRoomService();
//        crs.deleteChatRoom(id);
//    }
//
//    /**
//     * Updates a chat room.
//     *
//     * @param id            The ID of the chat room to update.
//     * @param chatRoomInput The chat room to update.
//     * @return The updated chat room.
//     * @throws IOException If an I/O error occurs.
//     */
//    @PUT
//    @Path("/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public ChatRoom updateChatRoom(@PathParam("id") int id, ChatRoomInput chatRoomInput) throws IOException {
//        ChatRoomService crs = new ChatRoomService();
//        return crs.updateChatRoom(id, chatRoomInput.getName(), chatRoomInput.getModeratorId(), chatRoomInput.getIcon());
//    }
//}
