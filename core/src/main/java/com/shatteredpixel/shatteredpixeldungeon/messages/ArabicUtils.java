package com.shatteredpixel.shatteredpixeldungeon.messages;

import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//various functions needed to convert or adjust Arabic text for display
public class ArabicUtils {

	private static Matcher ARMatcher = Pattern.compile("\\p{InArabic}|\\p{InARABIC_PRESENTATION_FORMS_B}").matcher("");

	public static boolean containsARChars(String text){
		return ARMatcher.reset(text).find();
	}

	//Maps Arabic Unicode block chars to their equivalents in Arabic Presentation Forms
	//then we can further increment based on specific form:
	// Isolated: unchanged
	// End of Word: +1      (leftmost, remember we read right-to-left!)
	// Start of Word: +2    (rightmost)
	// Middle of Word: +3
	private static SparseArray<Character> ARMap = new SparseArray<>();
	static {
		ARMap.put(0x0622, (char) 0xFE81);
		ARMap.put(0x0627, (char) 0xFE8D);
		ARMap.put(0x0628, (char) 0xFE8F);
		ARMap.put(0x0629, (char) 0xFE93);
		ARMap.put(0x062A, (char) 0xFE95);
		ARMap.put(0x062B, (char) 0xFE99);
		ARMap.put(0x062C, (char) 0xFE9D);
		ARMap.put(0x062D, (char) 0xFEA1);
		ARMap.put(0x062E, (char) 0xFEA5);
		ARMap.put(0x062F, (char) 0xFEA9);

		ARMap.put(0x0630, (char) 0xFEAB);
		ARMap.put(0x0631, (char) 0xFEAD);
		ARMap.put(0x0632, (char) 0xFEAF);
		ARMap.put(0x0633, (char) 0xFEB1);
		ARMap.put(0x0634, (char) 0xFEB5);
		ARMap.put(0x0635, (char) 0xFEB9);
		ARMap.put(0x0636, (char) 0xFEBD);
		ARMap.put(0x0637, (char) 0xFEC1);
		ARMap.put(0x0638, (char) 0xFEC5);
		ARMap.put(0x0639, (char) 0xFEC9);
		ARMap.put(0x063A, (char) 0xFECD);

		ARMap.put(0x0641, (char) 0xFED1);
		ARMap.put(0x0642, (char) 0xFED5);
		ARMap.put(0x0643, (char) 0xFED9);
		ARMap.put(0x0644, (char) 0xFEDD);
		ARMap.put(0x0645, (char) 0xFEE1);
		ARMap.put(0x0646, (char) 0xFEE5);
		ARMap.put(0x0647, (char) 0xFEE9);
		ARMap.put(0x0648, (char) 0xFEED);
		ARMap.put(0x0649, (char) 0xFEEF);
		ARMap.put(0x064A, (char) 0xFEF1);
	}

	//these chars only have transformations for being at the end of a word
	private static ArrayList<Character> finalOnly = new ArrayList<>();
	static {
		finalOnly.add((char) 0x0627);
		finalOnly.add((char) 0x0629);
		finalOnly.add((char) 0x062F);
		finalOnly.add((char) 0x0630);
		finalOnly.add((char) 0x0631);
		finalOnly.add((char) 0x0632);
		finalOnly.add((char) 0x0648);
		finalOnly.add((char) 0x0649);
	}


	//FIXME this has a lot of issues atm! Needs work! It holds up pretty well for single lines but falls apart on bigger paragraphs
	public static String convertTextBlockForDisplay( String input ){
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			//TODO underscores and punctuation?
			if (Character.isWhitespace(c)) {
				result.insert(0, c);

			} else {

				boolean arabicWord = false;
				int j = i + 1;
				while (j < input.length() && !Character.isWhitespace(input.charAt(j))) {
					if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.ARABIC) {
						arabicWord = true;
						break;
					}
					j++;
				}

				if (arabicWord) {

					j = i + 1;
					while (j < input.length() && !Character.isWhitespace(input.charAt(j))) {
						j++;
					}

					for (int k = i; k < j; k++) {
						c = input.charAt(k);

						//need to modify chars here based on position within a word
						if (j - i > 1) { //don't modify on isolated letters
							//k == j-1 is end (leftmost, going right-to-left!)
							//k == i is start (rightmost)
							//otherwise middle
							//some letter only have alt forms for end state
							if (ARMap.containsKey(c)) {
								if (finalOnly.contains(c)) {
									c = ARMap.get(c);
									if (k == j - 1) {
										c += 1;
									}
								} else {
									c = ARMap.get(c);
									if (k == j - 1) {
										c += 1;
									} else if (k == i) {
										c += 2;
									} else {
										c += 3;
									}
								}
							}
						}

						result.insert(0, c);
					}
					i = j - 1;

				} else {
					j = i + 1;
					while (j < input.length() && !Character.isWhitespace(input.charAt(j))) {
						j++;
					}
					result.insert(0, input.substring(i, j));
					i = j - 1;
				}
			}
		}

		return result.toString();
	}

}
