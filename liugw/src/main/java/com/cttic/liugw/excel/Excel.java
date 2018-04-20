package com.cttic.liugw.excel;

import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.InputStream;  
import java.io.OutputStream;  
  
import jxl.Cell;  
import jxl.Sheet;  
import jxl.Workbook;  
import jxl.format.Colour;  
import jxl.format.UnderlineStyle;  
import jxl.write.Label;  
import jxl.write.Number;  
import jxl.write.WritableCellFormat;  
import jxl.write.WritableFont;  
import jxl.write.WritableSheet;  
import jxl.write.WritableWorkbook;  
  
  
public class Excel {  
  
    /** 
     * @param args 
     * @author  
     * @throws Exception  
     * @createtime 2015-7-31 上午09:58:41 
     */  
    public static void main(String[] args) throws  Exception {  
//        createExcel();  
        copeExcel();  
        readExcel();  
    }  
      
    public static void createExcel() throws  Exception{  
        String url="E:\\excel\\javaExcel.xls";  
        //创建文件  
        OutputStream os=new FileOutputStream(url);  
        WritableWorkbook wwb=Workbook.createWorkbook(os);  
        //创建第一张sheet  
        WritableSheet ws=wwb.createSheet("sheet1", 0);  
        //添加label对象  
        Label label=new Label(0, 0, "label cell");  
        ws.addCell(label);  
        //添加带有字型的label对象  
        WritableFont wf=new WritableFont(WritableFont.TIMES, 18, WritableFont.BOLD, true);  
        WritableCellFormat wcf=new WritableCellFormat(wf);  
        Label labelwf=new Label(0, 1, "label cell 0 1", wcf);  
        ws.addCell(labelwf);  
        //添加带有字体颜色的label对象  
        WritableFont wfc=new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.RED);  
        WritableCellFormat wcfc=new WritableCellFormat(wfc);  
        Label labelwcfc=new Label(0,2,"label cell 0 2",wcfc);  
        ws.addCell(labelwcfc);  
        //添加number,还有其他类型没有写  
        Number labelN=new Number(0, 3, 2.1);  
        ws.addCell(labelN);  
        wwb.write();  
        wwb.close();  
          
          
    }  
      
    public static void copeExcel() throws Exception{  
        String url="E:\\javaExcel1.xls";  
        //创建文件  
        OutputStream os=new FileOutputStream(url);  
        WritableWorkbook wwb=Workbook.createWorkbook(os);  
        //输入流文件路径读取  
        InputStream in=new FileInputStream("D:\\work\\svn\\Transport_IC_Card\\00-DOCUMENT\\工作量\\孙跃\\青海省一卡通_孙跃_钱包交易(20161020~20161021).xls");  
        //构建workbook  
        Workbook workBook=Workbook.getWorkbook(in);  
        //获取读取文件的sheet数  
        int sheet=workBook.getNumberOfSheets();  
        for (int i = 0; i < sheet; i++) {  
            //获取读取文件的sheet对象  
            Sheet st=workBook.getSheet(i);  
            //复制文件创建sheet对象  
            WritableSheet ws=wwb.createSheet("sheet"+(i+1), i);  
            for (int j = 0; j < st.getColumns(); j++) {//循环读取文件的列  
                for (int j2 = 0; j2 < st.getRows(); j2++) {//循环读取文件的行  
                    Label label=new Label(j, j2, st.getCell(j, j2).getContents());//获取读取文件的值创建label  
                    ws.addCell(label);//将label添加的复杂文件中  
                }  
            }  
        }  
        wwb.write();//写入  
        wwb.close();//关闭  
    }  
      
    public static void readExcel() throws Exception{  
        //输入流文件路径读取  
        InputStream in=new FileInputStream("E:\\excel\\javaExcel.xls");  
        //构建workbook  
        Workbook workBook=Workbook.getWorkbook(in);  
        //读取第一张sheet表  
        Sheet st=workBook.getSheet(0);  
        //读取第一行第一列  
        Cell cell=st.getCell(0, 0);  
        //将读取的excel数据转换成字符串  
        String cellStr=cell.getContents();  
        //读取第二行第一列  
        Cell cell1=st.getCell(st.getColumns()-1>1?1:st.getColumns()-1, 0);  
        String cellStr1=cell1.getContents();  
        //读取第一行第二列  
        Cell cell01=st.getCell(0, st.getRows()-1>1?1:st.getRows()-1);  
        String cellStr01=cell01.getContents();  
        System.out.println(cellStr+"|"+cellStr1+"|"+cellStr01);  
        //打印读取到的excel数据类型  
        System.out.println(cell.getType());  
        //获取sheet个数  
        int sheetNumber=workBook.getNumberOfSheets();  
        //获取sheet对象数组  
        Sheet[] sheetArr=workBook.getSheets();  
        //获取sheet的名称  
        String sheetName=st.getName();  
        //获取sheet表中的总列数  
        int sheetColumn=st.getColumns();  
        //获取某一列的单元格，返回数组对象  
        Cell[] cellColumnArr=st.getColumn(0);  
        //获取行数  
        int sheetRow=st.getRows();  
        //获取行下的单元格  
        Cell[] cellRowArr=st.getRow(0);  
          
        //操作完成关闭对象，释放占用的内存  
        workBook.close();  
          
    }  
  
}  