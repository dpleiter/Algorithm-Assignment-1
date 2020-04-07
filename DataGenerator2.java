import java.io.*;
import java.util.Random;

/**
 * Generates collection of integers from sampling a uniform distribution.
 *
 * @author Jeffrey Chan
 */
public class DataGenerator2 {
	/** Program name. */
	private static final String progName = "DataGenerator";

	/** Start of integer range to generate values from. */
	private int startOfRange;
	/** End of integer range to generate values from. */
	private int intervalSize;
	/** Random generator to use. */
	Random mRandGen;

	/**
	 * Constructor.
	 *
	 * @param startOfRange Start of integer range to generate values.
	 * @param endOfRange   End of integer range to generate values.
	 * @throws IllegalArgumentException If range of integers is inappropriate
	 */
	public DataGenerator2(int startOfRange, int endOfRange) throws IllegalArgumentException {
		if (startOfRange < 0 || endOfRange < 0 || startOfRange > endOfRange) {
			throw new IllegalArgumentException("startOfRange or endOfRange is invalid.");
		}

		this.startOfRange = startOfRange;
		this.intervalSize = endOfRange - startOfRange + 1;

		// use current time as seed
		mRandGen = new Random(System.currentTimeMillis());
	} // end of DataGenerator()

	/**
	 * Sample without replacement, using "Algorithm R" by Jeffrey Vitter, in paper
	 * "Random sampling without a reservoir". This algorithm has O(size of range)
	 * time complexity.
	 *
	 * @param sampleSize Number of samples to generate.
	 * @throws IllegalArgumentException When sampleSize is greater than the valid
	 *                                  integer range.
	 */
	public int[] sampleWithOutReplacement(int sampleSize) throws IllegalArgumentException {
		int populationSize = intervalSize - startOfRange + 1;

		if (sampleSize > populationSize) {
			throw new IllegalArgumentException(
					"SampleSize cannot be greater than populationSize for sampling without replacement.");
		}

		int[] samples = new int[sampleSize];
		// fill it with initial values in the range
		for (int i = 0; i < sampleSize; i++) {
			samples[i] = i + startOfRange;
		}

		// replace
		for (int j = sampleSize; j < populationSize; j++) {
			int t = mRandGen.nextInt(j + 1);
			if (t < sampleSize) {
				samples[t] = j + startOfRange;
			}
		}

		return samples;
	} // end of sampleWithOutReplacement()

	
	public int[] sampleWithReplacement(int sampleSize) {
		int[] samples = new int[sampleSize];

		for (int i = 0; i < sampleSize; i++) {
			samples[i] = startOfRange + mRandGen.nextInt(this.intervalSize);
		}

		return samples;
	}

	// Message to display on error
	public static void usage() {
		System.err.println(progName
				+ " <number of values to sample>");
		System.exit(1);
	} // end of usage()

	public static void main(String[] args) {
		// check correct number of command line arguments
		if (args.length != 1) {
			usage();
		}

		try {
			// Range of Labels
			int startOfRange = 0;
			int endOfRange = 50000;
			
			int startOfTimeRange = 1;
			int endOfTimeRange = 100;

			// number of values to sample
			int sampleSize = Integer.parseInt(args[0]);

			DataGenerator2 labelGen = new DataGenerator2(startOfRange, endOfRange);
			int[] procLabels = labelGen.sampleWithOutReplacement(sampleSize);
			
			DataGenerator2 timeGen = new DataGenerator2(startOfTimeRange, endOfTimeRange);
			int[] procRunTimes = timeGen.sampleWithReplacement(sampleSize);

			// Write samples to file
			StringBuilder en = new StringBuilder();
			StringBuilder pt = new StringBuilder();
			StringBuilder de = new StringBuilder();
			
			for(int i = 0; i < sampleSize; i++) {
				en.append("EN P" + procLabels[i] + " " + procRunTimes[i] + "\n");
				pt.append("PT P" + procLabels[i] + "\n");
				de.append("DE\n");
			}
			
			StringBuilder processes = en.append(pt).append(de);
			PrintWriter writer = new PrintWriter("in/processes_" + sampleSize + ".txt", "UTF-8");
			writer.print(processes);
			writer.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			usage();
		}
	} // end of main()
} // end of class DataGenerator
