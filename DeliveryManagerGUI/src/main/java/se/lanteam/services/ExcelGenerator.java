package se.lanteam.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ExcelGenerator {
	
	@SuppressWarnings("unchecked")
	public Workbook generate(Map<String, Object> model) {
		
		Workbook workbook = new HSSFWorkbook();
		
		//Variables required in model
        String sheetName = (String)model.get("sheetname");
        List<String> headers = (List<String>)model.get("headers");
        List<List<String>> results = (List<List<String>>)model.get("results");
        List<String> numericColumns = new ArrayList<String>();
        if (model.containsKey("numericcolumns"))
            numericColumns = (List<String>)model.get("numericcolumns");
        //Build doc
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth((short) 12);
        int currentRow = 0;
        short currentColumn = 0;
        //Create style for header
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont); 
        //Populate header columns
        Row headerRow = sheet.createRow(currentRow);
        for(String header:headers){	            
            Cell cell = headerRow.createCell(currentColumn); 
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);            
            currentColumn++;
        }
        //Poppulate value rows/columns
        currentRow++;
        for(List<String> result: results){
            currentColumn = 0;
            Row row = sheet.createRow(currentRow);
            for(String value : result){
                Cell cell = row.createCell(currentColumn);
                if (numericColumns.contains(headers.get(currentColumn))){
                	cell.setCellType(CellType.NUMERIC);
                	Double doubleValue = 0.0;
                	if (!StringUtils.isEmpty(value)) {
                		doubleValue = Double.parseDouble(value);	
                	}	                	
                    cell.setCellValue(doubleValue);
                } else {
                    HSSFRichTextString text = new HSSFRichTextString(value);                
                    cell.setCellValue(text);                    
                }
                currentColumn++;
            }
            currentRow++;
        }
		return workbook;
	}
	
	public byte[] wbToByteArray(Workbook wb){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] bytes = null;
		try {
			try {
			    wb.write(bos);
			} finally {
			    bos.close();
			}
			bytes = bos.toByteArray();
		} catch (IOException e) {
		}		
		return bytes;
	}
}