package com.michael.stievenart.reactivespringexample.web.rest;

import com.michael.stievenart.reactivespringexample.domain.UserProfile;
import com.michael.stievenart.reactivespringexample.service.UserProfileService;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(value = "profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Profile("classic")
public class UserProfileResource {

    private final MediaType mediaType = MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE);
    private final UserProfileService service;

    public UserProfileResource(UserProfileService service) {
        this.service = service;
    }

    @GetMapping
    Publisher<UserProfile> getAll() {
        return this.service.all();
    }

    @GetMapping("/{id}")
    Publisher<UserProfile> getById(@PathVariable("id") String id) {
        return this.service.get(id);
    }

    @PostMapping
    Publisher<ResponseEntity<UserProfile>> create(@RequestBody UserProfile profile) {
        return this.service.create(profile.getEmail())
                .map(userProfile -> ResponseEntity.created(URI.create("/profiles/" + userProfile.getId()))
                .contentType(mediaType)
                .build());
    }

    @DeleteMapping("/{id}")
    Publisher<UserProfile> deleteById(@PathVariable String id) {
        return this.service.delete(id);
    }

    @PutMapping("/{id}")
    Publisher<ResponseEntity<UserProfile>> updateById(@PathVariable String id, @RequestBody UserProfile profile) {
        return Mono
                .just(profile).
                flatMap(p -> this.service.update(id, p.getEmail()))
                .map(p -> ResponseEntity
                        .ok()
                        .contentType(mediaType)
                        .build());
    }
}
