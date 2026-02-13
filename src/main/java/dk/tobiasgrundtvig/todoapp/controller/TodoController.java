package dk.tobiasgrundtvig.todoapp.controller;

import dk.tobiasgrundtvig.todoapp.dto.CreateTodoRequest;
import dk.tobiasgrundtvig.todoapp.dto.TodoDTO;
import dk.tobiasgrundtvig.todoapp.dto.UpdateTodoRequest;
import dk.tobiasgrundtvig.todoapp.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDTO create(@Valid @RequestBody CreateTodoRequest request) {
        return todoService.create(request);
    }

    @GetMapping
    public List<TodoDTO> findAll() {
        return todoService.findAll();
    }

    @GetMapping("/{id}")
    public TodoDTO findById(@PathVariable Long id) {
        return todoService.findById(id);
    }

    @PutMapping("/{id}")
    public TodoDTO update(@PathVariable Long id, @Valid @RequestBody UpdateTodoRequest request) {
        return todoService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        todoService.delete(id);
    }
}
