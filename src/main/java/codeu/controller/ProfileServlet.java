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
import codeu.model.data.Message;
import codeu.model.data.Conversation;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.ConversationStore;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Servlet class responsible for the profile page.
    * Provides methods for accessing and editing about information when user requests the /profile URL.
    */
public class ProfileServlet extends HttpServlet {


  /** Store class that gives access to Users. */
  private UserStore userStore;

  private UUID profileId;

  /**Store class that gives access to Conversations  */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages */
  private MessageStore messageStore;

  /**All the messages for a given ID */
  private List <Message> messages;


  /**All the conversations */
  private List <Conversation> conversations;

  /* Only messages that correspond to a given user ID */
  private List <Message> realMessages;

/**
   * Set up state for handling profile-related requests. This method is only called when running in a
   * server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
  }
  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }
  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * This function fires when a user requests the /profile URL. It gets the ID of the user of the profile page from
   * the URL and finds the corresponding about information.
   * It then forwards to profile.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String requestUrl = request.getRequestURI();
    Matcher m = Pattern.compile("^/profile/(.+)").matcher(requestUrl);
    if (!m.find()) {
      System.out.println("wrong URL pattern: " + requestUrl);
      return; // return error 
    }
    String extractedId = m.group(1);

    profileId = UUID.fromString(extractedId);

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // User is not logged in, don't let them see a profile
      System.out.println("Please login before viewing profiles");
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(profileId); 

    conversations = conversationStore.getAllConversations(); //list of conversations
    for(Conversation c : conversations) {
      messages = messageStore.getMessagesInConversation(c.getId());
    }
     for(Message message:messages) {
       if(message.getAuthorId().equals(user.getId())) {
      realMessages.add(message);
    }
  }

    if (user == null) {
      // Couldn't find user, redirect to conversation list
      System.out.println("User does not exist: " + profileId);
      response.sendRedirect("/conversations");
      return;
    }
    request.setAttribute("user", user);
    request.setAttribute("messages", realMessages);
    request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on their profile page. It gets the
   * logged-in user ID from the session and the new about information from the submitted form
   * data. It uses this to update the user's about information that is displayed on their profile.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    UUID userId = ((User) request.getSession().getAttribute("user")).getId();

    String requestUrl = request.getRequestURI();
    Matcher m = Pattern.compile("^/profile/(.+)").matcher(requestUrl);
    if (!m.find()) {
      System.out.println("wrong URL pattern: " + requestUrl);
      return; // return error 
    }
    String extractedId = m.group(1);
    System.out.println("extracted Id from URL (doPost): " + extractedId);
    UUID profileId = UUID.fromString(extractedId);

    if(userId.equals(profileId)) {
      // User is viewing their own profile page
      String about = request.getParameter("editAbout");
      userStore.getUser(userId).setAbout(about);
      request.getSession().setAttribute("about", about);
    }

    response.sendRedirect("/profile/" + userId.toString());
  }
}
