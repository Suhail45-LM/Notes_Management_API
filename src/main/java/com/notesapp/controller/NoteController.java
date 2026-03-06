package com.notesapp.controller;

import com.notesapp.dtos.request.NoteRequest;
import com.notesapp.dtos.request.UpdateNoteRequest;
import com.notesapp.dtos.response.ApiResponse;
import com.notesapp.dtos.response.NoteResponse;
import com.notesapp.services.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Notes", description = "Notes management endpoints")
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Create a new note",
               description = "Creates a note owned by the currently authenticated user")
    public ResponseEntity<ApiResponse<NoteResponse>> createNote(
            @Valid @RequestBody NoteRequest request) {

        NoteResponse note = noteService.createNote(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Note created successfully", note));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get all notes",
               description = "USER sees only their own notes. ADMIN sees all notes.")
    public ResponseEntity<ApiResponse<List<NoteResponse>>> getAllNotes() {

        List<NoteResponse> notes = noteService.getAllNotes();
        return ResponseEntity.ok(
                ApiResponse.success("Notes retrieved successfully", notes));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get note by ID",
               description = "USER can only access their own notes. ADMIN can access any note.")
    public ResponseEntity<ApiResponse<NoteResponse>> getNoteById(
            @PathVariable UUID id) {

        NoteResponse note = noteService.getNoteById(id);
        return ResponseEntity.ok(ApiResponse.success("Note retrieved successfully", note));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update a note",
               description = "Only the note owner can update their note")
    public ResponseEntity<ApiResponse<NoteResponse>> updateNote(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateNoteRequest request) {

        NoteResponse updated = noteService.updateNote(id, request);
        return ResponseEntity.ok(ApiResponse.success("Note updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Delete a note",
               description = "USER can delete their own notes. ADMIN can delete any note.")
    public ResponseEntity<ApiResponse<Void>> deleteNote(@PathVariable UUID id) {

        noteService.deleteNote(id);
        return ResponseEntity.ok(ApiResponse.success("Note deleted successfully"));
    }
}
