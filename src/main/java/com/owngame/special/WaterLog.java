package com.owngame.special;

import com.owngame.utils.ReadFile;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 专门针对河溪电站水位文件读取的类 ##已废弃
 * Created by Administrator on 2016-11-7.
 */
public class WaterLog {
    double storage[] = {699.98, 710.91, 721.84, 732.77, 743.7, 754.64, 765.58, 776.52, 787.46, 798.4, 809.34, 821.54, 833.74, 845.94, 858.14, 870.34, 882.55, 894.75, 906.96, 919.17, 931.37, 944.84, 958.31, 971.78, 985.25, 998.72, 1012.19, 1025.65, 1039.12, 1052.59, 1066.06, 1080.79, 1095.52, 1110.25, 1124.98, 1139.72, 1154.46, 1169.2, 1183.94, 1198.68, 1213.42, 1229.42, 1245.42, 1261.42, 1277.42, 1293.42, 1309.43, 1325.43, 1341.44, 1357.44, 1373.44, 1390.13, 1406.82, 1423.5, 1440.19, 1456.88, 1473.57, 1490.26, 1506.94, 1523.63, 1540.32, 1558.57, 1576.81, 1595.06, 1613.3, 1631.54, 1649.79, 1668.04, 1686.28, 1704.53, 1722.77, 1742.57, 1762.37, 1782.17, 1801.97, 1821.77, 1841.58, 1861.38, 1881.18, 1900.98, 1920.78, 1942.14, 1963.5, 1984.85, 2006.21, 2027.57, 2048.93, 2070.29, 2091.64, 2113, 2134.36, 2157.28, 2180.19, 2203.11, 2226.02, 2248.94, 2271.85, 2294.77, 2317.68, 2340.59, 2363.51, 2386.98, 2410.44, 2433.91, 2457.38, 2480.85, 2504.31, 2527.78, 2551.25, 2574.71, 2598.18, 2623.71, 2649.23, 2674.76, 2700.28, 2775.81, 2751.34, 2776.86, 2802.39, 2827.97, 2853.44, 2881.03, 2908.61, 2936.19, 2963.78, 2991.36, 3018.95, 3046.54, 3074.12, 3101.71, 3129.29, 3158.93, 3188.58, 3218.22, 3247.86, 3277.51, 3307.15, 3336.79, 3366.43, 3396.08, 3425.72, 3457.42, 3489.12, 3520.83, 3552.53, 3584.23, 3615.93, 3647.63, 3679.34, 3711.04, 3742.74, 3776.5, 3810.26, 3844.02, 3877.78, 3911.54, 3945.3, 3979.06, 4012.82, 4046.58, 4080.34, 4116.1, 4151.98, 4187.8, 4223.62, 4259.44, 4296.25, 4331.07, 4366.89, 4402.71, 4438.53};
    int startWLine = 165;// 起始水位
    /**
     * 读取水位
     *
     * @return
     */
    public String getWaterLog() {
        // 读取文件 每次读取20行 这样容错率就很高了
        ArrayList<String> lines = (ArrayList<String>) ReadFile.readLastNLine(new File("D:/water_log.txt"), 20L);
        if (lines != null) {
            if (lines.size() != 0) {//读取到了内容
                // 判断内容
                boolean isWLineReaded = false;// 是否读取到了水位数据
                boolean isTimeReaded = false;// 是否读取到了时间
                String wLine = null;// 水位
                String time = null;// 时间
                for(int i=0;i<lines.size();i++){// 要读取一个完整的结构
                    String s = lines.get(i);
                    if(isWLineReaded){// 读取到了水位
                        // 读取时间
                        if(s.length()<9){
                            continue;
                        }
                        if(isDate(s.substring(0,9))){
                            time = s;
                            isTimeReaded = true;
                            break;
                        }else{
                            continue;
                        }
                    }else{// 要先读到水位
                        if(isNumeric(s)){
                            isWLineReaded = true;
                            wLine = s;
                        }else{
                            continue;
                        }
                    }
                }
                if(isWLineReaded && isTimeReaded){
                    String s = getStorage(wLine);
                    String w = wLine.substring(0,3) + "." + wLine.substring(3);
                    System.out.println("水位：" + w + "；库容：" + s + "；时间：" + time);
                }else{
                    return "未读取到最近一次的水位数据，原因是记录失败。";
                }
            }
        }
        return null;
    }

    // 获得库容
    private String getStorage(String wLine){
        // 先解析水位，得到数组坐标
        // 整数部分
        String z = wLine.substring(0, 3);
        int t = Integer.parseInt(z);
        int indexZB = (t - startWLine) * 10;// 整数部分获得起始坐标

        // 小数部分
        int XB_1 = Integer.valueOf(wLine.substring(3, 4));// 第一位小数
        int XB_2 = Integer.valueOf(wLine.substring(4));// 第二位小数
        // 根据第一位小数 获得库容 所在区间
        double startStorage = storage[indexZB + XB_1];
        double endStorage = storage[indexZB + XB_1 + 1];
        double dValue = endStorage - startStorage;// 库容区间
        // 根据第二位小数 获得具体的值
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(startStorage + dValue * 0.1 * XB_2);
    }

    public static void main(String args[]){
        new WaterLog().getWaterLog();
    }

    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    private boolean isDate(String str){
        Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{1,2}");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}

