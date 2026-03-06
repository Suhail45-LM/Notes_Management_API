package com.notesapp.mapper;

import com.notesapp.dtos.response.NoteResponse;
import com.notesapp.models.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Mapping(target = "owner.id",       source = "user.id")
    @Mapping(target = "owner.username", source = "user.username")
    NoteResponse toResponse(Note note);

    List<NoteResponse> toResponseList(List<Note> notes);
}
