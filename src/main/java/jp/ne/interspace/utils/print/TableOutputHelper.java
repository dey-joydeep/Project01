package jp.ne.interspace.utils.print;

import lombok.Getter;
import lombok.Setter;

/**
 * A helper class for {@link PrintUtil} to generate the final formatting
 * pattern.
 * 
 * @author Joydeep
 *
 */
public class TableOutputHelper {

	@Getter
	@Setter
	private String[] headers;

	@Setter
	private int[] widths;

	public String getPattern() {
		String pattern = "";
		for (int i = 0; i < widths.length; i++) {
			pattern += "%" + widths[i] + "s";
			if (i < widths.length - 1)
				pattern += "%2s";
		}
		return pattern;
	}

	public int separatorLength() {
		int length = 0;
		for (int i = 0; i < widths.length; i++) {
			length += widths[i];
			if (i < widths.length - 1)
				length += 2;
		}
		return length;
	}
}
