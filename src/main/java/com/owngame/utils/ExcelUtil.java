package com.owngame.utils;

import com.owngame.entity.Contact;
import com.owngame.service.MessageHandler;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
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
     * @param file
     */
    public static Object parseContent(File file) {
        ArrayList<Contact> contacts = null;
        DecimalFormat df = new DecimalFormat("#");// 用来转换手机号码数字类型为文本类型
        String errorInfo = "error:";
        boolean somethingWrong = false;
        try {
            Workbook workbook = WorkbookFactory.create(file);
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
                tempS = row.getCell(0).toString().trim();
                if (tempS.equals("")) {
                    isSomethingWrong = true;
                    wrongInfo += "分组名称必须要填写哦；";
                } else {
                    contact.setGroupname(tempS);
                }
                tempS = row.getCell(1).toString().trim();
                if (tempS.equals("")) {
                    isSomethingWrong = true;
                    wrongInfo += "人员姓名必须要填写哦；";
                } else {
                    contact.setName(tempS);
                }
                contact.setTitle(row.getCell(2).toString());
                // 判断手机号码的正确性
                tempS = df.format(row.getCell(3).getNumericCellValue());
                if (MessageHandler.isMobile(tempS)) {
                    contact.setPhone(tempS);
                }else{
                    isSomethingWrong = true;
                    wrongInfo += "手机号有误；";
                }
                contact.setDescription(row.getCell(4).toString());
                if (isSomethingWrong) {// 上面的检查发现了问题，返回给前台
                    somethingWrong = true;
                    errorInfo += "\n" + wrongInfo;
                }
                contacts.add(contact);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        if(somethingWrong){
            return errorInfo;
        }
        return contacts;
    }


    public static void main(String args[]) {
        File file = new File("D:/contacts.xls");
        Object o = ExcelUtil.parseContent(file);
        String ss = o.toString();
        ArrayList<Contact> contacts = null;
        if (ss.startsWith("error")) {
            System.out.println(ss);
        } else {
            contacts = (ArrayList<Contact>) o;
            for (Contact contact : contacts) {
                System.out.println(contact.toString());
            }
        }

    }

}
