package com.owngame.utils;

import com.owngame.entity.ContactDisplay;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;

import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * 专门处理Excel文件的工具类
 * Created by Administrator on 2016-9-7.
 */
public class ExcelUtil {
    /**
     * 解析Excel文件内容 返回Contacts
     *
     * @param fileStream
     */
    public static Object parseContent(InputStream fileStream) {
        ArrayList<ContactDisplay> contactDisplays = null;
//        DecimalFormat df = new DecimalFormat("#");// 用来转换手机号码数字类型为文本类型
        DecimalFormat df = new DecimalFormat("0");// 将数字转换成文本
        String errorInfo = "ERR错误信息提示如下:";
        boolean somethingWrong = false;
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(fileStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            // 拿到行数
            int rowNumbers = sheet.getLastRowNum() + 1;// 计数从0开始的
            contactDisplays = new ArrayList<ContactDisplay>();
            for (int i = 1; i < rowNumbers; i++) {
                boolean isSomethingWrong = false;
                String wrongInfo = "在第" + (i + 1) + "行:";
                String tempS = null;
                // 处理一行
                HSSFRow row = sheet.getRow(i);
                ContactDisplay contactDisplay = new ContactDisplay();
                if (null == row) {
                    somethingWrong = true;
                    errorInfo += "<br>" + wrongInfo + "这一行为空；";
                }
                System.out.println("aaa:" + row.getCell(0).toString());
                if (null != row.getCell(0)) {// id
                    int cellType = row.getCell(0).getCellType();
                    if (cellType == Cell.CELL_TYPE_NUMERIC) {
                        //CELL_TYPE_NUMERIC 数值型 0
                        tempS = df.format(row.getCell(0).getNumericCellValue());
                    } else if (cellType == Cell.CELL_TYPE_STRING) { // CELL_TYPE_STRING 字符串型 1
                        tempS = row.getCell(0).toString().trim();
                    } else { // 不填写或者填写错误 都给它弄成0
                        tempS = "0";
                    }

                    contactDisplay.setBase_id(Long.parseLong(tempS));
                }

                if (null != row.getCell(1)) {// 组名
                    tempS = row.getCell(1).toString().trim();
                    if (tempS.equals("")) {
                        isSomethingWrong = true;
                        wrongInfo += "分组名称必须要填写哦；";
                    } else {
                        contactDisplay.setGroupname(tempS);
                    }
                } else {
                    isSomethingWrong = true;
                    wrongInfo += "分组名称必须要填写哦；";
                }

                if (null != row.getCell(2)) {// 姓名
                    tempS = row.getCell(2).toString().trim();
                    if (tempS.equals("")) {
                        isSomethingWrong = true;
                        wrongInfo += "人员姓名必须要填写哦；";
                    } else {
                        contactDisplay.setName(tempS);
                    }
                } else {
                    isSomethingWrong = true;
                    wrongInfo += "人员姓名必须要填写哦；";
                }

                if (null == row.getCell(3)) {// 职务
                    contactDisplay.setTitle("");
                } else {
                    contactDisplay.setTitle(row.getCell(3).toString().trim());
                }

                // 判断手机号码的正确性
                if (null != row.getCell(4)) {// 手机号
                    int cellType = row.getCell(4).getCellType();
                    if (cellType == Cell.CELL_TYPE_NUMERIC) {
                        //CELL_TYPE_NUMERIC 数值型 0
                        tempS = df.format(row.getCell(4).getNumericCellValue());
                    } else if (cellType == Cell.CELL_TYPE_STRING) { // CELL_TYPE_STRING 字符串型 1
                        tempS = row.getCell(4).toString().trim();
                    }

                    if (PhoneUtil.isMobile(tempS)) {
                        contactDisplay.setPhone(tempS);
                    } else {
                        isSomethingWrong = true;
                        wrongInfo += "手机号有误；";
                    }
                } else {
                    isSomethingWrong = true;
                    wrongInfo += "手机号必须填写；";
                }

                if (null == row.getCell(5)) {// 等级
                    contactDisplay.setGrade("");
                } else {
                    int gr = Integer.parseInt(row.getCell(5).toString());
                    contactDisplay.setGrade(gr + "");
                }

                if (null == row.getCell(6)) {// 备注
                    contactDisplay.setDescription("");
                } else {
                    contactDisplay.setDescription(row.getCell(6).toString().trim());
                }
                if (isSomethingWrong) {// 上面的检查发现了问题，返回给前台
                    somethingWrong = true;
                    errorInfo += "<br>" + wrongInfo;
                }
                contactDisplays.add(contactDisplay);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return myEncode(errorInfo + "   <br> 文件解析失败。");
        }
        if (somethingWrong) {
            return myEncode(errorInfo);
        }
        return contactDisplays;
    }

    /**
     * 将联系人信息转换成Excel文件
     *
     * @param contactDisplays
     * @return
     */
    public static boolean initContactsFile(String filePath, ArrayList<ContactDisplay> contactDisplays) {
        // 不管原来的文件是否存在，均新建文件
        File f = new File(filePath);
        try {
            FileUtils.write(f, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet("SheetOne");
            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth((short) 15);
            // 声明一个画图的顶级管理器
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            // 定义注释的大小和位置,详见文档
            HSSFComment comment;

            //产生表格标题行
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(0);
            // 定义单元格为字符串类型
            cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
            cell.setCellValue(new HSSFRichTextString("序号"));
            comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
            // 设置注释内容
            comment.setString(new HSSFRichTextString("序号这一栏填数字，从1开始，顺序递增。"));


            cell = row.createCell(1);
            // 定义单元格为字符串类型
            cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
            cell.setCellValue(new HSSFRichTextString("组名（必填）"));


            cell = row.createCell(2);
            // 定义单元格为字符串类型
            cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
            cell.setCellValue(new HSSFRichTextString("人名（必填）"));
            cell = row.createCell(3);
            // 定义单元格为字符串类型
            cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
            cell.setCellValue(new HSSFRichTextString("职务"));
            cell = row.createCell(4);
            // 定义单元格为字符串类型
            cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
            cell.setCellValue(new HSSFRichTextString("电话号码（必填）"));
            cell = row.createCell(5);
            // 定义单元格为字符串类型
            cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
            cell.setCellValue(new HSSFRichTextString("等级（0-7数字）"));
            cell = row.createCell(6);
            // 定义单元格为字符串类型
            cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
            cell.setCellValue(new HSSFRichTextString("备注"));
            // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
            comment.setAuthor("Owngame");


            //遍历集合数据，产生数据行
            // 处理联系人信息
            int rowIndex = 1;// 行号
            int colIndex = 0;// 列号
//            int colLength = 7;// 列长

            while ((rowIndex - 1) < contactDisplays.size()) {
                row = sheet.createRow(rowIndex);// 设置一行
                ContactDisplay contactDisplay = contactDisplays.get(rowIndex - 1);
                cell = row.createCell(colIndex);
                // 定义单元格为字符串类型
                cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
                cell.setCellValue(new HSSFRichTextString(contactDisplay.getBase_id() + ""));
                cell = row.createCell(++colIndex);
                // 定义单元格为字符串类型
                cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
                cell.setCellValue(new HSSFRichTextString(contactDisplay.getGroupname() + ""));
                cell = row.createCell(++colIndex);
                // 定义单元格为字符串类型
                cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
                cell.setCellValue(new HSSFRichTextString(contactDisplay.getName() + ""));
                cell = row.createCell(++colIndex);
                // 定义单元格为字符串类型
                cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
                cell.setCellValue(new HSSFRichTextString(contactDisplay.getTitle() + ""));
                cell = row.createCell(++colIndex);
                // 定义单元格为字符串类型
                cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
                cell.setCellValue(new HSSFRichTextString(contactDisplay.getPhone() + ""));
                cell = row.createCell(++colIndex);
                // 定义单元格为字符串类型
                cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
                cell.setCellValue(new HSSFRichTextString(contactDisplay.getGrade() + ""));
                cell = row.createCell(++colIndex);
                // 定义单元格为字符串类型
                cell.setCellType(HSSFCell.ENCODING_UTF_16);// 中文处理
                cell.setCellValue(new HSSFRichTextString(contactDisplay.getDescription() + ""));

                // 回归设置
                colIndex = 0;
                ++rowIndex;
            }

            FileOutputStream fileOutputStream = new FileOutputStream(f);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String myEncode(String ss) {
        String a = "";
        try {
            a = URLEncoder.encode(ss, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return a;
    }
}
