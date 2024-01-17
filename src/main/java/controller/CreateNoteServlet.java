package controller;

import exception.EmptyTextException;
import model.Note;
import repository.note.NoteRepository;
import repository.note.impl.NoteRepositoryJDBCImpl;
import service.note.NoteService;
import service.note.impl.NoteServiceJDBCImpl;
import util.DataSource;
import util.constants.Parameter;
import util.constants.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class CreateNoteServlet extends HttpServlet {
    Connection connection = DataSource.getConnection();
    NoteRepository noteRepository = new NoteRepositoryJDBCImpl(connection);
    NoteService noteService = new NoteServiceJDBCImpl(noteRepository);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter(Parameter.TITLE);
        String description = req.getParameter(Parameter.DESCRIPTION);
        Note note = new Note(title, description);
        try {
            noteService.create(note, (Integer) req.getSession().getAttribute(Parameter.ID));
            resp.sendRedirect(Path.NOTE);
        } catch (EmptyTextException e) {
            resp.sendRedirect(Path.NOTE);
        }
    }
}