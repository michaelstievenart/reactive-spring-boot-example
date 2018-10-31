package com.michael.stievenart.reactivespringexample.service;

import com.michael.stievenart.reactivespringexample.domain.UserProfile;
import com.michael.stievenart.reactivespringexample.events.ProfileCreatedEvent;
import com.michael.stievenart.reactivespringexample.repository.UserProfileRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class UserProfileService {

    private final ApplicationEventPublisher publisher;
    private final UserProfileRepository repository;

    public UserProfileService(ApplicationEventPublisher publisher, UserProfileRepository repository) {
        this.publisher = publisher;
        this.repository = repository;
    }

    public Flux<UserProfile> all() {
        return this.repository.findAll();
    }

    public Mono<UserProfile> get(String id) {
        return this.repository.findById(id);
    }

    public Mono<UserProfile> update(String id, String email) {
        return this.repository.findById(id)
                .map(userProfile -> new UserProfile(userProfile.getId(), email))
                .flatMap(this.repository::save);
    }

    public Mono<UserProfile> delete(String id) {
        return this.repository
                .findById(id)
                .flatMap(userProfile -> this.repository.deleteById(userProfile.getId()).thenReturn(userProfile));
    }

    public Mono<UserProfile> create(String email) {
        return this.repository
                .save(new UserProfile(null, email))
                .doOnSuccess(userProfile -> this.publisher.publishEvent(new ProfileCreatedEvent(userProfile)));
    }
}
