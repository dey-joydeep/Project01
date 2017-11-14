package jp.ne.interspace.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import jp.ne.interspace.constants.DBConstants;
import jp.ne.interspace.constants.Messages;
import jp.ne.interspace.model.output.DataModelOut;
import jp.ne.interspace.mongo.service.DBService;
import jp.ne.interspace.mongo.service.DBServiceImpl;
import jp.ne.interspace.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SearchServiceImpl implements SearchService {

	private DBService _dbService;

	public SearchServiceImpl() {
		_dbService = new DBServiceImpl();
	}

	public List<DataModelOut> findAll() {
		return _dbService.fetchAll();
	}

	public List<DataModelOut> findByCliendID(String clientID) {
		return _dbService.fetchDataWithSingleCondition("client_identifier", clientID,
				DBConstants.Operations.EQUAL);

	}

	public List<DataModelOut> findByBtcAddress(String btcAddress) {
		return _dbService.fetchDataWithSingleCondition("btc_address", btcAddress,
				DBConstants.Operations.EQUAL);

	}

	public List<DataModelOut> findByCountryCode(String countryCode) {
		return _dbService.fetchDataWithSingleCondition("country_code", countryCode,
				DBConstants.Operations.EQUAL);

	}

	public List<DataModelOut> findByItemName(String item) {
		return _dbService.fetchDataWithSingleCondition("transactions.item", item,
				DBConstants.Operations.LIKE);

	}

	public List<DataModelOut> findByDateGreaterThanEqual(String strDate) {
		Date date = null;
		try {
			date = CommonUtil.convertStringToDate(strDate);
		} catch (ParseException e) {
			log.error(Messages.ERR_INVALID_DATE_FORMAT);
		}
		return _dbService.fetchDataWithSingleCondition("transactions.date", date,
				DBConstants.Operations.GREATER_THAN_EQUAL);

	}

	public List<DataModelOut> findByDateLessThanEqual(String strDate) {
		Date date = null;
		try {
			date = CommonUtil.convertStringToDate(strDate);
		} catch (ParseException e) {
			log.error(Messages.ERR_INVALID_DATE_FORMAT);
		}
		return _dbService.fetchDataWithSingleCondition("transactions.date", date,
				DBConstants.Operations.LESS_THAN_EQUAL);

	}

	public List<DataModelOut> findByDateBetween(String startDate, String endDate) {
		Date sd = null;
		Date ed = null;
		try {
			sd = CommonUtil.convertStringToDate(startDate);
			ed = CommonUtil.convertStringToDate(endDate);
		} catch (ParseException e) {
			log.error(Messages.ERR_INVALID_DATE_FORMAT);
		}
		return _dbService.fetchDateBetween("transactions.date", sd, ed);

	}

	public List<DataModelOut> findBetweenDateWithClientID(String clientID,
			String startDate, String endDate) {
		Date sd = null;
		Date ed = null;
		try {
			sd = CommonUtil.convertStringToDate(startDate);
			ed = CommonUtil.convertStringToDate(endDate);
		} catch (ParseException e) {
			log.error(Messages.ERR_INVALID_DATE_FORMAT);
		}
		return _dbService.fetchDateBetweenWithClientID("client_identifier",
				clientID, "transactions.date", sd, ed);
	}
}
