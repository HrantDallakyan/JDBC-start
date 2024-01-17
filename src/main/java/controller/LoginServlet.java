package controller;

import exception.ValidationException;
import repository.user.UserRepository;
import repository.user.impl.UserRepositoryJDBCImpl;
import service.user.AuthService;
import service.user.impl.AuthServiceImpl;
import util.DataSource;
import util.constants.Parameter;
import util.constants.Path;
import util.encoder.AESManager;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

public class LoginServlet extends HttpServlet {
    Connection connection = DataSource.getConnection();
    UserRepository userRepository = new UserRepositoryJDBCImpl(connection);
    AuthService authService = new AuthServiceImpl(userRepository);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter(Parameter.EMAIL);
        String password = req.getParameter(Parameter.PASSWORD);
        try {
            authService.login(email, password);
            String rememberMe = req.getParameter(Parameter.REMEMBER_ME);
            if (rememberMe != null && rememberMe.equalsIgnoreCase(Parameter.ON)) {
                Cookie cookie = new Cookie(Parameter.REMEMBER_COOKIE, AESManager.encrypt(email + ":" + password));
                cookie.setMaxAge(360000);
                resp.addCookie(cookie);
            }
            HttpSession session = req.getSession();
            session.setAttribute(Parameter.ID, userRepository.getByEmail(email).getId());
            session.setAttribute(Parameter.NAME, userRepository.getByEmail(email).getName());
            resp.sendRedirect(Path.HOME);
        } catch (ValidationException e) {
            req.setAttribute(Parameter.MESSAGE, e.getMessage());
            req.getRequestDispatcher(Path.WELCOME).forward(req, resp);
        }
    }
}