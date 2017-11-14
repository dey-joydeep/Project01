package jp.ne.interspace.service;

import java.util.List;

import jp.ne.interspace.model.output.DataModelOut;

/**
 * This class holds different kind of search methods based on some criterias
 *
 * @author Joydeep Dey
 *
 */
public interface SearchService {

	/**
	 * Search for all records
	 *
	 * @return Search result
	 */
	public List<DataModelOut> findAll();

	/**
	 * Search by Client ID
	 *
	 * @param clientID
	 * @return Search result
	 */
	public List<DataModelOut> findByCliendID(String clientID);

	/**
	 * Search by BTC address
	 *
	 * @param btcAddress
	 * @return Search result
	 */
	public List<DataModelOut> findByBtcAddress(String btcAddress);

	/**
	 * Search by country code
	 *
	 * @param countryCode
	 * @return Search result
	 */
	public List<DataModelOut> findByCountryCode(String countryCode);

	/**
	 * Search by item name (supports partial matching)
	 *
	 * @param item
	 * @return Search result
	 */
	public List<DataModelOut> findByItemName(String item);

	/**
	 * Search by a transaction date, greater than or equal
	 *
	 * @param date
	 * @return Search result
	 */
	public List<DataModelOut> findByDateGreaterThanEqual(String date);

	/**
	 * Search by a transaction date, less than or equal
	 *
	 * @param date
	 * @return Search result
	 */
	public List<DataModelOut> findByDateLessThanEqual(String date);

	/**
	 * Search by records between two transaction dates, inclusively.
	 *
	 * @param startDate
	 * @param endDate
	 * @return Search result
	 */
	public List<DataModelOut> findByDateBetween(String startDate, String endDate);

	/**
	 * Search by records between two transaction dates, inclusively, having a
	 * Client ID.
	 *
	 * @param clientID
	 * @param startDate
	 * @param endDate
	 * @return Search result
	 */
	public List<DataModelOut> findBetweenDateWithClientID(String clientID,
			String startDate, String endDate);
}
