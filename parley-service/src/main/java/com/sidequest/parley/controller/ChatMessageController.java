package com.sidequest.parley.controller;

import com.sidequest.parley.service.ChatMessageService;
import org.sidequest.parley.api.ChatsApi;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.NewChatMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatMessageController implements ChatsApi {

    @Override
    public ResponseEntity<List<ChatMessage>> getChatMessagesById(Integer id) {
        try {
            ChatMessageService cms = new ChatMessageService(id);
            List<ChatMessage> cm = cms.getChatMessages();
            return ResponseEntity.ok(cm);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<ChatMessage> createChatMessage(NewChatMessage newChatMessage) {
        try {
            ChatMessageService cms = new ChatMessageService(newChatMessage.getChatRoomId());
            cms.createChatMessage(newChatMessage);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @Override
//    public ResponseEntity<List<ChatMessage>> getChatMessages() {
//        try {
//            ChatMessageService cms = new ChatMessageService();
//            List<ChatMessage> cm = cms.getChatMessages();
//            return ResponseEntity.ok(cm);
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}


///**
// * A RESTful web service controller for managing chat messages.
// */
////@Path("/chats")
//public class ChatMessageController {
//
//	/**
//	 * Retrieves a list of chat messages for a given chat ID in JSON format.
//	 *
//	 * @param chatID The ID of the chat for which to retrieve messages.
//	 * @return A JSON-formatted string containing an array of chat messages.
//	 * @throws IOException If an I/O error occurs.
//	 */
////	@GET
////	@Path("/{id}")
////	@Produces(MediaType.APPLICATION_JSON)
//	public String getChatMessages(@PathParam("id") int chatID) throws IOException, SQLException {
//		ChatMessageService cms = new ChatMessageService(chatID);
//		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
//		for (ChatMessage cm : cms.getChatMessages()) {
//			JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
//			JsonObjectBuilder userBuilder = Json.createObjectBuilder();
//			userBuilder.add("id", cm.getUser().getId());
//			userBuilder.add("name", cm.getUser().getName());
//			objectBuilder.add("user", userBuilder);
//
//			JsonObjectBuilder timestampBuilder = Json.createObjectBuilder();
//			timestampBuilder.add("year", cm.getTimestamp().getYear());
//			timestampBuilder.add("monthValue", cm.getTimestamp().getMonthValue());
//			timestampBuilder.add("dayOfMonth", cm.getTimestamp().getDayOfMonth());
//			timestampBuilder.add("hour", cm.getTimestamp().getHour());
//			timestampBuilder.add("minute", cm.getTimestamp().getMinute());
//			timestampBuilder.add("second", cm.getTimestamp().getSecond());
//			timestampBuilder.add("nano", cm.getTimestamp().getNano());
//			timestampBuilder.add("month", cm.getTimestamp().getMonth().toString());
//			timestampBuilder.add("dayOfWeek", cm.getTimestamp().getDayOfWeek().toString());
//			timestampBuilder.add("dayOfYear", cm.getTimestamp().getDayOfYear());
//			timestampBuilder.add("chronology", cm.getTimestamp().getChronology().toString());
//			timestampBuilder.add("epochSecond", cm.getTimestamp().toEpochSecond(ZoneOffset.UTC)); //all our users are in the same time zone because we make up this reality.
//			timestampBuilder.add("localTime", cm.getTimestamp().toLocalTime().toString()); //all our users are in the same time zone because we make up this reality.
//			objectBuilder.add("timestamp", timestampBuilder);
//
//			objectBuilder.add("chatMessageId", cm.getId());
//			objectBuilder.add("content", cm.getContent());
//			//objectBuilder.add("name", cm.getName());
//			arrayBuilder.add(objectBuilder.build());
//		}
//		JsonObjectBuilder rootBuilder = Json.createObjectBuilder();
//		rootBuilder.add("messages", arrayBuilder.build());
//		JsonObject root = rootBuilder.build();
//		StringWriter writer = new StringWriter();
//		Json.createWriter(writer).write(root);
//		return writer.toString();
//	}
//
//	/**
//	 * Creates a new chat message using the provided input data.
//	 *
//	 * @param chatMessageInput A ChatMessageInput object containing the data for the
//	 *                         new chat message.
//	 * @return A ChatMessage object representing the created chat message.
//	 * @throws IOException If an I/O error occurs.
//	 */
////	@POST
////	@Consumes(MediaType.APPLICATION_JSON)
////	@Produces(MediaType.APPLICATION_JSON)
//	public ChatMessage createChatMessage(ChatMessageInput chatMessageInput) throws IOException, SQLException {
//		int chatRoomId = chatMessageInput.getChatRoomId();
//
//		ChatMessageService chatMessageService = new ChatMessageService(chatRoomId);
//		return chatMessageService.createChatMessage(chatMessageInput);
//	}
//}