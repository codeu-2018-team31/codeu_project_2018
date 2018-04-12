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
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<%
User loggedInUser = UserStore.getUser(request.getSession().getAttribute("user"));
User profileUser = request.getAttribute("user");
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
    <% if(loggedInUser != null){ %>
      <a>Hello <%= loggedInUser.getName(); %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
      <a href="/register">Register</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1><%= profileUser.getName(); %>'s Profile Page</h1>
      
      <hr/>

      <h2>About</h2>
        <h1>About <%= profileUser.getName(); %></h1>
        <p href="aboutDisplay">
          <%= profileUser.getAbout(); %>
        </p>
      <% if(profileUser.getId().equals(loggedInUser.getId());) { %>
        <h3>Edit your About Me (only you can see this)</h3>

          <form action="/profile/ <%= loggedInUser.getId().toString() %>" method="POST">
            <textarea name="editAbout" rows="10"> 
              <%= loggedInUser.getAbout(); %> 
            </textarea>
            <br/><br/>
            <input type="submit" value="Submit">
          </form>

          <hr/>

      <h2>Sent Messages</h2>

      <div id="chat">
        <ul>
          <li><strong>Timestamp:</strong> your message</li>
        </ul>
      </div>

      <hr/>

    </div>
  </div>
</body>
</html>
