package com.cttic.liugw.excel;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

public class POIUtils {  
//  /**  
//   * 把一个excel中的cellstyletable复制到另一个excel，这里会报错，不能用这种方法，不明白呀？？？？？  
//   * @param fromBook  
//   * @param toBook  
//   */  
//  public static void copyBookCellStyle(HSSFWorkbook fromBook,HSSFWorkbook toBook){  
//      for(short i=0;i<fromBook.getNumCellStyles();i++){  
//          HSSFCellStyle fromStyle=fromBook.getCellStyleAt(i);  
//          HSSFCellStyle toStyle=toBook.getCellStyleAt(i);  
//          if(toStyle==null){  
//              toStyle=toBook.createCellStyle();  
//          }  
//          copyCellStyle(fromStyle,toStyle);  
//      }  
//  }  
    /** 
     * 复制一个单元格样式到目的单元格样式 
     * @param fromStyle 
     * @param toStyle 
     */  
    public static void copyCellStyle(HSSFCellStyle fromStyle,  
            HSSFCellStyle toStyle) {  
        toStyle.setAlignment(fromStyle.getAlignment());  
        //边框和边框颜色  
        toStyle.setBorderBottom(fromStyle.getBorderBottom());  
        toStyle.setBorderLeft(fromStyle.getBorderLeft());  
        toStyle.setBorderRight(fromStyle.getBorderRight());  
        toStyle.setBorderTop(fromStyle.getBorderTop());  
        toStyle.setTopBorderColor(fromStyle.getTopBorderColor());  
        toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());  
        toStyle.setRightBorderColor(fromStyle.getRightBorderColor());  
        toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());  
          
        //背景和前景  
        toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());  
        toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());  
          
        toStyle.setDataFormat(fromStyle.getDataFormat());  
        toStyle.setFillPattern(fromStyle.getFillPattern());  
//      toStyle.setFont(fromStyle.getFont(null));  
        toStyle.setHidden(fromStyle.getHidden());  
        toStyle.setIndention(fromStyle.getIndention());//首行缩进  
        toStyle.setLocked(fromStyle.getLocked());  
        toStyle.setRotation(fromStyle.getRotation());//旋转  
        toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());  
        toStyle.setWrapText(fromStyle.getWrapText());  
          
    }  
    /** 
     * Sheet复制 
     * @param fromSheet 
     * @param toSheet 
     * @param copyValueFlag 
     */  
    public static void copySheet(HSSFWorkbook wb,HSSFSheet fromSheet, HSSFSheet toSheet,  
            boolean copyValueFlag) {  
        //合并区域处理  
        mergerRegion(fromSheet, toSheet);  
        for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {  
            HSSFRow tmpRow = (HSSFRow) rowIt.next();  
            HSSFRow newRow = toSheet.createRow(tmpRow.getRowNum());  
            //行复制  
            copyRow(wb,tmpRow,newRow,copyValueFlag);  
        }  
    }  
    /** 
     * 行复制功能 
     * @param fromRow 
     * @param toRow 
     */  
    public static void copyRow(HSSFWorkbook wb,HSSFRow fromRow,HSSFRow toRow,boolean copyValueFlag){
        int cols = 0;
        for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {  
            HSSFCell tmpCell = (HSSFCell) cellIt.next();  
            HSSFCell newCell = toRow.createCell(tmpCell.getColumnIndex());  
            copyCell(wb,tmpCell, newCell, copyValueFlag,false);  
        }  
    }  
    /** 
    * 复制原有sheet的合并单元格到新创建的sheet 
    *  
    * @param sheetCreat 新创建sheet 
    * @param sheet      原有的sheet 
    */  
    public static void mergerRegion(HSSFSheet sheetFrom, HSSFSheet sheetTo) {  
     // 初期化 

        CellRangeAddress region = null; 
        Row rowFrom = null; 
        Row rowTo = null; 
        Cell cellFrom = null; 
        Cell cellTo = null; 

        //セル結合のコピー 
        for (int i = 0; i < sheetFrom.getNumMergedRegions(); i++) { 
            region = sheetFrom.getMergedRegion(i); 
            if ((region.getFirstColumn() >= sheetFrom.getFirstRowNum()) 
                    && (region.getLastRow() <= sheetFrom.getLastRowNum())) {
                sheetTo.addMergedRegion(region); 
            }

        }

        //セルのコピー 
        for (int intRow = sheetFrom.getFirstRowNum(); intRow < sheetFrom.getLastRowNum(); intRow++) { 
            rowFrom = sheetFrom.getRow(intRow); 
            rowTo = sheetTo.createRow(intRow); 
            if (null == rowFrom) 
                continue;
            rowTo.setHeight(rowFrom.getHeight());
            for (int intCol = 0; intCol < rowFrom.getLastCellNum(); intCol++) { 
                //セル幅のコピー 
//                sheetTo.setDefaultColumnStyle(intCol, sheetFrom.getColumnStyle(intCol)); 
                sheetTo.setColumnWidth(intCol, sheetFrom.getColumnWidth(intCol)); 
                cellFrom = rowFrom.getCell(intCol); 
                cellTo = rowTo.createCell(intCol); 

                if (null == cellFrom) 
                    continue; 

                //セルスタイルとタイプのコピー 
                cellTo.setCellStyle(cellFrom.getCellStyle()); 
                cellTo.setCellType(cellFrom.getCellType()); 

                //タイトル内容のコピー 
                if (null != cellFrom.getStringCellValue() && !"".equals(cellFrom.getStringCellValue().trim())) 
                    cellTo.setCellValue(cellFrom.getStringCellValue()); 
            } 
        } 

        //枠線の設定 
        sheetTo.setDisplayGridlines(false); 

        //Excelのズーム設定 
        sheetTo.setZoom(80, 100); 
    }  

    /** 
     * 复制单元格 
     *  
     * @param srcCell 
     * @param distCell 
     * @param copyValueFlag 
     *            true则连同cell的内容一起复制 
     */  
    public static void copyCell(HSSFWorkbook wb,HSSFCell srcCell, HSSFCell distCell,  
            boolean copyValueFlag, boolean firstFile) {  
        
        if(srcCell == null ){
            return;
        };
//        if( firstFile ){
//            HSSFCellStyle newstyle=wb.createCellStyle();
////            copyCellStyle(srcCell.getCellStyle(), newstyle);
//            if(srcCell.getRowIndex() == 0){
//                newstyle1[srcCell.getColumnIndex()] = newstyle;
//            }
//            
//            if(srcCell.getRowIndex() == 1){
//                newstyle2[srcCell.getColumnIndex()] = newstyle;
//            }
//            
//            if(srcCell.getRowIndex() == 2){
//                newstyle3[srcCell.getColumnIndex()] = newstyle;
//            }
//        }
//        
//        HSSFCellStyle newstyle =wb.createCellStyle();;
//        if(srcCell.getRowIndex() == 0){
//            newstyle = newstyle1[srcCell.getColumnIndex()];
//        }else if(srcCell.getRowIndex() == 1){
//            newstyle = newstyle2[srcCell.getColumnIndex()];
//        }else{
//            newstyle = newstyle3[srcCell.getColumnIndex()];
//        }

//        distCell.setEncoding(srcCell.getEncoding());  
        //样式  
        HSSFCellStyle newstyle1=wb.createCellStyle();
        copyCellStyle(srcCell.getCellStyle(), newstyle1);
//        newstyle1.setWrapText(false);
//        newstyle1.setFillBackgroundColor(IndexedColors.DARK_YELLOW.getIndex());
//        newstyle1.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
//      //设置背景颜色
//        newstyle1.setFillForegroundColor(HSSFColor.LIME.index);
//        //solid 填充  foreground  前景色
//        newstyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
////        newstyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        
//        newstyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
//        newstyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//        newstyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//        newstyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        
//        newstyle1.setBorderBottom(BorderStyle.THIN); //下边框
//        newstyle1.setBorderLeft(BorderStyle.THIN);//左边框
//        newstyle1.setBorderTop(BorderStyle.THIN);//上边框
//        newstyle1.setBorderRight(BorderStyle.THIN);//右边框
        
        
//        distCell.setCellStyle(newstyle1);  
        //评论  
        if (srcCell.getCellComment() != null) {  
            distCell.setCellComment(srcCell.getCellComment());  
        }  
        // 不同数据类型处理  
        int srcCellType = srcCell.getCellType();  
        distCell.setCellType(srcCellType);  
        if (copyValueFlag) {  
            if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) {  
                if (HSSFDateUtil.isCellDateFormatted(srcCell)) {  
                    distCell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(srcCell.getDateCellValue()));
//                    System.out.println("日期");
                } else {  
                    distCell.setCellValue(srcCell.getNumericCellValue());  
                }  
            } else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {  
                distCell.setCellValue(srcCell.getRichStringCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {  
                // nothing21  
            } else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {  
                distCell.setCellValue(srcCell.getBooleanCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {  
                distCell.setCellErrorValue(srcCell.getErrorCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {  
                distCell.setCellFormula(srcCell.getCellFormula());  
            } else { // nothing29  
            }  
        }  
    }  
}  