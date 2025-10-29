package com.learnspring.repository;


import com.learnspring.entity.TaskEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TaskRepository extends ReactiveMongoRepository<TaskEntity, String> ,TaskRepositoryCustom{
    Mono<TaskEntity> findById(String id);
    Flux<TaskEntity> findByUsername(String username);
    Mono<TaskEntity> findByIdAndUsername(String id, String username);
}
