package com.notesapp.repositories;

import com.notesapp.models.Note;
import com.notesapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {

    List<Note> findAllByUser(User user);

    @Query("SELECT n FROM Note n JOIN FETCH n.user ORDER BY n.createdAt DESC")
    List<Note> findAllWithOwner();

    Optional<Note> findByIdAndUser(UUID id, User user);

    @Query("SELECT COUNT(n) > 0 FROM Note n WHERE n.id = :id")
    boolean existsById(@Param("id") UUID id);
}
