<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head >
    <meta charset="UTF-8"/>
    <title>Документы</title>
</head>
<body>
<h1>Документы</h1>
    <table>
        <tr>
            <th>Название</th>
            <th>Url</th>
            <th>Тип</th>
            <th>Имя сотрудника</th>
            <th>Фамилия сотрудника</th>
        </tr>
        <c:forEach var="doc" items="${docs}" >
            <tr>
                <td><c:out value="${doc.docTitle}"/></td>
                <td><a href="file/<c:out value="${doc.docUrl}"/>">Скачать документ</a>></td>
                <td><c:out value="${doc.docType}"/></td>
                <td><c:out value="${doc.firstName}"/></td>
                <td><c:out value="${doc.secondName}"/></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
