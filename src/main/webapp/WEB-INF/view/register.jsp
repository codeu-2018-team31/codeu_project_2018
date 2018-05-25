<%@ page import="java.util.List" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.User" %>

<%
String user = (String) request.getSession().getAttribute("user");
User loggedInUser = UserStore.getInstance().getUser(user);
%>

<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
 <title>Register</title>
 <link rel="stylesheet" href="/css/main.css">
 <style>
   label {
     display: inline-block;
     width: 100px;
   }
 </style>
</head>
<body>

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
   <h1>Register</h1>

   <% if(request.getAttribute("error") != null){ %>
       <h2 style="color:red"><%= request.getAttribute("error") %></h2>
   <% } %>

   <form action="/register" method="POST">
     <label for="username">Username: </label>
     <input type="text" name="username" id="username">
     <br/>
     <label for="password">Password: </label>
     <input type="password" name="password" id="password">
     <br/><br/>
     <label for="about">About You (Optional): </label>
     <textarea name="about" rows="10"></textarea>
     <br/><br/>
     <button type="submit">Submit</button>
   </form>
 </div>
</body>
</html>
