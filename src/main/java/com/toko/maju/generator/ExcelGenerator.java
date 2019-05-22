package com.toko.maju.generator;

import com.toko.maju.domain.SaleTransactions;
import com.toko.maju.service.dto.SaleItemDTO;
import com.toko.maju.service.dto.SaleTransactionsDTO;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class ExcelGenerator {

//    private static Workbook workbook = new XSSFWorkbook();
//    private static ByteArrayOutputStream out = new ByteArrayOutputStream();

//    public static ByteArrayInputStream toExcel() {
//        String[] COLUMNs = {"Id", "Name", "Address", "Age"};
////        Workbook workbook = new XSSFWorkbook();
////        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
////        try {
////            CreationHelper createHelper = workbook.getCreationHelper();
////
////            Sheet sheet = workbook.createSheet("Customers");
////
////            Font headerFont = workbook.createFont();
////            headerFont.setBold(true);
////            headerFont.setColor(IndexedColors.BLUE.getIndex());
////
////            CellStyle headerCellStyle = workbook.createCellStyle();
////            headerCellStyle.setFont(headerFont);
////
////            // Row for Header
////            Row headerRow = sheet.createRow(0);
////
////            // Header
////            for (int col = 0; col < COLUMNs.length; col++) {
////                Cell cell = headerRow.createCell(col);
////                cell.setCellValue(COLUMNs[col]);
////                cell.setCellStyle(headerCellStyle);
////            }
////
////            // CellStyle for Age
////            CellStyle ageCellStyle = workbook.createCellStyle();
////            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
////
////
////            Row row = sheet.createRow(1);
////
////            row.createCell(0).setCellValue(1);
////            row.createCell(1).setCellValue("Name");
////            row.createCell(2).setCellValue("Address");
////
////            Cell ageCell = row.createCell(3);
////            ageCell.setCellValue(2);
////            ageCell.setCellStyle(ageCellStyle);
////
////
////            workbook.write(out);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        return new ByteArrayInputStream(out.toByteArray());
//    }

    public static ByteArrayInputStream saleReportDetail(List<SaleTransactionsDTO> sales) {
        String[] COLUMNs = {"Tanggal Penjualan", "Barcode", "Nama Barang", "Satuan", "Harga Jual", "Jumlah", "Total"};
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
//            Sheet sheet = createSheet("Sale Report");
            Sheet sheet = workbook.createSheet("Sale Report");

            int rowIdx = 0;
            for (SaleTransactionsDTO s : sales) {
                Row customerInfo = sheet.createRow(rowIdx);
                customerInfo.createCell(0).setCellValue("Pelanggan");
                customerInfo.createCell(1).setCellValue(s.getCustomerFullName());
                customerInfo.setRowStyle(infoStyle(workbook));
                customerInfo.createCell(COLUMNs.length - 2).setCellValue("Project: ");
                customerInfo.createCell(COLUMNs.length - 1).setCellValue(s.getProjectName());

//                createHeaderRow(COLUMNs, sheet, ++rowIdx);
                // Row for Header
                Row headerRow = sheet.createRow(++rowIdx);

                // Header
                for (int col = 0; col < COLUMNs.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    cell.setCellValue(COLUMNs[col]);
                    cell.setCellStyle(headerStyle(workbook));
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
                    .withZone(ZoneId.of("Asia/Jakarta"));


                for (SaleItemDTO item : s.getItems()) {
                    Row row = sheet.createRow(++rowIdx);
                    row.createCell(0).setCellValue(formatter.format(s.getSaleDate()));
                    row.createCell(1).setCellValue(item.getBarcode());
                    row.createCell(2).setCellValue(item.getProductName());
                    row.createCell(3).setCellValue(item.getUnit());
                    row.createCell(4).setCellValue(item.getSellingPrice().toPlainString());
                    row.createCell(5).setCellValue(item.getQuantity());
                    row.createCell(6).setCellValue(item.getTotalPrice().toPlainString());
                }

                Row totalRow = sheet.createRow(++rowIdx);
                totalRow.createCell(COLUMNs.length-2).setCellValue("Total");
                totalRow.createCell(COLUMNs.length-1).setCellValue(s.getTotalPayment().toPlainString());

                rowIdx += 2;
            }

            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new ByteArrayInputStream(out.toByteArray());
    }

//    private static Sheet createSheet(String sheetName) {
//        workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet(sheetName);
//        return sheet;
//    }

    private static CellStyle headerStyle(Workbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return headerCellStyle;
    }

    private static CellStyle infoStyle(Workbook workbook) {
        Font infoFont = workbook.createFont();
        infoFont.setBold(true);
        infoFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle infoCell = workbook.createCellStyle();
        infoCell.setFont(infoFont);

        return infoCell;
    }

//    private static Sheet createHeaderRow(String[] columns, Sheet sheet, int position) {
//        Row headerRow = sheet.createRow(position);
//        for (int col = 0; col < columns.length; col++) {
//            Cell cell = headerRow.createCell(col);
//            cell.setCellValue(columns[col]);
//            cell.setCellStyle(headerStyle());
//        }
//
//        return sheet;
//    }
}
