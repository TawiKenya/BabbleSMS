/**
 * Copyright 2015 Tawi Commercial Services Ltd
 * 
 * Licensed under the Open Software License, Version 3.0 (the “License”); you may
 * not use this file except in compliance with the License. You may obtain a copy
 * of the License at:
 * http://opensource.org/licenses/OSL-3.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package ke.co.tawi.babblesms.server.utils.export.topups;

import ke.co.tawi.babblesms.server.beans.log.IncomingLog;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * Utility class dealing with the export of all topup activity for one account.
 * <p>
 * Copyright (c) Tawi Commercial Services Ltd., Oct 31, 2013
 *
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 *
 */
public class AllTopupsExportUtil {

    // The headings to be contained in the first row of the Excel sheet
    final static String[] TOPUP_TITLES = {"Transaction Id", "Phone Number",
        "Amount (KES)", "Network Operator", "Transaction Status", "Time"};

    private static Logger logger = LogManager.getLogger(AllTopupsExportUtil.class);

    /**
     * Creates a Microsoft Excel Workbook containing Topup activity provided in
     * a CSV text file. The format of the created file will be Office Open XML
     * (OOXML).
     * <p>
     * It expects the CSV to have the following columns from left to right:<br
     * />
     * topup.uuid, topup.msisdn, topup.amount, network.name, topupStatus.status,
     * topup.topupTime
     * <p>
     * This method has been created to allow for large Excel files to be created
     * without overwhelming memory.
     *
     *
     * @param topupCSVFile a valid CSV text file. It should contain the full
     * path and name of the file e.g. "/tmp/export/topups.csv"
     * @param delimiter the delimiter used in the CSV file
     * @param excelFile the Microsoft Excel file to be created. It should
     * contain the full path and name of the file e.g. "/tmp/export/topups.xlsx"
     * @return whether the creation of the Excel file was successful or not
     */
    public static boolean createExcelExport(final String topupCSVFile, final String delimiter,
            final String excelFile) {
        boolean success = true;

        int rowCount = 0;	// To keep track of the row that we are on

        Row row;
        Map<String, CellStyle> styles;

        SXSSFWorkbook wb = new SXSSFWorkbook(5000); // keep 5000 rows in memory, exceeding rows will be flushed to disk
        // Each line of the file is approximated to be 200 bytes in size, 
        // therefore 5000 lines are approximately 1 MB in memory
        // wb.setCompressTempFiles(true); // temporary files will be gzipped on disk

        Sheet sheet = wb.createSheet("Airtime Topup");
        styles = createStyles(wb);

        PrintSetup printSetupTopup = sheet.getPrintSetup();
        printSetupTopup.setLandscape(true);
        sheet.setFitToPage(true);

        // Set up the heading to be seen in the Excel sheet
        row = sheet.createRow(rowCount);

        Cell titleCell;

        row.setHeightInPoints(45);
        titleCell = row.createCell(0);
        titleCell.setCellValue("Airtime Topups");
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$L$1"));
        titleCell.setCellStyle(styles.get("title"));

        rowCount++;
        row = sheet.createRow(rowCount);
        row.setHeightInPoints(12.75f);

        for (int i = 0; i < TOPUP_TITLES.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(TOPUP_TITLES[i]);
            cell.setCellStyle(styles.get("header"));
        }

        rowCount++;

        FileUtils.deleteQuietly(new File(excelFile));
        FileOutputStream out;

        try {
            FileUtils.touch(new File(excelFile));

            // Read the CSV file and populate the Excel sheet with it
            LineIterator lineIter = FileUtils.lineIterator(new File(topupCSVFile));
            String line;
            String[] lineTokens;
            int size;

            while (lineIter.hasNext()) {
                row = sheet.createRow(rowCount);
                line = lineIter.next();
                lineTokens = StringUtils.split(line, delimiter);
                size = lineTokens.length;

                for (int cellnum = 0; cellnum < size; cellnum++) {
                    Cell cell = row.createCell(cellnum);
                    cell.setCellValue(lineTokens[cellnum]);
                }

                rowCount++;
            }

            out = new FileOutputStream(excelFile);
            wb.write(out);
            out.close();

        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException while trying to create Excel file '" + excelFile
                    + "' from CSV file '" + topupCSVFile + "'.");
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;

        } catch (IOException e) {
            logger.error("IOException while trying to create Excel file '" + excelFile
                    + "' from CSV file '" + topupCSVFile + "'.");
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        }

        wb.dispose(); // dispose of temporary files backup of this workbook on disk

        return success;
    }

    /**
     * Used to create a MS Excel file from a list of
     *
     * @param topups
     * @param networkHash a map with an UUID as the key and the name of the
     * network as the value
     * @param statusHash a map with an UUID as the key and the name of the
     * transaction status as the value
     * @param delimiter
     * @param excelFile the Microsoft Excel file to be created. It should
     * contain the full path and name of the file e.g. "/tmp/export/topups.xlsx"
     * @return whether the creation of the Excel file was successful or not
     */
    public static boolean createExcelExport(final List<IncomingLog> topups, final HashMap<String, String> networkHash,
            final HashMap<String, String> statusHash, final String delimiter, final String excelFile) {
        boolean success = true;

        int rowCount = 0;	// To keep track of the row that we are on

        Row row;
        Map<String, CellStyle> styles;

        SXSSFWorkbook wb = new SXSSFWorkbook(5000); // keep 5000 rows in memory, exceeding rows will be flushed to disk
        // Each line of the file is approximated to be 200 bytes in size, 
        // therefore 5000 lines are approximately 1 MB in memory
        // wb.setCompressTempFiles(true); // temporary files will be gzipped on disk

        Sheet sheet = wb.createSheet("Airtime Topup");
        styles = createStyles(wb);

        PrintSetup printSetupTopup = sheet.getPrintSetup();
        printSetupTopup.setLandscape(true);
        sheet.setFitToPage(true);

        // Set up the heading to be seen in the Excel sheet
        row = sheet.createRow(rowCount);

        Cell titleCell;

        row.setHeightInPoints(45);
        titleCell = row.createCell(0);
        titleCell.setCellValue("Airtime Topups");
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$L$1"));
        titleCell.setCellStyle(styles.get("title"));

        rowCount++;
        row = sheet.createRow(rowCount);
        row.setHeightInPoints(12.75f);

        for (int i = 0; i < TOPUP_TITLES.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(TOPUP_TITLES[i]);
            cell.setCellStyle(styles.get("header"));
        }

        rowCount++;

        FileUtils.deleteQuietly(new File(excelFile));
        FileOutputStream out;

        try {
            FileUtils.touch(new File(excelFile));

            Cell cell;

            for (IncomingLog topup : topups) {
                row = sheet.createRow(rowCount);

                cell = row.createCell(0);
                cell.setCellValue(topup.getUuid());

                //cell = row.createCell(1);
                //cell.setCellValue(topup.getMessageid());

                cell = row.createCell(2);
                cell.setCellValue(topup.getDestination());

                cell = row.createCell(3);
                cell.setCellValue(networkHash.get(topup.getOrigin()));

                cell = row.createCell(4);
                cell.setCellValue(statusHash.get(topup.getMessage()));

                cell = row.createCell(5);
                cell.setCellValue(topup.getLogTime().toString());

                rowCount++;
            }

            out = new FileOutputStream(excelFile);
            wb.write(out);
            out.close();

        } catch (IOException e) {
            logger.error("IOException while trying to create Excel file '" + excelFile
                    + "' from list of topups.");
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        }

        wb.dispose(); // dispose of temporary files backup of this workbook on disk

        return success;
    }
    public static boolean createExcelExport2(final List<OutgoingLog> topups, final HashMap<String, String> networkHash,
            final HashMap<String, String> statusHash, final String delimiter, final String excelFile) {
        boolean success = true;

        int rowCount = 0;	// To keep track of the row that we are on

        Row row;
        Map<String, CellStyle> styles;

        SXSSFWorkbook wb = new SXSSFWorkbook(5000); // keep 5000 rows in memory, exceeding rows will be flushed to disk
        // Each line of the file is approximated to be 200 bytes in size, 
        // therefore 5000 lines are approximately 1 MB in memory
        // wb.setCompressTempFiles(true); // temporary files will be gzipped on disk

        Sheet sheet = wb.createSheet("Airtime Topup");
        styles = createStyles(wb);

        PrintSetup printSetupTopup = sheet.getPrintSetup();
        printSetupTopup.setLandscape(true);
        sheet.setFitToPage(true);

        // Set up the heading to be seen in the Excel sheet
        row = sheet.createRow(rowCount);

        Cell titleCell;

        row.setHeightInPoints(45);
        titleCell = row.createCell(0);
        titleCell.setCellValue("Airtime Topups");
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$L$1"));
        titleCell.setCellStyle(styles.get("title"));

        rowCount++;
        row = sheet.createRow(rowCount);
        row.setHeightInPoints(12.75f);

        for (int i = 0; i < TOPUP_TITLES.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(TOPUP_TITLES[i]);
            cell.setCellStyle(styles.get("header"));
        }

        rowCount++;

        FileUtils.deleteQuietly(new File(excelFile));
        FileOutputStream out;

        try {
            FileUtils.touch(new File(excelFile));

            Cell cell;

            for (OutgoingLog topup : topups) {
                row = sheet.createRow(rowCount);

                cell = row.createCell(0);
                cell.setCellValue(topup.getUuid());

                //cell = row.createCell(1);
                //cell.setCellValue(topup.getMessageid());

                cell = row.createCell(2);
                cell.setCellValue(topup.getDestination());

                cell = row.createCell(3);
                cell.setCellValue(networkHash.get(topup.getOrigin()));

                cell = row.createCell(4);
                cell.setCellValue(statusHash.get(topup.getMessage()));

                cell = row.createCell(5);
                cell.setCellValue(topup.getLogTime().toString());

                rowCount++;
            }

            out = new FileOutputStream(excelFile);
            wb.write(out);
            out.close();

        } catch (IOException e) {
            logger.error("IOException while trying to create Excel file '" + excelFile
                    + "' from list of topups.");
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        }

        wb.dispose(); // dispose of temporary files backup of this workbook on disk

        return success;
    }

    /**
     * Cell styles used for formatting the sheets
     *
     * @param wb
     * @return Map<String, {@link CellStyle}>
     */
    public static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();

        CellStyle style;
        Font headerFont = wb.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        styles.put("header", style);

        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 48);
        titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        return styles;
    }

    /**
     *
     * @param wb
     * @return CellStyle
     */
    public static CellStyle createBorderedStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }
}

/*
** Local Variables:
**   mode: java
**   c-basic-offset: 2
**   tab-width: 2
**   indent-tabs-mode: nil
** End:
**
** ex: set softtabstop=2 tabstop=2 expandtab:
**
*/
