package com.toko.maju.view;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ExcelView extends AbstractXlsView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
// change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"my-xls-file.xls\"");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("User Detail");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
//        style.setFillForegroundColor(HSSFColor.getIndexHash().);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
//        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("No Fraktur");
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("Pelanggan");
        header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("Project");
        header.getCell(2).setCellStyle(style);
        header.createCell(3).setCellValue("Total");
        header.getCell(3).setCellStyle(style);
        header.createCell(4).setCellValue("Sisa");
        header.getCell(4).setCellStyle(style);
        header.createCell(5).setCellValue("Dibayar");
        header.getCell(5).setCellStyle(style);
        header.createCell(6).setCellValue("Tanggal");
        header.getCell(6).setCellStyle(style);


        Row userRow =  sheet.createRow(1);
        userRow.createCell(0).setCellValue("INV000001");
        userRow.createCell(1).setCellValue("Fransisko Sanaky");
        userRow.createCell(2).setCellValue("-");
        userRow.createCell(3).setCellValue(2000);
        userRow.createCell(4).setCellValue(2000);
        userRow.createCell(5).setCellValue(2000);
        userRow.createCell(6).setCellValue("12-12-12");
    }
}
