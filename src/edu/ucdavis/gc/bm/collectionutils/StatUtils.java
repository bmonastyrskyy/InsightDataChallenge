package edu.ucdavis.gc.bm.collectionutils;

import java.util.List;

public class StatUtils {
	/**
	 * Static method calculates mean and standard deviation.
	 * 
	 * @param list
	 * @return
	 */
	public static Double[] MeanSD(final List<Double> list) {
		Double[] result = new Double[2];
		if (list == null || list.size() == 0 || list.size() == 1) {
			result[0] = null;
			result[1] = null;
		} else {
			double mean = 0.0;
			for (Double e : list) {
				mean += e;
			}
			mean /= list.size();
			double sd = 0.0;
			for (Double e : list) {
				sd += (e - mean) * (e - mean);
			}
			sd /= list.size();
			sd = Math.sqrt(sd);
			result[0] = mean;
			result[1] = sd;
		}
		return result;
	}

	/**
	 * The method checks if an element is the positive outlier of the list.<br>
	 * The positive outlier satisfies the condition: e > mean + factor*SD<br>
	 * 
	 * @param list
	 *            - list of elements
	 * @param e
	 *            - element of the list
	 * @param sdFactor
	 *            - factor used in the definition of outlier - default value 3.0
	 * @return
	 */
	public static boolean isPositiveOutlier(final List<Double> list, Double e,
			Double sdFactor) {
		if (list == null || list.size() == 0 || list.size() == 1) {
			return false;
		} else {
			if (sdFactor == null || sdFactor < 0.0) {
				sdFactor = 3.0;
			}
			Double[] par = StatUtils.MeanSD(list);
			if (e > par[0] + sdFactor * par[1]) {
				return true;
			} else {
				return false;
			}
		}
	}
	/**
	 * The method checks if an element is the positive outlier of the list.<br>
	 * The positive outlier satisfies the condition: e > mean + factor*SD<br>
	 * 
	 * @param e
	 * @param mean
	 * @param sd
	 * @param sdFactor
	 * @return
	 */
	public static boolean isPositiveOutlier(Double e, Double mean, Double sd,
			Double sdFactor) {

		if (sdFactor == null || sdFactor < 0.0) {
			sdFactor = 3.0;
		}
		if (e > mean + sdFactor * sd) {
			return true;
		} else {
			return false;
		}

	}
}