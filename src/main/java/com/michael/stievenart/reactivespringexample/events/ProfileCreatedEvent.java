package com.michael.stievenart.reactivespringexample.events;

import com.michael.stievenart.reactivespringexample.domain.UserProfile;
import org.springframework.context.ApplicationEvent;

public class ProfileCreatedEvent extends ApplicationEvent {
    public ProfileCreatedEvent(UserProfile userProfile) {
        super(userProfile);
    }
}
