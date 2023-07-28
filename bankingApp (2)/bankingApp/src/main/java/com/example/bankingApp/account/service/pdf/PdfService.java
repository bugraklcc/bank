package com.example.bankingApp.account.service.pdf;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PdfService {

    public String createPdf(String content, String outputPath) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // Font ayarlarını yap (UTF-8 karakter kodlamasını kullanarak Türkçe karakterleri desteklemek için)
            BaseFont baseFont = BaseFont.createFont("FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 24, Font.BOLD, BaseColor.BLACK);
            Font headerFont = new Font(baseFont, 16, Font.BOLD, BaseColor.BLACK);
            Font contentFont = new Font(baseFont, 12, Font.NORMAL, BaseColor.BLACK);

            // Başlık, başlık fontu kullanılarak PDF'e ekle
            Paragraph title = new Paragraph("Banking App Raporu", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            // Boşluk ekle
            document.add(new Paragraph("\n"));

            // İçerik, başlık fontu kullanılarak PDF'e ekle
            Paragraph header = new Paragraph("Hesap Detayları", headerFont);
            document.add(header);

            // Boşluk ekle
            document.add(new Paragraph("\n"));

            // Metin içeriği ve içerik fontu kullanılarak PDF'e ekle
            Paragraph contentParagraph = new Paragraph(content, contentFont);
            document.add(contentParagraph);

            document.close();
            return "PDF oluşturuldu ve kaydedildi: " + outputPath;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return "PDF oluşturma ve kaydetme işlemi başarısız oldu.";
        }
    }
}
