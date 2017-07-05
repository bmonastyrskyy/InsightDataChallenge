package edu.ucdavis.gc.bm.starthere;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;

import edu.ucdavis.gc.bm.network.BadFormatException;
import edu.ucdavis.gc.bm.network.Network;
import edu.ucdavis.gc.bm.network.Parser;
import edu.ucdavis.gc.bm.network.Purchase;

public class Main {


	public static void main(String[] args) {
		
		String inBatchPath = args[0];
		String inStreamPath = args[1];
		String outPath = args[2];
		
		Network network = null;
		Parser parser = new Parser();
		
		// read batch_log file
		BufferedReader br;
		String line = null;
		try {
			br = new BufferedReader(new FileReader(new File(inBatchPath)));
			line = br.readLine();
			HashMap<Character, Integer> par = parser.parseNetworkParameters(line);
			network = new Network(par.get('D'), par.get('T'));
			while ((line = br.readLine()) != null ) {
				String event = parser.detectEvent(line);
				HashMap<String, String> hash = null;
				if (event.equalsIgnoreCase("purchase")) {
					hash = parser.parsePurchase(line);
					network.addPurchase( new Purchase(Integer.valueOf(hash
							.get("id")), Timestamp.valueOf(hash
							.get("timestamp")), Double.valueOf(hash
							.get("amount"))));
				} else if (event.equalsIgnoreCase("befriend")){
					hash = parser.parseBeFriend(line);
					network.beFriend(Integer.valueOf(hash.get("id1")), Integer.valueOf(hash.get("id2")));
				} else if (event.equalsIgnoreCase("unfriend")){
					hash = parser.parseUnFriend(line);
					network.unFriend(Integer.valueOf(hash.get("id1")), Integer.valueOf(hash.get("id2")));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("File " + inBatchPath + " coudln't be found.");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("I/O error has happened while trying to read " + inBatchPath + " file.");
			e.printStackTrace();
			return;
		} catch (BadFormatException e){
			System.err.println("Bad format of the line:\n" + line);
			System.err.println("The line was skipped.");
		}
		
		//read stream_log and write to output
		br = null;
		PrintWriter pw;
		try {
			br = new BufferedReader(new FileReader(new File(inStreamPath)));
			pw = new PrintWriter(new FileWriter(outPath));
			while ((line = br.readLine()) != null ) {
				String event = parser.detectEvent(line);
				HashMap<String, String> hash = null;
				if (event.equalsIgnoreCase("purchase")) {
					hash = parser.parsePurchase(line);
					String outlierPurchase = network.processPurchase( new Purchase(Integer.valueOf(hash
							.get("id")), Timestamp.valueOf(hash
							.get("timestamp")), Double.valueOf(hash
							.get("amount"))));
					if (outlierPurchase != null) {
						pw.println("{" + outlierPurchase + "}");
					}
				} else if (event.equalsIgnoreCase("befriend")){
					hash = parser.parseBeFriend(line);
					network.beFriend(Integer.valueOf(hash.get("id1")), Integer.valueOf(hash.get("id2")));
				} else if (event.equalsIgnoreCase("unfriend")){
					hash = parser.parseUnFriend(line);
					network.unFriend(Integer.valueOf(hash.get("id1")), Integer.valueOf(hash.get("id2")));
				}
			}
			br.close();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("File " + inStreamPath + " coudln't be found.");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("I/O error has happened while trying to read " + inStreamPath + " file.");
			e.printStackTrace();
			return;
		} catch (BadFormatException e){
			System.err.println("Bad format of the line:\n" + line);
			System.err.println("The line was skipped.");
		}
		

	}

}
