package com.trading.nikhil;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

public class Test {

	private static int MARKET_START_HOUR = 9;
	private static int MARKET_START_MIN = 00;
	private static int MARKET_END_HOUR = 15;
	private static int MARKET_END_MIN = 40;

	public static void main(String[] args) {
		Test t = new Test();
		LocalDateTime now = LocalDateTime.now();
		/*
		 * String str = "2024-12-01 12:30"; DateTimeFormatter formatter =
		 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); LocalDateTime now =
		 * LocalDateTime.parse(str, formatter);
		 */
		
		System.out.println(now);
		t.isMarketHours(now);
	}

	public void isMarketHours(LocalDateTime ld) {

		int hour = ld.getHour();
		int min = ld.getMinute();

		if (!isWeekend(ld) && hour >= MARKET_START_HOUR && min >= MARKET_START_MIN && hour <= MARKET_END_HOUR
				&& min <= MARKET_END_MIN) {
			System.out.println("MARKET OPEN");
		} else {
			System.out.println("MARKET CLOSED");
		}
		// if (!isWeekend(ld) && (hour >= 9) && (hour <= 16)

	}

	public static boolean isWeekend(final LocalDateTime ld) {
		DayOfWeek day = DayOfWeek.of(ld.get(ChronoField.DAY_OF_WEEK));
		return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
	}

}
