package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.Tag;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.TagStore;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagServlet extends HttpServlet {

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Tags. */
  private TagStore tagStore;

  /**
   * Set up state for handling tag-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
    setTagStore(TagStore.getInstance());
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }
  
  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the TagStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setTagStore(TagStore tagStore) {
    this.tagStore = tagStore;
  }

  /**
   * This function fires when a user requests the /tag URL. It gets the contents of the tag from
   * the URL and finds the corresponding conversation information.
   * It then forwards to tag.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // User is not logged in, don't let them see a profile
      System.out.println("Please login before viewing tagged conversations");
      response.sendRedirect("/login");
      return;
    }

    String requestUrl = request.getRequestURI();
    Matcher m = Pattern.compile("^/tag/(.+)").matcher(requestUrl);
    if (!m.find()) {
      System.out.println("wrong URL pattern: " + requestUrl);
      return; // return error 
    }
    String extractedTag = m.group(1);
    List<String> conversationTitles = new ArrayList<>();

    // Sets request attribute to a list of Conversation titles that are associated with the Tag
    for (Tag tag : tagStore.getAllTags()) {
      if (tag.getTag().equals(extractedTag)) {
        Conversation convo = conversationStore.getConversationWithID(tag.getConversation());
        conversationTitles.add(convo.getTitle());
      }
    }
    
    request.setAttribute("extracted_tag", extractedTag);
    request.setAttribute("conversations", conversationTitles);
    
    request.getRequestDispatcher("/WEB-INF/view/tag.jsp").forward(request, response);
  }

  /**
   * Handles adding tags to an already existing Conversation.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    Matcher m = Pattern.compile("^/addtags/(.+)").matcher(requestUrl);
    if (!m.find()) {
      System.out.println("wrong URL pattern: " + requestUrl);
      return; // return error 
    }

    String conversationTitle = m.group(1);
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
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them add tags
      System.out.println("User not found: " + username);
      response.sendRedirect("/login");
      return;
    }

    // Set request attributes to be ready to reload the chat page
    request.setAttribute("conversation", conversation);
    request.setAttribute("messages", messageStore.getMessagesInConversation(conversation.getId()));

    // Get all tags
    String tags = request.getParameter("tags");
    if (tags != null && tags.length() > 0) {
      if (!tags.matches("(\\w*(,\\s)?)*")) {
        request.setAttribute("error", "Please enter tags as comma-separated words with one space between them. Tags can only contain letters and numbers.");
        request.setAttribute("tags", tagStore.getTagsInConversation(conversation.getId()));
        request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
        return;
      }

      List<String> splitTags = Arrays.asList(tags.split(", "));

      for (String tag : splitTags) {
        Tag newTag = new Tag(UUID.randomUUID(), conversation.getId(), tag.toLowerCase(), Instant.now());
        tagStore.addTag(newTag);
      }
    }

    request.setAttribute("tags", tagStore.getTagsInConversation(conversation.getId()));
    response.sendRedirect("/chat/" + conversationTitle);
  }

}
