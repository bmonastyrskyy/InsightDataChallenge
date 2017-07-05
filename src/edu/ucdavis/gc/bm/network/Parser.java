package edu.ucdavis.gc.bm.network;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Parser - collection of methods to process the input stream.
 * 
 * @author Bohdan Monastyrskyy
 * 
 */
public class Parser {
	/**
	 * The variable of type Timestamp used to resolve ties in events if the
	 * timestamps are the same.
	 */
	private Timestamp previous = null;
	/**
	 * The variable of type Long used to resolve ties in events if the
	 * timestamps are the same.
	 */
	private long nano = 0;

	/**
	 * The method parses line and detects the event type.
	 * 
	 * @param line
	 * @return
	 */
	public String detectEvent(String line) throws BadFormatException {
		line = line.replaceAll("\\s+", "");
		Pattern p = Pattern.compile("\\{\"event_type\":\"([a-z]+)\".*");
		Matcher m = p.matcher(line);
		if (m.find()) {
			String event = m.group(1);
			return event;
		} else {
			throw new BadFormatException();
		}
	}

	/**
	 * Parse timestamp and handle with ties.
	 * 
	 * @param line
	 * @return
	 */
	public String parseTimestamp(String line) throws BadFormatException {
		Pattern p = Pattern
				.compile(".*\"timestamp\":\"([0-9]+-[0-9]+-[0-9]+ [0-9]+:[0-9]+:[0-9]+)\".*");
		Matcher m = p.matcher(line);
		if (m.find()) {
			String timestamp = m.group(1);
			Timestamp curTS = Timestamp.valueOf(timestamp);
			if (previous == null || previous.before(curTS)) {
				previous = curTS;
				nano = 0;
			} else {
				nano++;
				timestamp = String.format("%s.%09d", timestamp, nano);
				curTS = Timestamp.valueOf(timestamp);
			}
			return curTS.toString();
		} else {
			throw new BadFormatException();
		}
	}

	/**
	 * The method parses the purchase line.
	 * 
	 * @param line
	 * @return
	 */
	public HashMap<String, String> parsePurchase(String line)
			throws BadFormatException {
		String timestamp = parseTimestamp(line);
		line = line.replaceAll("\\s+", "");
		// {"event_type":"purchase", "timestamp":"2017-06-13 11:33:01", "id":
		// "1", "amount": "16.83"}
		Pattern p = Pattern
				.compile("\\{\"event_type\":\"purchase\",\"timestamp\":\"\\S+\",\"id\":\"(\\d+)\",\"amount\":\"(\\S+)\"\\}");
		Matcher m = p.matcher(line);
		if (m.find()) {
			String id = m.group(1);
			String amount = m.group(2);
			HashMap<String, String> result = new HashMap<String, String>();
			result.put("id", id);
			result.put("timestamp", timestamp);
			result.put("amount", amount);
			return result;
		} else {
			throw new BadFormatException();
		}
	}

	/**
	 * The method parses the "befriend" line.
	 * 
	 * @param line
	 * @return
	 */
	public HashMap<String, String> parseBeFriend(String line)
			throws BadFormatException {
		String timestamp = parseTimestamp(line);
		line = line.replaceAll("\\s+", "");
		// {"event_type":"befriend", "timestamp":"2017-06-13 11:33:01", "id1":
		// "1", "id2": "2"}
		Pattern p = Pattern
				.compile("\\{\"event_type\":\"befriend\",\"timestamp\":\"\\S+\",\"id1\":\"(\\d+)\",\"id2\":\"(\\d+)\"\\}");
		Matcher m = p.matcher(line);
		if (m.find()) {
			String id1 = m.group(1);
			String id2 = m.group(2);
			HashMap<String, String> result = new HashMap<String, String>();
			result.put("id1", id1);
			result.put("timestamp", timestamp);
			result.put("id2", id2);
			return result;
		} else {
			throw new BadFormatException();
		}
	}

	/**
	 * The method parses the "unfriend" line.
	 * 
	 * @param line
	 * @return
	 */
	public HashMap<String, String> parseUnFriend(String line)
			throws BadFormatException {
		String timestamp = parseTimestamp(line);
		line = line.replaceAll("\\s+", "");
		// {"event_type":"befriend", "timestamp":"2017-06-13 11:33:01", "id1":
		// "1", "id2": "2"}
		Pattern p = Pattern
				.compile("\\{\"event_type\":\"unfriend\",\"timestamp\":\"\\S+\",\"id1\":\"(\\d+)\",\"id2\":\"(\\d+)\"\\}");
		Matcher m = p.matcher(line);
		if (m.find()) {
			String id1 = m.group(1);
			String id2 = m.group(2);
			HashMap<String, String> result = new HashMap<String, String>();
			result.put("id1", id1);
			result.put("timestamp", timestamp);
			result.put("id2", id2);
			return result;
		} else {
			throw new BadFormatException();
		}
	}

	/**
	 * The method parses the first line of the batch_log file.<br>
	 * It's supposed to be in format:<br>
	 * {"D":"3", "T":"50"}
	 * 
	 * @param line
	 * @return
	 */
	public HashMap<Character, Integer> parseNetworkParameters(String line) {
		line = line.replaceAll("\\s+", "");
		HashMap<Character, Integer> result = new HashMap<Character, Integer>();
		Pattern p = Pattern.compile("\\{\"D\":\"(\\d+)\",\"T\":\"(\\d+)\"\\}");
		Matcher m = p.matcher(line);
		if (m.find()) {
			result.put('D', Integer.valueOf(m.group(1)));
			result.put('T', Integer.valueOf(m.group(2)));
		}
		return result;
	}

}