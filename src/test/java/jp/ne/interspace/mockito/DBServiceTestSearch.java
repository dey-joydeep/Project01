package jp.ne.interspace.mockito;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.ne.interspace.constants.DBConstants;
import jp.ne.interspace.model.input.DataModelIn;
import jp.ne.interspace.model.input.TransactionModelIn;
import jp.ne.interspace.model.output.DataModelOut;
import jp.ne.interspace.model.output.ISODate;
import jp.ne.interspace.model.output.TransactionModelOut;
import jp.ne.interspace.mongo.config.Configurator;
import jp.ne.interspace.mongo.service.DBService;
import jp.ne.interspace.mongo.service.DBServiceImpl;
import jp.ne.interspace.utils.CommonUtil;
import jp.ne.interspace.utils.DBProperty;
import jp.ne.interspace.utils.print.PrintUtil;
import junit.framework.TestCase;

import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class DBServiceTestSearch extends TestCase {

	private DBServiceImpl dbServiceImpl = null;

	private MongodExecutable mongodExe;
	private MongodProcess mongod;
	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	private MongoCollection<Document> collection;

	private static final MongodStarter starter = MongodStarter
			.getDefaultInstance();

	/**
	 * Initial setup of the unit test
	 *
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public void setUp() throws Exception {

		mongodExe = starter.prepare(new MongodConfigBuilder()
				.version(Version.Main.DEVELOPMENT)
				.net(new Net("localhost", 12345, Network.localhostIsIPv6()))
				.build());
		mongod = mongodExe.start();
		super.setUp();
		mongoClient = new MongoClient("localhost", 12345);

		Configurator.openConnection();
		dbServiceImpl = new DBServiceImpl();
		long count = dbServiceImpl.getRecordCount();
		if (count == 0) {
			String datapath = this.getClass()
					.getResource("/data/main/data_test.json").getFile();
			dbServiceImpl.insert(datapath);
		}
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		mongod.stop();
		mongodExe.stop();
	}

	@Test
	public void fetchAll() {
		List<DataModelOut> list = dbServiceImpl.fetchAll();
		Assert.assertNotNull("List should not be null", list);
		Assert.assertEquals("Expected to fetch all 50 records.", 50,
				list.size());
	}

	/**
	 * Exception generation test (field_name=null) for {@link DBService}
	 * .fetchDataWithSingleCondition()
	 */
	@Test
	public void fetchDataEqualsToTest1() {
		try {
			dbServiceImpl.fetchDataWithSingleCondition(null, "Zap",
					DBConstants.Operations.EQUAL);
			Assert.fail("It should generate an exception");
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * Empty list and not null test for {@link DBService}
	 * .fetchDataWithSingleCondition() with [btc_address] field
	 */
	@Test
	public void fetchDataEqualsToTest2() {

		List<DataModelOut> list = dbServiceImpl.fetchDataWithSingleCondition(
				DBConstants.FIELD_BTC_ADDR, null, DBConstants.Operations.EQUAL);
		Assert.assertNotNull("List should not be null.", list);
		Assert.assertEquals("Expected no records.", 0, list.size());
	}

	/**
	 * [ <= ] query test for {@link DBService}.fetchDataWithSingleCondition()
	 * with [transactions.date] field
	 */
	@Test
	public void fetchDataEqualsToTest3() {
		try {
			Date date = CommonUtil.convertStringToDate("2017-05-05");
			List<DataModelOut> list = dbServiceImpl
					.fetchDataWithSingleCondition(
							DBConstants.NESTED_FIELDS_TRANSACTIONS_DATE, date,
							DBConstants.Operations.GREATER_THAN_EQUAL);

			Assert.assertNotNull("List should not be null", list);
			Assert.assertTrue(list.size() > 0);
		} catch (ParseException e) {
			Assert.fail("Exception should not be generated.");
		}
	}

	/**
	 * [ >= ] query test for {@link DBService}.fetchDataWithSingleCondition()
	 * with [transactions.date] field
	 */
	@Test
	public void fetchDataEqualsToTest4() {
		try {
			Date date = CommonUtil.convertStringToDate("2017-05-05");
			List<DataModelOut> list = dbServiceImpl
					.fetchDataWithSingleCondition(
							DBConstants.NESTED_FIELDS_TRANSACTIONS_DATE, date,
							DBConstants.Operations.LESS_THAN_EQUAL);
			Assert.assertNotNull("List should not be null", list);
			Assert.assertTrue(list.size() > 0);
		} catch (ParseException e) {
			Assert.fail("ParseException should not be generated. Check input value.");
		}
	}

	/**
	 * LIKE query test for {@link DBService}.fetchDataWithSingleCondition() with
	 * [transactions.item] field
	 */
	@Test
	public void fetchDataEqualsToTest5() {

		List<DataModelOut> list = dbServiceImpl.fetchDataWithSingleCondition(
				DBConstants.NESTED_FIELDS_TRANSACTIONS_ITEM, "Zap",
				DBConstants.Operations.LIKE);
		Assert.assertNotNull("List should not be null.", list);
		Assert.assertTrue("List should not be empty.", list.size() > 0);
		Assert.assertEquals("Fetched record size should be 2", 2, list.size());
		Assert.assertEquals(prepareTargetDataForEqualTest(), list);
	}

	/**
	 * Exception generation test for {@link DBService}.fetchDateBetween()
	 */
	@Test
	public void fetchDateBetween1() {

		try {
			Date startDate = CommonUtil.convertStringToDate("2017/04/01");
			Date endDate = CommonUtil.convertStringToDate("2017/04/30");
			dbServiceImpl.fetchDateBetween(
					DBConstants.NESTED_FIELDS_TRANSACTIONS_DATE, startDate,
					endDate);
			Assert.fail("It should generate an exception.");
		} catch (ParseException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * Empty list test for {@link DBService}.fetchDateBetween()
	 */
	@Test
	public void fetchDateBetween2() {
		try {
			Date startDate = CommonUtil.convertStringToDate("2010-04-01");
			Date endDate = CommonUtil.convertStringToDate("2010-04-30");
			List<DataModelOut> list = dbServiceImpl.fetchDateBetween(
					DBConstants.NESTED_FIELDS_TRANSACTIONS_DATE, startDate,
					endDate);
			Assert.assertNotNull("List should not be null.", list);
			Assert.assertTrue("Expected an empty list but got " + list.size()
					+ " records.", list.size() == 0);
		} catch (ParseException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * Normal data fetch test for {@link DBService}.fetchDateBetween()
	 */
	@Test
	public void fetchDateBetween3() {
		try {
			Date startDate = CommonUtil.convertStringToDate("2017-05-01");
			Date endDate = CommonUtil.convertStringToDate("2017-05-31");
			List<DataModelOut> list = dbServiceImpl.fetchDateBetween(
					DBConstants.NESTED_FIELDS_TRANSACTIONS_DATE, startDate,
					endDate);
			PrintUtil.displayResult(list);
			Assert.assertNotNull("List should not be null.", list);
			Assert.assertTrue("Expected 24 results but got " + list.size()
					+ " records.", list.size() == 24);
		} catch (ParseException e) {
			Assert.fail("ParseException should not be generated. Check input value.");
		}
	}

	/**
	 * Exception generation test for {@link DBService}
	 * .fetchDateBetweenWithClientID()
	 */
	@Test
	public void fetchDateBetweenWithClientIDTest1() {
		try {
			dbServiceImpl
					.fetchDateBetweenWithClientID(
							DBConstants.FIELD_CLIENT_ID,
							"df52bcfaa573865c56b2d660fd7e99b5ff5584949742231a7a6df3c506dbd8b1",
							null, null, null);
			Assert.fail("It should generate an exception.");
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * Empty list test for {@link DBService}.fetchDateBetweenWithClientID()
	 */
	@Test
	public void fetchDateBetweenWithClientIDTest2() {

		try {
			Date startDate = CommonUtil.convertStringToDate("2010-04-01");
			Date endDate = CommonUtil.convertStringToDate("2010-04-30");
			List<DataModelOut> list = dbServiceImpl
					.fetchDateBetweenWithClientID(
							DBConstants.FIELD_CLIENT_ID,
							"df52bcfaa573865c56b2d660fd7e99b5ff5584949742231a7a6df3c506dbd8b1",
							"date", startDate, endDate);
			Assert.assertNotNull("Null FetchAll List", list);
			Assert.assertTrue(list.size() == 0);
		} catch (ParseException e) {
			Assert.fail("ParseException should not be generated. Check input value.");
		}
	}

	/**
	 * Normal execution test for {@link DBService}
	 * .fetchDateBetweenWithClientID()
	 */
	@Test
	public void fetchDateBetweenWithClientIDTest3() {
		try {
			Date startDate = CommonUtil.convertStringToDate("2017-05-01");
			Date endDate = CommonUtil.convertStringToDate("2017-05-31");
			List<DataModelOut> list = dbServiceImpl
					.fetchDateBetweenWithClientID(
							DBConstants.FIELD_CLIENT_ID,
							"77db323bd9bebb6c576e9d2c77b1abb7e1caf8f1ac81899d8d23e15d1d386dc8",
							"transactions.date", startDate, endDate);

			Assert.assertNotNull("List should not be null.", list);
			Assert.assertTrue("Expected single result, but got " + list.size(),
					list.size() == 1);
		} catch (ParseException e) {
			Assert.fail("ParseException should not be generated. Check input value.");
		}
	}

	/**
	 * Testing method of DBServiceImpl.mapToMpdel({@link FindIterable} <
	 * {@link Document}>)
	 */
	@Test
	public void mapToModelTest() {
		Object obj = null;
		String path = this.getClass().getResource("/mongodb.properties")
				.getFile();
		FindIterable<Document> documentCollection = Configurator.getDatabase()
				.getCollection(DBProperty.getInstance(path).getCollection())
				.find();
		try {
			Method method = dbServiceImpl.getClass().getDeclaredMethod(
					"mapToModel", FindIterable.class);
			method.setAccessible(true);
			obj = method.invoke(dbServiceImpl, documentCollection);
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertNotNull("List should not be null", obj);
		List<DataModelOut> list = new ArrayList<>();
		assertThat(obj, instanceOf(list.getClass()));
	}

	@After
	public void closeAll() {
		dbServiceImpl.truncate();
		Configurator.terminate();
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
