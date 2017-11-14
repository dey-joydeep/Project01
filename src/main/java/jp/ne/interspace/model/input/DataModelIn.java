package jp.ne.interspace.model.input;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Data model class to map the "data" attribute of JSON file during insert the
 * data in DB
 *
 * @author Joydeep Dey
 *
 */
@ToString
public class DataModelIn {

	@Getter
	@Setter
	@SerializedName("_id")
	private ObjectId id;

	@Getter
	@Setter
	@SerializedName("client_identifier")
	private String clientIdentifier;

	@Getter
	@Setter
	@SerializedName("btc_address")
	private String btcAddress;

	@Getter
	@Setter
	@SerializedName("country_code")
	private String countryCode;

	@Getter
	@Setter
	@SerializedName("avatar")
	private String avatar;

	@Getter
	@Setter
	@SerializedName("transactions")
	private List<TransactionModelIn> transactions;
}
