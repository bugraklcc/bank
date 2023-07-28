package com.example.bankingApp.account.service.pdf;

import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

public class PdfService {

    public static byte[] createPdfContent(String content) {
        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString("<html><body>" + content + "</body></html>");
            renderer.layout();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            renderer.createPDF(outputStream);
            renderer.finishPDF();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createPdfDownloadLink(String content) {
        byte[] pdfContent = createPdfContent(content);
        if (pdfContent == null) {
            return null;
        }

        // PDF içeriğini byte dizisini Base64 formatına çevirerek linki oluşturuyoruz
        else {
            return null;
        }
    }
}
