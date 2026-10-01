package com.finora.finora.Service;

import com.finora.finora.Model.Pemasukan;
import com.finora.finora.Model.Pengeluaran;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class LaporanExcelService {

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Export laporan keuangan Finora ke Excel.
     */
    public void export(
            OutputStream outputStream,
            Map<String, Object> reportData
    ) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {

            // Membuat style yang digunakan bersama
            ExcelStyles styles = createStyles(workbook);

            // Membuat semua sheet
            createRingkasanSheet(workbook, reportData, styles);
            createPemasukanSheet(workbook, reportData, styles);
            createPengeluaranSheet(workbook, reportData, styles);

            workbook.write(outputStream);
        }
    }

    // =========================================================
    // RINGKASAN
    // =========================================================

    private void createRingkasanSheet(
            Workbook workbook,
            Map<String, Object> reportData,
            ExcelStyles styles
    ) {

        Sheet sheet = workbook.createSheet("Ringkasan");

        // -------------------------
        // Judul
        // -------------------------

        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(34);

        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("FINORA");
        titleCell.setCellStyle(styles.title);

        sheet.addMergedRegion(
                new CellRangeAddress(0, 0, 0, 1)
        );

        // -------------------------
        // Subtitle
        // -------------------------

        Row subtitleRow = sheet.createRow(1);
        subtitleRow.setHeightInPoints(24);

        Cell subtitleCell = subtitleRow.createCell(0);
        subtitleCell.setCellValue("Laporan Keuangan");
        subtitleCell.setCellStyle(styles.subtitle);

        sheet.addMergedRegion(
                new CellRangeAddress(1, 1, 0, 1)
        );

        // -------------------------
        // Periode
        // -------------------------

        Row periodRow = sheet.createRow(2);

        Cell periodLabel = periodRow.createCell(0);
        periodLabel.setCellValue("Periode");
        periodLabel.setCellStyle(styles.label);

        Cell periodValue = periodRow.createCell(1);

        int selectedYear = getIntValue(
                reportData,
                "selectedYear",
                0
        );

        int selectedMonth = getIntValue(
                reportData,
                "selectedMonth",
                0
        );

        if (selectedYear > 0 && selectedMonth > 0) {

            periodValue.setCellValue(
                    getMonthName(selectedMonth)
                            + " "
                            + selectedYear
            );

        } else {

            periodValue.setCellValue("Tidak tersedia");
        }

        periodValue.setCellStyle(styles.period);

        // -------------------------
        // Ringkasan Keuangan
        // -------------------------

        createSummaryRow(
                sheet,
                4,
                "Total Pemasukan",
                getBigDecimal(
                        reportData,
                        "totalPemasukan"
                ),
                styles
        );

        createSummaryRow(
                sheet,
                5,
                "Total Pengeluaran",
                getBigDecimal(
                        reportData,
                        "totalPengeluaran"
                ),
                styles
        );

        createSummaryRow(
                sheet,
                6,
                "Saldo",
                getBigDecimal(
                        reportData,
                        "saldo"
                ),
                styles
        );

        // -------------------------
        // Rasio Pengeluaran
        // -------------------------

        Row ratioRow = sheet.createRow(7);

        Cell ratioLabel = ratioRow.createCell(0);
        ratioLabel.setCellValue("Rasio Pengeluaran");
        ratioLabel.setCellStyle(styles.label);

        Cell ratioValue = ratioRow.createCell(1);

        BigDecimal ratio = getBigDecimal(
                reportData,
                "rasio"
        );

        /*
         * LaporanService menyimpan rasio dalam bentuk
         * angka persen, contoh:
         *
         * 22.6
         *
         * Excel membutuhkan:
         *
         * 0.226
         *
         * agar format 0.0% menghasilkan 22.6%.
         */
        ratioValue.setCellValue(
                ratio.doubleValue() / 100
        );

        ratioValue.setCellStyle(styles.percentage);

        // -------------------------
        // Lebar kolom
        // -------------------------

        sheet.setColumnWidth(
                0,
                28 * 256
        );

        sheet.setColumnWidth(
                1,
                28 * 256
        );

        // Tinggi default
        sheet.setDefaultRowHeightInPoints(22);

        // Tidak perlu gridline
        sheet.setDisplayGridlines(false);
    }

    private void createSummaryRow(
            Sheet sheet,
            int rowIndex,
            String label,
            BigDecimal value,
            ExcelStyles styles
    ) {

        Row row = sheet.createRow(rowIndex);
        row.setHeightInPoints(24);

        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(styles.label);

        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value.doubleValue());
        valueCell.setCellStyle(styles.money);
    }

    // =========================================================
    // PEMASUKAN
    // =========================================================

    private void createPemasukanSheet(
            Workbook workbook,
            Map<String, Object> reportData,
            ExcelStyles styles
    ) {

        Sheet sheet = workbook.createSheet("Pemasukan");

        // -------------------------
        // Header
        // -------------------------

        Row header = sheet.createRow(0);
        header.setHeightInPoints(28);

        header.createCell(0).setCellValue("Tanggal");
        header.createCell(1).setCellValue("Keterangan");
        header.createCell(2).setCellValue("Kategori");
        header.createCell(3).setCellValue("Jumlah");

        formatHeader(
                header,
                styles.tableHeader
        );

        // -------------------------
        // Data
        // -------------------------

        List<Pemasukan> pemasukanList =
                getList(
                        reportData,
                        "pemasukan"
                );

        int rowIndex = 1;

        if (pemasukanList != null) {

            for (Pemasukan pemasukan : pemasukanList) {

                Row row = sheet.createRow(rowIndex++);

                // Tanggal
                Cell tanggalCell = row.createCell(0);

                if (pemasukan.getTanggal() != null) {

                    tanggalCell.setCellValue(
                            pemasukan
                                    .getTanggal()
                                    .format(dateFormatter)
                    );

                } else {

                    tanggalCell.setCellValue("");
                }

                tanggalCell.setCellStyle(
                        styles.date
                );

                // Keterangan
                Cell keteranganCell =
                        row.createCell(1);

                keteranganCell.setCellValue(
                        pemasukan.getKeterangan() != null
                                ? pemasukan.getKeterangan()
                                : ""
                );

                keteranganCell.setCellStyle(
                        styles.text
                );

                // Kategori
                Cell kategoriCell =
                        row.createCell(2);

                kategoriCell.setCellValue(
                        pemasukan.getKategori() != null
                                ? pemasukan
                                    .getKategori()
                                    .getNama()
                                : "Lainnya"
                );

                kategoriCell.setCellStyle(
                        styles.text
                );

                // Jumlah
                Cell jumlahCell =
                        row.createCell(3);

                jumlahCell.setCellValue(
                        pemasukan.getJumlah() != null
                                ? pemasukan
                                    .getJumlah()
                                    .doubleValue()
                                : 0
                );

                jumlahCell.setCellStyle(
                        styles.tableMoney
                );
            }
        }

        configureTransactionSheet(
                sheet,
                rowIndex
        );
    }

    // =========================================================
    // PENGELUARAN
    // =========================================================

    private void createPengeluaranSheet(
            Workbook workbook,
            Map<String, Object> reportData,
            ExcelStyles styles
    ) {

        Sheet sheet =
                workbook.createSheet("Pengeluaran");

        // -------------------------
        // Header
        // -------------------------

        Row header = sheet.createRow(0);
        header.setHeightInPoints(28);

        header.createCell(0).setCellValue("Tanggal");
        header.createCell(1).setCellValue("Keterangan");
        header.createCell(2).setCellValue("Kategori");
        header.createCell(3).setCellValue("Jumlah");

        formatHeader(
                header,
                styles.tableHeader
        );

        // -------------------------
        // Data
        // -------------------------

        List<Pengeluaran> pengeluaranList =
                getList(
                        reportData,
                        "pengeluaran"
                );

        int rowIndex = 1;

        if (pengeluaranList != null) {

            for (Pengeluaran pengeluaran :
                    pengeluaranList) {

                Row row =
                        sheet.createRow(rowIndex++);

                // Tanggal
                Cell tanggalCell =
                        row.createCell(0);

                if (pengeluaran.getTanggal() != null) {

                    tanggalCell.setCellValue(
                            pengeluaran
                                    .getTanggal()
                                    .format(dateFormatter)
                    );

                } else {

                    tanggalCell.setCellValue("");
                }

                tanggalCell.setCellStyle(
                        styles.date
                );

                // Keterangan
                Cell keteranganCell =
                        row.createCell(1);

                keteranganCell.setCellValue(
                        pengeluaran.getKeterangan() != null
                                ? pengeluaran.getKeterangan()
                                : ""
                );

                keteranganCell.setCellStyle(
                        styles.text
                );

                // Kategori
                Cell kategoriCell =
                        row.createCell(2);

                kategoriCell.setCellValue(
                        pengeluaran.getKategori() != null
                                ? pengeluaran
                                    .getKategori()
                                    .getNama()
                                : "Lainnya"
                );

                kategoriCell.setCellStyle(
                        styles.text
                );

                // Jumlah
                Cell jumlahCell =
                        row.createCell(3);

                jumlahCell.setCellValue(
                        pengeluaran.getJumlah() != null
                                ? pengeluaran
                                    .getJumlah()
                                    .doubleValue()
                                : 0
                );

                jumlahCell.setCellStyle(
                        styles.tableMoney
                );
            }
        }

        configureTransactionSheet(
                sheet,
                rowIndex
        );
    }

    // =========================================================
    // KONFIGURASI SHEET TRANSAKSI
    // =========================================================

    private void configureTransactionSheet(
            Sheet sheet,
            int rowCount
    ) {

        // Freeze header
        sheet.createFreezePane(0, 1);

        // Filter
        if (rowCount > 1) {

            sheet.setAutoFilter(
                    new CellRangeAddress(
                            0,
                            rowCount - 1,
                            0,
                            3
                    )
            );
        }

        // Lebar kolom
        sheet.setColumnWidth(
                0,
                16 * 256
        );

        sheet.setColumnWidth(
                1,
                42 * 256
        );

        sheet.setColumnWidth(
                2,
                26 * 256
        );

        sheet.setColumnWidth(
                3,
                24 * 256
        );

        // Tinggi baris
        sheet.setDefaultRowHeightInPoints(22);

        // Hilangkan grid bawaan Excel
        sheet.setDisplayGridlines(false);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private void formatHeader(
            Row header,
            CellStyle style
    ) {

        for (Cell cell : header) {

            cell.setCellStyle(style);
        }
    }

    // =========================================================
    // BORDER
    // =========================================================

    private void applyBorders(
            Row row,
            CellStyle baseStyle
    ) {

        for (Cell cell : row) {

            cell.setCellStyle(baseStyle);
        }
    }

    // =========================================================
    // STYLE FACTORY
    // =========================================================

    private ExcelStyles createStyles(
            Workbook workbook
    ) {

        ExcelStyles styles = new ExcelStyles();

        // -----------------------------------------------------
        // Title
        // -----------------------------------------------------

        styles.title =
                workbook.createCellStyle();

        Font titleFont =
                workbook.createFont();

        titleFont.setBold(true);
        titleFont.setFontHeightInPoints(
                (short) 22
        );

        styles.title.setFont(
                titleFont
        );

        styles.title.setAlignment(
                HorizontalAlignment.LEFT
        );

        styles.title.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        // -----------------------------------------------------
        // Subtitle
        // -----------------------------------------------------

        styles.subtitle =
                workbook.createCellStyle();

        Font subtitleFont =
                workbook.createFont();

        subtitleFont.setBold(true);
        subtitleFont.setFontHeightInPoints(
                (short) 13
        );

        styles.subtitle.setFont(
                subtitleFont
        );

        styles.subtitle.setAlignment(
                HorizontalAlignment.LEFT
        );

        // -----------------------------------------------------
        // Label
        // -----------------------------------------------------

        styles.label =
                workbook.createCellStyle();

        Font labelFont =
                workbook.createFont();

        labelFont.setBold(true);

        styles.label.setFont(
                labelFont
        );

        styles.label.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        // -----------------------------------------------------
        // Period
        // -----------------------------------------------------

        styles.period =
                workbook.createCellStyle();

        Font periodFont =
                workbook.createFont();

        periodFont.setItalic(true);

        styles.period.setFont(
                periodFont
        );

        // -----------------------------------------------------
        Font regularFont =
                workbook.createFont();
        regularFont.setFontName("Calibri");
        regularFont.setFontHeightInPoints((short) 11);

        Font boldValueFont =
                workbook.createFont();
        boldValueFont.setFontName("Calibri");
        boldValueFont.setBold(true);
        boldValueFont.setFontHeightInPoints((short) 11);

        // -----------------------------------------------------
        // Money
        // -----------------------------------------------------

        styles.money =
                workbook.createCellStyle();

        styles.money.setFont(
                boldValueFont
        );

        DataFormat moneyFormat =
                workbook.createDataFormat();

        styles.money.setDataFormat(
                moneyFormat.getFormat(
                        "\"Rp\" #,##0"
                )
        );

        styles.money.setAlignment(
                HorizontalAlignment.RIGHT
        );

        styles.money.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        // -----------------------------------------------------
        // Table Money
        // -----------------------------------------------------

        styles.tableMoney =
                workbook.createCellStyle();

        styles.tableMoney.setFont(
                regularFont
        );

        styles.tableMoney.setDataFormat(
                moneyFormat.getFormat(
                        "\"Rp\" #,##0"
                )
        );

        styles.tableMoney.setAlignment(
                HorizontalAlignment.RIGHT
        );

        styles.tableMoney.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        setThinBorders(
                styles.tableMoney
        );

        // -----------------------------------------------------
        // Percentage
        // -----------------------------------------------------

        styles.percentage =
                workbook.createCellStyle();

        styles.percentage.setFont(
                boldValueFont
        );

        DataFormat percentageFormat =
                workbook.createDataFormat();

        styles.percentage.setDataFormat(
                percentageFormat.getFormat(
                        "0.0%"
                )
        );

        styles.percentage.setAlignment(
                HorizontalAlignment.RIGHT
        );

        styles.percentage.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        // -----------------------------------------------------
        // Table Header
        // -----------------------------------------------------

        styles.tableHeader =
                workbook.createCellStyle();

        Font tableHeaderFont =
                workbook.createFont();

        tableHeaderFont.setBold(true);
        tableHeaderFont.setFontHeightInPoints(
                (short) 11
        );

        styles.tableHeader.setFont(
                tableHeaderFont
        );

        styles.tableHeader.setAlignment(
                HorizontalAlignment.CENTER
        );

        styles.tableHeader.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        styles.tableHeader.setWrapText(true);

        setThinBorders(
                styles.tableHeader
        );

        // -----------------------------------------------------
        // Body
        // -----------------------------------------------------

        styles.body =
                workbook.createCellStyle();

        styles.body.setFont(
                regularFont
        );

        styles.body.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        setThinBorders(
                styles.body
        );

        // -----------------------------------------------------
        // Date
        // -----------------------------------------------------

        styles.date =
                workbook.createCellStyle();

        styles.date.setFont(
                regularFont
        );

        styles.date.setAlignment(
                HorizontalAlignment.CENTER
        );

        styles.date.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        setThinBorders(
                styles.date
        );

        // -----------------------------------------------------
        // Text
        // -----------------------------------------------------

        styles.text =
                workbook.createCellStyle();

        styles.text.setFont(
                regularFont
        );

        styles.text.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        styles.text.setWrapText(true);

        setThinBorders(
                styles.text
        );

        // -----------------------------------------------------
        // Return
        // -----------------------------------------------------

        return styles;
    }

    private void setThinBorders(
            CellStyle style
    ) {

        style.setBorderTop(
                BorderStyle.THIN
        );

        style.setBorderBottom(
                BorderStyle.THIN
        );

        style.setBorderLeft(
                BorderStyle.THIN
        );

        style.setBorderRight(
                BorderStyle.THIN
        );
    }

    // =========================================================
    // HELPER DATA
    // =========================================================

    private BigDecimal getBigDecimal(
            Map<String, Object> data,
            String key
    ) {

        Object value = data.get(key);

        if (value instanceof BigDecimal) {

            return (BigDecimal) value;
        }

        if (value instanceof Number) {

            return BigDecimal.valueOf(
                    ((Number) value).doubleValue()
            );
        }

        return BigDecimal.ZERO;
    }

    private int getIntValue(
            Map<String, Object> data,
            String key,
            int defaultValue
    ) {

        Object value = data.get(key);

        if (value instanceof Number) {

            return ((Number) value).intValue();
        }

        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getList(
            Map<String, Object> data,
            String key
    ) {

        Object value = data.get(key);

        if (value instanceof List<?>) {

            return (List<T>) value;
        }

        return null;
    }

    // =========================================================
    // NAMA BULAN
    // =========================================================

    private String getMonthName(
            int month
    ) {

        String[] months = {
                "Januari",
                "Februari",
                "Maret",
                "April",
                "Mei",
                "Juni",
                "Juli",
                "Agustus",
                "September",
                "Oktober",
                "November",
                "Desember"
        };

        if (month < 1 || month > 12) {

            return "Tidak diketahui";
        }

        return months[month - 1];
    }

    // =========================================================
    // HOLDER STYLE
    // =========================================================

    private static class ExcelStyles {

        CellStyle title;

        CellStyle subtitle;

        CellStyle label;

        CellStyle period;

        CellStyle money;

        CellStyle tableMoney;

        CellStyle percentage;

        CellStyle tableHeader;

        CellStyle body;

        CellStyle date;

        CellStyle text;
    }
}