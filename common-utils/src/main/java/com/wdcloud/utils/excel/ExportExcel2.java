package com.wdcloud.utils.excel;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.wdcloud.utils.StringUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * 导出Excel文件（导出“XLSX”格式，支持大数据量导出   @see org.apache.poi.ss.SpreadsheetVersion）
 *
 * @author 赵秀非 E-mail:zhaoxiufei@gmail.com
 * @version 创建时间：2017/12/14 15:56
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "SameParameterValue", "unused", "Duplicates"})
public class ExportExcel2 {
    private static final Logger log = LoggerFactory.getLogger(ExportExcel2.class);
    /**
     * 工作薄对象
     */
    private SXSSFWorkbook wb;

    /**
     * 工作表对象
     */
    private SXSSFSheet sheet;

    /**
     * 当前行号
     */
    private int rowNum;

    private Map<String, CellStyle> fieldCellStyleMap = new HashMap<>();
    private List<String> headerList = Lists.newArrayList();
    private List<Integer> widthList = Lists.newArrayList();
    private List<Integer> alignList = Lists.newArrayList();

    public ExportExcel2(List<ExcelHeader> headers) {
        build(headers);
    }


    /**
     * 构造函数
     */
    private void build(List<ExcelHeader> headers) {
        headers.forEach(o -> {
            if (StringUtil.isNotEmpty(o.getNameCN()) && StringUtil.isNotEmpty(o.getNameEN())) {
                headerList.add(o.getNameCN() + "/" + o.getNameEN());
            } else if (StringUtil.isNotEmpty(o.getNameCN())) {
                headerList.add(o.getNameCN());
            } else if (StringUtil.isNotEmpty(o.getNameEN())) {
                headerList.add(o.getNameEN());
            } else {
                throw new RuntimeException("表头不能为空");
            }
            widthList.add(o.getWidth() == 0 ? 9 : o.getWidth());
            alignList.add(o.getAlign());
        });
        // Initialize
        initialize(headerList, widthList);
    }

    /**
     * 初始化函数
     *
     * @param headerList 表头列表
     * @param widthList  表头宽度
     */
    private void initialize(List<String> headerList, List<Integer> widthList) {
        this.wb = new SXSSFWorkbook(500);//内存行数
        this.sheet = wb.createSheet("sheet");//暂时不分sheet
        //样式列表
        Map<String, CellStyle> styles = createStyles(wb);
        //构建表头
        Row headerRow = this.addRow();
        headerRow.setHeightInPoints(20);
        for (int i = 0; i < headerList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(headerList.get(i));
            //此处是固定公式 也别问我为什么 出处 http://blog.csdn.net/duqian42707/article/details/51491312
            sheet.setColumnWidth(i, 256 * widthList.get(i) + 184);
            //自动列宽
            //sheet.trackAllColumnsForAutoSizing();
            //sheet.autoSizeColumn(i);
        }
    }

    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();

        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);//垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//水平居中
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(Boolean.TRUE);
        style.setFont(titleFont);
        styles.put("name", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("name"));
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(Boolean.TRUE);
        style.setFont(headerFont);
        styles.put("header", style);

        return styles;
    }

    /**
     * 添加一行
     *
     * @return 行对象
     */
    private Row addRow() {
        return sheet.createRow(rowNum++);
    }

    /**
     * 添加一个单元格
     *
     * @param row    添加的行
     * @param column 添加列号
     * @param val    添加值
     * @param align  对齐方式（1：靠左；2：居中；3：靠右）
     * @return 单元格对象
     */
    private Cell addCell(Row row, int column, Object val, String header, int align) {
        Cell cell = row.createCell(column);
        CellStyle style = fieldCellStyleMap.get(header);//缓存拿
        if (style == null) {
            style = createStyle(align);
            fieldCellStyleMap.put(header, style);
        }
        if (val == null) {
            cell.setCellValue("");
        } else if (val instanceof String) {

            cell.setCellValue((String) val);
        } else if (val instanceof Integer) {
            cell.setCellValue((Integer) val);
        } else if (val instanceof Long) {
            cell.setCellValue((Long) val);
        } else if (val instanceof Double) {
            DataFormat format = wb.createDataFormat();
            style.setDataFormat(format.getFormat("#,##0.00"));
            cell.setCellValue((Double) val);
        } else if (val instanceof Float) {
            DataFormat format = wb.createDataFormat();
            style.setDataFormat(format.getFormat("#,##0.00"));
            cell.setCellValue((Float) val);
        } else if (val instanceof Date) {
            DataFormat format = wb.createDataFormat();
            style.setDataFormat(format.getFormat("yyyy-mm-dd h:mm:ss"));
            cell.setCellValue((Date) val);
        }
        cell.setCellStyle(style);
        return cell;
    }

    private CellStyle createStyle(int align) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);//垂直居中
        //默认
        style.setVerticalAlignment(VerticalAlignment.CENTER);//水平居中
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        return style;
    }

    /**
     * 添加数据（通过annotation.ExportField添加数据）
     *
     * @return data 数据列表
     */
    public ExportExcel2 setData(List<List<?>> data) {
        for (List<?> o : data) {
            Row row = this.addRow();
            for (int j = 0; j < headerList.size(); j++) {
                // Get entity value
                this.addCell(row, j, o.get(j), headerList.get(j), alignList.get(j));
            }
        }
        return this;
    }

    /**
     * 输出数据流
     *
     * @param os 输出数据流
     */
    public ExportExcel2 write(OutputStream os) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            wb.write(baos);
            bos = new BufferedOutputStream(os);
            bis = new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()));
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("Exception={}", Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } finally {
                if (bos != null) {
                    bos.close();
                }
            }
        }
        return this;
    }

    /**
     * 输出到客户端
     */
    public ExportExcel2 write(HttpServletResponse response) throws IOException {
        write(response, UUID.randomUUID().toString() + "-" + System.currentTimeMillis() + ".xlsx");
        return this;
    }

    /**
     * 输出到客户端
     *
     * @param fileName 输出文件名
     */
    public ExportExcel2 write(HttpServletResponse response, String fileName) throws IOException {
        response.reset();
//        response.setContentType("application/vnd.ms-excel; charset=utf-8");
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        write(response.getOutputStream());
        return this;
    }

    /**
     * 输出到文件
     *
     * @param fileName 输出文件名
     */
    public ExportExcel2 write(String fileName) throws IOException {
        FileOutputStream os = new FileOutputStream(fileName);
        this.write(os);
        return this;
    }

    /**
     * 清理临时文件
     */
    public void dispose() {
        wb.dispose();
    }
}
