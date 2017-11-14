package jp.ne.interspace.mongo.service;

import java.util.Date;
import java.util.List;

import jp.ne.interspace.constants.DBConstants.Operations;
import jp.ne.interspace.model.output.DataModelOut;

/**
 * Perform several DB related services.
 *
 * @author Joydeep Dey
 *
 */
public interface DBService {

	/**
	 * Insert record(s) from the JSON file, defined in mongodb.properties
	 *
	 */
	public void insert(String datapath);

	/**
	 * Remove all the data, i.e. drop the collection.
	 */
	public void truncate();

	/**
	 * Fetch all the stored records.
	 *
	 * @return Search result
	 */
	public List<DataModelOut> fetchAll();

	/**
	 * Fetch the records that match the conditions as per {@link DBOperations}
	 * value
	 *
	 * @param searchColumn
	 *            the search field
	 * @param searchValue
	 *            condition
	 * @param op
	 *            {@link DBOperations}
	 * @return Search result
	 */
	public List<DataModelOut> fetchDataWithSingleCondition(String searchColumn,
			Object searchValue, Operations op);

	/**
	 * Search records between two transaction dates, inclusive.
	 *
	 * @param searchColumn
	 *            the search field
	 * @param fromDate
	 *            The starting date
	 * @param toDate
	 *            The ending date date
	 * @return Search result
	 */
	public List<DataModelOut> fetchDateBetween(String searchColumn,
			Date fromDate, Date toDate);

	/**
	 * Search records between two transaction dates, inclusive, with the given
	 * client ID.
	 *
	 * @param idColumn
	 *            Client ID column name
	 * @param idValue
	 *            Client ID
	 * @param rangeColumn
	 *            Transaction date column name
	 * @param fromDate
	 *            Transaction start date
	 * @param toDate
	 *            Transaction end date
	 * @return Search result
	 */
	public List<DataModelOut> fetchDateBetweenWithClientID(String idColumn,
			String idValue, String rangeColumn, Date fromDate, Date toDate);

	/**
	 * Get count of all records
	 *
	 * @return record count
	 */
	public long getRecordCount();
}
