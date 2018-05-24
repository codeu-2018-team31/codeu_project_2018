package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.Tag;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TagServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /**
   * Set up state for handling tag-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }
  
  /**
   * Displays a page of all Conversations that have a specific Tag.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    // Render "Tag profile pages" with URLs in the form of "/tag/<tagname>"
  }
  
  /**
   * Handles adding tags to an already existing Conversation.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/addtags/".length());
    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // Couldn't find conversation, redirect to conversation list
      System.out.println("Conversation was null: " + conversationTitle);
      response.sendRedirect("/conversations");
      return;
    }

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them add tags
      response.sendRedirect("/chat/" + conversationTitle);
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them add tags
      System.out.println("User not found: " + username);
      response.sendRedirect("/chat/" + conversationTitle);
      return;
    }

    // Set request attributes to be ready to reload the chat page
    request.setAttribute("conversation", conversation);
    request.setAttribute("messages", messageStore.getMessagesInConversation(conversation.getId()));
    List<Tag> tagsList = conversation.getTags();
    request.setAttribute("tags", tagsList);

    // Get all tags
    String tags = request.getParameter("tags");
    if (tags != null && tags.length() > 0) {
      if (!tags.matches("([\\w*](, ))*[\\w*]*")) {
        request.setAttribute("error", "Please enter tags as comma-separated words with one space between them. Tags can only contain letters and numbers.");
        request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
        return;
      }

      // Add all tags to the user's tagged conversations Map
      List<String> splitTags = Arrays.asList(tags.split(", "));
      for (String tag : splitTags) {
        // TODO: Check if the Tag already exists in TagStore
        Tag newTag = new Tag(UUID.randomUUID(), conversation, tag, Instant.now());
        conversation.addTag(newTag);
      }
      //conversationStore.putConversation(conversation);
      for (Tag tag : conversation.getTags()) {
        System.out.println(tag.getTag());
      }
      // Update tags attribute to the expanded Tag list
      request.setAttribute("tags", conversation.getTags());
    }
    request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
  }

}
