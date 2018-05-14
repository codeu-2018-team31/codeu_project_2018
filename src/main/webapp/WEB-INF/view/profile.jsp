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

<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.format.FormatStyle" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.time.ZoneId" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>

<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>

<%
String user = (String) request.getSession().getAttribute("user");
User loggedInUser = UserStore.getInstance().getUser(user);
User profileUser = (User) request.getAttribute("user");
List<Message> messages = (List<Message>) request.getAttribute("messages");
DateTimeFormatter formatter =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                     .withLocale(Locale.US)
                     .withZone(ZoneId.systemDefault());
%>

<!DOCTYPE html>
<html>
<head>
  <title>CodeU Chat App</title>
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
    <div style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">
        
      <h1><%= profileUser.getName() %>'s Profile Page</h1>
        
      <hr/>

      <h2>About <%= profileUser.getName() %></h2>
        <% if (loggedInUser != null && profileUser != null) {%>
          <% if (loggedInUser.getAbout().isEmpty()) {%>
              <p> <%= profileUser.getName() %> has not added any information yet! </p>
            <% } else{ %>
              <p>
                <%= profileUser.getAbout() %>
              </p>
          <% } %>
          
          <% if(profileUser.getId().equals(loggedInUser.getId())) { %>
            <h3>Edit your About Me</h3>
      
            <form action="/profile/<%= loggedInUser.getId().toString() %>" method="POST">
              <textarea name="editAbout" rows="10" tabindex="6" maxlength="1500"> 
                <%= loggedInUser.getAbout() %> 
              </textarea>
              <br/><br/>
              <input type="submit" value="Submit">
            </form>
        <% } %>
      <hr/>

      <h2>Sent Messages</h2>

      <div id="chat">
        <ul> 
          <%
          for(Message m : messages){
          %>
            <li><strong><%= formatter.format(m.getCreationTime()) %>: </strong><%= m.getContent() %></li>
          <%
          }
          %>
        </ul>
      </div>

      <hr/>
    </div>
  </div>
</body>
</html>
