package com.michael.stievenart.reactivespringexample.repository;

import com.michael.stievenart.reactivespringexample.domain.UserProfile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserProfileRepository extends ReactiveMongoRepository<UserProfile, String> {
}
