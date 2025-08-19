package ht.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class TimeService {

	public int expire_second = 600;

	// 인증 시간 리턴
	public String expireDate() throws Exception {

		Date date = new Date();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Java 시간 더하기
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, this.expire_second);

		return sdformat.format(cal.getTime());
	}

	// 인증 코드 유효 시간 리턴 (2분)
	public String expireAuthCodeDate() throws Exception {

		Date date = new Date();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Java 시간 더하기
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, 120);

		return sdformat.format(cal.getTime());
	}
	
	// 캐시 코드 유효시간
	public String expireCacheTime() throws Exception {

		Date date = new Date();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Java 시간 더하기
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, -60);

		return sdformat.format(cal.getTime());
	}

	// 인증 시간 리턴
	public String expireToDate() throws Exception {

		Date date = new Date();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Java 시간 더하기
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return sdformat.format(cal.getTime());
	}

	// 인증 시간 리턴
	public String expireDate(int second) throws Exception {

		Date date = new Date();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Java 시간 더하기
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, second);

		return sdformat.format(cal.getTime());
	}
}