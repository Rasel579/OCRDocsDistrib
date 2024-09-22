package com.ocr.ocrdocs;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    private Tesseract tesseract;

    public void init() {
        File tmpFolder = LoadLibs.extractTessResources("win32-x86-64");
        System.setProperty("java.library.path", tmpFolder.getPath());
        tesseract = new Tesseract();
        tesseract.setLanguage("rus");
        tesseract.setOcrEngineMode(1);

        try {
            tesseract.setDatapath(getServletContext().getRealPath("") + "WEB-INF\\classes\\testdata");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        InputStream fileContent = filePart.getInputStream();

        String result = null;
        String path = null;
        try {
            File file = new File("file.pdf");
            FileUtils.copyInputStreamToFile(fileContent, file);
            result = tesseract.doOCR(file);
            path = file.getPath();
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }

        PrintWriter out = response.getWriter();
        out.println("<html><head><meta charset=\"UTF-8\"></meta></head><body>");
        out.println("<h1>" + result + "</h1>");
        out.println("<h1>" + path + "</h1>");
        out.println("</body></html>");
    }
}
