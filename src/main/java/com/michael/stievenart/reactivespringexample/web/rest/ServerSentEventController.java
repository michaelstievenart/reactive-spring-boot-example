package com.michael.stievenart.reactivespringexample.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michael.stievenart.reactivespringexample.events.ProfileCreatedEvent;
import com.michael.stievenart.reactivespringexample.publisher.ProfileCreatedEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ServerSentEventController {

    private final Flux<ProfileCreatedEvent> events;
    private final ObjectMapper mapper;

    public ServerSentEventController(ProfileCreatedEventPublisher publisher, ObjectMapper mapper) {
        this.events = Flux.create(publisher).share();
        this.mapper = mapper;
    }

    @GetMapping(path = "/sse/profiles", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> profiles() {
        return this.events.map(profileCreatedEvent -> {
           try {
               return this.mapper.writeValueAsString(profileCreatedEvent);
           } catch (JsonProcessingException e) {
               throw new RuntimeException(e);
           }
        });
    }
}
