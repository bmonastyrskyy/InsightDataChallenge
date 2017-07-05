package edu.ucdavis.gc.bm.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import edu.ucdavis.gc.bm.collectionutils.BoundedTreeSet;
import edu.ucdavis.gc.bm.collectionutils.StatUtils;

public class Network {
	/**
	 * set of members of the network
	 */
	private final HashMap<Integer, Member> members = new HashMap<Integer, Member>();
	
	/**
	 * Degrees of freedom in definition of neighborhood. 
	 */
	private final int D; 
	/**
	 * Number of purchases in the local network to process. 
	 */
	private final int T;
	/**
	 * Constructor.
	 * @param D - degrees of freedom 
	 * @param T - number of purchases to 
	 */
	public Network(final int D, final int T){
		this.D  = D;
		this.T = T;
	}
	/**
	 * Add new member to the network.<>
	 * @param m
	 */
	public void addMember(Member m){
		members.put(m.getID(), m);
	}
	/**
	 * Set friendship between two members.
	 * @param id1
	 * @param id2
	 */
	public void beFriend(int id1, int id2){
		if (!members.keySet().contains(id1)) {
			members.put(id1, new Member(id1, T));
		}
		members.get(id1).beFriend(id2);
		if (!members.keySet().contains(id2)) {
			members.put(id2, new Member(id2, T));
		}
		members.get(id2).beFriend(id1);
	}
	/**
	 * Cancel friendship between two members.
	 * @param id1
	 * @param id2
	 */
	public void unFriend(int id1, int id2){
		if (!members.keySet().contains(id1)) {
			members.put(id1, new Member(id1, T));
		}
		members.get(id1).unFriend(id2);
		if (!members.keySet().contains(id2)) {
			members.put(id2, new Member(id2, T));
		}
		members.get(id2).unFriend(id1);
	}
	/**
	 * Method make record of current purchase.<br>
	 * The method should be used in batch mode.
	 * @param curPurchase
	 */
	public void addPurchase(Purchase curPurchase){
		// add purchase
		int member_id = curPurchase.getmemberID();
		if (!members.keySet().contains(member_id)) {
			members.put(member_id, new Member(member_id, T));
		}
		members.get(member_id).addPurchase(curPurchase);
	}
	/**
	 * Method makes record of the current purchase and check if is an outlier.<br>
	 * If yes, it returns String in json format, and null otherwise.
	 * @param purchase
	 * @return String in jason format or null
	 */
	public String processPurchase(Purchase purchase){
		// add purchase
		int member_id = purchase.getmemberID();
		if (!members.keySet().contains(member_id)) {
			members.put(member_id, new Member(member_id, T));
		}
		members.get(member_id).addPurchase(purchase);
		TreeSet<Integer> neighbors =  calcNeighbors(member_id, D);
		return printIfPurchaseIsOutlier(purchase, neighbors);
	}
	/**
	 * The method calculates all neighbors of the member with the given id.
	 * @param id - member's id
	 * @param D - degrees of freedom
	 * @return - set of ids
	 */
	private TreeSet<Integer> calcNeighbors(int id, int D){
		TreeSet<Integer> result = new TreeSet <Integer>();		
		if (D == 0) {
			result.add(id);
			return result;
		} else {
			for (Integer id1 : calcNeighbors(id, D-1)) {
				result.addAll(this.members.get(id1).getFriends());
			}
		}
		return result;
	}
	
	private String printIfPurchaseIsOutlier(Purchase purchase, TreeSet<Integer> neighbors){
		BoundedTreeSet<Purchase> bts = new BoundedTreeSet<Purchase> (T);
		for(Integer id : neighbors){
			bts.addAll(members.get(id).getPurchases());
		}
		List<Double> amounts = new ArrayList<Double>();
		for (Purchase p : bts){
			amounts.add(p.getAmount());
		} 
		Double [] par =  StatUtils.MeanSD(amounts);
		if (StatUtils.isPositiveOutlier(purchase.getAmount(), par[0], par[1], 3.0)) {
			return purchase.toString(par[0], par[1]);
		}
		return null;
	}
}
