package com.example.taskManager.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum TaskPriority {
    HIGH,
    MEDIUM,
    LOW;

    @JsonCreator
    public static TaskPriority fromString(String value) {
        return Arrays.stream(TaskPriority.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + value));
    }
}
