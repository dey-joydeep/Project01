package jp.ne.interspace.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.ne.interspace.constants.Commons;

/**
 * General purpose utility class
 *
 * @author Joydeep Dey
 *
 */
public class CommonUtil {
	private CommonUtil() {
	}

	/**
	 * Read a text file from the given path a get the content as a string
	 *
	 * @param filepath
	 *            the path of the file
	 * @return file content
	 * @throws IOException
	 */
	public static String readTextFile(String filepath) throws IOException {
		Path path = Paths.get(new File(filepath).toURI());
		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		StringBuilder lineBuilder = new StringBuilder();
		lines.forEach(lineBuilder::append);

		return lineBuilder.toString();
	}

	/**
	 * Convert String date to {@link Date} format
	 *
	 * @param strDate
	 *            A string in YYYY-MM-DD format
	 * @return Date representation of the given string
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(Commons.INPUT_DATE_FORMAT);
		return sdf.parse(strDate);
	}
}
