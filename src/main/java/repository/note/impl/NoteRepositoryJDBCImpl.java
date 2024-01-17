package repository.note.impl;

import model.Note;
import repository.note.NoteRepository;
import util.constants.Parameter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NoteRepositoryJDBCImpl implements NoteRepository {

    private final Connection connection;

    public NoteRepositoryJDBCImpl(Connection connection) {
        this.connection = connection;
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS notes(
                        id serial primary key,
                        title varchar not null unique ,
                        description text not null unique,
                        user_id integer
                        references users(id)
                    );
                    """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Note note) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format("INSERT INTO notes (title, description, user_id) VALUES ('%s','%s', %d)", note.getTitle(), note.getDescription(), note.getUserId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Note note) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    String.format("UPDATE notes SET title = '%s', description WHERE id = %d", note.getTitle(), note.getDescription(), note.getId())
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format("DELETE FROM notes WHERE id = %d", id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Note> getAll(int userId) {
        List<Note> list = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM notes WHERE user_id = %d", userId));
            while (resultSet.next()) {
                list.add(prepareNote(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Note getById(int id) {
        try (Statement statement = connection.createStatement()) {
            Note note = null;
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM notes WHERE id = %d", id));
            if (resultSet.next()) {
                note = prepareNote(resultSet);
            }
            return note;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Note prepareNote(ResultSet resultSet) throws SQLException {
        Note note = new Note();
        note.setId(resultSet.getInt(Parameter.ID));
        note.setTitle(resultSet.getString(Parameter.TITLE));
        note.setDescription(resultSet.getString(Parameter.DESCRIPTION));
        return note;
    }
}
