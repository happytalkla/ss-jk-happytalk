package ht.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;

public class DateUtil {

	/**
	 * 현재 날짜 format에 맞게 리턴
	 *
	 * @param format
	 * @return
	 */
	public static String getCurrentDate(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * 현재 날짜 리턴
	 *
	 * @return
	 */
	public static String getCurrentDate() {
		String format = "yyyy-MM-dd";
		// String format = "yyyy-MM-dd HH:mm:ss";
		return getCurrentDate(format);
	}

	public static String getDay(Timestamp time) {

		if (time == null) {
			return "";
		}

		DateTime dateTime = new DateTime(time.getTime());
		return "" + dateTime.getDayOfMonth();
	}

	public static final String WEEKDAY_NAME[] = { "일", "월", "화", "수", "목", "금", "토", "일" };

	public static String getWeekdayName(Timestamp time) {

		if (time == null) {
			return "";
		}

		DateTime dateTime = new DateTime(time.getTime());
		return WEEKDAY_NAME[dateTime.getDayOfWeek()];
	}
}
