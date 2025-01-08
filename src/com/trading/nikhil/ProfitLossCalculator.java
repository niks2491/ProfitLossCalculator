package com.trading.nikhil;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.spi.FileSystemProvider;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ProfitLossCalculator {

	private static final String READ_TRADE_LINE_SEPARATOR = "\\|\\|";

	public static BigDecimal investedCapital = new BigDecimal(70000).setScale(2, RoundingMode.HALF_DOWN);
	public static final BigDecimal PERCENTAGEMODIFIER = new BigDecimal(100).setScale(2, RoundingMode.HALF_DOWN);
	public static final String DRIVE = "C:";
	public static final String tradeFilePath = DRIVE + File.separator + "Users" + File.separator + "niks2"
			+ File.separator + "OneDrive" + File.separator + "Desktop" + File.separator + "AlgoTrading" + File.separator
			+ "Common Space" + File.separator + "Trades";
	public BigDecimal profit = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
	public BigDecimal loss = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
	public int nooftrades = 0;
	public double brokerage = 0.00;
	public int noofdaystraded = 0;

	public static void main(String[] args) {
		ProfitLossCalculator plc = new ProfitLossCalculator();
		plc.process();
	}

	public void process() {
		File dir = new File(tradeFilePath);
		File[] directoryListing = dir.listFiles();
		ArrayList<Integer> fileSeq = new ArrayList<Integer>();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.isFile() && child.getName().contains(".txt")) {
					noofdaystraded++;
					readTradeFile(child);
				}
			}
		}

		BigDecimal netProfit = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
		BigDecimal netLoss = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
		BigDecimal brokerage = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
		BigDecimal profitAftBrokerage = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
		BigDecimal lossAftBrokerage = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
		boolean isProfit = false;
		if (profit.compareTo(loss) > 0) {
			isProfit = true;
			netProfit = profit.subtract(loss).setScale(2, RoundingMode.HALF_DOWN);
		} else if (profit.compareTo(loss) < 0) {
			netLoss = loss.subtract(profit).setScale(2, RoundingMode.HALF_DOWN);
		}

		System.out.println("PROFIT : " + profit);
		System.out.println("LOSS : " + loss);
		System.out.println("Total Number of trades : " + nooftrades);
		brokerage = BigDecimal.valueOf(nooftrades * 2 * 30);
		System.out.println("Brokerage(Around Rs. 30 per order) : " + brokerage);
		if (isProfit) {
			System.out.println("Net Profit : " + netProfit);
			profitAftBrokerage = netProfit.subtract(brokerage);
			System.out.println("Profit after brokerage : " + profitAftBrokerage);
		} else {
			System.out.println("Net Loss : -" + netLoss);
			lossAftBrokerage = netLoss.add(brokerage);
			System.out.println("Loss after brokerage : -" + netLoss.add(lossAftBrokerage));
		}
		System.out.println("No of days traded : " + noofdaystraded);

		System.out.println("--------------------------");
		System.out.println("--------------------------");
		System.out.println("Invested Capital (capital to buy one nifty future lot) : " + investedCapital);

		System.out.println("Return on Capital : " + profitAftBrokerage.divide(investedCapital, RoundingMode.HALF_DOWN)
				.multiply(PERCENTAGEMODIFIER).setScale(2, RoundingMode.HALF_DOWN) + "%");

		/*
		 * System.out.println("Return on Capital : " +
		 * ((profit.subtract(loss)).divide(investedCapital).setScale(2,
		 * RoundingMode.HALF_DOWN)) .multiply(PERCENTAGEMODIFIER).setScale(2,
		 * RoundingMode.HALF_DOWN));
		 */

	}

	public String readTradeFile(File file) {
		// System.out.println("Trade File Path : " + file);
		String line = null;
		try {
			if (file.exists()) {
				Scanner sc = new Scanner(file);
				while (sc.hasNextLine()) {
					line = sc.nextLine();
					String[] arrLine = line.split(READ_TRADE_LINE_SEPARATOR);
					if (!line.isEmpty()) {
						if (!arrLine[6].equalsIgnoreCase("PROFIT")) {
							nooftrades++;
							profit = profit.add(new BigDecimal(arrLine[6]));
						}
						if (!arrLine[7].equalsIgnoreCase("LOSS")) {
							loss = loss.add(new BigDecimal(arrLine[7]));
						}
					}
				}
				sc.close();
			} else {
				System.err.println("Trade File not found...");
				// TODO : Continue searching for it..
			}
		} catch (FileNotFoundException e) {
			System.err.println("Trade text file not found.." + file);
			e.printStackTrace();
		}
		return line;
	}
}
