<html>
<body>

<h2>Update Contact</h2>

<form action="updateContact" method="post">

ID: <input type="text" name="id" value="<%= request.getParameter("id") %>" readonly><br><br>
Name: <input type="text" name="name" value="<%= request.getParameter("name") %>"><br><br>
Phone: <input type="text" name="phone" value="<%= request.getParameter("phone") %>"><br><br>

<input type="submit" value="Update">
</form>

</body>
</html>
