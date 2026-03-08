package com.notesapp.mapper;

import com.notesapp.dtos.response.NoteResponse;
import com.notesapp.models.Note;
import com.notesapp.models.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-08T15:47:36+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class NoteMapperImpl implements NoteMapper {

    @Override
    public NoteResponse toResponse(Note note) {
        if ( note == null ) {
            return null;
        }

        NoteResponse.NoteResponseBuilder noteResponse = NoteResponse.builder();

        noteResponse.owner( userToNoteOwner( note.getUser() ) );
        noteResponse.id( note.getId() );
        noteResponse.title( note.getTitle() );
        noteResponse.content( note.getContent() );
        noteResponse.createdAt( note.getCreatedAt() );
        noteResponse.updatedAt( note.getUpdatedAt() );

        return noteResponse.build();
    }

    @Override
    public List<NoteResponse> toResponseList(List<Note> notes) {
        if ( notes == null ) {
            return null;
        }

        List<NoteResponse> list = new ArrayList<NoteResponse>( notes.size() );
        for ( Note note : notes ) {
            list.add( toResponse( note ) );
        }

        return list;
    }

    protected NoteResponse.NoteOwner userToNoteOwner(User user) {
        if ( user == null ) {
            return null;
        }

        NoteResponse.NoteOwner.NoteOwnerBuilder noteOwner = NoteResponse.NoteOwner.builder();

        noteOwner.id( user.getId() );
        noteOwner.username( user.getUsername() );

        return noteOwner.build();
    }
}
