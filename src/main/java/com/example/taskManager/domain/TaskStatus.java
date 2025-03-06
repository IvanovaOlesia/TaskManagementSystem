package com.example.taskManager.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED;

    @JsonCreator
    public static TaskStatus fromString(String value) {
        return Arrays.stream(TaskStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + value));
    }
}
