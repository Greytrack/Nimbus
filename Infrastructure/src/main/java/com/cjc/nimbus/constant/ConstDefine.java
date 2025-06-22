package com.cjc.nimbus.constant;

import lombok.NonNull;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 通用常量类
 *
 * @author huanggs
 * @date 2024/10/08
 */
public final class ConstDefine {

    public static final class C_COMMON {

        public static final Integer INTEGER_ONE_NEGATIVE = -1;
        public static final Integer INTEGER_ZERO = 0;
        public static final Integer INTEGER_ONE = 1;
        public static final Integer INTEGER_TWO = 2;
        public static final Integer INTEGER_THREE = 3;
        public static final Integer INTEGER_FOUR = 4;
        public static final Integer INTEGER_FIVE = 5;
        public static final Integer INTEGER_SIX = 6;
        public static final Integer INTEGER_SEVEN = 7;
        public static final Integer INTEGER_EIGHT = 8;
        public static final Integer INTEGER_NINE = 9;
        public static final Integer INTEGER_TEN = 10;
        public static final Integer INTEGER_HUNDRED = 100;
        public static final Integer INTEGER_FIVE_THOUSAND = 5000;
        public static final Integer INTEGER_TEN_THOUSAND = 10000;
        public static final Long LONG_ONE_NEGATIVE = -1L;
        public static final Long LONG_ZERO = 0L;
        public static final Long LONG_ONE = 1L;
        public static final Long LONG_TWO = 2L;
        public static final Long LONG_THREE = 3L;
        public static final Long LONG_FOUR = 4L;
        public static final Long LONG_FIVE = 5L;
        public static final Long LONG_SIX = 6L;
        public static final Long LONG_SEVEN = 7L;
        public static final Long LONG_EIGHT = 8L;
        public static final Long LONG_NINE = 9L;
        public static final Long LONG_TEN = 10L;
        public static final Long LONG_HUNDRED = 100L;
        public static final Boolean BOOLEAN_TRUE = true;
        public static final Boolean BOOLEAN_FALSE = false;
        public static final BigDecimal DECIMAL_ZERO = BigDecimal.ZERO;

        /**
         * 提现最小金额.
         */
        public static final BigDecimal DECIMAL_TEN = new BigDecimal(10);

        /**
         * 系统中使用到的默认编码
         */
        public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

        public static final String APPLICATION_JSON = "application/json";

        /**
         * 分段尺寸, 1000.
         */
        public static final Integer SECTION_SIZE = 1000;

        /**
         * 排序
         */
        public static final String ASC = "asc";

        public static final String DESC = "desc";

        /**
         * 空字符串.
         */
        public static final String EMPTY = "";

        /**
         * 空格.
         */
        public static final String SPACE = " ";

        /**
         * 逗号.
         */
        public static final String COMMA = ",";

        /**
         * 冒号.
         */
        public static final String COLON = ":";

        /**
         * 分号.
         */
        public static final String SEMICOLON = ";";

        /**
         * 加号.
         */
        public static final String PLUS = "\\+";

        /**
         * 点.
         */
        public static final String POINT_TRANSFER = "\\.";

        /**
         * 点.
         */
        public static final String POINT = ".";

        /**
         * 下划线.
         */
        public static final String UNDERLINE = "_";

        /**
         * 短横线.
         */
        public static final String TRANSVERSE = "-";

        /**
         * 竖线.
         */
        public static final String VERTICAL = "|";

        /**
         * 文件夹分隔符或者默认斜杠分隔符.
         */
        public static final String FOLDER_SEPARATOR = "/";

        public static final String SINGLE_QUOTATION_MARK = "'";

        /**
         * 中括号
         */
        public static final String MIDDEL_BRACKET = "[]";

        /**
         * 美元大括号
         */
        public static final String DOLLAR_BRACE = "${";

        /**
         * 右大括号
         */
        public static final String BRACE_RIGHT = "}";

        /**
         * 小括号
         */
        public static final String SMALL_LEFT_BRACKET = "(";

        /**
         * 小括号
         */
        public static final String SMALL_RIGHT_BRACKET = ")";

        public static final String WAVY = "~";

        public static final String WORKS_DEFAULT_VERSION = "Rev001";

        public static final LocalDateTime date19700101 = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        public static final LocalDateTime date20990101 = LocalDateTime.of(2099, 1, 1, 0, 0, 0);
    }

    public static class C_TIME {
        public static final String YMD = "yyyyMMdd";
        public static final String YMD_H = "yyyyMMdd HH";
        public static final String YMD_HcM = "yyyyMMdd HH:mm";
        public static final String YMD_HcMcS = "yyyyMMdd HH:mm:ss";
        public static final String YMD_HcMcS_SSS = "yyyyMMdd HH:mm:ss.SSS";
        public static final String YMDHMS = "yyyyMMddHHmmss";

        public static final String Y_M_D = "yyyy-MM-dd";
        public static final String Y_M_D_H = "yyyy-MM-dd HH";
        public static final String Y_M_D_HcM = "yyyy-MM-dd HH:mm";
        public static final String Y_M_D_HcMcS = "yyyy-MM-dd HH:mm:ss";
        public static final String Y_M_D_HcMcS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

        public static final String YlMlD = "yyyy/MM/dd";
        public static final String YlMlD_H = "yyyy/MM/dd HH";
        public static final String YlMlD_HcM = "yyyy/MM/dd HH:mm";
        public static final String YlMlD_HcMcS = "yyyy/MM/dd HH:mm:ss";
        public static final String YlMlD_HcMcS_SSS = "yyyy/MM/dd HH:mm:ss.SSS";

        public static final String MzD = "MM月dd日";
        public static final String YzMzD_HzMzSz = "yyyy年MM月dd日 HH时mm分ss秒";

        public static final String HM = "HH:mm";
        public static final String HMS = "HH:mm:ss";
        public static final int DAY_HOUR = 24;
        public static final int HALF_DAY_HOUR = 12;
    }

    public static class C_TIME_FORMATTER {
        public static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern(C_TIME.YMD);
        public static final DateTimeFormatter YMD_H = DateTimeFormatter.ofPattern(C_TIME.YMD_H);
        public static final DateTimeFormatter YMD_HcM = DateTimeFormatter.ofPattern(C_TIME.YMD_HcM);
        public static final DateTimeFormatter YMD_HcMcS = DateTimeFormatter.ofPattern(C_TIME.YMD_HcMcS);
        public static final DateTimeFormatter YMD_HcMcS_SSS = DateTimeFormatter.ofPattern(C_TIME.YMD_HcMcS_SSS);
        public static final DateTimeFormatter YMDHMS = DateTimeFormatter.ofPattern(C_TIME.YMDHMS);

        public static final DateTimeFormatter Y_M_D = DateTimeFormatter.ofPattern(C_TIME.Y_M_D);
        public static final DateTimeFormatter Y_M_D_H = DateTimeFormatter.ofPattern(C_TIME.Y_M_D_H);
        public static final DateTimeFormatter Y_M_D_HcM = DateTimeFormatter.ofPattern(C_TIME.Y_M_D_HcM);
        public static final DateTimeFormatter Y_M_D_HcMcS = DateTimeFormatter.ofPattern(C_TIME.Y_M_D_HcMcS);
        public static final DateTimeFormatter Y_M_D_HcMcS_SSS = DateTimeFormatter.ofPattern(C_TIME.Y_M_D_HcMcS_SSS);

        public static final DateTimeFormatter YlMlD = DateTimeFormatter.ofPattern(C_TIME.YlMlD);
        public static final DateTimeFormatter YlMlD_H = DateTimeFormatter.ofPattern(C_TIME.YlMlD_H);
        public static final DateTimeFormatter YlMlD_HcM = DateTimeFormatter.ofPattern(C_TIME.YlMlD_HcM);
        public static final DateTimeFormatter YlMlD_HcMcS = DateTimeFormatter.ofPattern(C_TIME.YlMlD_HcMcS);
        public static final DateTimeFormatter YlMlD_HcMcS_SSS = DateTimeFormatter.ofPattern(C_TIME.YlMlD_HcMcS_SSS);

        public static final DateTimeFormatter MzD = DateTimeFormatter.ofPattern(C_TIME.MzD);
        public static final DateTimeFormatter YzMzD_HzMzSz = DateTimeFormatter.ofPattern(C_TIME.YzMzD_HzMzSz);

        public static final DateTimeFormatter HM = DateTimeFormatter.ofPattern(C_TIME.HM);
        public static final DateTimeFormatter HMS = DateTimeFormatter.ofPattern(C_TIME.HMS);

        static DateTimeFormatter of(@NonNull String pattern) {
            return switch (pattern) {
                case C_TIME.YMD -> YMD;
                case C_TIME.YMD_H -> YMD_H;
                case C_TIME.YMD_HcM -> YMD_HcM;
                case C_TIME.YMD_HcMcS -> YMD_HcMcS;
                case C_TIME.YMD_HcMcS_SSS -> YMD_HcMcS_SSS;
                case C_TIME.YMDHMS -> YMDHMS;
                case C_TIME.Y_M_D -> Y_M_D;
                case C_TIME.HM -> HM;
                case C_TIME.Y_M_D_H -> Y_M_D_H;
                case C_TIME.Y_M_D_HcM -> Y_M_D_HcM;
                case C_TIME.Y_M_D_HcMcS -> Y_M_D_HcMcS;
                case C_TIME.Y_M_D_HcMcS_SSS -> Y_M_D_HcMcS_SSS;
                case C_TIME.YlMlD -> YlMlD;
                case C_TIME.YlMlD_H -> YlMlD_H;
                case C_TIME.YlMlD_HcM -> YlMlD_HcM;
                case C_TIME.YlMlD_HcMcS -> YlMlD_HcMcS;
                case C_TIME.YlMlD_HcMcS_SSS -> YlMlD_HcMcS_SSS;
                case C_TIME.YzMzD_HzMzSz -> YzMzD_HzMzSz;
                default -> throw new RuntimeException("不支持的日期格式".concat(pattern));
            };
        }
    }

}
