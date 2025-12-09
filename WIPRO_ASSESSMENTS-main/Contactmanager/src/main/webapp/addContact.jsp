<%@ page import="java.util.*, com.contactmanager.model.Contact" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Contact Manager</title>
  <style>
    body { font-family: Arial; margin: 40px; }
    form { margin-bottom: 30px; }
    input { margin: 5px; padding: 8px; }
    table { width: 100%; border-collapse: collapse; }
    th, td { padding: 10px; border: 1px solid #ccc; text-align: left; }
  </style>
</head>
<body>

  <h2>Add New Contact</h2>
  <form action="addContact" method="post">
    <input type="text" name="name" placeholder="Name" required />
    <input type="email" name="email" placeholder="Email" required />
    <input type="text" name="phone" placeholder="Phone" required />
    <input type="submit" value="Add Contact" />
  </form>

  <h2>All Contacts</h2>
  <table>
    <tr>
      <th>Name</th><th>Email</th><th>Phone</th>
    </tr>
    <%
      List<Contact> contacts = (List<Contact>) request.getAttribute("contacts");
      if (contacts != null && !contacts.isEmpty()) {
        for (Contact c : contacts) {
    %>
          <tr>
            <td><%= c.getName() %></td>
            <td><%= c.getEmail() %></td>
            <td><%= c.getPhone() %></td>
          </tr>
    <%
        }
      } else {
    %>
        <tr><td colspan="3">No contacts found</td></tr>
    <%
      }
    %>
  </table>

</body>
</html>
