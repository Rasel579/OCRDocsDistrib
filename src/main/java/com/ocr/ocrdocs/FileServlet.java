package com.ocr.ocrdocs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLDecoder;

/**
 *   Сервелет для скачивания файла с сервера
 */
public class FileServlet extends HttpServlet {
    private static final int DEFAULT_BUFFER_SIZE = 10240000;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestedFile = req.getPathInfo();
        if (requestedFile == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }
        File file = new File(URLDecoder.decode(requestedFile, "UTF-8"));

        if (!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        String contentType = getServletContext().getMimeType(file.getName());

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        resp.reset();
        resp.setBufferSize(DEFAULT_BUFFER_SIZE);
        resp.setContentType(contentType);
        resp.setHeader("Content-Length", String.valueOf(file.length()));
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        try( BufferedInputStream input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
             BufferedOutputStream output = new BufferedOutputStream(resp.getOutputStream(), DEFAULT_BUFFER_SIZE)) {

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
