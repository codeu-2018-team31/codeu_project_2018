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
String about = request.getAttribute("about");
UUID profileID = UUID.fromString(requestUrl.substring("/profile/".length()));
UUID userID = request.getSession().getAttribute("user").getID();
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
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
      <a href="/register">Register</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">
      <%
        String profileName = UserStore.getInstance()
          .getUser(profileID).getName();
      %>
      <h1><%= profileName %>'s Profile Page</h1>
      
      <hr/>

      <h2>About</h2>
        <h1>About <%= profileName %></h1>
        <p>
          <%= about %>
        </p>
      <% if(profileID.equals(userID)) { %>
        <h3>Edit your About Me (only you can see this)</h3>

          <form action="/profile/ <%= profileID.toString() %>" method="POST">
            <textarea name="editAbout" rows="10"> 
              <%= about %> 
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
