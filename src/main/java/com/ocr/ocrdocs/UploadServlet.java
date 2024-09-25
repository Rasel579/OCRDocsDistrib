package com.ocr.ocrdocs;

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
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Сервлет для загрузки файлов на сервер определния категорий документа
 * и принадлежности их определенным сотрудникам
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "upload";
    private String UPLOAD_PATH = "upload";
    private Tesseract tesseract;

    //Инициализируем Teseract для перевода сканированного текста в машинный, а также пути для сохранения файлов
    public void init() {
        File tmpFolder = LoadLibs.extractTessResources("win32-x86-64");
        System.setProperty("java.library.path", tmpFolder.getPath());
        tesseract = new Tesseract();
        tesseract.setLanguage("rus");
        tesseract.setOcrEngineMode(1);
        UPLOAD_PATH = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            tesseract.setDatapath(getServletContext().getRealPath("") + "WEB-INF\\classes\\testdata");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //определяем список файлов загруженных пользователем
        List<Part> fileParts = request.getParts().stream().filter(part -> "files".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());
        //Для каждого файла определяем категорию, принадлежность к пользователю и сохраняем на сервер и в БД
        for (Part filePart : fileParts) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            InputStream fileContent = filePart.getInputStream();
            String result;
            DocsTypes doc;
            String fileUrl;
            String docPath;
            String fileTitle;
            try {
                File file = new File(fileName);
                FileUtils.copyInputStreamToFile(fileContent, file);
                result = tesseract.doOCR(file);
                doc = findType(result.split(" "));

                docPath = UPLOAD_PATH + File.separator + doc.getPath();
                fileTitle = doc.getDescription() + System.currentTimeMillis() + fileName;
                fileUrl = docPath + File.separator + fileTitle;

                saveFileToServer(docPath, filePart, fileUrl);
                saveFileToDb(doc.getDescription(), fileTitle, fileUrl);

                findEmployee(result.split(" "));

            } catch (TesseractException e) {
                throw new RuntimeException(e);
            }

            request.setAttribute("docs", DocsProvider.getUsersDocs());
            request.getRequestDispatcher("doc.jsp").forward(request, response);
        }
    }
    //Определить категорию документа
    private DocsTypes findType(String[] words) {
        for (String word : words) {
            if (DocsTypes.findType(word) != DocsTypes.UNKNOWN) {
                return DocsTypes.findType(word);
            }
        }

        return DocsTypes.UNKNOWN;
    }
    //сохранить файл на сервер в нужной категории
    private void saveFileToServer(String docPath, Part filePart, String fileUrl) throws IOException {
        File docPAthFile = new File(docPath);
        if (!docPAthFile.exists()) {
            docPAthFile.mkdir();
        }
        filePart.write(fileUrl);
    }
    //сохранить файл в БД
    private void saveFileToDb(String type, String fileTitle, String url) {
        DocsProvider.saveDoc(type, fileTitle, url);
    }
    //найти принадлежность файла к сотруднику и сохранить в БД принадлежность
    private void findEmployee(String[] words) {
        DocsProvider.getUserData().forEach(user -> {
            for (String word : words) {
                if (Objects.equals(word.toLowerCase(), user.getSecondName().toLowerCase())) {
                    DocsProvider.saveUserDoc(DocsProvider.getLastIdDoc(), user.getId());
                }
            }
        });
    }
}
