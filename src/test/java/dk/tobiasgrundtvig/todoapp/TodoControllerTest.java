package dk.tobiasgrundtvig.todoapp;

import dk.tobiasgrundtvig.todoapp.dto.CreateTodoRequest;
import dk.tobiasgrundtvig.todoapp.dto.TodoDTO;
import dk.tobiasgrundtvig.todoapp.dto.UpdateTodoRequest;
import dk.tobiasgrundtvig.todoapp.exception.ResourceNotFoundException;
import dk.tobiasgrundtvig.todoapp.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private TodoService todoService;

    private final TodoDTO sampleTodo = new TodoDTO(1L, "Test todo", false, Instant.now());

    @Test
    void createTodo_returnsCreated() throws Exception {
        when(todoService.create(any(CreateTodoRequest.class))).thenReturn(sampleTodo);

        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("Test todo");

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test todo"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void createTodo_blankTitle_returnsBadRequest() throws Exception {
        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("");

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_returnsList() throws Exception {
        when(todoService.findAll()).thenReturn(List.of(sampleTodo));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test todo"));
    }

    @Test
    void findById_returnsTodo() throws Exception {
        when(todoService.findById(1L)).thenReturn(sampleTodo);

        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test todo"));
    }

    @Test
    void findById_notFound() throws Exception {
        when(todoService.findById(99L)).thenThrow(new ResourceNotFoundException("Todo not found with id: 99"));

        mockMvc.perform(get("/api/todos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Todo not found with id: 99"));
    }

    @Test
    void updateTodo_returnsTodo() throws Exception {
        TodoDTO updated = new TodoDTO(1L, "Updated", true, sampleTodo.createdAt());
        when(todoService.update(eq(1L), any(UpdateTodoRequest.class))).thenReturn(updated);

        UpdateTodoRequest request = new UpdateTodoRequest();
        request.setTitle("Updated");
        request.setCompleted(true);

        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteTodo_returnsNoContent() throws Exception {
        doNothing().when(todoService).delete(1L);

        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());
    }
}
