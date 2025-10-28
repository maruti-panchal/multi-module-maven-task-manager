package com.learnspring.repository;


import com.learnspring.entity.TaskEntity;
import com.learnspring.entity.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserEntity, String> {
    Mono<UserEntity> findByUsername(String username);


    Mono<UserEntity> save(TaskEntity taskEntity);
}
