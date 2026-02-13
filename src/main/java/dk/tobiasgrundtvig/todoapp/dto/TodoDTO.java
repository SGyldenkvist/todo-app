package dk.tobiasgrundtvig.todoapp.dto;

import dk.tobiasgrundtvig.todoapp.entity.Todo;

import java.time.Instant;

public record TodoDTO(Long id, String title, boolean completed, Instant createdAt) {

    public static TodoDTO fromEntity(Todo todo) {
        return new TodoDTO(todo.getId(), todo.getTitle(), todo.isCompleted(), todo.getCreatedAt());
    }

}
