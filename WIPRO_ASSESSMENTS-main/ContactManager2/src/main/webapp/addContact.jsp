<%@ page language="java" %>
<html>
<body>

<h2>Add Contact</h2>

<form action="addContact" method="post">
    ID: <input type="text" name="id"><br><br>
    Name: <input type="text" name="name"><br><br>
    Phone: <input type="text" name="phone"><br><br>

    <input type="submit" value="Add Contact">
</form>

<h3>${message}</h3>

</body>
</html>
