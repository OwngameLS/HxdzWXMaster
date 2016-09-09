package com.owngame.utils;

import com.owngame.entity.Contact;
import com.owngame.service.MessageHandler;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 专门处理Excel文件的工具类
 * Created by Administrator on 2016-9-7.
 */
public class ExcelUtil {
    /**
     * 处理excel文件 返回Contacts
     *
     * @param fileStream
     */
    public static Object parseContent(InputStream fileStream) {
        ArrayList<Contact> contacts = null;
        DecimalFormat df = new DecimalFormat("#");// 用来转换手机号码数字类型为文本类型
        String errorInfo = "ERR错误信息提示如下:";
        boolean somethingWrong = false;
        try {
            Workbook workbook = WorkbookFactory.create(fileStream);
            // 拿到行数
            Sheet sheet = workbook.getSheetAt(0);
            int rowNumbers = sheet.getLastRowNum();
            contacts = new ArrayList<Contact>();
            for (int i = 1; i < rowNumbers; i++) {
                boolean isSomethingWrong = false;
                String wrongInfo = "在第" + (i + 1) + "行:";
                String tempS = null;
                Row row = sheet.getRow(i);
                Contact contact = new Contact();
                if (row.getCell(0) != null) {
                    tempS = row.getCell(0).toString().trim();
                    if (tempS.equals("")) {
                        isSomethingWrong = true;
                        wrongInfo += "分组名称必须要填写哦；";
                    } else {
                        contact.setGroupname(tempS);
                    }
                } else {
                    isSomethingWrong = true;
                    wrongInfo += "分组名称必须要填写哦；";
                }

                if (row.getCell(1) != null) {
                    tempS = row.getCell(1).toString().trim();
                    if (tempS.equals("")) {
                        isSomethingWrong = true;
                        wrongInfo += "人员姓名必须要填写哦；";
                    } else {
                        contact.setName(tempS);
                    }
                } else {
                    isSomethingWrong = true;
                    wrongInfo += "人员姓名必须要填写哦；";
                }

                if (row.getCell(2) == null) {
                    contact.setTitle("");
                } else {
                    contact.setTitle(row.getCell(2).toString());
                }

                // 判断手机号码的正确性
                if (row.getCell(3) != null) {
                    tempS = df.format(row.getCell(3).getNumericCellValue());
                    if (MessageHandler.isMobile(tempS)) {
                        contact.setPhone(tempS);
                    } else {
                        isSomethingWrong = true;
                        wrongInfo += "手机号有误；";
                    }
                } else {
                    isSomethingWrong = true;
                    wrongInfo += "手机号必须填写；";
                }

                if (row.getCell(4) == null) {
                    contact.setDescription("");
                } else {
                    contact.setDescription(row.getCell(4).toString());
                }
                if (isSomethingWrong) {// 上面的检查发现了问题，返回给前台
                    somethingWrong = true;
                    errorInfo += "<br>" + wrongInfo;
                }
                contacts.add(contact);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        if (somethingWrong) {
            return myEncode(errorInfo);
        }
        return contacts;
    }


    public static String myEncode(String ss){
        String a = "";
        try {
            a = URLEncoder.encode(ss, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return a;
    }
}
