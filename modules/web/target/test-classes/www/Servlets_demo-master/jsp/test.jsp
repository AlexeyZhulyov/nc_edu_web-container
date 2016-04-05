<%! private int count = 1; %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Test JSP</title>
</head>
<h1>Hello JSP</h1>
<body>
Test JSP #<%=count%>.
</body>
</html>
<% count++; %>
