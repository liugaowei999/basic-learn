package com.cttic.liugw.excelToPdf;

import java.io.OutputStream;

import com.itextpdf.text.Document;

public class PdfTool {
    //
    protected Document document;
    //
    protected OutputStream os;
    
    public Document getDocument() {
        if (document == null) {
            document = new Document();
        }
        return document;
    }
}