package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateFormatUtils;



public class TimeUtils {
    private static char DAY_DELIMITER = '-';
    public static int YEAR = Calendar.YEAR;
    public static int MONTH = Calendar.MONDAY;
    public static int DAY = Calendar.DAY_OF_MONTH;
    public static int HOUR = Calendar.HOUR_OF_DAY;
    public static int MINUTE = Calendar.MINUTE;
    public static int SECOND = Calendar.SECOND;
    /**
     */
    public static final String FORMAT2 = "yyyy-MM-dd HH:mm:ss";


    public static String getTime(int step) {
        Date d1 = new Date();
        long diff = 0;
        diff = d1.getTime() + step * 1000L;
        return getTime(diff);
    }

    public static String getHour(String hour, int hourDif) {
        Calendar calendar = new GregorianCalendar(Integer.parseInt(hour.substring(0, 4)),
                Integer.parseInt(hour.substring(5, 7)) - 1, Integer.parseInt(hour.substring(8, 10)),
                Integer.parseInt(hour.substring(11, 13)), 0, 0);
        calendar.add(Calendar.HOUR, hourDif);
        return getDateTime(calendar).substring(0, 13);
    }



    public static String getDay(String day, int dayDif) {
        Calendar calendar = new GregorianCalendar(Integer.parseInt(day.substring(0, 4)),
                Integer.parseInt(day.substring(5, 7)) - 1, Integer.parseInt(day.substring(8, 10)),
                0, 0, 0);
        calendar.add(Calendar.DAY_OF_MONTH, dayDif);
        return getDateTime(calendar).substring(0, 10);
    }



    public static String getMonth(String day, int monthDif) {
        Calendar calendar = new GregorianCalendar(Integer.parseInt(day.substring(0, 4)),
                Integer.parseInt(day.substring(5, 7)) - 1, 0, 0, 0, 0);
        calendar.add(Calendar.MONTH, monthDif);
        return getDateTime(calendar).substring(0, 7);
    }


    public static String getStartDayWeek(String day) {
        if (day != null && !"".equals(day)) {
            return getFirstDayOfWeek(Integer.parseInt(day.substring(0, 4)), getWeekByDate(day));
        }
        return "";
    }


    public static String getFirstDayOfWeek(int year, int week) {
        Calendar firDay = Calendar.getInstance();
        firDay.set(Calendar.YEAR, year);
        firDay.set(Calendar.WEEK_OF_YEAR, week);
        firDay.set(Calendar.DAY_OF_WEEK, 2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDay = sdf.format(firDay.getTime());
        return firstDay;
    }

    public static String getLastDayOfWeek(int year, int week) {
        Calendar lasDay = Calendar.getInstance();
        lasDay.set(Calendar.YEAR, year);
        lasDay.set(Calendar.WEEK_OF_YEAR, week + 1);
        lasDay.set(Calendar.DAY_OF_WEEK, 2);
        // lasDay.add(Calendar.DAY_OF_WEEK, 6);
        lasDay.roll(Calendar.DAY_OF_WEEK, 6);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDay = sdf.format(lasDay.getTime());
        return lastDay;
    }

    public static int getWeekByDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        int week = 0;
        try {
            d = sdf.parse(date);
            Calendar c = Calendar.getInstance();
            c.setFirstDayOfWeek(Calendar.MONDAY);
            c.setTime(d);
            week = c.get(Calendar.WEEK_OF_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return week;
    }

    /**
     * 
     * @param format
     * @param dateStr
     */
    public static java.util.Date toUtilDate(String format, String dateStr) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        java.util.Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
        }
        return date;
    }

    /**
     * 
     * @param calendar
     * @return YYYY-MM-DD HH:MM:DD
     */
    private static String getDateTime(Calendar calendar) {
        StringBuffer buf = new StringBuffer("");

        buf.append(calendar.get(Calendar.YEAR));
        buf.append(DAY_DELIMITER);
        buf.append(calendar.get(Calendar.MONTH) + 1 > 9 ? calendar.get(Calendar.MONTH) + 1 + ""
                : "0" + (calendar.get(Calendar.MONTH) + 1));
        buf.append(DAY_DELIMITER);
        buf.append(
                calendar.get(Calendar.DAY_OF_MONTH) > 9 ? calendar.get(Calendar.DAY_OF_MONTH) + ""
                        : "0" + calendar.get(Calendar.DAY_OF_MONTH));
        buf.append(" ");
        buf.append(calendar.get(Calendar.HOUR_OF_DAY) > 9 ? calendar.get(Calendar.HOUR_OF_DAY) + ""
                : "0" + calendar.get(Calendar.HOUR_OF_DAY));
        buf.append(":");
        buf.append(calendar.get(Calendar.MINUTE) > 9 ? calendar.get(Calendar.MINUTE) + ""
                : "0" + calendar.get(Calendar.MINUTE));
        buf.append(":");
        buf.append(calendar.get(Calendar.SECOND) > 9 ? calendar.get(Calendar.SECOND) + ""
                : "0" + calendar.get(Calendar.SECOND));
        return buf.toString();
    }

    public static int getMinute() {
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        return minute;
    }

    public static int getHour() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    public static int getDayOfWeek() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        return day;
    }

    public static int getDayOfMonth() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 
     * @param datetime YYYY-MM-DD HH:MM:SS
     * @param type YEAR,MONTH,DAY,HOUR,MINUTE,SECOND
     * @param step
     */
    public static String getPreDateTime(String datetime, int type, int step) {
        Calendar calendar = new GregorianCalendar(Integer.parseInt(datetime.substring(0, 4)),
                Integer.parseInt(datetime.substring(5, 7)) - 1,
                Integer.parseInt(datetime.substring(8, 10)),
                Integer.parseInt(datetime.substring(11, 13)),
                Integer.parseInt(datetime.substring(14, 16)),
                Integer.parseInt(datetime.substring(17, 19)));
        calendar.add(type, step);
        return getDateTime(calendar);
    }

    /**
     * 
     * @param date
     * @return
     */
    public static String getTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(FORMAT2);
        return dateFormat.format(date);
    }


    public static String getLastDayOfMonth(String yearMonth) {
        Calendar calendar = new GregorianCalendar(Integer.parseInt(yearMonth.substring(0, 4)),
                Integer.parseInt(yearMonth.substring(5, 7)), 0, 0, 0, 0);
        final int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (lastDay < 10) {
            return "0" + lastDay;
        }
        return String.valueOf(lastDay);
    }

    /**
     * 字符串时间格式转换成时间戳
     * 
     * @throws ParseException
     */
    public static String getTimeStr(String time) {
        String timeStr = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeStr = String.valueOf(date.getTime());
        return timeStr;
    }

    /**
     * 时间戳转换成字符串类的时间格式
     * 
     * @param time
     * @return
     */
    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(time);
        return d;
    }

    public static String addDateByDay(String date, int days) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1;
        long diff = 0;
        try {
            d1 = df.parse(date);
            diff = d1.getTime() + days * 24 * 60 * 60 * 1000L;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getTime(diff);
    }

    /**
     * 取当前时间
     * 
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(new Date());
        return d;
    }

    /**
     * 取当前时间
     * 
     * @return
     */
    public static String getCurrentTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String d = format.format(new Date());
        return d;
    }

    public static String getCurrentTimeMills() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String d = format.format(new Date());
        return d;
    }

    public static String getFormattedTime(String dateStr, String orginalFormat,
            String targetFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(orginalFormat);
        Date date;
        try {
            date = simpleDateFormat.parse(dateStr);
            simpleDateFormat = new SimpleDateFormat(targetFormat);
            String formattedTime = simpleDateFormat.format(date);
            return formattedTime;
        } catch (ParseException e) {
        }
        return dateStr;
    }

    public static long getFormattedTimeStamp(String dateStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date;
        try {
            date = simpleDateFormat.parse(dateStr);
            return date.getTime();
        } catch (ParseException e) {
        }
        return 0;
    }

    public static String formatTime(long time, String format) {
        return DateFormatUtils.format(time, format);
    }

    public static String getStcOssTime(long time) {
        return DateFormatUtils.format(time, "yyyyMMddHHmmss");
    }

    public static void main(String[] args) {
        /*
         * System.out.println(getTime(new Date())); String currentTime = DateFormatUtils.format(new
         * Date(), "yyyy-MM-dd HH:mm:ss"); System.out.println(currentTime.substring(0, 10));
         */
    
        System.out.println(getCurrentTime());
    }

}
