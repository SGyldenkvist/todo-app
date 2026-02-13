package dk.tobiasgrundtvig.todoapp;

import dk.tobiasgrundtvig.todoapp.dto.CreateTodoRequest;
import dk.tobiasgrundtvig.todoapp.dto.TodoDTO;
import dk.tobiasgrundtvig.todoapp.dto.UpdateTodoRequest;
import dk.tobiasgrundtvig.todoapp.entity.Todo;
import dk.tobiasgrundtvig.todoapp.exception.ResourceNotFoundException;
import dk.tobiasgrundtvig.todoapp.repository.TodoRepository;
import dk.tobiasgrundtvig.todoapp.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo sampleTodo;

    @BeforeEach
    void setUp() {
        sampleTodo = new Todo();
        sampleTodo.setId(1L);
        sampleTodo.setTitle("Test todo");
    }

    @Test
    void create_savesAndReturnsTodo() {
        when(todoRepository.save(any(Todo.class))).thenReturn(sampleTodo);

        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("Test todo");

        TodoDTO result = todoService.create(request);

        assertThat(result.title()).isEqualTo("Test todo");
        assertThat(result.completed()).isFalse();
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void findAll_returnsList() {
        when(todoRepository.findAll()).thenReturn(List.of(sampleTodo));

        List<TodoDTO> result = todoService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().title()).isEqualTo("Test todo");
    }

    @Test
    void findById_returnsTodo() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(sampleTodo));

        TodoDTO result = todoService.findById(1L);

        assertThat(result.title()).isEqualTo("Test todo");
    }

    @Test
    void findById_throwsWhenNotFound() {
        when(todoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void update_updatesFields() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(sampleTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(sampleTodo);

        UpdateTodoRequest request = new UpdateTodoRequest();
        request.setTitle("Updated");
        request.setCompleted(true);

        todoService.update(1L, request);

        assertThat(sampleTodo.getTitle()).isEqualTo("Updated");
        assertThat(sampleTodo.isCompleted()).isTrue();
        verify(todoRepository).save(sampleTodo);
    }

    @Test
    void delete_removesTodo() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(sampleTodo));

        todoService.delete(1L);

        verify(todoRepository).delete(sampleTodo);
    }

    @Test
    void delete_throwsWhenNotFound() {
        when(todoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

}
