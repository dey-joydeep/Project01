package jp.ne.interspace.utils;

import java.util.Properties;

import jp.ne.interspace.constants.Messages;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Manipulator class of the mongodb.properties file
 *
 * @author Joydeep Dey
 *
 */
@Slf4j
public class DBProperty {

	private static final String KEY_URL = "db.url";
	private static final String KEY_PORT = "db.port";
	private static final String KEY_DATABASE = "db.database";
	private static final String KEY_COLLECTION = "db.collection";

	@Getter
	private String url;
	@Getter
	private int port;
	@Getter
	private String database;
	@Getter
	private String collection;

	private static DBProperty dbPropertyInstance = null;

	private DBProperty(String propertiesPath) {
		Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream(propertiesPath));
		} catch (Exception e) {
			log.error(Messages.ERR_PROPERTY_NOT_FOUND, e);
		}

		this.url = properties.getProperty(KEY_URL);
		this.port = Integer.parseInt(properties.getProperty(KEY_PORT));
		this.database = properties.getProperty(KEY_DATABASE);
		this.collection = properties.getProperty(KEY_COLLECTION);
	}

	/**
	 * Create an instance of this class
	 *
	 * @return A DbProperty instance
	 */
	public static DBProperty getInstance(String propertiesPath) {
		if (dbPropertyInstance == null)
			dbPropertyInstance = new DBProperty(propertiesPath);
		return dbPropertyInstance;
	}
}
