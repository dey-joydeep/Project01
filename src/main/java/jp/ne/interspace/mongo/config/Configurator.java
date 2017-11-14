package jp.ne.interspace.mongo.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import jp.ne.interspace.constants.Commons;
import jp.ne.interspace.constants.Messages;
import jp.ne.interspace.utils.DBProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for MongoDB connection
 *
 * @author Joydeep Dey
 *
 */
@Slf4j
public class Configurator {

	private static MongoClient _client = null;
	private static DBProperty _dbProperty = null;

	private Configurator() {
	}

	/**
	 * Open a connection to MongoDB with the parameters specified in the
	 * mongodb.properties file
	 */
	public static void openConnection() {
		if (_client == null) {
			_dbProperty = DBProperty.getInstance(Commons.DB_PROPERTIES_FILE_PATH);
			try {
				_client = new MongoClient(_dbProperty.getUrl(), _dbProperty.getPort());
				log.info(Messages.INFO_CONNECTION_SUCCESS);
			} catch (Exception e) {
				log.error(Messages.ERR_CONNECTION_FAILED, e);
				System.exit(-1);
			}
		}
	}

	/**
	 * Load the database, specified in the mongodb.properties file
	 *
	 * @return MongoDB database
	 */
	public static MongoDatabase getDatabase() {

		return _client.getDatabase(_dbProperty.getDatabase());
	}

	/**
	 * Terminate the open DB connection
	 */
	public static void terminate() {
		if (_client != null) {
			_client.close();
			_client = null;
			log.info(Messages.INFO_CONNECTION_CLOSED);
		}
	}
}
