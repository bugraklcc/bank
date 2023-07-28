package com.example.bankingApp.account.service.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Service
public class PdfService {

    // PDF dosyasını oluşturacak ve belirtilen klasöre kaydedecek metod
    public String createPdf(String content, String outputPath) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // Font ayarlarını yap
            Font titleFont = new Font(FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.BLACK);
            Font headerFont = new Font(FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);
            Font contentFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

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
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            return "PDF oluşturma ve kaydetme işlemi başarısız oldu.";
        }
    }
}
