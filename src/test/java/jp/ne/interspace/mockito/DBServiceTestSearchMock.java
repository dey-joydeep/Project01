package jp.ne.interspace.mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jp.ne.interspace.constants.DBConstants;
import jp.ne.interspace.model.input.DataModelIn;
import jp.ne.interspace.model.input.TransactionModelIn;
import jp.ne.interspace.model.output.DataModelOut;
import jp.ne.interspace.model.output.ISODate;
import jp.ne.interspace.model.output.TransactionModelOut;
import jp.ne.interspace.mongo.service.DBService;
import jp.ne.interspace.mongo.service.DBServiceImpl;
import jp.ne.interspace.utils.CommonUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class DBServiceTestSearchMock {

	private DBServiceImpl wrapper = null;

	@Mock
	private MongoClient mockClient;
	@Mock
	private MongoDatabase mockDB;

	private MongodExecutable mongodExe;
	private MongodProcess mongod;

	private static final MongodStarter starter = MongodStarter
			.getDefaultInstance();

	/**
	 * Initial setup of the unit test
	 *
	 */
	@Before
	public void init() throws Exception {

		mongodExe = starter.prepare(new MongodConfigBuilder()
				.version(Version.Main.DEVELOPMENT)
				.net(new Net("localhost", 12345, Network.localhostIsIPv6()))
				.build());

		mongod = mongodExe.start();
		mockClient = new MongoClient("localhost", 12345);

		when(mockClient.getDatabase(anyString())).thenReturn(mockDB);
		wrapper = new DBServiceImpl(mockDB);
		MockitoAnnotations.initMocks(this);
		wrapper.insert("/data/main/data_test.json");
	}

	/**
	 * LIKE query test for {@link DBService}.fetchDataWithSingleCondition() with
	 * [transactions.item] field
	 */
	@Test
	public void fetchDataEqualsToTest5() {

		List<DataModelOut> list = wrapper.fetchDataWithSingleCondition(
				DBConstants.NESTED_FIELDS_TRANSACTIONS_ITEM, "Zap",
				DBConstants.Operations.LIKE);
		Assert.assertNotNull("List should not be null.", list);
		Assert.assertTrue("List should not be empty.", list.size() > 0);
		Assert.assertEquals("Fetched record size should be 2", 2, list.size());
		Assert.assertEquals(prepareTargetDataForEqualTest(), list);
	}

	@After
	public void closeAll() {
		wrapper.truncate();
		mongod.stop();
		mongodExe.stop();
	}

	private List<DataModelOut> prepareTargetDataForEqualTest() {
		try {
			String json = CommonUtil.readTextFile(this.getClass()
					.getResource("/data/result/test_result_01.json").getFile());
			Gson gson = new Gson();
			Type listType = new TypeToken<List<DataModelIn>>() {
			}.getType();
			List<DataModelIn> dataInList = gson.fromJson(json, listType);
			List<DataModelOut> dataOutList = new ArrayList<>();
			dataInList.forEach(in -> {
				DataModelOut out = new DataModelOut();
				out.setId(in.getId());
				out.setClientIdentifier(in.getClientIdentifier());
				out.setBtcAddress(in.getBtcAddress());
				out.setCountryCode(in.getCountryCode());
				out.setAvatar(in.getAvatar());
				List<TransactionModelIn> tInList = in.getTransactions();
				if (tInList != null) {
					List<TransactionModelOut> tOutList = new ArrayList<>();
					tInList.forEach(tIn -> {
						TransactionModelOut tOut = new TransactionModelOut();
						tOut.setItem(tIn.getItem());
						tOut.setPayment(tIn.getPayment());
						tOut.setDate(new ISODate(tIn.getDate().getTime()));
						tOutList.add(tOut);
					});
					out.setTransactions(tOutList);
				}
				dataOutList.add(out);
			});
			return dataOutList;
		} catch (IOException e) {
		}
		return null;
	}
}
