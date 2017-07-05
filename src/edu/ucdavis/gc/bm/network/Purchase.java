package edu.ucdavis.gc.bm.network;

import java.sql.Timestamp;
import java.util.Comparator;

/**
 * Class represents the purchase made in the social network.
 * @author Bohdan Monastyrskyy
 *
 */
public class Purchase implements Comparator<Purchase>, Comparable<Purchase>{

	/**
	 * The timestamp of the purchase.
	 */
	private final Timestamp timestamp; 
	/**
	 * Member's id who made the purchase.
	 */
	private final int member_id;
	/**
	 * The amount of the purchase.
	 */
	private final  double amount;
	/**
	 * Constructor.
	 * @param member_id
	 * @param timestamp
	 * @param amount
	 */
	public Purchase(int member_id, Timestamp timestamp, double amount){
		this.member_id = member_id;
		this.timestamp = timestamp;
		this.amount = amount;
	}
	/**
	 * Getter.
	 * @return timestamp of the purchase.
	 */
	public Timestamp getTimestamp(){
		return timestamp;
	}
	/**
	 * Getter.
	 * @return the amount of the purchase.
	 */
	public double getAmount(){
		return amount;
	}
	/**
	 * Getter.
	 * @return the member's id.
	 */
	public int getmemberID(){
		return member_id;
	}
	@Override
	public int hashCode(){
		return member_id;
	}
	/**
	 * The overridden method produces the output in json format.
	 */
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		String reducedTimestamp = timestamp.toString();
		reducedTimestamp = reducedTimestamp.replaceAll("\\.\\d+", "");
		sb.append(String.format("\"event_type\":\"purchase\", \"timestamp\":\"%s\", \"id\": \"%d\", \"amount\": \"%.2f\"",  reducedTimestamp, member_id, amount));
		return sb.toString();
	}
	/**
	 * Two fields are added the toString() result - "mean" and "sd".
	 * @param mean
	 * @param sd
	 * @return
	 */
	public String toString(double mean, double sd){
		StringBuilder sb = new StringBuilder(toString());
		sb.append(String.format(", \"mean\": \"%.2f\", \"sd\": \"%.2f\"", mean, sd));
		return sb.toString();
	}
	/**
	 * Overriding method : comparing of two purchases by their timestamp.
	 * @param p1
	 * @param p2
	 * @return
	 */
	@Override
	public int compare(Purchase p1, Purchase p2) {
		int res =  p1.getTimestamp().compareTo(p2.getTimestamp());
		return res;
	}

	@Override
	public int compareTo(Purchase p) {
		return this.getTimestamp().compareTo(p.getTimestamp());		
	}
	
	
}

