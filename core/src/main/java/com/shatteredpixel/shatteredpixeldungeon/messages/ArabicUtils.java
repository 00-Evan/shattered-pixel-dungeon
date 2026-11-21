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
	// Isolated:    +0
	// End:         +1 (leftmost, remember we read right-to-left!)
	// Start:       +2 (rightmost)
	// Middle:      +3
	private static final SparseArray<Character> ARMap = new SparseArray<>();
	static {
		ARMap.put(0x0622, (char) 0xFE81);
		ARMap.put(0x0623, (char) 0xFE83);
		ARMap.put(0x0624, (char) 0xFE85);
		ARMap.put(0x0625, (char) 0xFE87);
		ARMap.put(0x0626, (char) 0xFE89);
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

	//these chars only have transformations for being at the end of a sequence
	private static final ArrayList<Character> finalOnly = new ArrayList<>();
	static {
		finalOnly.add((char) 0x0622);
		finalOnly.add((char) 0x0623);
		finalOnly.add((char) 0x0624);
		finalOnly.add((char) 0x0625);
		finalOnly.add((char) 0x0627);
		finalOnly.add((char) 0x0629);
		finalOnly.add((char) 0x062F);
		finalOnly.add((char) 0x0630);
		finalOnly.add((char) 0x0631);
		finalOnly.add((char) 0x0632);
		finalOnly.add((char) 0x0648);
		finalOnly.add((char) 0x0649);
	}

	//the four variants of the 'Alef' symbol form ligatures with 'Lam' if they are after it
	//Alef is also final only
	private static final char LAM = 0x0644;
	private static final SparseArray<Character> AlefLamLigatures = new SparseArray<>();
	static {
		AlefLamLigatures.put(0x0622, (char) 0xFEF5);
		AlefLamLigatures.put(0x0623, (char) 0xFEF7);
		AlefLamLigatures.put(0x0625, (char) 0xFEF9);
		AlefLamLigatures.put(0x0627, (char) 0xFEFB);
	}

	//These symbols are meant to sit between two other chars, and so should be skipped when determining combining
	private static boolean isZeroWidthSymbol(char c){
		return c >= 0x064B && c <= 0x065F;
	}

	private static boolean splitOn( char c ){
		return Character.isWhitespace(c) || c == ')' || c == '(';
	}

	public static String convertTextBlockForDisplay( String input ){
		StringBuilder result = new StringBuilder();

		//TODO when some English is present it gets reversed too, should see if we can fix that (creates issues with line-breaks though)
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (splitOn(c)) {
				//swap ( and )
				if (c == '('){
					result.append(')');
				} else if (c == ')'){
					result.append('(');
				} else {
					result.append(c);
				}

			} else {

				boolean arabicWord = false;
				int j = i + 1;
				while (j < input.length() && !splitOn(input.charAt(j))) {
					if (Character.UnicodeBlock.of(input.charAt(j)) == Character.UnicodeBlock.ARABIC) {
						arabicWord = true;
					}
					j++;
				}

				//for arabic words, we need to invert character order and also handle combining forms
				if (arabicWord) {

					int resultEnd = result.length();

					for (int k = i; k < j; k++) {
						c = input.charAt(k);

						if (ARMap.containsKey(c)) {

							//these chars aren't joinable to the left, so they can only 'end' a sequence
							//we need to check whether they're isolated or can join with a letter to their right
							if (finalOnly.contains(c)) {

								//Alef specifically forms ligatures with Lam, so we need to handle that
								if (AlefLamLigatures.containsKey(c) && k != i && input.charAt(k-1) == LAM){
									c = AlefLamLigatures.get(c);

									//Lam wasn't added, so we need to check the symbol before it
									if (k != i+1
											&& ARMap.containsKey(getLeftSymbol(input, k-1))
											&& !finalOnly.contains(getLeftSymbol(input, k-1))){
										c += 1;
									}

								//otherwise handle as normal
								} else {
									c = ARMap.get(c);

									if (k != i
											&& ARMap.containsKey(getLeftSymbol(input, k))
											&& !finalOnly.contains(getLeftSymbol(input, k))){
										c += 1;
									}
								}

							//these chars can be isolated, or at the start/middle/end of a sequence
							} else {

								//we're going to form a ligature, so skip inserting this char
								if (c == LAM && k != j-1 && AlefLamLigatures.containsKey(input.charAt(k+1))){
									continue;
								}

								c = ARMap.get(c);
								boolean end = k == j-1 || !ARMap.containsKey(getRightSymbol(input, k));
								boolean start = k == i || !ARMap.containsKey(getLeftSymbol(input, k));

								//need to check if the char to the right is final only
								// final only chars don't join in this direction
								if (!start && finalOnly.contains(getLeftSymbol(input, k))){
									start = true;
								}

								if (end && start){
									//isolated, no change
								} else if (end){
									c += 1; //end (no combining char after)
								} else if (start){
									c += 2; //start (no combining char before)
								} else {
									c += 3; //middle (combining before and after)
								}
							}
						}

						result.insert(resultEnd, c);
					}

				} else {
					//non-Arabic words just get appended
					result.append(input.substring(i, j));
				}

				i = j - 1; //-1 because it's about to be incremented
			}
		}

		return result.toString();
	}

	private static char getLeftSymbol(String input, int idx){
		int i = idx-1;
		while (i >= 0){
			char c = input.charAt(i);
			if (ARMap.containsKey(c)){
				return c;
			} else if (isZeroWidthSymbol(c)){
				i--; //loop again
			} else {
				return 0;
			}
		}
		return 0;
	}

	private static char getRightSymbol(String input, int idx){
		int i = idx+1;
		while (i < input.length()){
			char c = input.charAt(i);
			if (ARMap.containsKey(c)){
				return c;
			} else if (isZeroWidthSymbol(c)){
				i++; //loop again
			} else {
				return 0;
			}
		}
		return 0;
	}

}
