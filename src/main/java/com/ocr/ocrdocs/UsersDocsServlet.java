package com.ocr.ocrdocs;

import com.ocr.ocrdocs.data.DocsProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Сервлет для отображения на странице docs.jsp данных по обработанным документам из БД
 */
@WebServlet("/docs")
public class UsersDocsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("docs",DocsProvider.getUsersDocs());
        request.getRequestDispatcher("doc.jsp").forward(request, response);
    }
}
