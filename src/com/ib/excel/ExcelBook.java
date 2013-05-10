/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 *
 * @author iL FeD
 */
public class ExcelBook {

    private HSSFWorkbook workbook;
    private final HSSFCellStyle dateStyle;
    private final HSSFCellStyle intStyle;
    private final HSSFCellStyle doubleStyle;
    private final HSSFCellStyle stringStyle;

    public ExcelBook(XLSExporter sheetData) throws Exception {
        workbook = new HSSFWorkbook();
        dateStyle = workbook.createCellStyle();
        intStyle = workbook.createCellStyle();
        doubleStyle = workbook.createCellStyle();
        stringStyle = workbook.createCellStyle();
        initStyles();
        this.createSheet(sheetData);
    }

    public ExcelBook(HSSFWorkbook book, XLSExporter sheetData) throws Exception {
        this.workbook = book;
        dateStyle = workbook.createCellStyle();
        intStyle = workbook.createCellStyle();
        doubleStyle = workbook.createCellStyle();
        stringStyle = workbook.createCellStyle();
        initStyles();
        this.createSheet(sheetData);
    }

    private void initStyles() {
        dateStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dateStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dateStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dateStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        intStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        intStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        intStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        intStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        intStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
        doubleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        doubleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        doubleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        doubleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        doubleStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        stringStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        stringStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        stringStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        stringStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        stringStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
    }

    private void createSheet(XLSExporter sheetData) throws Exception {
        HSSFSheet sheet = createSheet(sheetData.getSheetName());
//      DESIGN HEADERS
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell;
        int cellCounter = 0;

        HSSFCellStyle titleStyle = workbook.createCellStyle();
        HSSFFont titleFont = workbook.createFont();
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
        titleStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THICK);

        for (Iterator<String> it = sheetData.getTitles().iterator(); it.hasNext();) {
            cell = row.createCell(cellCounter);
            cell.setCellStyle(titleStyle);
            String title = it.next();
            if (title == null) {
                cell.setCellValue(new HSSFRichTextString(""));
            } else {
                cell.setCellValue(new HSSFRichTextString(title));
            }
            cellCounter++;
        }
        sheet.createFreezePane(0, 1);
//        fila.setHeight((short) (fila.getHeight() * 3));
//      DESIGN BODY
        int rowCounter = 1;
        for (Iterator<Object[]> it = sheetData.getCellData().iterator(); it.hasNext();) {
            Object[] rowData = it.next();
            row = sheet.createRow(rowCounter);
            rowCounter++;
            for (int cellCounter2 = 0; cellCounter2 < rowData.length; cellCounter2++) {

                Object value = rowData[cellCounter2];
                if (value != null) {
                    cell = row.createCell(cellCounter2);
                    this.fillCell(value, cell);
                } else {
                    continue;
                }
            }
        }

        for (int i = 0; i < cellCounter; i++) {
            sheet.autoSizeColumn(i);
        }

    }

    private void fillCell(Object value, HSSFCell cell) {
        if (value != null) {
            String clase = value.getClass().getName();
            if (clase.equals("java.util.Calendar") || clase.equals("java.util.GregorianCalendar")) {
                cell.setCellStyle(dateStyle);
                cell.setCellValue((Calendar) value);
            } else if (clase.equals("java.lang.Integer")) {
                cell.setCellStyle(intStyle);
                cell.setCellValue((Integer) value);
            } else if (clase.equals("java.lang.Long")) {
                cell.setCellStyle(intStyle);
                cell.setCellValue((Long) value);
            } else if (clase.equals("java.lang.Double")) {
                cell.setCellStyle(doubleStyle);
                cell.setCellValue((Double) value);
            } else if (clase.equals("java.lang.String")) {
                cell.setCellStyle(stringStyle);
                cell.setCellValue(new HSSFRichTextString(value.toString()));
            }
        } else {
            cell.setCellStyle(intStyle);
            cell.setCellValue(0);
        }
    }

    private HSSFSheet createSheet(String name) throws Exception {
        if (this.workbook != null) {
            return workbook.createSheet(name);
        } else {
            throw new Exception("Error, no se puede utilizar la referencia al libro");
        }
    }

    /**
     * @return the workbook
     */
    public HSSFWorkbook getWorkbook() {
        return workbook;
    }

    /**
     * @param workbook the workbook to set
     */
    public void setWorkbook(HSSFWorkbook workbook) {
        this.workbook = workbook;
    }
    
    public void write(OutputStream stream) throws IOException{
        this.workbook.write(stream);
    }
}
