package com.azilen.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class DateUtil {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

	public String formatDate(Date date) {
		return dateFormat.format(date);
	}

	public Calendar parseStringToCalender(String date) {
		try {
			dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateFormat.getCalendar();
	}

}
