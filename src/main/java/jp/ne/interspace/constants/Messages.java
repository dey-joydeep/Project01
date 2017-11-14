package jp.ne.interspace.constants;

/**
 * Messages to displayed for Info, Error and Debug and Others
 *
 * @author Joydeep Dey
 *
 */
public class Messages {
	private Messages() {
	}

	// Error messages
	/** mongodb.properties file load problem */
	public static final String ERR_PROPERTY_NOT_FOUND = "Failed to load database configuratiion.";

	/** MongoDB connection fail message */
	public static final String ERR_CONNECTION_FAILED = "Failed to load database configuratiion.";

	/** Insert data load fail message */
	public static final String ERR_DATA_INSERT_FAILED = "Failed to insert data into document.";

	/** Insert data load fail message */
	public static final String ERR_INVALID_DATE_FORMAT = "Invalid date format specified. Please enter date in YYYY-MM-DD format";

	// Info messages
	/** MongoDB connection success message */
	public static final String INFO_CONNECTION_SUCCESS = "A connection is establised successfully";

	/** MongoDB connection closed message */
	public static final String INFO_CONNECTION_CLOSED = "A connection is closed successfully";

	// User message
	/** No data found message */
	public static final String NO_DATA_FOUND = "No data found.";

	/** No transactions found message */
	public static final String NO_TRANSACTIONS_FOUND = "No transaction details found.";
}
