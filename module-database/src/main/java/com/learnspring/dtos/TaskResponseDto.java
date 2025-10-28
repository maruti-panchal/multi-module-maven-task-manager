package com.learnspring.dtos;

import lombok.Builder;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@Data
public class TaskResponseDto {
    private String id;
    private String title;
    private String description;
    private int dueDays;
    private String username;
}
