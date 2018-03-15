package codeu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Servlet class responsible for user registration.
*/
public class RegisterServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    response.getWriter().println("<h1>RegisterServlet GET request.</h1>");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = request.getParameter("username");
    String password = request.getParameter("password");

    response.getWriter().println("<p>Username: " + username + "</p>");
    response.getWriter().println("<p>Password: " + password + "</p>");
  }
}