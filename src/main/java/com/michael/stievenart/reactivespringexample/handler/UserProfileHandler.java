package com.michael.stievenart.reactivespringexample.handler;

import com.michael.stievenart.reactivespringexample.domain.UserProfile;
import com.michael.stievenart.reactivespringexample.service.UserProfileService;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class UserProfileHandler {

    private final UserProfileService service;

    public UserProfileHandler(UserProfileService service) {
        this.service = service;
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return defaultReadResponse(this.service.get(id(request)));
    }

    public Mono<ServerResponse> all(ServerRequest request) {
        return defaultReadResponse(this.service.all());
    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        return defaultReadResponse(this.service.delete(id(request)));
    }

    public Mono<ServerResponse> updateById(ServerRequest request) {
        Flux<UserProfile> profileFlux = request
                .bodyToFlux(UserProfile.class)
                .flatMap(profile -> this.service.update(id(request), profile.getEmail()));
        return defaultReadResponse(profileFlux);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Flux<UserProfile> flux = request
                .bodyToFlux(UserProfile.class)
                .flatMap(profile -> this.service.create(profile.getEmail()));
        return defaultWriteResponse(flux);
    }

    private static Mono<ServerResponse> defaultWriteResponse(Publisher<UserProfile> profiles) {
        return Mono
                .from(profiles)
                .flatMap(profile -> ServerResponse
                        .created(URI.create("/profiles/" + profile.getId()))
                        .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)).build());
    }


    private static Mono<ServerResponse> defaultReadResponse(Publisher<UserProfile> profiles) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(profiles, UserProfile.class);
    }

    private static String id(ServerRequest r) {
        return r.pathVariable("id");
    }
}
