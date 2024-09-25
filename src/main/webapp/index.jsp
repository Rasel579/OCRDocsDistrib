<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Интеллектуальная система верификации и сортировки входящей документации</title>
</head>
<body>
<h1><%= "Интеллектуальная система верификации и сортировки входящей документации"%></h1>
<br/>
<h3><%= "Добавьте файлы"%></h3>
<form action="upload" method="post" enctype="multipart/form-data">
  <input type="file" name="files" multiple="true" />
  <input type="submit" />
</form>
<h3><%= "Все документы"%></h3>
<a href="docs">Посмотреть все документы</a>
</body>
</html>