package jp.ne.interspace.model.input;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Data model class to map the root attribute of JSON file. Require to maintain
 * the structure only, not required thereafter.
 * 
 * @author Joydeep Dey
 *
 */
@ToString
public class RootModel {

	@Getter
	@Setter
	@SerializedName("start_date")
	private String startDate;

	@Getter
	@Setter
	@SerializedName("end_date")
	private String endDate;

	@Getter
	@Setter
	@SerializedName("data")
	private List<DataModelIn> data;
}
