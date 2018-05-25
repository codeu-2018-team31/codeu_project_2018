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
import codeu.model.data.Tag;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.TagStore;
import codeu.model.store.persistence.PersistentStorageAgent;

import codeu.model.store.basic.UserStore;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TagServletTest {

  private TagServlet tagServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private ConversationStore conversationStore;
  private MessageStore messageStore;
  private TagStore tagStore;
  private UserStore userStore;

  static final String TEST_USERNAME = "test username";
  static final UUID TEST_CONVO_ID = UUID.randomUUID();
  static final Conversation TEST_CONVO = new Conversation(TEST_CONVO_ID, UUID.randomUUID(), "test_convo", Instant.now());
  static final Tag TEST_TAG = new Tag(UUID.randomUUID(), TEST_CONVO_ID, "test_tag", Instant.now());
  static final User TEST_USER = new User(UUID.randomUUID(), TEST_USERNAME, Instant.now(), "test_password", "test_about");
  static final Message TEST_MESSAGE = new Message(UUID.randomUUID(), TEST_CONVO_ID, UUID.randomUUID(), "test_about", Instant.now());

  @Before
  public void setup() {
    tagServlet = new TagServlet();

    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/tag.jsp"))
        .thenReturn(mockRequestDispatcher);
    
    userStore = UserStore.getTestInstance(mockPersistentStorageAgent);
    conversationStore = ConversationStore.getTestInstance(mockPersistentStorageAgent);
    messageStore = MessageStore.getTestInstance(mockPersistentStorageAgent);
    tagStore = TagStore.getTestInstance(mockPersistentStorageAgent);

    tagServlet.setConversationStore(conversationStore);
    tagServlet.setMessageStore(messageStore);
    tagServlet.setUserStore(userStore);
    tagServlet.setTagStore(tagStore);

    conversationStore.addConversation(TEST_CONVO);
    tagStore.addTag(TEST_TAG);
  }

  @Test
    public void testDoGet() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/tag/" + TEST_TAG.getTag());
    
    List<String> conversations = new ArrayList<>();
    conversations.add(TEST_CONVO.getTitle()); 

    tagServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("extracted_tag", TEST_TAG.getTag());
    Mockito.verify(mockRequest).setAttribute("conversations", conversations);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
   
  @Test
  public void testDoPost_UserNotLoggedIn() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/addtags/" + TEST_CONVO.getTitle());
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    tagServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/login");
  }

  @Test
  public void testDoPost_InvalidUser() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/addtags/" + TEST_CONVO.getTitle());
    Mockito.when(mockSession.getAttribute("user")).thenReturn(TEST_USERNAME);

    tagServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/login");
  }

  @Test
  public void testDoPost_AddNewTags() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/addtags/" + TEST_CONVO.getTitle());
    Mockito.when(mockSession.getAttribute("user")).thenReturn(TEST_USERNAME);
    Mockito.when(mockRequest.getParameter("tags")).thenReturn("test, tags, now");

    userStore.addUser(TEST_USER);

    messageStore.addMessage(TEST_MESSAGE);

    tagServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("conversation", TEST_CONVO);
    Mockito.verify(mockRequest).setAttribute("tags", tagStore.getTagsInConversation(TEST_CONVO.getId()));
    Mockito.verify(mockResponse).sendRedirect("/chat/test_convo");
  }
}
