package jp.ne.interspace.mongo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import jp.ne.interspace.constants.Commons;
import jp.ne.interspace.constants.DBConstants;
import jp.ne.interspace.constants.DBConstants.Operations;
import jp.ne.interspace.constants.Messages;
import jp.ne.interspace.model.input.DataModelIn;
import jp.ne.interspace.model.input.RootModel;
import jp.ne.interspace.model.input.TransactionModelIn;
import jp.ne.interspace.model.output.DataModelOut;
import jp.ne.interspace.mongo.config.Configurator;
import jp.ne.interspace.utils.CommonUtil;
import jp.ne.interspace.utils.DBProperty;
import lombok.extern.slf4j.Slf4j;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@Slf4j
public class DBServiceImpl implements DBService {

	private MongoDatabase database;
	private DBProperty dbProperty;
	private MongoCollection<Document> collection;

	public DBServiceImpl() {
		if (dbProperty == null)
			dbProperty = DBProperty
					.getInstance(Commons.DB_PROPERTIES_FILE_PATH);
		this.database = Configurator.getDatabase();
	}

	public DBServiceImpl(MongoDatabase database) {
		this.database = database;
	}

	public void init() {
		this.collection = Configurator.getDatabase().getCollection(
				dbProperty.getCollection());
	}

	public void insert(String datapath) {
		try {
			String btcData = CommonUtil.readTextFile(datapath);
			Gson gson = new Gson();
			RootModel rootModel = gson.fromJson(btcData, RootModel.class);
			List<DataModelIn> dataModelList = rootModel.getData();
			List<Document> docList = new ArrayList<>();
			dataModelList
					.forEach(dataModel -> {
						Document doc = new Document(DBConstants.FIELD_ID,
								dataModel.getId())
								.append(DBConstants.FIELD_CLIENT_ID,
										dataModel.getClientIdentifier())
								.append(DBConstants.FIELD_BTC_ADDR,
										dataModel.getBtcAddress())
								.append(DBConstants.FIELD_COUNTRY_CODE,
										dataModel.getCountryCode())
								.append(DBConstants.FIELD_AVATAR,
										dataModel.getAvatar());

						List<TransactionModelIn> transactions = dataModel
								.getTransactions();
						if (transactions != null && !transactions.isEmpty()) {
							List<Document> innerDocList = new ArrayList<>();
							transactions.forEach(transaction -> {
								Document innerDoc = new Document();
								innerDoc.append(DBConstants.FIELD_ITEM,
										transaction.getItem())
										.append(DBConstants.FIELD_PAYMENT,
												transaction.getPayment())
										.append(DBConstants.FIELD_DATE,
												transaction.getDate());
								innerDocList.add(innerDoc);
							});
							doc.append(DBConstants.FIELD_TRANSACTIONS,
									innerDocList);
						} else {
							doc.append(DBConstants.FIELD_TRANSACTIONS, null);
						}
						docList.add(doc);
					});

			collection.insertMany(docList);
		} catch (IOException e) {
			log.error(Messages.ERR_DATA_INSERT_FAILED, e);
		}
	}

	public void truncate() {
		database.getCollection(dbProperty.getCollection()).drop();

	}

	public List<DataModelOut> fetchAll() {
		FindIterable<Document> data = collection.find();
		return mapToModel(data);
	}

	public List<DataModelOut> fetchDataWithSingleCondition(String searchColumn,
			Object searchValue, Operations op) {

		FindIterable<Document> data = null;

		switch (op) {
		case EQUAL:
			data = collection.find(Filters.eq(searchColumn, searchValue));
			break;
		case GREATER_THAN_EQUAL:
			data = collection.find(Filters.gte(searchColumn, searchValue));
			break;
		case LESS_THAN_EQUAL:
			Bson f = Filters.lte(searchColumn, searchValue);
			data = collection.find(f);
			break;
		case LIKE:
			Pattern regex = Pattern.compile(searchValue.toString(),
					Pattern.CASE_INSENSITIVE);
			data = collection.find(Filters.eq(searchColumn, regex));
			break;
		default:
			break;
		}

		return mapToModel(data);
	}

	public List<DataModelOut> fetchDateBetween(String searchColumn,
			Date fromDate, Date toDate) {
		Bson filters = Filters.and(Filters.gte(searchColumn, fromDate),
				Filters.lte(searchColumn, toDate));
		FindIterable<Document> data = collection.find(filters);

		return mapToModel(data);
	}

	public List<DataModelOut> fetchDateBetweenWithClientID(String idColumn,
			String idValue, String rangeColumn, Date fromDate, Date toDate) {
		Bson filters = Filters.and(
				Filters.eq(idColumn, idValue),
				Filters.and(Filters.gte(rangeColumn, fromDate),
						Filters.lte(rangeColumn, toDate)));
		FindIterable<Document> data = collection.find(filters);

		return mapToModel(data);
	}

	/**
	 * Method to map the {@link Document} to a POJO list
	 *
	 * @param documentCollection
	 * @return List of {@link DataModelOut}
	 */
	private List<DataModelOut> mapToModel(
			FindIterable<Document> documentCollection) {
		List<DataModelOut> dataModelList = new ArrayList<>();
		if (documentCollection != null) {
			Iterator<Document> docIterator = documentCollection.iterator();

			while (docIterator.hasNext()) {
				Document document = docIterator.next();
				String jsonData = document.toJson();
				Gson gson = new Gson();
				DataModelOut dataModelOut = gson.fromJson(jsonData,
						DataModelOut.class);
				dataModelList.add(dataModelOut);
			}
		}
		return dataModelList;
	}

	public long getRecordCount() {
		return collection.count();
	}
}
