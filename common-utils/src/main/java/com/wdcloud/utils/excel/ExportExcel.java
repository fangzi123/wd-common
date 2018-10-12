package com.wdcloud.utils.excel;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.wdcloud.utils.excel.annotation.ExcelField;
import com.wdcloud.utils.excel.annotation.ExcelSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

/**
 * 导出Excel文件（导出“XLSX”格式，支持大数据量导出   @see org.apache.poi.ss.SpreadsheetVersion）
 *
 * @author 赵秀非 E-mail:zhaoxiufei@gmail.com
 * @version 创建时间：2017/12/14 15:56
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "SameParameterValue", "unused"})
public class ExportExcel {
    private static final Logger log = LoggerFactory.getLogger(ExportExcel.class);

    private String title;//将title作为文件名
    private boolean titleShow;//将title作为文件名

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

    /**
     * 注解列表（Object[]{ ExcelField, Field}）
     */
    private List<Object[]> annotationList = Lists.newArrayList();

    private Map<Field, CellStyle> fieldCellStyleMap = new HashMap<>();

    private void build(Class<?> cls, int type) {
        build(cls, type, null);
    }

    /**
     * 构造函数
     *
     * @param cls  实体对象，通过annotation.ExportField获取标题
     * @param type 导出类型（1:导出数据；2：导出模板）
     */
    private void build(Class<?> cls, int type, MessageSource messageSource) {
        ExcelSheet annotation = cls.getAnnotation(ExcelSheet.class);
        if (annotation == null) {
            throw new RuntimeException("不是ExcelSheet对象");
        }
        // Get annotation field
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs) {
            ExcelField ef = f.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == 0 || ef.type() == type)) {
                annotationList.add(new Object[]{ef, f});
            }
        }
        // Field sorting
        annotationList.sort(Comparator.comparingInt(o -> ((ExcelField) o[0]).sort()));
        List<String> headerList = Lists.newArrayList();
        List<Integer> widthList = Lists.newArrayList();
        if (messageSource != null) {
            Locale locale = LocaleContextHolder.getLocale();
            this.title = messageSource.getMessage(annotation.title(), null, locale);
            annotationList.forEach(o -> {
                ExcelField field = (ExcelField) o[0];
                headerList.add(messageSource.getMessage(field.name(), null, locale));
                widthList.add(field.width());
            });
        } else {
            this.title = annotation.title();
            this.titleShow = annotation.show();
            annotationList.forEach(o -> {
                ExcelField field = (ExcelField) o[0];
                headerList.add(field.name());
                widthList.add(field.width());
            });
        }

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
        //构建表格标题
        if (titleShow) {
            Row titleRow = this.addRow();
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("name"));
            titleCell.setCellValue(this.title);
            if (headerList.size() > 1) {
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerList.size() - 1));
            }
        }
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
     * @param field  当前field
     * @return 单元格对象
     */
    private Cell addCell(Row row, int column, Object val, int align, Field field) {
        Cell cell = row.createCell(column);
        CellStyle style = fieldCellStyleMap.get(field);//缓存拿
        if (style == null) {
            style = createStyle(align);
            fieldCellStyleMap.put(field, style);
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
    public ExportExcel setData(List<?> data) {
        setData(data, null);
        return this;
    }

    public ExportExcel setData(List<?> data, MessageSource messageSource) {
        build(data.get(0).getClass(), 1, messageSource);
        for (Object o : data) {
            Row row = this.addRow();
            for (int j = 0; j < annotationList.size(); ) {
                Object[] os = annotationList.get(j);
                ExcelField ef = (ExcelField) os[0];
                Object val = Reflections.invokeGetter(o, ((Field) os[1]).getName());
                // Get entity value
                this.addCell(row, j++, val, ef.align(), (Field) os[1]);
            }
        }
        return this;
    }

    /**
     * 输出数据流
     *
     * @param os 输出数据流
     */
    public ExportExcel write(OutputStream os) throws IOException {
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
    public ExportExcel write(HttpServletResponse response) throws IOException {
        write(response, title + "-" + System.currentTimeMillis() + ".xlsx");
        return this;
    }

    /**
     * 输出到客户端
     *
     * @param fileName 输出文件名
     */
    public ExportExcel write(HttpServletResponse response, String fileName) throws IOException {
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
    public ExportExcel write(String fileName) throws IOException {
        FileOutputStream os = new FileOutputStream(fileName);
        this.write(os);
        return this;
    }

    /**
     * 清理临时文件
     */
    public ExportExcel dispose() {
        wb.dispose();
        return this;
    }
}
