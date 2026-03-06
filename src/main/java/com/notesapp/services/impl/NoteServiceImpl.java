package com.notesapp.services.impl;

import com.notesapp.dtos.request.NoteRequest;
import com.notesapp.dtos.request.UpdateNoteRequest;
import com.notesapp.dtos.response.NoteResponse;
import com.notesapp.models.Note;
import com.notesapp.models.Role;
import com.notesapp.models.User;
import com.notesapp.exceptions.ResourceNotFoundException;
import com.notesapp.exceptions.UnauthorizedAccessException;
import com.notesapp.mapper.NoteMapper;
import com.notesapp.repositories.NoteRepository;
import com.notesapp.services.NoteService;
import com.notesapp.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper     noteMapper;

    @Override
    @Transactional
    public NoteResponse createNote(NoteRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();

        Note note = Note.builder()
                .title(request.title())
                .content(request.content())
                .user(currentUser)
                .build();

        Note saved = noteRepository.save(note);
        log.info("Note created: id='{}', owner='{}'", saved.getId(), currentUser.getUsername());

        return noteMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponse> getAllNotes() {
        User currentUser = SecurityUtils.getCurrentUser();

        List<Note> notes = isAdmin(currentUser)
                ? noteRepository.findAllWithOwner()
                : noteRepository.findAllByUser(currentUser);

        log.debug("Fetched {} notes for user='{}'", notes.size(), currentUser.getUsername());
        return noteMapper.toResponseList(notes);
    }


    @Override
    @Transactional(readOnly = true)
    public NoteResponse getNoteById(UUID id) {
        User currentUser = SecurityUtils.getCurrentUser();
        Note note        = findNoteOrThrow(id);

        if (!isAdmin(currentUser) && !isOwner(note, currentUser)) {
            throw new UnauthorizedAccessException(
                    "You do not have permission to view this note");
        }

        return noteMapper.toResponse(note);
    }

    @Override
    @Transactional
    public NoteResponse updateNote(UUID id, UpdateNoteRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();
        Note note        = findNoteOrThrow(id);

        if (!isOwner(note, currentUser)) {
            throw new UnauthorizedAccessException(
                    "You do not have permission to update this note");
        }

        note.setTitle(request.title());
        note.setContent(request.content());

        Note updated = noteRepository.save(note);
        log.info("Note updated: id='{}', owner='{}'", id, currentUser.getUsername());

        return noteMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteNote(UUID id) {
        User currentUser = SecurityUtils.getCurrentUser();
        Note note        = findNoteOrThrow(id);

        // Users can only delete their own; Admins can delete any note
        if (!isAdmin(currentUser) && !isOwner(note, currentUser)) {
            throw new UnauthorizedAccessException(
                    "You do not have permission to delete this note");
        }

        noteRepository.delete(note);
        log.info("Note deleted: id='{}', deletedBy='{}'", id, currentUser.getUsername());
    }


    private Note findNoteOrThrow(UUID id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", id));
    }

    private boolean isOwner(Note note, User user) {
        return note.getUser().getId().equals(user.getId());
    }

    private boolean isAdmin(User user) {
        return Role.ADMIN.equals(user.getRole());
    }
}
