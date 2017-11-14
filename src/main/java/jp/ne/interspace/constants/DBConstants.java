package jp.ne.interspace.constants;

public final class DBConstants {

	private DBConstants() {
	}

	/**
	 * Define the type of conditions to be applied.
	 * 
	 * @author Joydeep Dey
	 *
	 */
	public enum Operations {
		/** DB [=] operator */
		EQUAL,

		/** DB [>=] operator */
		GREATER_THAN_EQUAL,

		/** DB [<=] operator */
		LESS_THAN_EQUAL,

		/** DB [LIKE] operator */
		LIKE
	}

	public static final String PATHS_DATA_FILE = "/data/data_main.json";

	// MongoDB Field Names
	public static final String FIELD_ID = "_id";
	public static final String FIELD_CLIENT_ID = "client_identifier";
	public static final String FIELD_BTC_ADDR = "btc_address";
	public static final String FIELD_COUNTRY_CODE = "country_code";
	public static final String FIELD_TRANSACTIONS = "transactions";
	public static final String FIELD_ITEM = "item";
	public static final String FIELD_PAYMENT = "payment";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_AVATAR = "avatar";

	// MongoDB Nested Field Notation
	public static final String NESTED_FIELDS_TRANSACTIONS_ITEM = "transactions.item";
	public static final String NESTED_FIELDS_TRANSACTIONS_PAYMENT = "transactions.payment";
	public static final String NESTED_FIELDS_TRANSACTIONS_DATE = "transactions.date";


}
