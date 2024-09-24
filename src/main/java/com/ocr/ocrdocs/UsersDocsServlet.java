package com.ocr.ocrdocs;

import com.ocr.ocrdocs.data.DataProvider;
import com.ocr.ocrdocs.data.DocsData;
import com.ocr.ocrdocs.data.DocsProvider;
import com.ocr.ocrdocs.utils.DocsTypes;
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
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/docs")
public class UsersDocsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("docs",DocsProvider.getData());
        request.getRequestDispatcher("doc.jsp").forward(request, response);
    }
}
