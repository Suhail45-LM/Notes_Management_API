package com.notesapp.dtos.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record NoteResponse(
        UUID          id,
        String        title,
        String        content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        NoteOwner     owner
) {
    /**
     * Minimal owner representation embedded in a note response.
     */
    @Builder
    public record NoteOwner(UUID id, String username) {}
}
