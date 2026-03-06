package com.notesapp.services;

import com.notesapp.dtos.request.NoteRequest;
import com.notesapp.dtos.request.UpdateNoteRequest;
import com.notesapp.dtos.response.NoteResponse;

import java.util.List;
import java.util.UUID;

public interface NoteService {

    NoteResponse createNote(NoteRequest request);

    List<NoteResponse> getAllNotes();

    NoteResponse getNoteById(UUID id);

    NoteResponse updateNote(UUID id, UpdateNoteRequest request);

    void deleteNote(UUID id);
}
