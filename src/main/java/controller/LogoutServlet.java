package controller;

import util.CookieUtil;
import util.constants.Parameter;
import util.constants.Path;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        Cookie rememberCookie = CookieUtil.getCookieByName(req.getCookies(), Parameter.REMEMBER_COOKIE);
        if (rememberCookie != null) {
            rememberCookie.setMaxAge(0);
            resp.addCookie(rememberCookie);
        }
        resp.sendRedirect(Path.WELCOME);
    }
}