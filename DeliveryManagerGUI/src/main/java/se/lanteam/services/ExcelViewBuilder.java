package se.lanteam.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.document.AbstractXlsView;


public class ExcelViewBuilder extends AbstractXlsView {


	    @SuppressWarnings("unchecked")
	    protected void buildExcelDocument(Map<String, Object> model,
	            Workbook workbook,
	            HttpServletRequest request,
	            HttpServletResponse response)
	    {
	        //Variables required in model
	        String sheetName = (String)model.get("sheetname");
	        List<String> headers = (List<String>)model.get("headers");
	        List<List<String>> results = (List<List<String>>)model.get("results");
	        List<String> numericColumns = new ArrayList<String>();
	        if (model.containsKey("numericcolumns"))
	            numericColumns = (List<String>)model.get("numericcolumns");
	        String pageHeader = "Resultat";
	        if (model.containsKey("pageheader"))	        	
	        	pageHeader = (String)model.get("pageheader");
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
	        // Create page header
	        Row pageHeaderRow = sheet.createRow(currentRow);
	        Cell pageHeaderCell = pageHeaderRow.createCell(currentColumn);
	        pageHeaderCell.setCellStyle(headerStyle);
	        pageHeaderCell.setCellValue(pageHeader);
	        currentRow = 2;
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
	    }
}