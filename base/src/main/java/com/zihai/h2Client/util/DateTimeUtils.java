package com.zihai.h2Client.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class DateTimeUtils {
	public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static void main(String[] args) {

		System.out.println("============返回当前的日期=============");
		System.out.println(getCurrentLocalDate());
		System.out.println("============返回当前日期时间=============");
		System.out.println(getCurrentLocalDateTime());
		System.out.println("============返回当前日期字符串 yyyyMMdd=============");
		System.out.println(getCurrentDateStr());
		System.out.println("============返回当前日期时间字符串 yyyyMMddHHmmss=============");
		System.out.println(getCurrentDateTimeStr());
		System.out.println("============日期相隔天数=============");
		LocalDate startDateInclusive = LocalDate.of(2019, 06, 04);
		LocalDate endDateExclusive = LocalDate.of(2019, 06, 05);
		System.out.println(periodDays(startDateInclusive, endDateExclusive));
		System.out.println("============日期相隔小时=============");

		String dt = "2019年06月04日";
		String dt2 = "2019年06月05日";
		SimpleDateFormat sd = new SimpleDateFormat("yyyy年MM月dd日");
		try {
			Instant start = sd.parse(dt).toInstant();
			Instant end = sd.parse(dt2).toInstant();
			System.out.println(durationHours(start, end));
			System.out.println("============日期相隔分钟=============");
			System.out.println(durationMinutes(start, end));
			System.out.println("============日期相隔毫秒数=============");
			System.out.println(durationMillis(start, end));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("============是否当天=============");
		LocalDate today = LocalDate.of(2019, 06, 05);
		System.out.println(isToday(today));
		System.out.println("============获取本月的第一天=============");
		System.out.println(getFirstDayOfThisMonth());
		System.out.println("============获取本月的最后一天=============");
		System.out.println(getLastDayOfThisMonth());
		System.out.println("============获取2017-01的第一个周一=============");
		System.out.println(getFirstMonday());
		System.out.println("============获取当前日期的后两周=============");
		System.out.println(getCurDateAfterTwoWeek());
		System.out.println("============获取当前日期的6个月后的日期=============");
		System.out.println(getCurDateAfterSixMonth());
		System.out.println("============获取当前日期的5年后的日期=============");
		System.out.println(getCurDateAfterFiveYear());
		System.out.println("============获取当前日期的20年后的日期=============");
		System.out.println(getCurDateAfterTwentyYear());

		//测试 是否支持科令夏令时
		//美国纽约时区为例
		ZoneId zoneId = ZoneId.of("America/New_York");
		ZoneId zoneId2 = ZoneId.of("Asia/Shanghai");

		System.out.println(LocalDateTime.now(zoneId).with(LocalTime.MIN)); //begint
		System.out.println(LocalDateTime.now(zoneId).with(LocalTime.MAX)); //begint
		//获取当前时区当前时间
		LocalDateTime localDateTime1 = LocalDateTime.parse("2021-07-06 15:10:10", DATETIME_FORMATTER);
		LocalDateTime localDateTime2 = LocalDateTime.parse("2021-01-06 15:10:10", DATETIME_FORMATTER);

		System.out.println(localDateTime1.atZone(zoneId2).withZoneSameInstant(zoneId).toLocalDateTime());
		System.out.println(localDateTime2.atZone(zoneId2).withZoneSameInstant(zoneId).toLocalDateTime());
	}

	/**
	 * 返回当前的日期
	 *
	 * @return
	 */
	public static LocalDate getCurrentLocalDate() {
		return LocalDate.now();
	}

	/**
	 * 返回当前日期时间
	 *
	 * @return
	 */
	public static LocalDateTime getCurrentLocalDateTime() {
		return LocalDateTime.now();
	}

	/**
	 * 返回当前日期字符串 yyyyMMdd
	 *
	 * @return
	 */
	public static String getCurrentDateStr() {
		return LocalDate.now().format(DATE_FORMATTER);
	}

	/**
	 * 返回当前日期时间字符串 yyyyMMddHHmmss
	 *
	 * @return
	 */
	public static String getCurrentDateTimeStr() {
		return LocalDateTime.now().format(DATETIME_FORMATTER);
	}

	public static LocalDate parseLocalDate(String dateStr, String pattern) {
		return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
	}

	public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
		return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 日期相隔天数
	 *
	 * @param startDateInclusive
	 * @param endDateExclusive
	 * @return
	 */
	public static int periodDays(LocalDate startDateInclusive, LocalDate endDateExclusive) {
		return Period.between(startDateInclusive, endDateExclusive).getDays();
	}

	/**
	 * 日期相隔小时
	 *
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long durationHours(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toHours();
	}

	/**
	 * 日期相隔分钟
	 *
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long durationMinutes(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toMinutes();
	}

	/**
	 * 日期相隔毫秒数
	 *
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long durationMillis(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toMillis();
	}

	/**
	 * 日期相隔秒数
	 *
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long durationSecond(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toMillis() / 1000;
	}

	/**
	 * 是否当天
	 *
	 * @param date
	 * @return
	 */
	public static boolean isToday(LocalDate date) {
		return getCurrentLocalDate().equals(date);
	}

	/**
	 * 获取本月的第一天
	 *
	 * @return
	 */
	public static String getFirstDayOfThisMonth() {
		return getCurrentLocalDate().with(TemporalAdjusters.firstDayOfMonth()).format(DATE_FORMATTER);
	}

	/**
	 * 获取本月的最后一天
	 *
	 * @return
	 */
	public static String getLastDayOfThisMonth() {
		return getCurrentLocalDate().with(TemporalAdjusters.lastDayOfMonth()).format(DATE_FORMATTER);
	}

	/**
	 * 获取2017-01的第一个周一
	 *
	 * @return
	 */
	public static String getFirstMonday() {
		return LocalDate.parse("2017-01-01").with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))
				.format(DATE_FORMATTER);
	}

	/**
	 * 获取当前日期的后两周
	 *
	 * @return
	 */
	public static String getCurDateAfterTwoWeek() {
		return getCurrentLocalDate().plus(2, ChronoUnit.WEEKS).format(DATE_FORMATTER);
	}

	/**
	 * 获取当前日期的6个月后的日期
	 *
	 * @return
	 */
	public static String getCurDateAfterSixMonth() {
		return getCurrentLocalDate().plus(6, ChronoUnit.MONTHS).format(DATE_FORMATTER);
	}

	/**
	 * 获取当前日期的5年后的日期
	 *
	 * @return
	 */
	public static String getCurDateAfterFiveYear() {
		return getCurrentLocalDate().plus(5, ChronoUnit.YEARS).format(DATE_FORMATTER);
	}

	/**
	 * 获取当前日期的20年后的日期
	 *
	 * @return
	 */
	public static String getCurDateAfterTwentyYear() {
		return getCurrentLocalDate().plus(2, ChronoUnit.DECADES).format(DATE_FORMATTER);
	}

	/**
	 * 字符串转时间
	 *
	 * @return
	 */
	public static LocalDate stringToDate(String time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(time, formatter);
	}
	/**
	 * LocalDate转Date
	 * @param localDate
	 * @return
	 */
	public static Date localDate2Date(LocalDate localDate) {
		if(null == localDate) {
			return null;
		}
		ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
		return Date.from(zonedDateTime.toInstant());
	}
	/** LocalDateTime 转 Long */
	public static Long localDateTimeToLong(LocalDateTime localDateTime){
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	/** Long 转 LocalDateTime */
	public static LocalDateTime longToLocalDateTime(Long timestamp){
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
	}

	/** LocalDateTime 转 Date */
	public static Date dateTOLocalDateTime(LocalDateTime localDateTime){
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	/** Date 转 LocalDateTime */
	public static LocalDateTime dateTOLocalDateTime(Date date){
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}


}
