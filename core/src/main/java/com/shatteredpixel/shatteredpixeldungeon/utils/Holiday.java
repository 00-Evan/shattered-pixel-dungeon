/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public enum Holiday {

	NONE,

	LUNAR_NEW_YEAR,         //Varies, sometime in late Jan to Late Feb              (7 days)
	APRIL_FOOLS,            //April 1st, can override easter                        (1 day)
	EASTER,                 //Varies, sometime in Late Mar to Late Apr              (6-7 days)
	//Nothing in May
	PRIDE,                  //Jun 24th to Jun 30th                                  (7 days)
	//Nothing in Jul
	SHATTEREDPD_BIRTHDAY,   //Aug 1st to Aug 7th                                    (7 days)
	//Nothing in Sept
	HALLOWEEN,              //Oct 24th to Oct 31st                                  (7 days)
	//Nothing in Nov
	PD_BIRTHDAY,            //Dec 1st to Dec 7th                                    (7 days)
	WINTER_HOLIDAYS,        //Dec 15th to Dec 26th                                  (12 days)
	NEW_YEARS;              //Dec 27th to Jan 2nd                                   (7 days)

	//total of 61-62 festive days each year, mainly concentrated in Late Oct to Early Feb

	//we cache the holiday here so that holiday logic doesn't suddenly shut off mid-game
	//this gets cleared on game launch (of course), and whenever leaving a game scene
	private static Holiday cached;
	public static void clearCachedHoliday(){
		cached = null;
	}

	public static Holiday getCurrentHoliday(){
		if (cached == null){
			cached = getHolidayForDate((GregorianCalendar) GregorianCalendar.getInstance());
		}
		return cached;
	}

	//requires a gregorian calendar
	public static Holiday getHolidayForDate(GregorianCalendar cal){

		//Lunar New Year
		if (isLunarNewYear(cal.get(Calendar.YEAR),
				cal.get(Calendar.DAY_OF_YEAR))){
			return LUNAR_NEW_YEAR;
		}

		//April Fools
		if (cal.get(Calendar.MONTH) == Calendar.APRIL
				&& cal.get(Calendar.DAY_OF_MONTH) == 1){
			return APRIL_FOOLS;
		}

		//Easter
		if (isEaster(cal.get(Calendar.YEAR),
				cal.get(Calendar.DAY_OF_YEAR),
				cal.getActualMaximum(Calendar.DAY_OF_YEAR) == 366)){
			return EASTER;
		}

		//Pride
		if (cal.get(Calendar.MONTH) == Calendar.JUNE
				&& cal.get(Calendar.DAY_OF_MONTH) >= 24){
			return PRIDE;
		}

		//Shattered's Birthday
		if (cal.get(Calendar.MONTH) == Calendar.AUGUST
				&& cal.get(Calendar.DAY_OF_MONTH) <= 7){
			return SHATTEREDPD_BIRTHDAY;
		}

		//Halloween
		if (cal.get(Calendar.MONTH) == Calendar.OCTOBER
				&& cal.get(Calendar.DAY_OF_MONTH) >= 24){
			return HALLOWEEN;
		}

		//Pixel Dungeon's Birthday
		if (cal.get(Calendar.MONTH) == Calendar.DECEMBER
				&& cal.get(Calendar.DAY_OF_MONTH) <= 7){
			return PD_BIRTHDAY;
		}

		//Winter Holidays
		if (cal.get(Calendar.MONTH) == Calendar.DECEMBER
				&& cal.get(Calendar.DAY_OF_MONTH) >= 15
				&& cal.get(Calendar.DAY_OF_MONTH) <= 26){
			return WINTER_HOLIDAYS;
		}

		//New Years
		if ((cal.get(Calendar.MONTH) == Calendar.DECEMBER && cal.get(Calendar.DAY_OF_MONTH) >= 27)
				|| (cal.get(Calendar.MONTH) == Calendar.JANUARY && cal.get(Calendar.DAY_OF_MONTH) <= 2)){
			return NEW_YEARS;
		}

		return NONE;
	}

	//has to be hard-coded on a per-year basis =S
	public static boolean isLunarNewYear(int year, int dayOfYear){
		int lunarNewYearDayOfYear;
		switch (year){
			//yes, I really did hardcode this all the way from 2020 to 2100
			default:   lunarNewYearDayOfYear = 31+5; break;     //defaults to February 5th
			case 2020: lunarNewYearDayOfYear = 25; break;       //January 25th
			case 2021: lunarNewYearDayOfYear = 31+12; break;    //February 12th
			case 2022: lunarNewYearDayOfYear = 31+1; break;     //February 1st
			case 2023: lunarNewYearDayOfYear = 22; break;       //January 22nd
			case 2024: lunarNewYearDayOfYear = 31+10; break;    //February 10th
			case 2025: lunarNewYearDayOfYear = 29; break;       //January 29th
			case 2026: lunarNewYearDayOfYear = 31+17; break;    //February 17th
			case 2027: lunarNewYearDayOfYear = 31+6; break;     //February 6th
			case 2028: lunarNewYearDayOfYear = 26; break;       //January 26th
			case 2029: lunarNewYearDayOfYear = 31+13; break;    //February 13th
			case 2030: lunarNewYearDayOfYear = 31+3; break;     //February 3rd
			case 2031: lunarNewYearDayOfYear = 23; break;       //January 23rd
			case 2032: lunarNewYearDayOfYear = 31+11; break;    //February 11th
			case 2033: lunarNewYearDayOfYear = 31; break;       //January 31st
			case 2034: lunarNewYearDayOfYear = 31+19; break;    //February 19th
			case 2035: lunarNewYearDayOfYear = 31+8; break;     //February 8th
			case 2036: lunarNewYearDayOfYear = 28; break;       //January 28th
			case 2037: lunarNewYearDayOfYear = 31+15; break;    //February 15th
			case 2038: lunarNewYearDayOfYear = 31+4; break;     //February 4th
			case 2039: lunarNewYearDayOfYear = 24; break;       //January 24th
			case 2040: lunarNewYearDayOfYear = 31+12; break;    //February 12th
			case 2041: lunarNewYearDayOfYear = 31+1; break;     //February 1st
			case 2042: lunarNewYearDayOfYear = 22; break;       //January 22nd
			case 2043: lunarNewYearDayOfYear = 31+10; break;    //February 10th
			case 2044: lunarNewYearDayOfYear = 30; break;       //January 30th
			case 2045: lunarNewYearDayOfYear = 31+17; break;    //February 17th
			case 2046: lunarNewYearDayOfYear = 31+6; break;     //February 6th
			case 2047: lunarNewYearDayOfYear = 26; break;       //January 26th
			case 2048: lunarNewYearDayOfYear = 31+14; break;    //February 14th
			case 2049: lunarNewYearDayOfYear = 31+2; break;     //February 2nd
			case 2050: lunarNewYearDayOfYear = 23; break;       //January 23rd
			case 2051: lunarNewYearDayOfYear = 31+11; break;    //February 11th
			case 2052: lunarNewYearDayOfYear = 31+1; break;     //February 1st
			case 2053: lunarNewYearDayOfYear = 31+19; break;    //February 19th
			case 2054: lunarNewYearDayOfYear = 31+8; break;     //February 8th
			case 2055: lunarNewYearDayOfYear = 28; break;       //January 28th
			case 2056: lunarNewYearDayOfYear = 31+15; break;    //February 15th
			case 2057: lunarNewYearDayOfYear = 31+4; break;     //February 4th
			case 2058: lunarNewYearDayOfYear = 24; break;       //January 24th
			case 2059: lunarNewYearDayOfYear = 31+12; break;    //February 12th
			case 2060: lunarNewYearDayOfYear = 31+2; break;     //February 2nd
			case 2061: lunarNewYearDayOfYear = 21; break;       //January 21st
			case 2062: lunarNewYearDayOfYear = 31+9; break;     //February 9th
			case 2063: lunarNewYearDayOfYear = 29; break;       //January 29th
			case 2064: lunarNewYearDayOfYear = 31+17; break;    //February 17th
			case 2065: lunarNewYearDayOfYear = 31+5; break;     //February 5th
			case 2066: lunarNewYearDayOfYear = 26; break;       //January 26th
			case 2067: lunarNewYearDayOfYear = 31+14; break;    //February 14th
			case 2068: lunarNewYearDayOfYear = 31+3; break;     //February 3rd
			case 2069: lunarNewYearDayOfYear = 23; break;       //January 23rd
			case 2070: lunarNewYearDayOfYear = 31+11; break;    //February 11th
			case 2071: lunarNewYearDayOfYear = 31; break;       //January 31st
			case 2072: lunarNewYearDayOfYear = 31+19; break;    //February 19th
			case 2073: lunarNewYearDayOfYear = 31+7; break;     //February 7th
			case 2074: lunarNewYearDayOfYear = 27; break;       //January 27th
			case 2075: lunarNewYearDayOfYear = 31+15; break;    //February 15th
			case 2076: lunarNewYearDayOfYear = 31+5; break;     //February 5th
			case 2077: lunarNewYearDayOfYear = 24; break;       //January 24th
			case 2078: lunarNewYearDayOfYear = 31+12; break;    //February 12th
			case 2079: lunarNewYearDayOfYear = 31+2; break;     //February 2nd
			case 2080: lunarNewYearDayOfYear = 22; break;       //January 22nd
			case 2081: lunarNewYearDayOfYear = 31+9; break;     //February 9th
			case 2082: lunarNewYearDayOfYear = 29; break;       //January 29th
			case 2083: lunarNewYearDayOfYear = 31+17; break;    //February 17th
			case 2084: lunarNewYearDayOfYear = 31+6; break;     //February 6th
			case 2085: lunarNewYearDayOfYear = 26; break;       //January 26th
			case 2086: lunarNewYearDayOfYear = 31+14; break;    //February 14th
			case 2087: lunarNewYearDayOfYear = 31+3; break;     //February 3rd
			case 2088: lunarNewYearDayOfYear = 24; break;       //January 24th
			case 2089: lunarNewYearDayOfYear = 31+10; break;    //February 10th
			case 2090: lunarNewYearDayOfYear = 30; break;       //January 30th
			case 2091: lunarNewYearDayOfYear = 31+18; break;    //February 18th
			case 2092: lunarNewYearDayOfYear = 31+7; break;     //February 7th
			case 2093: lunarNewYearDayOfYear = 27; break;       //January 27th
			case 2094: lunarNewYearDayOfYear = 31+15; break;    //February 15th
			case 2095: lunarNewYearDayOfYear = 31+5; break;     //February 5th
			case 2096: lunarNewYearDayOfYear = 25; break;       //January 25th
			case 2097: lunarNewYearDayOfYear = 31+12; break;    //February 12th
			case 2098: lunarNewYearDayOfYear = 31+1; break;     //February 1st
			case 2099: lunarNewYearDayOfYear = 21; break;       //January 21st
			case 2100: lunarNewYearDayOfYear = 31+9; break;     //February 9th
		}

		//celebrate for 7 days total, with Lunar New Year on the 5th day
		return dayOfYear >= lunarNewYearDayOfYear-4 && dayOfYear <= lunarNewYearDayOfYear+2;
	}

	//has to be algorithmically computed =S
	public static boolean isEaster(int year, int dayOfYear, boolean isLeapYear){
		//if we're not in March or April, just skip out of all these calculations
		if (dayOfYear < 59 || dayOfYear > 121) {
			return false;
		}

		//Uses the Anonymous Gregorian Algorithm
		int a = year % 19;
		int b = year / 100;
		int c = year % 100;
		int d = b / 4;
		int e = b % 4;
		int f = (b + 8) / 25;
		int g = (b - f + 1) / 3;
		int h = (a*19 + b - d - g + 15) % 30;
		int i = c / 4;
		int k = c % 4;
		int l = (32 + 2*e + 2*i - h - k) % 7;
		int m = (a + h*11 + l*22)/451;
		int n = (h + l - m*7 + 114) / 31;
		int o = (h + l - m*7 + 114) % 31;

		int easterDayOfYear = 0;

		if (n == 3){
			easterDayOfYear += 59; //march
		} else {
			easterDayOfYear += 90; //april
		}

		if (isLeapYear) {
			easterDayOfYear += 1; //add an extra day to account for February 29th
		}

		easterDayOfYear += (o+1); //add day of month

		//celebrate for 7 days total, with Easter Sunday on the 5th day
		return dayOfYear >= easterDayOfYear-4 && dayOfYear <= easterDayOfYear+2;
	}

}
