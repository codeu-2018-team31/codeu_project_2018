// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mindrot.jbcrypt.BCrypt;

public class LoginServletTest {

  private LoginServlet loginServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;

  static final String TEST_USERNAME = "test username";
  static final String TEST_PASSWORD = "test password";
  static final String TEST_ABOUT = "test about";

  @Before
  public void setup() {
    loginServlet = new LoginServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/login.jsp"))
        .thenReturn(mockRequestDispatcher);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    loginServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_NewUser() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn(TEST_USERNAME);
    Mockito.when(mockRequest.getParameter("password")).thenReturn(TEST_PASSWORD);

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered(TEST_USERNAME)).thenReturn(false);
    loginServlet.setUserStore(mockUserStore);

    HttpSession mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    loginServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("error", "That username was not found.");
  }

  @Test
  public void testDoPost_ExistingUser() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn(TEST_USERNAME);
    Mockito.when(mockRequest.getParameter("password")).thenReturn(BCrypt.hashpw(TEST_PASSWORD, BCrypt.gensalt()));

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    User fakeUser = new User(UUID.randomUUID(), TEST_USERNAME, Instant.now(), BCrypt.hashpw(TEST_PASSWORD, BCrypt.gensalt()), TEST_ABOUT);
    Mockito.when(mockUserStore.isUserRegistered(TEST_USERNAME)).thenReturn(true);
    Mockito.when(mockUserStore.getUser(TEST_USERNAME)).thenReturn(fakeUser);
    loginServlet.setUserStore(mockUserStore);

    HttpSession mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
    mockSession.setAttribute("user", TEST_USERNAME);
    mockResponse.sendRedirect("/conversations");

    loginServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockUserStore, Mockito.never()).addUser(Mockito.any(User.class));

    Mockito.verify(mockSession).setAttribute("user", TEST_USERNAME);
    Mockito.verify(mockResponse).sendRedirect("/conversations");
  }
}
