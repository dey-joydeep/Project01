package jp.ne.interspace.service;

import jp.ne.interspace.constants.DBConstants;
import jp.ne.interspace.mongo.service.DBService;
import jp.ne.interspace.mongo.service.DBServiceImpl;

public class DataControlServiceImpl implements DataControlService {

	private DBService _dbService;

	public DataControlServiceImpl() {
		_dbService = new DBServiceImpl();
	}

	public void storeData() {
		String datapath = this.getClass().getResource(DBConstants.PATHS_DATA_FILE).getFile();
		_dbService.insert(datapath);
	}

	public void removeData(String id) {
		// Implement this method if ID based single deletion is required
	}

	public void removeAllData() {
		_dbService.truncate();
	}
}
