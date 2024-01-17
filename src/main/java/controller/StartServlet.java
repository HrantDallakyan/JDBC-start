package controller;

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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

public class StartServlet extends HttpServlet {
    Connection connection = DataSource.getConnection();
    UserRepository userRepository = new UserRepositoryJDBCImpl(connection);
    AuthService authService = new AuthServiceImpl(userRepository);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameter("param"));

        Cookie rememberCookie = CookieUtil.getCookieByName(req.getCookies(), Parameter.REMEMBER_COOKIE);
        try {
            if (rememberCookie != null) {
                String encriptedString = rememberCookie.getValue();
                String emailPass = AESManager.decrypt(encriptedString);
                String[] split = emailPass.split(":");
                String email = split[0];
                String password = split[1];
                authService.login(email, password);
                Cookie cookie = new Cookie(Parameter.REMEMBER_COOKIE, AESManager.encrypt(email + ":" + password));
                cookie.setMaxAge(360000);
                resp.addCookie(cookie);
                HttpSession session = req.getSession();
                session.setAttribute(Parameter.ID, userRepository.getByEmail(email).getId());
                session.setAttribute(Parameter.NAME, userRepository.getByEmail(email).getName());
                req.getRequestDispatcher(Path.HOME).forward(req, resp);
            } else {
                resp.sendRedirect(Path.WELCOME);
            }
        } catch (Exception e) {
            req.setAttribute(Parameter.MESSAGE, e.getMessage());
            req.getRequestDispatcher(Path.WELCOME).forward(req, resp);
        }
    }
}