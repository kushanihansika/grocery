package com.example.grocery.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateExcel {

    public static void main(String[] args) {
        String inputFile = "D:\\GroceryAssistance\\grocery\\src\\main\\resources\\your_data.txt";  // Replace with the path to your Notepad file
        String outputFile = "D:\\GroceryAssistance\\grocery\\src\\main\\resources\\grocery_data.xlsx";

        try {
            List<String[]> data = readDataFromFile(inputFile);
            createExcelFile(data, outputFile);
            System.out.println("Excel file created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String[]> readDataFromFile(String inputFile) throws IOException {
        List<String[]> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            data.add(values);
        }

        reader.close();
        return data;
    }

    private static void createExcelFile(List<String[]> data, String outputFile) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Grocery Data");

        int rowNum = 0;
        for (String[] rowValues : data) {
            Row row = sheet.createRow(rowNum++);
            int cellNum = 0;
            for (String value : rowValues) {
                Cell cell = row.createCell(cellNum++);
                cell.setCellValue(value);
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            workbook.write(outputStream);
        }

        workbook.close();
    }
}
