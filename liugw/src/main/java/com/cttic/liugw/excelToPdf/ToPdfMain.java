package com.cttic.liugw.excelToPdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ToPdfMain {
    public static void main(String[] args) throws Exception {
//      FileInputStream fis = new FileInputStream(new File(directory+"副本Common-ReportList-AssistPDF-Temp.xls"));
//      FileInputStream fis = new FileInputStream(new File("D:\\tmp.xls"));
        
        FileInputStream fis1 = new FileInputStream(new File("D:\\work\\svn\\Transport_IC_Card\\00-DOCUMENT\\工作量OUT\\曹文静_工作量.xls"));
//        FileInputStream fis2 = new FileInputStream(new File("D:\\work\\svn\\Transport_IC_Card\\00-DOCUMENT\\工作量OUT\\段春利_工作量.xls"));
//        FileInputStream fis3 = new FileInputStream(new File("D:\\work\\svn\\Transport_IC_Card\\00-DOCUMENT\\工作量OUT\\郭鑫_工作量.xls"));
        //
        FileOutputStream fos = new FileOutputStream(new File("D:\\work\\svn\\Transport_IC_Card\\00-DOCUMENT\\工作量OUT\\test.pdf"));
        //
        List<ExcelObject> objects = new ArrayList<ExcelObject>();
        objects.add(new ExcelObject("曹文静_工作量.xls",fis1));
//        objects.add(new ExcelObject("段春利_工作量.xls",fis2));
//        objects.add(new ExcelObject("郭鑫_工作量.xls",fis3));
//        
        Excel2Pdf pdf = new Excel2Pdf(objects , fos);
        pdf.convert();
    }
}
