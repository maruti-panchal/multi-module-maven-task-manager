package com.learnspring.service;


import com.learnspring.dtos.TaskResponseDto;
import com.learnspring.entity.TaskEntity;
import com.learnspring.repository.TaskRepository;
import com.learnspring.repository.TaskRepositoryCustomImpl;
import com.learnspring.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AdminService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskRepositoryCustomImpl taskRepositoryCustom;

    public AdminService(TaskRepository taskRepository, UserRepository userRepository, TaskRepositoryCustomImpl taskRepositoryCustom) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskRepositoryCustom = taskRepositoryCustom;
    }

    public Flux<TaskResponseDto> getAllTasks() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMapMany(auth -> taskRepository.findAll())
                .map(this::toResponse);
    }

    public Mono<TaskResponseDto> getTaskById(String id) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> taskRepository.findById(id))
                .map(this::toResponse);

    }

    public Mono<Void> deleteTaskById(String id) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> taskRepository.deleteById(id));

    }

    public Mono<Void> deleteAllTask() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> taskRepository.deleteAll());

    }

    public Mono<Void> deleteUserById(String id) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> userRepository.findById(id)
                        .flatMap(user -> userRepository.deleteById(id)));

    }

    public Flux<TaskResponseDto> filteredTasks(String username, Integer dueBefore, int offset, int limit) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMapMany(ctx -> {
                    Authentication auth = ctx.getAuthentication();
                    return taskRepositoryCustom.findByFilters(username, dueBefore, offset, limit);
                })
                .map(this::toResponse)
                .switchIfEmpty(Flux.empty());
    }


    public Mono<TaskResponseDto> findByTitle(String title) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(ctx -> {
                    Authentication auth = ctx.getAuthentication();
                    return taskRepositoryCustom.findByTitle(title);
                })
                .map(this::toResponse)
                .switchIfEmpty(Mono.empty());
    }


    public Flux<TaskResponseDto> findDueDateBetween(int min, int max) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMapMany(ctx -> {
                    Authentication auth = ctx.getAuthentication();
                    return taskRepositoryCustom.findDueDateBetween(min, max);
                })
                .map(this::toResponse)
                .switchIfEmpty(Flux.empty());
    }
    private TaskResponseDto toResponse(TaskEntity task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDays(task.getDueDays())
                .username(task.getUsername())
                .build();
    }
}
