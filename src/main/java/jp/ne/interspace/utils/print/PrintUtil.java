package jp.ne.interspace.utils.print;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import jp.ne.interspace.constants.Messages;
import jp.ne.interspace.model.output.DataModelOut;
import jp.ne.interspace.model.output.TransactionModelOut;
import jp.ne.interspace.utils.print.annotation.Header;

/**
 * An utility class for printing from the list of {@link DataModelOut} object
 *
 * @author Joydeep Dey
 *
 */
public final class PrintUtil {

	/***
	 * A list of {@link DataModelOut}
	 *
	 * @param dataList
	 */
	public static void displayResult(List<DataModelOut> dataList) {

		// Message to display in case of no records
		if (dataList.isEmpty()) {
			System.out.println(Messages.NO_DATA_FOUND);
			return;
		}

		// For each record
		dataList.forEach(dataModel -> {
			System.out.println("Client ID    : "
					+ dataModel.getClientIdentifier());
			System.out.println("BTC Address  : " + dataModel.getBtcAddress());
			System.out.println("Country Code : " + dataModel.getCountryCode());
			System.out.println("Avatar       : " + dataModel.getAvatar());

			int loopCount = dataModel.getClientIdentifier().length() + 15;
			for (int i = 0; i < loopCount; i++)
				System.out.print("_");
			System.out.println("\n");
			System.out.print("Transaction Details:");
			List<TransactionModelOut> transactions = dataModel
					.getTransactions();
			if (transactions != null && !transactions.isEmpty()) {
				System.out.println("\n");
				TableOutputHelper outputModel = new TableOutputHelper();
				constructTabularPattern(transactions, outputModel);
				System.out.format(outputModel.getPattern(),
						outputModel.getHeaders()[0], "|",
						outputModel.getHeaders()[1], "|",
						outputModel.getHeaders()[2]);
				System.out.println();
				for (int i = 0; i < outputModel.separatorLength(); i++) {
					System.out.print("-");
				}
				System.out.println();
				transactions.forEach(transaction -> {
					System.out.format(outputModel.getPattern(), transaction
							.getItem(), "|", transaction.getPayment(), "|",
							new Date(transaction.getDate().getDate()));
					System.out.println();
					for (int i = 0; i < outputModel.separatorLength(); i++) {
						System.out.print("-");
					}
					System.out.println();
				});
			} else {
				System.out.println(Messages.NO_TRANSACTIONS_FOUND);
			}
			System.out.println();
		});
		System.out.println("Total Records Fetched: " + dataList.size() + "\n");
	}

	/**
	 * Construct the formatting pattern for print records in tabular format. The
	 * format pattern will be decided based on the maximum length of a that
	 * column.
	 *
	 * @param transactions
	 * @param outputModel
	 */
	private static void constructTabularPattern(
			List<TransactionModelOut> transactions,
			TableOutputHelper outputModel) {
		Class<? extends TransactionModelOut> clazz = transactions.get(0)
				.getClass();
		Field[] fields = clazz.getDeclaredFields();
		int nColumns = fields.length;
		int[] dataWidths = new int[nColumns];
		String[] headers = new String[nColumns];
		int[] headerOrders = new int[nColumns];
		String[] methodNames = new String[nColumns];

		for (int i = 0; i < nColumns; i++) {
			Header header = fields[i].getAnnotation(Header.class);
			headers[i] = header.value();
			headerOrders[i] = header.order();
			String fieldName = fields[i].getName();
			String methodName = "get"
					+ Character.toUpperCase(fieldName.charAt(0))
					+ fieldName.substring(1);
			methodNames[i] = methodName;
		}

		// Calculating the maximum width for each field/column
		for (TransactionModelOut model : transactions)
			for (int i = 0; i < nColumns; i++) {
				try {
					Method method = clazz.getMethod(methodNames[i]);
					Object obj = method.invoke(model);
					if (obj == null)
						continue;

					String data = obj.toString();
					if (data.length() > dataWidths[i])
						dataWidths[i] = data.length();

				} catch (NoSuchMethodException | SecurityException
						| IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// assume access to public methods
				}
			}

		// Adjust the header printing
		for (int i = 0; i < dataWidths.length; i++) {
			String header = headers[i];
			if (dataWidths[i] > header.length()) {
				int leftPadBy = (dataWidths[i] - header.length()) / 2;
				for (int j = 0; j < leftPadBy; j++) {
					header += " ";
				}
				for (int j = 0; j < leftPadBy; j++) {
					header = " " + header;
				}
				headers[i] = header;
			}
			dataWidths[i] += 2;
		}

		outputModel.setHeaders(headers);
		outputModel.setWidths(dataWidths);
	}
}
