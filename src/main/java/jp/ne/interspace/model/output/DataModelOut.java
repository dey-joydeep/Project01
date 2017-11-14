package jp.ne.interspace.model.output;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.gson.annotations.SerializedName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Data model class to map the MongoDB document, during fetch the data from DB
 *
 * @author Joydeep Dey
 *
 */
@ToString
@EqualsAndHashCode(exclude = "id")
public class DataModelOut {

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
	private List<TransactionModelOut> transactions;
}
