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
import codeu.model.data.User;
import codeu.model.data.Tag;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.TagStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

public class ConversationServletTest {

  private ConversationServlet conversationServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private ConversationStore mockConversationStore;
  private UserStore mockUserStore;
  private TagStore mockTagStore;

  static final String TEST_USERNAME = "test_username";
  static final String TEST_PASSWORD = "test_password";
  static final String TEST_ABOUT = "test_about";
  static final String TEST_CONVERSATION = "test_conversation";
  static final String TEST_TAG = "test_tag";

  @Before
  public void setup() {
    conversationServlet = new ConversationServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/conversations.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    conversationServlet.setConversationStore(mockConversationStore);

    mockUserStore = Mockito.mock(UserStore.class);
    conversationServlet.setUserStore(mockUserStore);

    mockTagStore = Mockito.mock(TagStore.class);
    conversationServlet.setTagStore(mockTagStore);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    List<Conversation> fakeConversationList = new ArrayList<>();
    fakeConversationList.add(
        new Conversation(UUID.randomUUID(), UUID.randomUUID(), TEST_CONVERSATION, Instant.now()));
    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);

    conversationServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("conversations", fakeConversationList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_UserNotLoggedIn() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    conversationServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockConversationStore, Mockito.never())
        .addConversation(Mockito.any(Conversation.class));
    Mockito.verify(mockResponse).sendRedirect("/conversations");
  }

  @Test
  public void testDoPost_InvalidUser() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn(TEST_USERNAME);
    Mockito.when(mockUserStore.getUser(TEST_USERNAME)).thenReturn(null);

    conversationServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockConversationStore, Mockito.never())
        .addConversation(Mockito.any(Conversation.class));
    Mockito.verify(mockResponse).sendRedirect("/conversations");
  }

  @Test
  public void testDoPost_BadConversationName() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("conversationTitle")).thenReturn("bad !@#$% name");
    Mockito.when(mockSession.getAttribute("user")).thenReturn(TEST_USERNAME);
    Mockito.when(mockSession.getAttribute("password")).thenReturn(TEST_PASSWORD);

    User fakeUser = new User(UUID.randomUUID(), TEST_USERNAME, Instant.now(), TEST_PASSWORD, TEST_ABOUT);
    Mockito.when(mockUserStore.getUser(TEST_USERNAME)).thenReturn(fakeUser);

    conversationServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockConversationStore, Mockito.never())
        .addConversation(Mockito.any(Conversation.class));
    Mockito.verify(mockRequest).setAttribute("error", "Please enter only letters and numbers.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_ConversationNameTaken() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("conversationTitle")).thenReturn(TEST_CONVERSATION);
    Mockito.when(mockSession.getAttribute("user")).thenReturn(TEST_USERNAME);
    Mockito.when(mockSession.getAttribute("password")).thenReturn(TEST_PASSWORD);
    Mockito.when(mockSession.getAttribute("about")).thenReturn(TEST_ABOUT);

    User fakeUser = new User(UUID.randomUUID(), TEST_USERNAME, Instant.now(), TEST_PASSWORD, TEST_ABOUT);
    Mockito.when(mockUserStore.getUser(TEST_USERNAME)).thenReturn(fakeUser);

    Mockito.when(mockConversationStore.isTitleTaken(TEST_CONVERSATION)).thenReturn(true);

    conversationServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockConversationStore, Mockito.never())
        .addConversation(Mockito.any(Conversation.class));
    Mockito.verify(mockResponse).sendRedirect("/chat/test_conversation");
  }

  @Test
  public void testDoPost_NewConversation_NoTags() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("conversationTitle")).thenReturn(TEST_CONVERSATION);
    Mockito.when(mockSession.getAttribute("user")).thenReturn(TEST_USERNAME);
    Mockito.when(mockSession.getAttribute("password")).thenReturn(TEST_PASSWORD);
    Mockito.when(mockSession.getAttribute("about")).thenReturn(TEST_ABOUT);
    Mockito.when(mockRequest.getParameter("tags")).thenReturn(null);

    User fakeUser = new User(UUID.randomUUID(), TEST_USERNAME, Instant.now(), TEST_PASSWORD, TEST_ABOUT);
    Mockito.when(mockUserStore.getUser(TEST_USERNAME)).thenReturn(fakeUser);

    Mockito.when(mockConversationStore.isTitleTaken(TEST_CONVERSATION)).thenReturn(false);

    conversationServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<Conversation> conversationArgumentCaptor =
        ArgumentCaptor.forClass(Conversation.class);
    Mockito.verify(mockConversationStore).addConversation(conversationArgumentCaptor.capture());
    Assert.assertEquals(conversationArgumentCaptor.getValue().getTitle(), TEST_CONVERSATION);

    Mockito.verify(mockResponse).sendRedirect("/chat/test_conversation");
  }

  @Test
  public void testDoPost_NewConversation() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("conversationTitle")).thenReturn(TEST_CONVERSATION);
    Mockito.when(mockSession.getAttribute("user")).thenReturn(TEST_USERNAME);
    Mockito.when(mockSession.getAttribute("password")).thenReturn(TEST_PASSWORD);
    Mockito.when(mockSession.getAttribute("about")).thenReturn(TEST_ABOUT);
    Mockito.when(mockRequest.getParameter("tags")).thenReturn(TEST_TAG);

    User fakeUser = new User(UUID.randomUUID(), TEST_USERNAME, Instant.now(), TEST_PASSWORD, TEST_ABOUT);
    Mockito.when(mockUserStore.getUser(TEST_USERNAME)).thenReturn(fakeUser);

    Mockito.when(mockConversationStore.isTitleTaken(TEST_CONVERSATION)).thenReturn(false);

    conversationServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<Tag> tagArgumentCaptor =
        ArgumentCaptor.forClass(Tag.class);
    Mockito.verify(mockTagStore).addTag(tagArgumentCaptor.capture());
    Assert.assertEquals(tagArgumentCaptor.getValue().getTag(), TEST_TAG);


    ArgumentCaptor<Conversation> conversationArgumentCaptor =
        ArgumentCaptor.forClass(Conversation.class);
    Mockito.verify(mockConversationStore).addConversation(conversationArgumentCaptor.capture());
    Assert.assertEquals(conversationArgumentCaptor.getValue().getTitle(), TEST_CONVERSATION);

    Mockito.verify(mockResponse).sendRedirect("/chat/test_conversation");
  }
}
