package jp.ne.interspace.model.output;

import com.google.gson.annotations.SerializedName;

import jp.ne.interspace.utils.print.annotation.Header;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Data model class to map the MongoDB document's transaction attribute, during
 * fetch the data from DB
 *
 * @author Joydeep Dey
 *
 */
@ToString
@EqualsAndHashCode
public class TransactionModelOut {

	@Getter
	@Setter
	@SerializedName("item")
	@Header(value = "Item", order = 1)
	private String item;

	@Getter
	@Setter
	@SerializedName("payment")
	@Header(value = "Payment", order = 1)
	private Double payment;

	@Getter
	@Setter
	@SerializedName("date")
	@Header(value = "Date", order = 1)
	private ISODate date;
}
