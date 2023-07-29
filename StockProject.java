package StockProject;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class StockGame extends Thread {

	private static final Object Def = new Object();
	private static double stockPrice = 100;
	private static int availableShares = 100;
	private static int tradeCount;
	private String name;
	private int numShares;
	private String fileName;
	private ArrayList<String> task = new ArrayList<String>();

	@Override
	public String toString() {
		return "StockGame [name=" + name + ", numShares=" + numShares + ", fileName=" + fileName + ", task=" + task
				+ "]";
	}

	public String getname() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public int getNumShares() {
		return numShares;
	}

	public void setNumShares(int numShares) {
		this.numShares = numShares;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public StockGame(String name, String fileName) {
		super();
		this.name = name;
		this.fileName = fileName;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;

			while ((line = reader.readLine()) != null) {
				task.add(line);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch bDef
			e.printStackTrace();
		}
	}

	public boolean CheckTask(int num) {
		if (num < 0 || Character.isDigit(num) == false) {
			System.out.println("Error, invalid input");
			return true;
		}
		return false;
	}

	public boolean checkBuy(int num) {
		if (num >= availableShares) {
			System.out.println("Insufficient shares available, cancelling order...");
			return false;
		}

		return true;

	}

	public boolean CheckSell(int num) {
		if (num <= getNumShares()) {
			return true;
		} else {

			System.out.println("Insufficient shares available, cancelling order...");
		}
		return false;

	}

	public boolean checkValid(String str) {
		String[] arr = str.split(",");
		String Type = arr[0].trim();
		int num = Integer.parseInt(arr[1].trim());
		if (!Type.equals("BUY") && !Type.equals("SELL") || num < 0) {
			return false;
		}
		return true;
	}

	public void run() {

		for (String a : task) {

			String[] arr = a.split(",");
			String Type = arr[0].trim();
			int num = Integer.parseInt(arr[1].trim());

			synchronized (Def) {

				if (checkValid(a)) {
					synchronized (Def) {
						if ("BUY".equals(Type)) {
							boolean flag2 = checkBuy(num);
							if (flag2) {

								System.out.println("-----------------------------------------------");
								System.out.println(" Stock Price :" + stockPrice);
								System.out.println(" Available Shares :" + availableShares);
								System.out.println(" Trade Number :" + ++tradeCount);
								System.out.println(" Name  :" + name);
								System.out.println(" Purchasing " + num + " shares at " + stockPrice);

								availableShares = availableShares - num;
								setNumShares(getNumShares() + num);
								stockPrice = stockPrice + 1.5 * num;

							} 
							else {
								continue;
							}

						} 
						else {

							boolean flag2 = CheckSell(num);
							if (flag2) {
								System.out.println("-----------------------------------------------");
								System.out.println(" Stock Price :" + stockPrice);
								System.out.println(" Available Shares :" + availableShares);
								System.out.println(" Trade Number :" + ++tradeCount);
								System.out.println(" Name  :" + name);
								System.out.println(" sell " + num + " shares at " + stockPrice);

								availableShares = availableShares + num;
								setNumShares(getNumShares() - num);
								stockPrice = stockPrice - 2.0 * num;
							} else {
								continue;
							}
						}

					}
				} else {
					System.out.println("Enter input is not Valid");
					continue;
				}
			}
		}
	}
}

public class StockProject {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		try {
			StockGame[] StockTraders = { new StockGame("Xander", "F:\\eclipse-workplace\\Project2\\src\\StockProject\\FirstTrader.txt"),
					new StockGame("Afua", "F:\\eclipse-workplace\\Project2\\src\\StockProject\\SecondTrader.txt") };
			for (int i = 0; i < StockTraders.length; i++) {
				StockTraders[i].start();
			}
			for (int i = 0; i < StockTraders.length; i++) {
				StockTraders[i].join();
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
	}
}
