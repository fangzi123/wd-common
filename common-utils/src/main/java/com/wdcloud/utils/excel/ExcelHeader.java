package com.wdcloud.utils.excel;

/**
 * Created by nicole on 17-3-6.
 */
public class ExcelHeader {
    private String nameCN;
    private String nameEN;
    private int width;
    private int align;

    public static ExcelHeader builder() {
        return new ExcelHeader();
    }

    public String getNameCN() {
        return nameCN;
    }

    public void setNameCN(String nameCN) {
        this.nameCN = nameCN;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }
}
