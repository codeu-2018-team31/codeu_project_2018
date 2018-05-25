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
<%@ page import="java.util.UUID" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%@ page import="codeu.model.store.basic.TagStore" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Tag" %>
<%@ page import="codeu.model.data.User" %>

<%
String user = (String) request.getSession().getAttribute("user");
String tag = (String) request.getAttribute("extracted_tag");
List<String> conversationTitles = (List<String>) request.getAttribute("conversations");
User loggedInUser = UserStore.getInstance().getUser(user);
ConversationStore conversationStore = ConversationStore.getInstance();
%>

<!DOCTYPE html>
<html>
<head>
  <title>Tags</title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">

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

    <h1> <%= tag %></h1>
    <h2> Check out other conversations with tagged with <%= tag %>! </h2>
    <ul class="mdl-list"> 
    <% for (String convo : conversationTitles) { 
    %>
          <li><a href="/chat/<%= convo %>">
          <%= convo %></a></li>
    <% } %> 
    </ul>
  </div>

</body>
</html>
