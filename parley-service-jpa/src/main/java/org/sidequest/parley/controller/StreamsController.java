package org.sidequest.parley.controller;


import org.sidequest.parley.api.StreamsApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.URI;

@RestController
public class StreamsController implements StreamsApi {

    @Override
    public ResponseEntity<Object> createStream(URI callbackUrl) {
        SseEmitter emitter = new SseEmitter();
        try {
            emitter.send("Hello World!");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(emitter, HttpStatus.CREATED);

    }


//    @Override
//    public ResponseEntity<StreamUsersEvents200Response> streamUsersEvents() {
//                SseEmitter emitter = new SseEmitter();
//        try {
//            emitter.send("Hello World!");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//       return new ResponseEntity<>(emitter, HttpStatus.CREATED);
//
//    }

//   @Override
//    public ResponseEntity<Object> createStream( URI callbackUrl) {
//        SseEmitter emitter = new SseEmitter();
//        try {
//            emitter.send("Hello World!");
//            // Pause the current thread for 10 seconds
//            Thread.sleep(10000);
//
//            // Send the second event
//            emitter.send("Hello Moon!");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//       return new ResponseEntity<>(emitter, HttpStatus.CREATED);
//
//    }
}
