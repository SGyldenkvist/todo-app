package dk.tobiasgrundtvig.todoapp.service;

import dk.tobiasgrundtvig.todoapp.dto.CreateTodoRequest;
import dk.tobiasgrundtvig.todoapp.dto.TodoDTO;
import dk.tobiasgrundtvig.todoapp.dto.UpdateTodoRequest;
import dk.tobiasgrundtvig.todoapp.entity.Todo;
import dk.tobiasgrundtvig.todoapp.exception.ResourceNotFoundException;
import dk.tobiasgrundtvig.todoapp.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoDTO create(CreateTodoRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        return TodoDTO.fromEntity(todoRepository.save(todo));
    }

    public List<TodoDTO> findAll() {
        return todoRepository.findAll().stream()
                .map(TodoDTO::fromEntity)
                .toList();
    }

    public TodoDTO findById(Long id) {
        return TodoDTO.fromEntity(findEntityById(id));
    }

    public TodoDTO update(Long id, UpdateTodoRequest request) {
        Todo todo = findEntityById(id);
        if (request.getTitle() != null) {
            todo.setTitle(request.getTitle());
        }
        if (request.getCompleted() != null) {
            todo.setCompleted(request.getCompleted());
        }
        return TodoDTO.fromEntity(todoRepository.save(todo));
    }

    public void delete(Long id) {
        Todo todo = findEntityById(id);
        todoRepository.delete(todo);
    }

    private Todo findEntityById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
    }
}
