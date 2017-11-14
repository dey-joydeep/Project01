package jp.ne.interspace.service;

/**
 * Helper class to execute insert, delete, truncate operations
 * 
 * @author Joydeep Dey
 *
 */
public interface DataControlService {

	/**
	 * Insert the date
	 */
	public void storeData();

	/**
	 * Remove the date by id
	 * 
	 * @param id
	 */
	public void removeData(String id);

	/**
	 * Truncate the document, i.e. drop the collection
	 */
	public void removeAllData();
}
