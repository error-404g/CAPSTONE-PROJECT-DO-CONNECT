<%@ page import="java.util.*, com.contactmanager.model.Contact" %>

<html>
<body>

<h2>All Contacts</h2>

<table border="1" cellpadding="10">
<tr>
    <th>ID</th>
    <th>Name</th>
    <th>Phone</th>
    <th>Actions</th>
</tr>

<%
    List<Contact> list = (List<Contact>) request.getAttribute("contacts");
    if(list != null) {
        for (Contact c : list) {
%>
<tr>
    <td><%= c.getId() %></td>
    <td><%= c.getName() %></td>
    <td><%= c.getPhone() %></td>
    <td>
        <a href="updateContact.jsp?id=<%=c.getId()%>&name=<%=c.getName()%>&phone=<%=c.getPhone()%>">Edit</a> |
        <a href="delete?id=<%=c.getId()%>">Delete</a>
    </td>
</tr>
<%
        }
    }
%>
</table>

<br><a href="index.jsp">Home</a>

</body>
</html>
