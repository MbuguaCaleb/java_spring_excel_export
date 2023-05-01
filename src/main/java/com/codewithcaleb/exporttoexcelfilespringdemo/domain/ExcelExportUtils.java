package com.codewithcaleb.exporttoexcelfilespringdemo.domain;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

//Class that will help us export from DB TO Excel
public class ExcelExportUtils {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    //Data we are going to export to the Excel file
    private List<Customer> customerList;

    //constructor injection
    //Data coming in will be injected into the class
    //As I instantiate my class, I bring in customer list and create a new-WorkBook
    public ExcelExportUtils(List<Customer> customerList) {
        this.customerList = customerList;
        //creating the Object of a new Workbook
        workbook = new XSSFWorkbook();
    }

    //methods that do the export

    //Method that creates the cells in the WorkBook
    //When we don't know the datatype of the Values we can use the Object Syntax
    //Note what createCell Method takes In, (Very Important)
    private void createCell(Row row, int columnCount, Object value, CellStyle style){
        //sheet sized according to Column Count
        sheet.autoSizeColumn(columnCount);

        //create the cell as well based on the no of columns
        Cell cell = row.createCell(columnCount);

        //We do not know DT of the values, thus we are going to have conditions for various datatypes
        //we just take in any datatype then cast it to the right one
        if(value instanceof Integer){
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
        cell.setCellValue((Boolean) value);
        }else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else {
            //set the value to string as i put it into the Cell
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    //When methods are just used within the class, they should be private
    //Method that creates the Header Row/Title of the Sheet
    private void createHeaderRow(){
        //The style is per sheet(Sheet concept in Excel)
        sheet = workbook.createSheet("Customer Information");
        //The first Row of the Excel sheet will be the title
        Row rowZero = sheet.createRow(0);

        //Creating Header Style
        //Once I create syle Object i can use setters to create my style
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);

        //takes in a type of the above workbook font
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        //Creating the header Cell (Row O)
        //This will span across all columns
        createCell(rowZero,0,"Customer Information",style);
        //Making that first column span through all columns
        //from customer object i will have 8 columns
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,8));
        font.setFontHeightInPoints((short) 10);

        //Set the column header titles
        Row rowOne = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        //row create
        //row 1 * column count x
        createCell(rowOne,0,"ID",style);
        createCell(rowOne,1,"First Name",style);
        createCell(rowOne,2,"Last Name",style);
        createCell(rowOne,3,"Email",style);
        createCell(rowOne,4,"Country",style);
        createCell(rowOne,5,"State",style);
        createCell(rowOne,6,"City",style);
        createCell(rowOne,7,"Address",style);
    }

    //Getting Customer Data to fit in the different Columns we have Created
    private void writeCustomerData(){
        //starts from 2nd Row
        int rowCount = 2;
        //creating the Style of the Data being Put In
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for(Customer customer: customerList){
            //for each customer in the List, Start Creating a Row from Count 2
            Row row = sheet.createRow(rowCount ++);
            int columnCount = 0;

            //(For that Particular Row Index Create the Columns)
            createCell(row,columnCount++,customer.getId(),style);
            createCell(row,columnCount++,customer.getFirstName(),style);
            createCell(row,columnCount++,customer.getLastName(),style);
            createCell(row,columnCount++,customer.getEmail(),style);
            createCell(row,columnCount++,customer.getAddress().getCountry(),style);
            createCell(row,columnCount++,customer.getAddress().getState(),style);
            createCell(row,columnCount++,customer.getAddress().getCity(),style);
            createCell(row,columnCount++,customer.getAddress().getAddress(),style);
        }
    }

    //Export Method Joining Everything
    //Public since it is going to be called from Outside/Service/Controller
    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        createHeaderRow();
        writeCustomerData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
