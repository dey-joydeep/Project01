/**
 * Copyright © 2017 Interspace Co., Ltd. All rights reserved.
 *
 * Licensed under the Interspace's License,
 * you may not use this file except in compliance with the License.
 */
package jp.ne.interspace;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jp.ne.interspace.model.output.DataModelOut;
import jp.ne.interspace.mongo.config.Configurator;
import jp.ne.interspace.service.DataControlService;
import jp.ne.interspace.service.DataControlServiceImpl;
import jp.ne.interspace.service.SearchService;
import jp.ne.interspace.service.SearchServiceImpl;
import jp.ne.interspace.utils.print.PrintUtil;

/**
 * OBS Initiation Trial (F).
 *
 * @author Interspace OBS DEV Team
 */
public class Trial {

	public static void main(String[] args) {
		boolean quit = false;
		try (Scanner scanner = new Scanner(System.in)) {
			Configurator.openConnection();
			DataControlService controlService = new DataControlServiceImpl();
			controlService.removeAllData();
			controlService.storeData();
			SearchService searchService = new SearchServiceImpl();
			List<DataModelOut> dataList = new ArrayList<>();
			while (!quit) {
				boolean print = true;
				String clientId = null;
				String date = null;
				String startDate = null;
				String endDate = null;
				String btcAddr = null;
				String countryCode = null;
				String item = null;

				System.out.println("1. Find All Records.");
				System.out.println("2. Find by Client ID.");
				System.out.println("3. Find by BTC Address.");
				System.out.println("4. Find by Country Code.");
				System.out.println("5. Find by Item.");
				System.out.println("6. Find by Date Greater Than Equal.");
				System.out.println("7. Find by Date Less Than Equal.");
				System.out.println("8. Find by Date Between.");
				System.out.println("9. Find by Date Between with Client ID.");
				System.out.println("Q/q for Quit");

				System.out.print("Choose Option: ");
				String op = scanner.nextLine();
				switch (op) {
				case "1":
					dataList = searchService.findAll();
					break;
				case "2":
					System.out.print("Client ID: ");
					clientId = scanner.nextLine();
					dataList = searchService.findByCliendID(clientId);
					break;
				case "3":
					System.out.print("BTC Address: ");
					btcAddr = scanner.nextLine();
					dataList = searchService.findByBtcAddress(btcAddr);
					break;
				case "4":
					System.out.print("Country Code: ");
					countryCode = scanner.nextLine();
					dataList = searchService.findByCountryCode(countryCode);
					break;
				case "5":
					System.out.print("Item: ");
					item = scanner.nextLine();
					dataList = searchService.findByItemName(item);
					break;
				case "6":
					System.out.print("Date(YYYY-MM-DD): ");
					date = scanner.nextLine();
					dataList = searchService.findByDateGreaterThanEqual(date);
					break;
				case "7":
					System.out.print("Date(YYYY-MM-DD): ");
					date = scanner.nextLine();
					dataList = searchService.findByDateLessThanEqual(date);
					break;
				case "8":
					System.out.print("Start Date(YYYY-MM-DD): ");
					startDate = scanner.nextLine();
					System.out.print("To Date(YYYY-MM-DD): ");
					endDate = scanner.nextLine();
					dataList = searchService.findByDateBetween(startDate,
							endDate);
					break;
				case "9":
					System.out.print("Client ID: ");
					clientId = scanner.nextLine();
					System.out.print("Start Date(YYYY-MM-DD): ");
					startDate = scanner.nextLine();
					System.out.print("To Date(YYYY-MM-DD): ");
					endDate = scanner.nextLine();
					dataList = searchService.findBetweenDateWithClientID(
							clientId, startDate, endDate);
					break;
				case "Q":
				case "q":
					quit = true;
					print = false;
					break;
				default:
					print = false;
					System.out.println("Invalid option. Try again.");
					break;
				}
				System.out.println();
				if (print)
					PrintUtil.displayResult(dataList);
				System.out.println();
			}
		} finally {
			Configurator.terminate();
		}
	}
}
