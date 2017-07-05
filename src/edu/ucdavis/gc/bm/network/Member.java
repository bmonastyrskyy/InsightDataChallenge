package edu.ucdavis.gc.bm.network;

import java.util.TreeSet;

import edu.ucdavis.gc.bm.collectionutils.BoundedTreeSet;

/**
 * The class represents a member of network.
 * 
 * @author Bohdan Monastyrskyy
 * 
 */
public class Member {
	/**
	 * Unique integer member's ID.
	 */
	private final int id;
	/**
	 * Chronologically ordered set of recent purchases made by the member.
	 */
	private final BoundedTreeSet<Purchase> purchases;
	/**
	 * Set of other members' ids who are friends with the member.
	 */
	private TreeSet<Integer> friends = new TreeSet<Integer>();

	/**
	 * Constructor of the class - sets up the id value.
	 * 
	 * @param id
	 *            - member's id
	 * @purchasesSizeLimit - number of recent purchases to store
	 */
	public Member(int id, int purchasesSizeLimit) {
		this.id = id;
		purchases = new BoundedTreeSet<Purchase>(purchasesSizeLimit);
	}

	/**
	 * The method adds the current purchase.<br>
	 * The BoundedTreeSet class controls the size of the storage.
	 * 
	 * @param currPurchase
	 */
	public void addPurchase(Purchase currPurchase) {
		this.purchases.add(currPurchase);
	}

	/**
	 * Getter.
	 * 
	 * @return the members' id.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Getter.
	 * 
	 * @return set of friends' ids.
	 */
	public TreeSet<Integer> getFriends() {
		return friends;
	}
	/**
	 * Getter.
	 * 
	 * @return set of T recent purchases.
	 */
	public BoundedTreeSet<Purchase> getPurchases() {
		return purchases;
	}
	
	/**
	 * Set up friendship with other member.
	 * 
	 * @param otherMemberID
	 */
	public void beFriend(int otherMemberID) {
		this.friends.add(otherMemberID);
	}

	/**
	 * Cancel friendship with other member.
	 * 
	 * @param otherMemberID
	 */
	public void unFriend(int otherMemberID) {
		this.friends.remove(otherMemberID);
	}

	@Override
	public int hashCode() {
		return id / 1000; // optimization under assumption of the capacity of
							// the network
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Member)) {
			return false;
		}
		Member m = (Member) other;
		return this.id == m.getID();

	}
}
