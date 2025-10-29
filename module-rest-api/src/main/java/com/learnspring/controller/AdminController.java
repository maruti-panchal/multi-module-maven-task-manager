package com.learnspring.controller;


import com.learnspring.dtos.TaskResponseDto;
import com.learnspring.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {

        this.adminService = adminService;
    }


    @GetMapping("/task")
    public Flux<TaskResponseDto> getAllTask() {
        return adminService.getAllTasks();
    }


    @GetMapping("/task/{id}")
    public Mono<ResponseEntity<TaskResponseDto>> getTaskById(@PathVariable String id) {
        return adminService.getTaskById(id)
                .map(task->ResponseEntity.status(HttpStatus.FOUND).body(task));
    }


    @DeleteMapping("/task/{id}")
    public Mono<ResponseEntity<Boolean>> deleteTaskById(@PathVariable String id) {
        return adminService.deleteTaskById(id)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(true)));
    }

    // Delete all tasks
    @DeleteMapping("/task")
    public Mono<ResponseEntity<Boolean>> deleteAllTask() {
        return adminService.deleteAllTask()
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(true)));
    }

    // Delete user by ID
    @DeleteMapping("/user/{id}")
    public Mono<ResponseEntity<Boolean>> deleteUserById(@PathVariable String id) {
        return adminService.deleteUserById(id)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(true)));
    }

    @GetMapping
    public Flux<TaskResponseDto> list(@RequestParam(required=false) String username,
                                      @RequestParam(required=false) Integer dueDays,
                                      @RequestParam(defaultValue="0") int offset,
                                      @RequestParam(defaultValue="20") int limit) {
        int dueBeforeValue = (dueDays == null) ? 0 : dueDays;
        return adminService.filteredTasks(username,dueBeforeValue,offset,limit);

    }

    @GetMapping("/search")
    public Mono<TaskResponseDto> findByTitle(@RequestParam("title") String title) {
        return adminService.findByTitle(title);
    }

    @GetMapping("/due-between")
    public Flux<TaskResponseDto> findDueDateBetween(
            @RequestParam("min") int min,
            @RequestParam("max") int max) {

        return adminService.findDueDateBetween(min, max);
    }


}
