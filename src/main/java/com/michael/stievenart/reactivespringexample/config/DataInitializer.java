package com.michael.stievenart.reactivespringexample.config;

import com.michael.stievenart.reactivespringexample.domain.UserProfile;
import com.michael.stievenart.reactivespringexample.repository.UserProfileRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Log4j2
@Component
@Profile("demo")
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserProfileRepository repository;

    public DataInitializer(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        repository.deleteAll()
                .thenMany(
                        Flux
                                .just("A", "B", "C", "D")
                                .map(name -> new UserProfile(UUID.randomUUID().toString(), name + "@email.com"))
                                .flatMap(repository::save)
                )
        .thenMany(repository.findAll())
        .subscribe(profile -> log.info("saving " + profile.toString()));
    }
}
