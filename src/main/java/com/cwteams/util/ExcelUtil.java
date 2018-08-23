package com.cwteams.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtil {
	
	public static Workbook generearExcel(String[][] datos, String nombreHoja){
				
		Workbook libro = new HSSFWorkbook();		
		Sheet hoja = libro.createSheet(nombreHoja);
		
		CellStyle styleTitle = libro.createCellStyle();
		CellStyle styleData = libro.createCellStyle();
 
        Font font = libro.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);        
        styleTitle.setFont(font);    

        styleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        
        styleData.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleData.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleData.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleData.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        
        /*styleTitle.setFillForegroundColor(IndexedColors.AQUA.getIndex());        
        styleTitle.setFillPattern(CellStyle.SOLID_FOREGROUND);*/      
		
		for(int f=0;f<datos.length;f++){
			Row fila = hoja.createRow(f);
			
			for(int c=0;c<datos[f].length+1;c++){
			   Cell celda = fila.createCell(c);		
			   
			   if(c==0){
				    celda.setCellValue("Grupo #"+(f+1));
				    celda.setCellStyle(styleTitle);
				}else{
				    celda.setCellValue(datos[f][c-1]);
				    celda.setCellStyle(styleData);
				}  
			}
			
		}
		
		for (int x = 0; x < hoja.getRow(0).getPhysicalNumberOfCells(); x++) {
			hoja.autoSizeColumn(x);
	    }
		
		return libro;
	}	
}
