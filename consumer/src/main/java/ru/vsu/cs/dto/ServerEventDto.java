package ru.vsu.cs.dto;

public record ServerEventDto(
        String severity,
        String server,
        String location
) {
}
