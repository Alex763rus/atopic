package com.example.atopic.service.excel;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellStyle.*;

@Slf4j
@Service
public class ExcelService {

    public InputFile createExcelDocument(String sheetName, List<List<String>> excelData, int countAutoSizeColumn) {
        File tmpFile;
        try {
            tmpFile = Files.createTempFile("exel", ".xls").toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        val book = new HSSFWorkbook();
        val sheet = book.createSheet(sheetName);
        CellStyle styleFirstLine = book.createCellStyle();
        styleFirstLine.setWrapText(true);
        styleFirstLine.setVerticalAlignment(VERTICAL_CENTER);
        styleFirstLine.setAlignment(ALIGN_CENTER);

        for (int y = 0; y < excelData.size(); y++) {
            Row row = sheet.createRow(y);
            for (int x = 0; x < excelData.get(y).size(); x++) {
                val cell = row.createCell(x);
                cell.setCellValue(excelData.get(y).get(x));
                if (y == 0) {
                    cell.setCellStyle(styleFirstLine);
                }
            }
        }
        for (int x = 0; x < excelData.get(0).size(); x++) {
            if (x < countAutoSizeColumn) {
                sheet.autoSizeColumn(x);
            } else {
                sheet.setColumnWidth(x, 6000);
            }
        }
        try {
            book.write(new FileOutputStream(tmpFile));
            book.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new InputFile(tmpFile);
    }
}
