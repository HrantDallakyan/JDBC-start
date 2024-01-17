<%@ page import="repository.note.NoteRepository" %>
<%@ page import="service.note.NoteService" %>
<%@ page import="service.note.impl.NoteServiceJDBCImpl" %>
<%@ page import="model.Note" %>
<%@ page import="java.util.List" %>
<%@ page import="util.constants.Parameter" %>
<%@ page import="repository.note.impl.NoteRepositoryJDBCImpl" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="util.DataSource" %>
<%@ page import="util.constants.Path" %>
<%@ page import="util.constants.Massage" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
<style>
    table, th, td {
        border: 1px solid;
    }
</style>
<body>
<%
    if (request.getSession().getAttribute(Parameter.ID) == null) {
        request.setAttribute(Parameter.MESSAGE, Massage.LOGIN_FIRST);
        request.getRequestDispatcher(Path.WELCOME).forward(request, response);
    }
%>
<form action="http://localhost:8080/login" method="post">
    <h1>Notes</h1>

    <%
        Connection connection= DataSource.getConnection();
        NoteRepository noteRepositoryJPA = new NoteRepositoryJDBCImpl(connection);
        NoteService noteService = new NoteServiceJDBCImpl(noteRepositoryJPA);
        List<Note> notes = noteService.getAll((Integer) request.getSession().getAttribute(Parameter.ID));

    %>

    <table>
        <tr>
            <td>
                Title
            </td>

            <td>
                Description
            </td>
        </tr>

        <% for (Note note : notes) { %>
        <tr>
            <td>
                <%= note.getTitle()%>
            </td>

            <td>
                <%= note.getDescription()%>
            </td>
        </tr>

        <% } %>


    </table>

</form>

<form action="http://localhost:8080/createNote" method="post">
    Title: <input name="title"><br>
    Description: <input name="description"><br>
    <button type="submit">Create Note</button>
</form>

</body>
</html>