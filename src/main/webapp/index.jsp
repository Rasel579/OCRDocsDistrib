<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Интеллектуальная система верификации и сортировки входящий документации</title>
</head>
<body>
<h1><%= "Интеллектуальная система верификации и сортировки входящий документации"%></h1>
<br/>
<h3><%= "Добавьте файлы"%></h3>
<form action="upload" method="post" enctype="multipart/form-data">
  <input type="file" name="files" multiple="true" />
  <input type="submit" />
</form>
</body>
</html>