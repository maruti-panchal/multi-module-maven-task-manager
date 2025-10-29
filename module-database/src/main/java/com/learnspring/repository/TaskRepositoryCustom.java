package com.learnspring.repository;

import com.learnspring.entity.TaskEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface TaskRepositoryCustom {
    Flux<TaskEntity> findByFilters(String username, Integer dueBefore, int offset, int limit);
    Mono<TaskEntity> findByTitle(String title);
    Flux<TaskEntity> findDueDateBetween(int min, int max);
}
