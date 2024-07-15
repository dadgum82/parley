//package org.sidequest.parley.controller;
//
//
//import org.sidequest.parley.api.StreamsApi;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.net.URI;
//
//@RestController
//public class StreamsController implements StreamsApi {
//
//    @Override
//    public ResponseEntity<Object> createStream(URI callbackUrl) {
//        SseEmitter emitter = new SseEmitter();
//        try {
//            emitter.send("Hello World!");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>(emitter, HttpStatus.CREATED);
//
//    }
//}
