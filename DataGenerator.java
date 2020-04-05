import java.io.*;
import java.util.Random;

/**
 * Generates collection of integers from sampling a uniform distribution.
 *
 * @author Jeffrey Chan
 */
public class DataGenerator
{
	/** Program name. */
	protected static final String progName = "DataGenerator";

	/** Start of integer range to generate values from. */
	protected int mStartOfRange;
	/** End of integer range to generate values from. */
	protected int mEndOfRange;
	/** Random generator to use. */
	Random mRandGen;


	/**
	 * Constructor.
	 *
	 * @param startOfRange Start of integer range to generate values.
	 * @param endOfRange End of integer range to generate values.
	 * @throws IllegalArgumentException If range of integers is inappropriate
	 */
	public DataGenerator(int startOfRange, int endOfRange) throws IllegalArgumentException {
		if (startOfRange < 0 || endOfRange < 0 || startOfRange > endOfRange) {
			throw new IllegalArgumentException("startOfRange or endOfRange is invalid.");
		}
		mStartOfRange = startOfRange;
		mEndOfRange = endOfRange;
		// use current time as seed
		mRandGen = new Random(System.currentTimeMillis());
	} // end of DataGenerator()


	/**
	 * Sample without replacement, using "Algorithm R" by Jeffrey Vitter, in paper "Random sampling without a reservoir".
	 * This algorithm has O(size of range) time complexity.
	 *
	 * @param sampleSize Number of samples to generate.
	 * @throws IllegalArgumentException When sampleSize is greater than the valid integer range.
	 */
	public int[][] sampleWithOutReplacement(int sampleSize) throws IllegalArgumentException {
	    int populationSize = mEndOfRange - mStartOfRange + 1;

	    if (sampleSize > populationSize) {
	    	throw new IllegalArgumentException("SampleSize cannot be greater than populationSize for sampling without replacement.");
	    }

	    int[][] samples = new int[sampleSize][2];
	    // fill it with initial values in the range
	    for (int i = 0; i < sampleSize; i++) {
	    	samples[i][0] = i + mStartOfRange;
	    	samples[i][1] = i + mStartOfRange;
	    }

	    // replace
	    for (int j = sampleSize; j < populationSize; j++) {
	    	int t = mRandGen.nextInt(j+1);
	    	if (t < sampleSize) {
	    		samples[t][0] = j + mStartOfRange;
	    		samples[t][1] = j + mStartOfRange;
	    	}
	    }

	   return samples;
	} // end of sampleWithOutReplacement()


	/**
	 * Error message.
	 */
	public static void usage() {
		System.err.println(progName + ": <start of range to sample from> <end of range to sample from> <number of values to sample>");
		System.exit(1);
	} // end of usage()


	/**
	 * Main method.
	 */
	public static void main(String[] args) {

		// check correct number of command line arguments
		if (args.length != 3) {
			usage();
		}


		try {
			// integer range
			int startOfRange = Integer.parseInt(args[0]);
			int endOfRange = Integer.parseInt(args[1]);

			// number of values to sample
			int sampleSize = Integer.parseInt(args[2]);

			DataGenerator gen = new DataGenerator(startOfRange, endOfRange);
			int[][] samples = null;
			samples = gen.sampleWithOutReplacement(sampleSize);
			
			// Write samples to file
			PrintWriter enqueueWriter = new PrintWriter("enqueue" + sampleSize + ".txt", "UTF-8");
			PrintWriter dequeueWriter = new PrintWriter("dequeue" + sampleSize + ".txt", "UTF-8");
			if (samples != null) {
				for (int i = 0; i < samples.length; i++) {
					enqueueWriter.println("EN P" + samples[i][0] + " " + samples[i][1]);
					dequeueWriter.println("DE");
				}
			}
			enqueueWriter.close();
			dequeueWriter.close();

		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			usage();
		}

	} // end of main()
} // end of class DataGenerator
