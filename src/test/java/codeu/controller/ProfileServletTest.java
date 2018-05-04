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

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.persistence.PersistentStorageAgent;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

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

import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProfileServletTest {

  private ProfileServlet profileServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private HttpSession mockSession;
  private UserStore userStore;
  private ConversationStore conversationStore;
  private MessageStore messageStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  static final String TEST_USERNAME = "test username";
  static final String TEST_PASSWORD = "test password";
  static final String TEST_ABOUT = "test about";
  static final String TEST_TITLE = "test title";
  static final UUID TEST_USER_ID = UUID.randomUUID();
  static final UUID TEST_CONVO_ID = UUID.randomUUID();
  static final User TEST_USER = new User(TEST_USER_ID, TEST_USERNAME, Instant.now(), TEST_PASSWORD, TEST_ABOUT);
  static final Conversation TEST_CONVO = new Conversation(TEST_CONVO_ID, TEST_USER_ID, TEST_TITLE, Instant.now());
  static final Message TEST_MESSAGE = new Message(UUID.randomUUID(), TEST_CONVO_ID, TEST_USER_ID, TEST_ABOUT, Instant.now());


  @Before
  public void setup() {
    profileServlet = new ProfileServlet();  

    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    userStore = UserStore.getTestInstance(mockPersistentStorageAgent);
    conversationStore = ConversationStore.getTestInstance(mockPersistentStorageAgent);
    messageStore = MessageStore.getTestInstance(mockPersistentStorageAgent);

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/profile.jsp"))
        .thenReturn(mockRequestDispatcher);

    profileServlet.setUserStore(userStore);
    profileServlet.setConversationStore(conversationStore);
    profileServlet.setMessageStore(messageStore);

    userStore.addUser(TEST_USER);
    conversationStore.addConversation(TEST_CONVO);
    messageStore.addMessage(TEST_MESSAGE);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/profile/" + TEST_USER.getId().toString());
  
    Mockito.when(mockSession.getAttribute("user")).thenReturn(TEST_USERNAME);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Mockito.when(mockResponse.getWriter()).thenReturn(writer);

    List<Message> messages = new ArrayList<>();
    messages.add(TEST_MESSAGE); 

    profileServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("user", TEST_USER);
    Mockito.verify(mockRequest).setAttribute("messages", messages);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/profile/" + TEST_USER.getId().toString());

    Mockito.when(mockSession.getAttribute("user")).thenReturn(TEST_USERNAME);
    Mockito.when(mockRequest.getParameter("editAbout")).thenReturn(TEST_ABOUT);

    profileServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockSession).setAttribute("about", TEST_ABOUT);
    Mockito.verify(mockResponse).sendRedirect("/profile/" + TEST_USER.getId().toString());
  }
}
