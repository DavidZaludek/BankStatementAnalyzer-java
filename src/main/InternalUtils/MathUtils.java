package main.InternalUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class MathUtils {

	public static double calcMeanCI(SummaryStatistics stats, double level) {
		try {
			TDistribution logNormalDistribution = new TDistribution(stats.getMean(),stats.getStandardDeviation());
			double critVal = logNormalDistribution.inverseCumulativeProbability(1.0 - (1 - level) / 2);
			return critVal * stats.getStandardDeviation() / Math.sqrt(stats.getN());
		} catch (MathIllegalArgumentException e) {
			return Double.NaN;
		}
	}

	public static long[] calcHistogramData(ArrayList<Double> data){
		final int BIN_COUNT = 50;

		double[] tmpData = ArrayUtils.toPrimitive(data.toArray(new Double[0]));

		Arrays.sort(tmpData);

		tmpData = Arrays.copyOfRange(tmpData,0,(int)Math.floor(tmpData.length / 100 * 99.999) );

		long[] histogram = new long[BIN_COUNT];
		org.apache.commons.math3.random.EmpiricalDistribution distribution = new org.apache.commons.math3.random.EmpiricalDistribution(BIN_COUNT);
		distribution.load(tmpData);
		int k = 0;

		for(org.apache.commons.math3.stat.descriptive.SummaryStatistics stats: distribution.getBinStats())
		{
			histogram[k++] = stats.getN();
		}

		return histogram;
	}

	public long[] calcHistogramLogNormalDistribution(double[] data){



		return null;
	}
}
