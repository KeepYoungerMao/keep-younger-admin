package com.mao.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author mao by 16:33 2019/9/18
 */
public class SU {

    private static final char[] ALLs = {
            '1','2','3','4','5','6','7','8','9','0',
            'a','b','c','d','e','f','g','h','i','j',
            'k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z','A','B','C','D',
            'E','F','G','H','I','J','K','L','M','N',
            'O','P','Q','R','S','T','U','V','W','X',
            'Y','Z'
    };

    /**
     * 身份证权重因子
     */
    private static final int[] POWER = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

    /**
     * 手机号 正则表达式
     */
    private static final String PHONE_REGEX =
            "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(166)|(17[0135678])|(18[0-9])|(19[89]))\\d{8}$";

    /**
     * Email 正则表达式
     */
    private static final String EMAIL_REGEX = "(?:(?:[A-Za-z0-9\\-_@!#$%&'*+/=?" +
            "^`{|}~]|(?:\\\\[\\x00-\\xFF]?)|(?:\"[\\x00-\\xFF]*\"))+(?:\\.(?:(?:[A-Za-z0-9\\-" +
            "_@!#$%&'*+/=?^`{|}~])|(?:\\\\[\\x00-\\xFF]?)|(?:\"[\\x00-\\xFF]*\"))+)*)" +
            "@(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+" +
            "(?:(?:[A-Za-z0-9]*[A-Za-z][A-Za-z0-9]*)(?:[A-Za-z0-9-]*[A-Za-z0-9])?))";

    /**
     * 判断字符串是否全是字母
     * @param str 字符串
     * @return boolean
     */
    public static boolean isZM(String str){
        for (char c : str.toCharArray()) {
            if (!((c >= 'a' && c <= 'z')||(c >= 'A' && c <= 'Z')))
                return false;
        }
        return true;
    }

    /**
     * 判断字符串是否为空
     * 仿写commons-lang3
     * @param str str
     * @return true/false
     */
    public static boolean isEmpty(String str){
        // " " is false
        return null == str || str.length() == 0;
    }

    /**
     * 判断字符串是否不为空
     * 仿写commons-lang3
     * @param str str
     * @return true/false
     */
    public static boolean isNotEmpty(String str){
        // " " is true
        return null != str && str.length() > 0;
    }

    /**
     * 字符串转换成数字
     * 转换异常返回null，用于判断是否是数字
     * @param str 字符串
     * @return 数字 / null
     */
    public static Integer getNumber(String str){
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 判断字符串不能为空，并且在范围长度之内
     * @param str 字符串
     * @param min 最小长度
     * @param max 最大长度
     * @return boolean
     */
    public static boolean isRightString(String str, int min, int max){
        return isNotEmpty(str) && str.length() >= min && str.length() <= max;
    }

    /**
     * 判断字符串是否全是数字
     * @param str string
     * @return true/false
     */
    public static boolean isNumber(String str){
        if (isEmpty(str))
            return false;
        for (int i = 0 , len = str.length() ; i < len ; i ++)
            if (!Character.isDigit(str.charAt(i)))
                return false;
        return true;
    }

    /**
     * 获取随机字符串
     * @param length 长度
     * @return String
     */
    public static String getRandomString(int length){
        if (length > 0){
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < length; i ++){
                sb.append(ALLs[random.nextInt(ALLs.length)]);
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 中国 身份证识别
     * 可识别15位及18位身份证
     * @param idcard IdCard
     * @return true/false
     */
    public static boolean isIdCard(String idcard){
        if (isEmpty(idcard))
            return false;
        if (idcard.length() == 15){
            if (!isNumber(idcard))
                return false;
            idcard = translate15IdcardTo18Idcard(idcard);
            if (null == idcard)
                return false;
        }
        return isNumber(idcard.substring(0,idcard.length() - 1)) && validate18IdCard(idcard);
    }

    /**
     * translate IDCard-15 into IDCard-18
     * @param idcard IDCard-15
     * @return 18-IDCard or null
     */
    private static String translate15IdcardTo18Idcard(String idcard){
        //get birth
        String birth = idcard.substring(6,12);
        Date birthDate = null;
        try{
            birthDate = new SimpleDateFormat("yyMMdd").parse(birth);
        }catch (ParseException e){
            e.printStackTrace();
        }
        if (null == birthDate)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthDate);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String idcard17 = idcard.substring(0,6) + year + idcard.substring(8);
        int[] ints =convertCharToInt(idcard17.toCharArray());
        int sum17 = getPowerSum(ints);
        String checkCode = getCheckCodeBySum(sum17);
        if (null == checkCode)
            return null;
        return idcard17 + checkCode;
    }

    /**
     * Judging the Legitimacy of IDCard-18
     * According to the stipulation of citizenship number in the national
     * standard GB11643-1999 of the People's Republic of China，
     * Citizenship number is a feature combination code,
     * which consists of 17-digit digital ontology code and one-digit digital check code.
     * The order from left to right is: six digit address code,
     * eight digit date of birth code, three digit sequence code and one digit check code.
     * sequence code:
     * Indicates the person born in the same year, month and day within the
     * area marked by the same address code.
     * In sequence code, odd numbers for men and even numbers for women。
     *  1.前1、2位数字表示：所在省份的代码；
     *  1. The first 1 and 2 digits represent the code of the province in which it is located.
     *  2.第3、4位数字表示：所在城市的代码；
     *  2. The 3rd and 4th digits indicate: the code of the city in which it is located;
     *  3.第5、6位数字表示：所在区县的代码；
     *  3. Numbers 5 and 6 denote the code of the district or county in which it is located.
     *  4.第7~14位数字表示：出生年、月、日；
     *  4. Numbers 7-14 denote the year, month and day of birth;
     *  5.第15、16位数字表示：所在地的派出所的代码；
     *  The fifteenth and sixteenth digits indicate the code of the police station
     *  where it is located.
     *  6.第17位数字表示性别：奇数表示男性，偶数表示女性；
     *  6. Seventeenth digit denotes gender: odd number denotes male, even number denotes female;
     *  7.第18位数字是校检码：也有的说是个人信息码，
     *  7. The eighteenth digit is the proofreading code.
     *  Others say it is the personal information code.
     *    一般是随计算机的随机产生，用来检验身份证的正确性。
     *    Generally, it is generated randomly with the computer to
     *    verify the correctness of the ID card.
     *    校检码可以是0~9的数字，和罗马数字x表示。
     *    The check code can be a number of 0-9 and a Roman number X.
     * 第十八位数字(校验码)的计算方法为：
     * 1.将前面的身份证号码17位数分别乘以不同的系数。
     *   从第一位到第十七位的系数分别为：
     *    7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 2.将这17位数字和系数相乘的结果相加。
     * 3.用加出来和除以11，看余数是多少？
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。
     *   其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。
     *   如果余数是10，身份证的最后一位号码就是2。
     * @param idcard ID Card
     * @return true/false
     */
    private static boolean validate18IdCard(String idcard){
        if (idcard.length() == 18){
            String checkCode =idcard.substring(17,18);
            char[] chars = idcard.substring(0,17).toCharArray();
            int sum17 =getPowerSum(convertCharToInt(chars));
            String _checkCode =getCheckCodeBySum(sum17);
            return null != _checkCode && _checkCode.equals(checkCode);
        }
        return false;
    }

    /**
     * converting char arrays to int arrays
     * Used for authentication of identity cards
     * @param chars char arrays
     * @return int arrays
     * @throws NumberFormatException not a number
     */
    private static int[] convertCharToInt(char[] chars) throws NumberFormatException{
        int len = chars.length;
        int[] ints = new int[len];
        for (int i = 0 ; i < len ; i ++){
            ints[i] = Integer.parseInt(String.valueOf(chars[i]));
        }
        return ints;
    }

    /**
     * get the 18th Bit Check Code in the Id Card Number
     * Used for authentication of identity cards
     * @param sum the sum of Weighting factors
     * @return Bit Check Code
     */
    private static String getCheckCodeBySum(int sum){
        switch (sum%11){
            case 10: return "2";
            case 9: return "3";
            case 8: return "4";
            case 7: return "5";
            case 6: return "6";
            case 5: return "7";
            case 4: return "8";
            case 3: return "9";
            case 2: return "x";
            case 1: return "0";
            case 0: return "1";
            default: return null;
        }
    }

    /**
     * the sum of Weighting factors
     * Used for authentication of identity cards
     * @param ints Weighting factors
     * @return int
     */
    private static int getPowerSum(int[] ints){
        int sum = 0;
        int len = ints.length;
        if (POWER.length != len)
            return sum;
        for (int i = 0 ; i < len ; i ++ )
            sum += ints[i] * POWER[i];
        return sum;
    }

    /**
     * Determine whether a string is a cell phone number
     * China Telecom:133,149,153,173,177,180,181,189,199
     * China Unicom:130,131,132,145,155,156,166,175,176,185,186
     * China Mobile:134(0-8),13(5-9),147,15(0-2),15(7-9),178,18(2-4),187,188,198
     * @param phone phone number
     * @return true/false
     * @since 2019/03
     */
    public static boolean isPhone(String phone){
        if (phone.length() == 11){
            Pattern pattern = Pattern.compile(PHONE_REGEX);
            Matcher matcher = pattern.matcher(phone);
            return matcher.matches();
        }
        return false;
    }

    /**
     * Verify the correctness of Email
     * validation rule:RFC 5322
     * user name
     * 1.ALLOW:A-Z,a-z,0-9,.-_@!#$%&'*+/=?^{}|~
     * 2.ALLOW:ASCII char(including Control-char).
     * 3.[.] can not appear at the beginning or end,Can't be next to each other more than two.
     * domain name
     * 1. ONLY ALLOW:A-Z,a-z,0-9,-(this one can not appear at the beginning or end).
     * 2. Top-level domain name cannot be all digital.
     * 3. at least have a secondary domain name.
     * @param email email
     * @return true/false
     */
    public static boolean isEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}