package com.staroon.filezilla.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Staroon
 * Date: 2019-02-13
 * Time: 13:44:32
 */
public class ExcelUtil {

    public static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf(".")).toLowerCase();
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static String getCellFormatValue(Cell cell) throws Exception {
        String cellValue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case XSSFCell.CELL_TYPE_NUMERIC:
                    cellValue = new DecimalFormat("#.#########").format(cell.getNumericCellValue());
//                    cellValue = NumberFormat.getInstance().format(cell.getNumericCellValue());
//                    if (cellValue.contains(",")) {
//                        cellValue = cellValue.replace(",", "");
//                    }
                    break;
                case XSSFCell.CELL_TYPE_FORMULA:
                    try {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    } catch (IllegalStateException e) {
                        cellValue = String.valueOf(cell.getRichStringCellValue());
                    }
//                    throw new Exception("单元格中使用了公式，请不要在表格中使用公式！");
                    break;
                default:
                    cellValue = cell.getStringCellValue();
            }
        }
        return replaceBlank(cellValue);
    }

    private static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
