package com.ctts.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author liangbaichuan
 */
public class StringUtil {

    /**
     * 刪除str中的c代表的字符及其后的数字
     *
     * @param str
     * @param c
     * @return
     */
    public static String deleteCharAndNum(String str, char c) {
        int charIndex = str.indexOf(c);
        if (charIndex == -1) {
            return str;
        }
        StringBuilder resultSb = new StringBuilder();
        resultSb.append(str, 0, charIndex);
        int firstNotNumberCharIndex = -1;
        for (int i = charIndex + 1; i < str.length(); i++) {
            int charAscii = (int) str.charAt(i);
            if (charAscii < 47 || charAscii > 58) { // 非数字
                firstNotNumberCharIndex = i;
                break;
            }
        }
        if (firstNotNumberCharIndex != -1) {
            resultSb.append(str.substring(firstNotNumberCharIndex));
        }
        return resultSb.toString();
    }

    public static void trimStringArray(String[] stringArray) {
        for (int i = 0; i < stringArray.length; i++) {
            stringArray[i] = stringArray[i].trim();
        }
    }

    /**
     * 替换文件名中的特殊字符
     *
     * @param fileName
     * @return
     */
    public static String outPutToDiskFileNameHandle(String fileName) {
        return fileName.replaceAll("/", "!").replaceAll("\"", "!")
                .replaceAll("\\\\", "!").replaceAll("\\|", "!")
                .replaceAll(":", "：").replaceAll("\\?", "？")
                .replaceAll("<", "《").replaceAll(">", "》").replaceAll("\\*", "·").replaceAll("#", "!")
                .replaceAll("%", "!");
    }

    /**
     * 判断一个字符串是否是null和空字符串
     *
     * @param sourceStr
     * @return
     */
    public static boolean isNullOrNullStr(String sourceStr) {
        if (sourceStr == null || "".equals(sourceStr.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 如果字符串末尾是identify或者identify+数字，返回identify+数字，否则返回""
     *
     * @param sourceStr
     * @param identify
     * @return
     */
    public static String getIdentifyIntAfterSourceStrLastIdentify(String sourceStr, String identify) {
        if (isNullOrNullStr(sourceStr) || isNullOrNullStr(identify)) {
            return "";
        }
        sourceStr = sourceStr.toUpperCase();
        identify = identify.toUpperCase();
        int lastIndexOfIdentify = sourceStr.lastIndexOf(identify);
        if (lastIndexOfIdentify != -1) {//sourceStr包含identify
            String s1 = sourceStr.substring(lastIndexOfIdentify);//截取identify之后的所有字符串（包含identify）
            String s2 = sourceStr.substring(lastIndexOfIdentify + 1);//截取identify之后的所有字符串（不包含identify）
            if (StringUtils.isNotBlank(s2)) {
                if (StringUtil.isNumber(s2)) {
                    //代表identify之后存在字符串，且全是数字，那么返回identify+数字
                    return s1;
                } else {
                    //代表identify之后存在字符串，且不全是数字，那么返回""
                    return "";
                }
            } else {
                //代表只存在identify，不存在identify+数字，那么返回identify
                return s1;
            }
        }
        return "";
    }

    /**
     * 如果字符串末尾是R或r加数字，返回此数字，否则返回0
     *
     * @param s
     * @return
     */
    public static Integer getIntAfterStrLastR(String s) {
        if (s == null) {// 空字符串返回0
            return -1;
        }
        int lastIndexOfR = s.toUpperCase().lastIndexOf('R');
        if (lastIndexOfR != -1) {
            String s1 = s.substring(lastIndexOfR + 1);
            if (StringUtil.isNumber(s1)) {
                return Integer.parseInt(s1);
            }
            return 0;// 包含R但R后不是数字返回0
        }
        return 0;// 不包含R返回0
    }

    /**
     * 将返修、扩探焊口编号还原成原始焊口编号
     *
     * @param weldTagNumber
     * @return
     */
    public static String getOriginalWeldTagNumber(String weldTagNumber) {
        if (StringUtils.isBlank(weldTagNumber)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < weldTagNumber.length(); i++) {
            // 遇到K或者R停止循环
            char c = weldTagNumber.charAt(i);
            if (c == 'R' || c == 'K' || c == 'r' || c == 'k') {
                break;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 获取焊口编号后面的k和r的整体后缀（比如1K1返回的是K1,1K1R1返回的是K1R1）
     *
     * @param weldTagNumber
     * @return
     */
    public static String getAfterWeldTagNumber(String weldTagNumber) {
        if (weldTagNumber == null || "".equals(weldTagNumber)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean isStart = false;
        for (int i = 0; i < weldTagNumber.length(); i++) {
            // 遇到K或者R开始截取
            char c = weldTagNumber.charAt(i);
            if (c == 'R' || c == 'K' || c == 'r' || c == 'k') {
                isStart = true;
            }
            ;
            if (isStart) {
                sb.append(c);
            }
        }
        if (isStart) {
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * 统计字符串中指定字符的个数
     *
     * @param str
     * @param c
     * @return
     */
    public static int getSpecificCharCount(String str, char c) {
        if (isEmptyStr(str)) {
            return -1;
        }
        int count = 0;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                count++;
            }
        }
        return count;
    }

    /**
     * 给出索引位置的左括号匹配的右括号的索引
     *
     * @param str
     * @param leftCracketIndex 左括号索引
     * @param leftCracket      左括号的char类型，支持'(' '[' '{'
     * @return
     */
    public static int getCorrespondingCracketIndex(String str, int leftCracketIndex, char leftCracket) {
        char rightCracket = getCorrespondingCracket(leftCracket);
        Stack<Character> stack = new Stack<>();
        Character leftCracketCharacter = null;
        int leftCracketTimes = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == leftCracket) {
                leftCracketTimes++;
                if (leftCracketTimes == leftCracketIndex) {
                    leftCracketCharacter = new Character(leftCracket);
                    stack.push(leftCracketCharacter);
                } else {
                    stack.push(c);
                }
            } else if (c == rightCracket) {
                if (leftCracketCharacter.equals(stack.peek())) {
                    return i;
                }
                stack.pop();
            } else {
                continue;
            }
        }
        return -1;
    }

    private static char getCorrespondingCracket(char leftCracket) {
        switch (leftCracket) {
            case '[':
                return ']';
            case '(':
                return ')';
            case '{':
                return '}';
            default:
                return ')';
        }
    }

    /**
     * 删除指定的最后一个字符串
     *
     * @param sb
     * @param lastString
     */
    public static void deleteLastString(StringBuilder sb, String lastString) {
        int i = sb.lastIndexOf(lastString);
        if (i == -1) {
            return;
        } else {
            sb.deleteCharAt(i);
        }
    }

    /**
     * 把str里面最后一次出现的fromStr替换成toStr
     *
     * @param str
     * @param fromStr
     * @param toStr
     * @return
     */
    public static String replaceLastStrToStr(String str, String fromStr, String toStr) {
        int i = str.lastIndexOf(fromStr);
        if (i == -1) {
            return str;
        } else {
            String substring = str.substring(0, i);
            substring += toStr;
            return substring;
        }
    }

    /**
     * 获取排序参数
     *
     * @param orderParamList
     * @return
     */
    public static String[] getOrderParam(List<Map<String, Object>> orderParamList) {
        String[] orderParam = new String[2];
        if (orderParamList != null) {
            String columnNames = "";
            String order = "";
            for (Map<String, Object> map : orderParamList) {
                if ("weld.weldTagNumber".equals(map.get("columnName"))) {
                    columnNames += "weld.orderNumber,";
                } else {
                    columnNames += map.get("columnName") + ",";
                }
                order += map.get("sortOrder") + ",";
            }
            orderParam[0] = columnNames;
            orderParam[1] = order;
        }
        return orderParam;
    }

    //判断是否空字符串
    public static boolean isEmptyStr(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 匹配所有数字（包含零、正整数、负整数、正负浮点数）
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否由纯数字组成，比如（1,01,009都返回true）
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将字符串首字母小写
     *
     * @param string
     * @return
     */
    public static String getFirstToLowerCase(String string) {
        return string == null ? string : string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    /**
     * 将字符串首字母大写
     *
     * @param string
     * @return
     */
    public static String getFirstToUpCase(String string) {
        return string == null ? string : string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    /**
     * 将下划线分隔单词的方式变成驼峰命名法（首字母小写）
     *
     * @param string
     * @return
     */
    public static String underline2Hump(String string) {
        if (string != null) {
            //先将下划线后面的字符转为小写
            char[] chars = string.toLowerCase().toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (i > 0 && chars[i - 1] == '_') {
                    chars[i] -= 32;
                }
            }
            return String.valueOf(chars).replace("_", "");
        }
        return null;
    }

    /**
     * 将驼峰命名法变成下划线分隔单词的方式
     *
     * @param string
     * @return
     */
    public static String hump2Underline(String string) {
        if (string != null) {
            StringBuilder sb = new StringBuilder();
            char[] chars = string.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (Character.isUpperCase(chars[i])) {
                    sb.append("_");
                }
                sb.append(chars[i]);
            }
            return sb.toString();
        }
        return null;
    }

    //	类成员变量名根据字母大小写转成带下划线的字符串
    public static String meber2ColumnName(String member) {
        if (member.startsWith("\"")) {
            return member;
        }
        if (member.indexOf("TagNumber") != -1) {
            member = member.replace("agNumber", "agnumber");
        }
        int pointIndex = member.lastIndexOf(".");
        return member == null ? null : (pointIndex == -1 ? hump2Underline(member) :
                (member.substring(0, pointIndex) + hump2Underline(member.substring(pointIndex))));
    }

    /**
     * 中文一，二，三。。。转换为1,2,3。。；十一转为101，二十一转为2101
     *
     * @param str
     * @return
     */
    public static String chineseNumToALaBoNum(String str) {
        str = str.trim();
        str = str.replaceAll("一", "1");
        str = str.replaceAll("二", "2");
        str = str.replaceAll("三", "3");
        str = str.replaceAll("四", "4");
        str = str.replaceAll("五", "5");
        str = str.replaceAll("六", "6");
        str = str.replaceAll("七", "7");
        str = str.replaceAll("八", "8");
        str = str.replaceAll("九", "9");
        str = str.replaceAll("十", "10");
        return str;
    }

    /**
     * 字符串里提取数字
     *
     * @param str
     * @return
     */
    public static String deleteAllChinese(String str) {
        str = str.trim();
        String str2 = "";
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 += str.charAt(i);
                }
            }

        }
        return str2;
    }

    /**
     *   罗马数字转Int 另一种写法
     *   public int romanToInt(String s) {
     *         int sum=0;
     *         char[] arr = s.toCharArray();
     *         for (int i = 0; i < arr.length; i++) {
     *             int value = getValue(arr[i]);
     *             if ( i != arr.length - 1  && getValue(arr[i + 1]) > value) {
     *                 sum -= value;
     *             } else {
     *                 sum += value;
     *             }
     *         }
     *         return sum;
     *     }
     *     private static int getValue(char ch) {
     *         switch (ch) {
     *             case 'I':
     *                 return 1;
     *             case 'V':
     *                 return 5;
     *             case 'X':
     *                 return 10;
     *             case 'L':
     *                 return 50;
     *             case 'C':
     *                 return 100;
     *             case 'D':
     *                 return 500;
     *             case 'M':
     *                 return 1000;
     *             default:
     *                 return 0;
     *         }
     *     }
     * 罗马数字转化成数字
     *
     * @param luoma
     * @return
     */
    public static int luomaToInt(String luoma) {
        int sum = 0;
        if (StringUtils.isNotBlank(luoma)) {
            int len = luoma.length();
            for (int i = 0; i < len; i++) {
                if (luoma.charAt(i) == "I".charAt(0)) {
                    if (i != len - 1 && (luoma.charAt(i + 1) == "V".charAt(0) || luoma.charAt(i + 1) == "X".charAt(0))) {
                        sum--;
                    } else {
                        sum++;
                    }
                }
                if (luoma.charAt(i) == "V".charAt(0)) {
                    sum += 5;
                }
                if (luoma.charAt(i) == "X".charAt(0)) {
                    if (i != len - 1 && (luoma.charAt(i + 1) == "C".charAt(0) || luoma.charAt(i + 1) == "L".charAt(0))) {
                        sum -= 10;
                    } else {
                        sum += 10;
                    }
                }
                if (luoma.charAt(i) == "L".charAt(0)) {
                    sum += 50;
                }
                if (luoma.charAt(i) == "C".charAt(0)) {
                    if (i != len - 1 && (luoma.charAt(i + 1) == "D".charAt(0) || luoma.charAt(i + 1) == "M".charAt(0))) {
                        sum -= 100;
                    } else {
                        sum += 100;
                    }
                }
                if (luoma.charAt(i) == "D".charAt(0)) {
                    sum += 500;
                }
                if (luoma.charAt(i) == "M".charAt(0)) {
                    sum += 1000;
                }
            }
        }
        return sum;
    }

    /**
     * list类型转sql拼接时in后括号里面拼接的字符串
     *
     * @param list
     * @return
     */
    public static String listToSqlStr(List<String> list) {
        if (list.isEmpty()) {
            return "";
        }
        String returnStr = "";
        for (String str : list) {
            returnStr += "'";
            returnStr += str;
            returnStr += "',";
        }
        return returnStr.substring(0, returnStr.length() - 1);
    }

    /**
     * set类型转sql拼接时in后括号里面拼接的字符串
     *
     * @param set
     * @return
     */
    public static String setToSqlStr(Set<String> set) {
        if (set.isEmpty()) {
            return "";
        }
        String returnStr = "";
        for (String str : set) {
            returnStr += "'";
            returnStr += str;
            returnStr += "',";
        }
        return returnStr.substring(0, returnStr.length() - 1);
    }

    /**
     * string数组类型转sql拼接时in后括号里面拼接的字符串
     *
     * @param spilt
     * @return
     */
    public static String stringArrayToSqlStr(String[] spilt) {
        if (spilt.length > 0) {
            String returnStr = "";
            for (String str : spilt) {
                returnStr += "'";
                returnStr += str;
                returnStr += "',";
            }
            return returnStr.substring(0, returnStr.length() - 1);
        }
        return "";
    }

    /**
     * 字符串集合转为数字集合
     *
     * @param strList
     * @return
     */
    public static List<Double> listStringToListDouble(List<String> strList) {
        List<Double> returnList = new ArrayList<>();
        for (String str : strList) {
            if (isNumber(str)) {
                returnList.add(Double.parseDouble(str));
            }
        }
        return returnList;
    }

    /**
     * 对double类型的集合求平均值，保留整数
     *
     * @param doubleList
     * @return
     */
    public static String averageList(List<Double> doubleList) {
        if (doubleList.isEmpty()) {
            return "";
        }
        int size = doubleList.size();
        double sum = 0;
        for (Double d : doubleList) {
            sum += d;
        }
        DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数
        return df.format((double) Math.round(sum / size));
    }

    /**
     * 全角字符转换成半角
     *
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        if (null != input) {
            char c[] = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if ('\u3000' == c[i]) {
                    c[i] = ' ';
                } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                    c[i] = (char) (c[i] - 65248);
                }
            }
            String dbc = new String(c);
            return dbc;
        } else {
            return null;
        }
    }

    /**
     * 判断一个字符是否是中文
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;
    }

    /**
     * 判断一个字符串是否含有中文
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 截取数字
     *
     * @param content
     * @return
     */
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    public static String setQualifiedLevel(String qualifiedLevel, String detectionPattern) {
        if ("PAUT".equals(detectionPattern)) {
            detectionPattern = "RT";
        }
        if (StringUtils.isNotBlank(qualifiedLevel)) {
            qualifiedLevel = qualifiedLevel.replaceAll(" ", "");
            final int detectionPatternIndx = qualifiedLevel.indexOf(detectionPattern);
            if (detectionPatternIndx != -1) {
                return switchRome(qualifiedLevel.substring(detectionPatternIndx + detectionPattern.length(), qualifiedLevel.indexOf("级", detectionPatternIndx)));
            }
        }
        return "";
    }

    private static String switchRome(String decRatio) {
        String result = "";
        switch (decRatio) {
            case "I":
                result = "Ⅰ";
                break;
            case "II":
                result = "Ⅱ";
                break;
            case "III":
                result = "Ⅲ";
                break;
            case "IV":
                result = "Ⅳ";
                break;
            default:
                result = decRatio;
                break;
        }
        return result;
    }

    /**
     * 从map里面取出来key的value
     *
     * @param map
     * @param key
     * @return
     */
    public static String getStringByMapAndKey(Map<String, Object> map, String key) {
        if (map.isEmpty() || map.get(key) == null || "".equals(map.get(key).toString().trim())) {
            return "";
        }
        return map.get(key).toString().trim();
    }

    /**
     * 过滤null和空字符串
     *
     * @param key
     * @return
     */
    public static String filterNullAndEmptyStr(String key) {
        if (key != null && !"".equals(key.toString().trim())) {
            return key.toString().trim();
        }
        return "";
    }

    /**
     * 获取返修后缀
     */
    public static String lastStrForRepair(String weldTagNumber) {
        String value = "";
        if (weldTagNumber.contains("r")) {
            value = weldTagNumber.substring(weldTagNumber.indexOf("r"), weldTagNumber.indexOf("r") + 2);
        } else if (weldTagNumber.contains("R")) {
            value = weldTagNumber.substring(weldTagNumber.indexOf("R"), weldTagNumber.indexOf("R") + 2);
        }
        return value;
    }

    /**
     * 获取扩探后缀
     */
    public static String lastStrForExtend(String weldTagNumber) {
        String value = "";
        if (weldTagNumber.contains("k")) {
            value = weldTagNumber.substring(weldTagNumber.indexOf("k"), weldTagNumber.indexOf("k") + 2);
        } else if (weldTagNumber.contains("K")) {
            value = weldTagNumber.substring(weldTagNumber.indexOf("K"), weldTagNumber.indexOf("K") + 2);
        }
        return value;
    }

    public static String numberTransation(String romNo) {
        if (romNo != null && !"".equals(romNo)) {
            switch (romNo) {
                case "Ⅰ":
                    return "1";
                case "Ⅱ":
                    return "2";
                case "Ⅲ":
                    return "3";
                case "Ⅳ":
                    return "4";
                default:
                    return "0";
            }
        }
        return "0";
    }

    /**
     * 将用“+”拼接的单个材质名称去两端空格后排序
     * @param materialName
     * @return
     */
    public static String getOrderedAndTrimedMaterial(String materialName) {
        if (materialName == null || "".equals(materialName)) {
            return materialName;
        }
        String[] split = materialName.split("\\+");
        StringUtil.trimStringArray(split);
        Arrays.sort(split);
        return StringUtils.join(split,'+');
    }

    /**
     * 把orderNumber按照分隔符separator的最后一次出现位置分为两部分，既前缀和后缀，如果后缀是纯数字，那么返回的数组arr[0]就是前缀，arr[1]就是后缀
     * 如果后缀不是纯数字，那么返回的数组arr[0]就是orderNumber，arr[1]就是空字符串
     * 在这里要注意的是后缀为001这样格式的要转换成1.既arr[1]要存的是转换格式后的数字
     * @param orderNumber
     * @param separator  分隔符，比如-
     * @return
     */
    public static String[] getPreStrAndLastNumberBySeparator(String orderNumber, String separator) {
        String[] returnArr = new String[3];
        //判断后缀是否是流水号，是的话当流水号处理存到后缀，不是的话跟前缀加起来一块儿存到前缀里
        String preOrdernumber = orderNumber;
        String lastOrdernumber = "";
        String actualSectionNo = orderNumber;
        if (orderNumber.contains("-") && (orderNumber.lastIndexOf("-") + 1) != orderNumber.length()) {
            //前缀
            String substring = orderNumber.substring(0, orderNumber.lastIndexOf("-"));
            //后缀，流水号
            String substring1 = orderNumber.substring(orderNumber.lastIndexOf("-") + 1, orderNumber.length());
            if (StringUtil.isNumeric(substring1)) {
                preOrdernumber = substring;
                //把001转换为1。。。
                substring1 = substring1.replaceAll("^(0+)", "");
                if (StringUtil.isNumber(substring1)) {
                    lastOrdernumber = substring1;
                }
            }
        }
        returnArr[0] = preOrdernumber;
        returnArr[1] = lastOrdernumber;
        if (!"".equals(lastOrdernumber)) {
            actualSectionNo = preOrdernumber + "-" + getActualSerialNumber(Integer.parseInt(lastOrdernumber));
        }
        returnArr[2] = actualSectionNo;
        return returnArr;
    }

    public static String getActualSerialNumber(Integer serialNumber) {
        String actualSeriNumber = "";
        if (serialNumber <= 99 && serialNumber > 9) {
            actualSeriNumber += "0";
        } else if (serialNumber > 0 && serialNumber <= 9) {
            actualSeriNumber += "00";
        }
        actualSeriNumber += serialNumber;
        return actualSeriNumber;
    }

    /**
     * 1.带百分号的，直接取原值 2.不带百分号的，且数值大于1的，直接加百分号
     * 3.不带百分号的，且数值小于等于1的，乘100+百分号   4.--和空，直接取--和空
     * 5.考虑多检测比例的情况，目前支持多检测比例之间隔开的符号 ， /
     * @param xmlRatio
     * @return
     */
    public static String getRatioByStrAndRule(String xmlRatio) {
        if (StringUtil.isNullOrNullStr(xmlRatio)) {
            return "";
        }
        String xmlRatioOne = xmlRatio.trim().replaceAll("/", ",").replaceAll("，", ",").replaceAll("——", "--");
        String[] split = xmlRatioOne.split(",");
        String returnStr = "";
        int num = 1;
        for (String str : split) {
            if (str.contains("%")) {
                returnStr += str;
            } else if (isNumber(str)) {
                if (Double.parseDouble(str) <= 1) {
                    String tempStr = Math.round(Double.parseDouble(str) * 100) + "%";
                    returnStr += tempStr;
                } else {
                    returnStr += str;
                    returnStr += "%";
                }
            } else {
                returnStr += str;
            }
            if (num < split.length) {
                returnStr += ",";
            }
            num++;
        }
        return returnStr;
    }

    /**
     * 1.带百分号的，直接取原值 2.不带百分号的，且数值大于1的，直接加百分号
     *  3.不带百分号的，且数值小于等于1的，乘100+百分号   4.--和空，直接取--和空
     *   5.只考虑单检测比例的情况
     * @param xmlRatioOne
     * @return
     */
    public static String getSingleRatioByStrAndRuleOne(String xmlRatioOne) {
        if (StringUtil.isNullOrNullStr(xmlRatioOne)) {
            return "";
        }
        String returnStr = "";
        String xmlRatio = xmlRatioOne.trim().replaceAll("——", "--");
        if (xmlRatio.contains("%")) {
            returnStr += xmlRatio;
        } else if (isNumber(xmlRatio)) {
            if (Double.parseDouble(xmlRatio) <= 1) {
                String tempStr = Math.round(Double.parseDouble(xmlRatio) * 100) + "%";
                returnStr += tempStr;
            } else {
                returnStr += xmlRatio;
                returnStr += "%";
            }
        } else {
            returnStr += xmlRatio;
        }
        return returnStr;
    }

    public static List<String> stringToList(final String str) {
        if (!"".equals(str)) {
            final String[] strs = str.split(";");
            List<String> arrList = Arrays.asList(strs);
            List list = new ArrayList(arrList);
            return list;
        } else {
            return null;
        }
    }

    public static String listToStr(List<String> list) {
        if (list.isEmpty()) {
            return "";
        }
        String returnStr = "";
        for (String str : list) {
            returnStr += str;
            returnStr += ";";
        }
        return returnStr.substring(0, returnStr.length() - 1);
    }

    /**
     * 计算一个字符串中包含某个字符串的个数
     * @param str
     * @param s
     * @return
     */
    public static int countString(String str, String s) {
        int count = 0, len = str.length();
        while (str.indexOf(s) != -1) {
            str = str.substring(str.indexOf(s) + 1, str.length());
            count++;
        }
        return count;
    }

    /**
     * 取出字符串中第一个非数字字符之前的数字作为新字符串
     * @param str
     * @return
     */
    public static String strToDoubleStr(String str) {
        StringBuilder resultSb = new StringBuilder();
        int firstNotNumberCharIndex = -1;
        for (int i = 0; i < str.length(); i++) {
            int charAscii = (int) str.charAt(i);
            if ((charAscii != 46 && charAscii < 47) || charAscii > 58) { // 非数字
                firstNotNumberCharIndex = i;
                break;
            }
        }
        if (firstNotNumberCharIndex != -1) {
            resultSb.append(str.substring(0, firstNotNumberCharIndex));
        }else {
            resultSb.append(str);
        }
        return resultSb.toString();
    }

    public static Map<String, String> toRomanData() {
        final Map<String, String> map = new LinkedHashMap<>();
        map.put("XIV", "ⅩⅣ");
        map.put("XIII", "ⅩⅢ");
        map.put("XII", "Ⅻ");
        map.put("XI", "Ⅺ");
        map.put("IX", "Ⅸ");
        map.put("X", "Ⅹ");
        map.put("VIII", "Ⅷ");
        map.put("VII", "Ⅶ");
        map.put("VI", "Ⅵ");
        map.put("IV", "Ⅳ");
        map.put("V", "Ⅴ");
        map.put("III", "Ⅲ");
        map.put("II", "Ⅱ");
        map.put("I", "Ⅰ");
        return map;
    }

    public static String checkQlevel(final String text) {
        String str = text;
        final Map<String, String> map = toRomanData();
        final Set<String> keys = map.keySet();
        for (String key : keys) {
            if (str.contains(key)) {
                str = str.replace(key, map.get(key));
            }
        }
        return str;
    }

    /**
     * 检测类别划分中材质字符串解析为材质数组
     *
     * @param materials
     * @return
     */
    public static String[] materialStrToArray(final String materials) {
        final String newMaterials = materials.substring(materials.indexOf("[") + 1, materials.lastIndexOf("]"));
        final String[] materialArray = newMaterials.split("]\\[");
        return materialArray;
    }

    public static String[] materialStrNoFirstAndLastRedexToArray(final String materials) {
        final String[] materialArray = materials.split("]\\[");
        return materialArray;
    }

    public static String getDifferentStr(final String str) {
        //将字符串变成数组，然后利用stream流变成集合
        List<Object> throughLines = Arrays.stream(str.split(",")).collect(Collectors.toList());
        //利用stream流将集合去重
        List<Object> throughLineList = throughLines.stream().distinct().collect(Collectors.toList());
        //然后再用Stringuitls.join将集合变成逗号分开的字符串
        String differentStr = StringUtils.join(throughLineList.toArray(), ",");
        return differentStr;
    }

    public static String getDifferentStrForMaterial(final String str) {
        //将字符串变成数组，然后利用stream流变成集合
        List<String> throughLines = Arrays.stream(str.split("\\*")).collect(Collectors.toList());
        //利用stream流将集合去重
        List<String> throughLineList = throughLines.stream().distinct().collect(Collectors.toList());
        //然后再用Stringuitls.join将集合变成逗号分开的字符串
        String differentStr = StringUtils.join(throughLineList.toArray(), "][");
        return "[" + differentStr + "]";
    }

    public static String packagePermissionSql(final Map<String, Object> argMap) {
        //获取数据
        String sqlPart = "";
        if (argMap.containsKey("projectId")) {
            sqlPart += " AND PIPELINE.PROJECT_ID = '" + argMap.get("projectId") + "'";
        }
        if (argMap.containsKey("deviceId")) {
            sqlPart += " AND PIPELINE.DEVICE_ID = '" + argMap.get("deviceId") + "'";
        }
        if (argMap.containsKey("zoneId")) {
            sqlPart += " AND PIPELINE.ZONE_ID = '" + argMap.get("zoneId") + "'";
        }
        return sqlPart;
    }

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * This method is used for converting object to string
     * @param obj
     * @return
     */
    public static String objectToString(Object obj) {
        return ReflectionToStringBuilder.toString(obj);
    }
}
