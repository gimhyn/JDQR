package com.example.backend.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class TimeUtil {

  private final static ZoneId timeZoneUTC = ZoneId.of("UTC");
  private final static ZoneId timeZoneSeoul = ZoneId.of("Asia/Seoul");

  public static long getTimeStampFrom(LocalDateTime localDateTime){
    return localDateTime.atZone(timeZoneSeoul).toInstant().toEpochMilli();
  }

  public static LocalDate getLocalDateFrom(long epochMillis){
    Instant instant = Instant.ofEpochMilli(epochMillis);
    return instant.atZone(timeZoneSeoul).toLocalDate();
  }

  public static long getCurrentTimeMillisUtc() {
    LocalDateTime localDateTime = LocalDateTime.now();
    return getTimeStampFrom(localDateTime);
  }

  public static long convertToEpochTime(String dobString) {
    // Parse the input string using the specified format (yyMMdd)
    LocalDate dob = LocalDate.parse(dobString, DateTimeFormatter.ofPattern("yyMMdd"));

    // Convert the LocalDate to UTC-0 epoch time in milliseconds
    return dob.atStartOfDay().atZone(timeZoneUTC).toInstant().toEpochMilli();
  }

  public static String convertEpochToDateString(long epochMillis, String pattern) {
    LocalDate localDate = Instant.ofEpochMilli(epochMillis).atZone(timeZoneSeoul).toLocalDate();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    return localDate.format(formatter);
  }

  public static int calculateAge(long dobEpochMillis) {
    Instant dobInstant = Instant.ofEpochMilli(dobEpochMillis);
    LocalDate dobDate = dobInstant.atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate currentDate = LocalDate.now();

    int age = currentDate.getYear() - dobDate.getYear();

    if (currentDate.getMonthValue() < dobDate.getMonthValue()
      || (currentDate.getMonthValue() == dobDate.getMonthValue() && currentDate.getDayOfMonth() < dobDate.getDayOfMonth())) {
      age--;
    }
    return age;
  }

  public static long toStartOfDaySeoul(LocalDate localDate) {
    return getTimeStampFrom(localDate.atStartOfDay(timeZoneSeoul).toLocalDateTime());
  }

  public static long toStartOfNextDaySeoul(LocalDate localDate) {
    return getTimeStampFrom(localDate.atStartOfDay(timeZoneSeoul).plusDays(1).toLocalDateTime());
  }

}
