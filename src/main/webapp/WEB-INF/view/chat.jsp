<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Tag" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
String user = (String) request.getSession().getAttribute("user");
User loggedInUser = UserStore.getInstance().getUser(user);
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %></title>

  <link rel="stylesheet" href="/css/main.css" type="text/css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/node_modules/emoji-js/lib/emoji.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/node_modules/emoji-js/lib/jquery.emoji.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/node_modules/textcomplete/dist/textcomplete.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/emoji-suggest.js"></script>

  <script>
    // scroll the chat div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };
  </script>
</head>
<body onload="scrollChat()">

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <a href="/about.jsp">About</a>
    <% if(loggedInUser != null){ %>
      <a href= <%= "/profile/" + loggedInUser.getId().toString() %> > Hello <%= loggedInUser.getName() %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
      <a href="/register">Register</a>
    <% } %>
  </nav>

  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <h1><%= conversation.getTitle() %>
      <a href="" style="float: right">&#8635;</a></h1>

    <% if (loggedInUser != null) { %>
        <label class="form-control-label">Current tags:</label>
        <p>
        <% Set<Tag> tags = (Set<Tag>) request.getAttribute("tags");
           for (Tag tag : tags) {
             String tagName = tag.getTag();
        %>
          <a href="/tag/<%= tagName %>"><%= tagName %></a> |
        <% } %>
        <p>

        <form action="/addtags/<%= conversation.getTitle() %>" method="POST">
          <div class="form-group">
            <label class="form-control-label">New tags:</label>
            <input type="text" name="tags" placeholder="Comma-separated tags" width="200px">
          </div>
          <button type="submit">Add tags</button>
        </form>

        <hr/>
    <% } %>
    <hr/>


    <div id="chat">
      <ul>
    <%
      for (Message message : messages) {
        String author = UserStore.getInstance()
          .getUser(message.getAuthorId()).getName();
        String id = UserStore.getInstance()
          .getUser(message.getAuthorId()).getId().toString();
    %>
      <li><strong><a href="/profile/<%= id %>"><%= author %></a>:</strong> <%= message.getContent() %></li>
    <%
      }
    %>
      </ul>
    </div>

    <hr/>

    <% if (request.getSession().getAttribute("user") != null) { %>
    <form action="/chat/<%= conversation.getTitle() %>" method="POST">
        <input type="text" name="message" id="userInput"></input>
        <br/>
        <button type="submit">Send</button>
    </form>
    <% } else { %>
      <p><a href="/login">Login</a> to send a message.</p>
    <% } %>

    <hr/>

  </div>

</body>
</html>
