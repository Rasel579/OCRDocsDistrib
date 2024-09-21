package com.ocr.ocrdocs;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import com.sun.tools.javac.Main;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.LoadLibs;

import javax.imageio.ImageIO;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
        File tmpFolder = LoadLibs.extractTessResources("win32-x86-64");
        System.setProperty("java.library.path", tmpFolder.getPath());
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("deu");
        tesseract.setOcrEngineMode(1);

        Path dataDirectory = null;
        try {
            dataDirectory = Paths.get(ClassLoader.getSystemResource("data").toURI());
            tesseract.setDatapath(dataDirectory.toString());

            BufferedImage image = ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream("/ocrexample.jpg")));
            String result = tesseract.doOCR(image);
            System.out.println(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}