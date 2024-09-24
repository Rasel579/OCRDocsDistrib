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
import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "upload";
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
        List<Part> fileParts = request.getParts().stream().filter(part -> "files".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());

        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }

        for (Part filePart : fileParts) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
            InputStream fileContent = filePart.getInputStream();
            String result = null;
            String path = null;
            try {
                File file = new File(fileName);
                FileUtils.copyInputStreamToFile(fileContent, file);
                result = tesseract.doOCR(file);
                path = file.getPath();

                filePart.write(uploadPath + File.separator + fileName);

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
}
