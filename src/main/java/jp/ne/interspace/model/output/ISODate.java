package jp.ne.interspace.model.output;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Map the long value of date from MongoDB date field
 *
 * @author Joydeep Dey
 *
 */
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class ISODate {

	@Getter
	@Setter
	@SerializedName("$date")
	private long date;
}
