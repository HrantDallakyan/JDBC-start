package controller;

import exception.ValidationException;
import model.User;
import repository.user.UserRepository;
import repository.user.impl.UserRepositoryJDBCImpl;
import service.user.AuthService;
import service.user.impl.AuthServiceImpl;
import util.CookieUtil;
import util.DataSource;
import util.constants.Parameter;
import util.constants.Path;
import util.encoder.AESManager;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class ChangePasswordServlet extends HttpServlet {
    Connection connection = DataSource.getConnection();
    UserRepository userRepository = new UserRepositoryJDBCImpl(connection);
    AuthService authService = new AuthServiceImpl(userRepository);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lastPassword = req.getParameter(Parameter.LAST_PASSWORD);
        String newPassword = req.getParameter(Parameter.NEW_PASSWORD);
        String repeatPassword = req.getParameter(Parameter.REPEAT_PASSWORD);
        try {
            authService.changePassword(lastPassword, newPassword, repeatPassword, (Integer) req.getSession().getAttribute(Parameter.ID));
            User user = userRepository.getById((Integer) req.getSession().getAttribute(Parameter.ID));
            Cookie cookie = CookieUtil.getCookieByName(req.getCookies(), Parameter.REMEMBER_COOKIE);
            if (cookie != null) {
                cookie.setMaxAge(360000);
                cookie.setValue(AESManager.encrypt(user.getEmail() + user.getPassword()));
            }
            resp.sendRedirect(Path.HOME);
        } catch (ValidationException e) {
            req.setAttribute(Parameter.MESSAGE, e.getMessage());
            req.getRequestDispatcher(Path.CHANGE_PASSWORD).forward(req, resp);
        }
    }
}