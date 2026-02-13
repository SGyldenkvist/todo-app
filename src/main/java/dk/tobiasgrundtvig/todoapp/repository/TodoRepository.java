package dk.tobiasgrundtvig.todoapp.repository;

import dk.tobiasgrundtvig.todoapp.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
