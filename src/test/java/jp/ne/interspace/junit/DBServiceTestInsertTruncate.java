package jp.ne.interspace.junit;

import jp.ne.interspace.mongo.config.Configurator;
import jp.ne.interspace.mongo.service.DBServiceImpl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DBServiceTestInsertTruncate {

	private DBServiceImpl _dbServiceImpl;

	@Before
	public void init() {
		Configurator.openConnection();
		_dbServiceImpl = new DBServiceImpl();
	}

	public void testInsert() {
		String datapath = this
				.getClass()
				.getResource(
						"/data/main/btc_transactions_20170401_20170630.json")
				.getFile();
		_dbServiceImpl.insert(datapath);
		long count = _dbServiceImpl.getRecordCount();
		Assert.assertEquals(1000, count);
	}

	@Test
	public void testTruncate() {
		_dbServiceImpl.truncate();
		long count = _dbServiceImpl.getRecordCount();
		Assert.assertEquals(0, count);
	}

	@After
	public void closeAll() {
		Configurator.terminate();
	}
}