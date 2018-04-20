package com.cttic.liugw.excel;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.cttic.liugw.excelToPdf.Excel2Pdf;
import com.cttic.liugw.excelToPdf.ExcelObject;
import com.itextpdf.text.DocumentException;





public class excelMain {
    static final String inPath = "D:\\work\\svn\\Transport_IC_Card\\00-DOCUMENT\\工作量";
    static final String outPath = "D:\\work\\svn\\Transport_IC_Card\\00-DOCUMENT\\工作量OUT";
    static final String PDFExcelPath = "D:\\work\\svn\\Transport_IC_Card\\00-DOCUMENT\\工作量合并";
    static final String PDFOutPath = "D:\\work\\svn\\Transport_IC_Card\\00-DOCUMENT\\工作量OUT";
    
    public static void  createExcel(String filename){
        File file = new File(filename);
        if(!file.exists()){
            try {


             // 创建工作薄
                HSSFWorkbook workbook = new HSSFWorkbook();
                // 创建工作表
                HSSFSheet sheet = workbook.createSheet("sheet1");
                
                for (int row = 0; row < 10; row++)
                {
                   HSSFRow rows = sheet.createRow(row);
                   for (int col = 0; col < 10; col++)
                   {
                      // 向工作表中添加数据
                      rows.createCell(col).setCellValue("data" + row + col);
                   }
                }

                File xlsFile = new File(filename);
                FileOutputStream xlsStream = new FileOutputStream(xlsFile);
                workbook.write(xlsStream);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public static List<HSSFSheet> readExcel(String filename) throws Exception{
//        File xlsFile = new File(filename);
        List<HSSFSheet> sheetList = new LinkedList<>();
        // 获得工作簿
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filename));;
        // 获得工作表个数
        int sheetCount = workbook.getNumberOfSheets();
        // 遍历工作表
        for (int i = 0; i < sheetCount; i++)
        {
            HSSFSheet sheet = workbook.getSheetAt(i);
           sheetList.add(sheet);
//           // 获得行数
//           int rows = sheet.getLastRowNum() + 1;
//           // 获得列数，先获得一行，在得到改行列数
//           Row tmp = sheet.getRow(0);
//           if (tmp == null)
//           {
//              continue;
//           }
//           int cols = tmp.getPhysicalNumberOfCells();
//           System.out.println("rows="+rows);
//           System.out.println("cols="+cols);
           // 读取数据
//           for (int row = 0; row < rows; row++)
//           {
//              Row r = sheet.getRow(row);
//              for (int col = 0; col < cols; col++)
//              {
//                 System.out.printf("%10s", r.getCell(col).getStringCellValue());
//              }
//              System.out.println();
//           }
        }
        return sheetList;
    }
    
    public static void genPdfFromExcel() throws MalformedURLException, DocumentException, IOException{
        FileOutputStream fos = new FileOutputStream(new File(PDFOutPath + "\\工作量.pdf"));
        List<ExcelObject> pdfObjects = new ArrayList<ExcelObject>();
        
        // 获取Excel文件列表
        Map<String, List<File>> fileMapList = getFileList1(PDFExcelPath);
        
        // 遍历文件列表
        for (Map.Entry<String, List<File>> entry: fileMapList.entrySet()) {
            for(File file2 : entry.getValue() ){
                System.out.println(file2.getAbsolutePath());
                //////////////////////////将Excel 转换为PDF
                FileInputStream fis1 = new FileInputStream(file2);
                pdfObjects.add(new ExcelObject(file2.getName(),fis1));
            }
        }
        Excel2Pdf pdf = new Excel2Pdf(pdfObjects , fos);
        pdf.convert();
    }
    
    public static Map<String, List<File>> getFileList1(String path){
        Map<String, List<File>> fileMapList = new HashMap<>();
        File dir = new java.io.File(path);

        if(dir.exists()){
                if(dir.isDirectory()){
                    List<File> fileExcelList= new LinkedList<>();
                    for(File excelfile:dir.listFiles(new FileFilter() {
                                @Override
                                public boolean accept(File pathname) {
                                    if(pathname.exists() && pathname.isFile() && pathname.getName().toUpperCase().endsWith("XLS"))
                                    {
                                        return true;
                                    }
                                    return false;
                                }
                            })
                    ){
                        fileExcelList.add(excelfile);
                    }
                    fileMapList.put(dir.getName(), fileExcelList);
                }else{
                    
                }
        }
        return fileMapList;
    }
    
    public static Map<String, List<File>> getFileList(String path){
        Map<String, List<File>> fileMapList = new HashMap<>();
        File file = new java.io.File(path);

        if(file.exists()){
            File[] files = file.listFiles();
            for (File dir : files) {
                if(dir.isDirectory()){
                    List<File> fileExcelList= new LinkedList<>();
                    for(File excelfile:dir.listFiles(new FileFilter() {
                                @Override
                                public boolean accept(File pathname) {
                                    if(pathname.exists() && pathname.isFile() && pathname.getName().toUpperCase().endsWith("XLS"))
                                    {
                                        return true;
                                    }
                                    return false;
                                }
                            })
                    ){
                        fileExcelList.add(excelfile);
                    }
                    fileMapList.put(dir.getName(), fileExcelList);
                }
            }
        }
        return fileMapList;
    }

    public static void mergeExcel()throws Exception {
        //  遍历目录下的所有excel文件
        Map<String, List<File>> fileMapList = getFileList(inPath);

        /////// 处理文件
        for (Map.Entry<String, List<File>> entry: fileMapList.entrySet()) {
            System.out.println(entry.getKey());
            String outFileName = outPath + "\\" + entry.getKey() + "_工作量.xls";
            
//            // 创建工作薄
//            HSSFWorkbook outWorkbook = new HSSFWorkbook();
            // 创建工作表
//            HSSFSheet outSheet = outWorkbook.createSheet(entry.getKey()+"-工作量");
            int outRow = 0;
            int sheetCnt = 0;
            boolean firstfile = true;
            
            File outfile = new File(outFileName);
            HSSFWorkbook outWorkbook;
            if(outfile.exists()){
//                outWorkbook = new HSSFWorkbook(new FileInputStream(outFileName));;
                outWorkbook = new HSSFWorkbook();
            }else{
                // 创建工作薄
                outWorkbook = new HSSFWorkbook();
            }
         // 创建工作表
          HSSFSheet outSheet = outWorkbook.createSheet(entry.getKey()+"-工作量");
          HSSFCellStyle newstyle = outWorkbook.createCellStyle();
          HSSFCellStyle newstyle1 = outWorkbook.createCellStyle();
          HSSFCellStyle newstyle0 = outWorkbook.createCellStyle();
          newstyle1.setWrapText(false);
          newstyle.setWrapText(true);
          newstyle0.setWrapText(false);

          for(File file2 : entry.getValue() ){
//                File outfile = new File(outFileName);
//                HSSFWorkbook outWorkbook;
//                if(outfile.exists()){
//                    outWorkbook = new HSSFWorkbook(new FileInputStream(outFileName));;
//                }else{
//                    // 创建工作薄
//                    outWorkbook = new HSSFWorkbook();
//                }
                System.out.println(file2.getAbsolutePath());
                List<HSSFSheet> sheetList = readExcel(file2.getAbsolutePath());
                // 处理当前文件的所有sheet
                for (HSSFSheet sheet : sheetList) {
//                    POIUtils.mergerRegion(sheet, outSheet);
//                    outRow = 0;
                 // 创建工作表
//                    HSSFSheet outSheet = outWorkbook.createSheet(sheet.getSheetName()+sheetCnt++);
//                    System.out.println(sheet.getSheetName());
//                    POIUtils.mergerRegion(sheet, outSheet);
//                    System.out.println("in_sheet.rows=" + sheet.getLastRowNum());
//                    System.out.println("outSheet.rows=" + outSheet.getLastRowNum());
                    outSheet.createRow(outRow++);
                    
                    
                    int rows = sheet.getLastRowNum() + 1;
                    for (int row = 0; row < rows; row++){
                        HSSFRow sourceRow = sheet.getRow(row);
                        if(row==0){
                            outSheet.addMergedRegion(new CellRangeAddress( outRow,outRow, 0, 5));// 设置单元格合
                        }
                        HSSFRow targetRow = outSheet.createRow(outRow++);
                        targetRow.setHeight(sourceRow.getHeight());
                   
                        
                        int sourceCols = sourceRow.getPhysicalNumberOfCells();
                        for (int col = 0; col < sourceCols; col++){
                            HSSFCell sourceCell = sourceRow.getCell(col);

                            HSSFCell targetCell = targetRow.createCell(col);
//                            targetCell.set
//                            sourceCell.getCellTypeEnum()
//                            targetCell.setCellValue(sourceCell.getStringCellValue());
//                            targetCell.setCellStyle(sourceCell.getCellStyle());
//                            targetCell.setCellType(sourceCell.getCellType());
                            POIUtils.copyCell(outWorkbook, sourceCell, targetCell, true,firstfile);
                            if (sourceCell != null) {
//                                HSSFCellStyle newstyle1=outWorkbook.createCellStyle();
//                                POIUtils.copyCellStyle(sourceCell.getCellStyle(), newstyle1);
                                
                                if(row==1){
                                    POIUtils.copyCellStyle(sourceCell.getCellStyle(), newstyle1);
////                                  //设置背景颜色
//                                  newstyle1.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
//                                  newstyle1.setFillBackgroundColor(IndexedColors.DARK_YELLOW.getIndex());
//                                  //solid 填充  foreground  前景色
//                                  newstyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//                                  newstyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                                  targetCell.setCellStyle(newstyle1);
                                }else{
                                    POIUtils.copyCellStyle(sourceCell.getCellStyle(), newstyle);
                                    if(row==0){
                                        newstyle0.setWrapText(false);
                                        targetCell.setCellStyle(newstyle0);
                                    }else{
                                        newstyle.setWrapText(true);
                                        targetCell.setCellStyle(newstyle);
                                    }
                                    
                                }
                                if(row==0 && col ==0){
                                    targetCell.setCellValue(targetCell.getStringCellValue() + "(" + entry.getKey() + ")" );
                                }
                            }
                            outSheet.setColumnWidth(col, sheet.getColumnWidth(col));
                        }
                    }
                    outSheet.setColumnWidth(1, (int)(sheet.getColumnWidth(1)*1.3));
//                    for(int ii=0; ii < 100; ii++){
//                        outSheet.autoSizeColumn(ii);
//                        
//                    }
                }
                firstfile = false;
//                File xlsFile = new File(outFileName);
//                FileOutputStream xlsStream = new FileOutputStream(xlsFile);
//                outWorkbook.write(xlsStream);
//                outWorkbook.close();
//                xlsStream.close();
//                break;
            }
            // sheet 写入
//            System.out.println("outSheet.rows=" + outSheet.getLastRowNum());
            File xlsFile = new File(outFileName);
            FileOutputStream xlsStream = new FileOutputStream(xlsFile);
            outWorkbook.write(xlsStream);
//            outWorkbook.close();
            xlsStream.close();
//            break;
        }

    }
    
    public static void main(String[] args) throws Exception {
        // 多个Excel文件合并为一个excel文件
        //mergeExcel();
        genPdfFromExcel();
    }
}
