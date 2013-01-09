<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.lang.System" %>
<%@ page import="java.util.Map.Entry" %>

<html>
<head>
 <title>include demo</title>
</head>
<body>
<%

for (Entry<String, String> entry : System.getenv().entrySet()) {
%>
	<br><%= entry.getKey() + " = " + entry.getValue() %><br>
<%
}

 %>

</body>
</html>