package jp.ne.interspace.model.input;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Data model class to map the "transactions" attribute of JSON file during
 * insert the data in DB.
 * 
 * @author Joydeep Dey
 *
 */
@ToString
public class TransactionModelIn {

	@Getter
	@Setter
	@SerializedName("item")
	private String item;

	@Getter
	@Setter
	@SerializedName("payment")
	private Double payment;

	@Getter
	@Setter
	@SerializedName("date")
	private Date date;
}
