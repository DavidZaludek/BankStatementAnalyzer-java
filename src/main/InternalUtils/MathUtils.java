package main.InternalUtils;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class MathUtils {

	private static double calcMeanCI(SummaryStatistics stats, double level) {
		try {
			TDistribution tDist = new TDistribution(stats.getN() - 1);
			double critVal = tDist.inverseCumulativeProbability(1.0 - (1 - level) / 2);
			return critVal * stats.getStandardDeviation() / Math.sqrt(stats.getN());
		} catch (MathIllegalArgumentException e) {
			return Double.NaN;
		}
	}
}
