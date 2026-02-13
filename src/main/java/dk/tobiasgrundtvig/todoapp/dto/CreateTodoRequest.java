package dk.tobiasgrundtvig.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTodoRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
