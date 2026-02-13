package dk.tobiasgrundtvig.todoapp.dto;

import jakarta.validation.constraints.Size;

public class UpdateTodoRequest {

    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    private Boolean completed;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
}
