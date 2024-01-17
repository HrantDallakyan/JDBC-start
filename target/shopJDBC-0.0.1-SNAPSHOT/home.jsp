<%@ page import="util.constants.Parameter" %>
<%@ page import="util.constants.Massage" %>
<%@ page import="util.constants.Path" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
<body>
<%
    if (request.getSession().getAttribute(Parameter.ID) == null) {
        request.setAttribute(Parameter.MESSAGE, Massage.LOGIN_FIRST);
        request.getRequestDispatcher(Path.WELCOME).forward(request, response);
    }
%>

<h1>Welcome to home page dear <%=request.getSession().getAttribute(Parameter.NAME)%>
</h1>

<button><a href="note.jsp">Note</a></button>
<br/>
<button><a href="changePassword.jsp">Change Password</a></button>
<br/>

<button><a href="logout">Logout</a></button>
<br/>

</body>
</html>