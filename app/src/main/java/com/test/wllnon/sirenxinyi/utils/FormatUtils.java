package com.test.wllnon.sirenxinyi.utils;

import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2016/4/21.
 */
public class FormatUtils {
    private static final String OUT_OF_BOUND = "OOB";

    private String[] units = { "", "K", "M", "G", "T" };
    private DecimalFormat simpleDecimalFormat;

    private static class FormatUtilsHolder {
        static final FormatUtils instance = new FormatUtils();
    }

    public static FormatUtils getInstance() {
        return FormatUtilsHolder.instance;
    }

    public String decimalSimplifyFormatter(int value) {
        prepareSimpleDecimalFormat();

        double temp = value;
        for (String unit : units) {
            if (temp / 1000.0 < 1.0) {
                return simpleDecimalFormat.format(temp) + unit;
            }
            temp /= 1000.0;
        }
        return OUT_OF_BOUND;
    }

    public String timeFormatter(Time time) {
        Date todayDate= new Date(System.currentTimeMillis());
        Date date = new Date(time.getTime());

        String dateStr = "";
        if (todayDate.getYear() == date.getYear()) {
            if (todayDate.getMonth() == date.getMonth()) {
                if (todayDate.getDay() == date.getDay()) {
                    dateStr = "今天 " + time.toString();
                } else {
                    dateStr = "本月" + todayDate.getDay() + "日 " + time.getHours() + ":" + time.getMinutes();
                }
            } else {
                dateStr = "" + todayDate.getMonth() + "月" + todayDate.getDay() + "日 " + time.getHours() + ":" + time.getMinutes();
            }
        } else {
            dateStr = "" + todayDate.getYear() + "年" + todayDate.getMonth() + "月" + todayDate.getDay() + "日 "+ time.getHours() + "时";
        }
        return dateStr;
    }

    public String dateFormatter(Date date) {
        Date today = new Date(System.currentTimeMillis());
        String dateStr = "";
        if (today.getYear() == date.getYear()) {
            if (today.getMonth() == date.getMonth()) {
                if (today.getDay() == date.getDay()) {
                    dateStr = "今天";
                } else {
                    dateStr = "本月" + date.getDay() + "日";
                }
            } else {
                dateStr = "" + date.getMonth() + "月" + date.getDay() + "日";
            }
        } else {
            dateStr = "" + date.getYear() + "年" + date.getMonth() + "月" + date.getDay() + "日";
        }
        return dateStr;
    }

    public String durationFormatter(long length) {
        Time time = new Time(length);
        String result = "";
        if (time.getSeconds() != 0) {
            result += time.getSeconds() + "″";
        }
        if (time.getMinutes() != 0) {
            result = time.getMinutes() + "′" + result;
        }
        return result;
    }

    private void prepareSimpleDecimalFormat() {
        if (simpleDecimalFormat == null) {
            simpleDecimalFormat = new DecimalFormat("###.#");
        }
    }
}
