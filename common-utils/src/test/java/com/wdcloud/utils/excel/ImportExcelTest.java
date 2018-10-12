package com.wdcloud.utils.excel;

import com.alibaba.fastjson.JSON;
import com.wdcloud.utils.excel.annotation.ExcelField;
import com.wdcloud.utils.excel.annotation.ExcelSheet;
import lombok.Data;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class ImportExcelTest {
    @Test
    public void testExcelImport() throws IOException {
        String fileName = "D:\\opt\\upfile\\tmp\\db73cc16-652e-46fb-a5da-2f5e0750fae3.xls";

        File file = new File(fileName);

        ImportExcel importExcel = new ImportExcel(file, 0, 0);
        List<VO> dataList = importExcel.getDataList(VO.class);
        System.out.println(JSON.toJSONString(dataList));
    }

    @Data

    public static class VO {
        @ExcelField(name = "教师编号")
        private String no;
        @ExcelField(name = "姓名")
        private String name;
        @ExcelField(name = "邮箱")
        private String email;
    }

    public static void main(String[] args) throws Exception {
        Number d = 13800138001F;
        System.out.println(d);
        System.out.println(new BigDecimal(d.toString()));
        System.out.println(new BigDecimal(new Double(d.toString())));


        System.out.println(new BigDecimal("1.0").toString());
        System.out.println(new BigDecimal(1.0).toString());
        System.out.println(new BigDecimal(new Double(1.0)).toString());
        System.out.println(new BigDecimal(new Double(1.0).toString()).toString());

    }
}
