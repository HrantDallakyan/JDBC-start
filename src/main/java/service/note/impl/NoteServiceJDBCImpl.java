package service.note.impl;

import exception.EmptyTextException;
import lombok.RequiredArgsConstructor;
import model.Note;
import repository.note.NoteRepository;
import service.note.NoteService;
import util.constants.Parameter;

import java.sql.Connection;
import java.util.List;

@RequiredArgsConstructor
public class NoteServiceJDBCImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public void create(Note note, int userId) {
        validateNote(note.getTitle(), note.getDescription());
        note.setUserId(userId);
        noteRepository.create(note);
    }

    @Override
    public void update(Note note) {
        validateNote(note.getTitle(), note.getDescription());
        noteRepository.update(note);
    }

    @Override
    public void delete(int id) {
        noteRepository.delete(id);

    }

    @Override
    public List<Note> getAll(int userId) {
        return noteRepository.getAll(userId);
    }

    @Override
    public Note getById(int userId) {
        return noteRepository.getById(userId);
    }


    private void validateNote(String title, String description) {
        if (!title.matches("[A-Za-z]+")
                || !description.matches("[A-Za-z]+"))
            throw new EmptyTextException(Parameter.NOTE_IS_EMPTY);

    }
}
