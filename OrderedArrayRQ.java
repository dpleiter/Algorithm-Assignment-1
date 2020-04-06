import java.io.PrintWriter;
import java.lang.String;

/**
 * Implementation of the Runqueue interface using an Ordered Array.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan
 */
public class OrderedArrayRQ implements Runqueue {
	private Proc array[];

	/**
	 * Constructs empty queue
	 */
	public OrderedArrayRQ() {
		array = null;
	} // end of OrderedArrayRQ()

	@Override
	public void enqueue(String procLabel, int vt) {
		Proc enqueuedProc = new Proc(procLabel, vt);

		if (array == null) {
			array = new Proc[] { enqueuedProc };
		} else {
			Proc[] tempArray = new Proc[array.length + 1];

			Integer position = getPosition(vt);

			for (int i = 0; i < position; i++) {
				tempArray[i] = array[i];
			}

			tempArray[position] = enqueuedProc;

			for (int i = position + 1; i < tempArray.length; i++) {
				tempArray[i] = array[i - 1];
			}

			array = tempArray;
		}
	} // end of enqueue()

	@Override
	public String dequeue() throws NullPointerException {
		if (array == null) {
			throw new NullPointerException("Nothing to dequeue.");
		}

		Proc dequeuedProc = array[0];
		int newArrayLength = array.length - 1;
		Proc tempArray[] = new Proc[newArrayLength];

		for (int i = 1; i < array.length; i++) {
			tempArray[i - 1] = array[i];
		}

		if (newArrayLength == 0)
			array = null;
		else
			array = tempArray;

		return dequeuedProc.getProcLabel();
	} // end of dequeue()

	@Override
	public boolean findProcess(String procLabel) {
		// Implement me
		for (int i = 0; i < array.length; i++) {
			if (array[i].getProcLabel().compareTo(procLabel) == 0) {
				return true;
			}
		}

		return false;
	} // end of findProcess()

	@Override
	public boolean removeProcess(String procLabel) throws NullPointerException {
		if (array == null)
			throw new NullPointerException("Queue is empty");

		Integer position = getPosition(procLabel);
		int newArrayLength = array.length - 1;
		Proc tempArray[] = new Proc[newArrayLength];

		if (position == null) {
			return false;
		}

		for (int i = 0; i < position; i++) {
			tempArray[i] = array[i];
		}

		for (int j = position + 1; j < array.length; j++) {
			tempArray[j - 1] = array[j];
		}

		if (newArrayLength == 0) {
			array = null;
		} else {
			array = tempArray;
		}

		return true;
	} // end of removeProcess()

	@Override
	public int precedingProcessTime(String procLabel) {
		int preceedingRunTime = 0;
		Integer position = this.getPosition(procLabel);

		if (position != null) {
			for (int i = 0; i < position; i++) {
				Proc currProc = array[i];
				preceedingRunTime = preceedingRunTime + currProc.getRunTime();
			}
		} else
			preceedingRunTime = -1;

		return preceedingRunTime;
	}// end of precedingProcessTime()

	@Override
	public int succeedingProcessTime(String procLabel) {
		int proceedingRunTime = 0;
		Integer position = this.getPosition(procLabel);

		if (position != null) {
			for (int i = position + 1; i < array.length; i++) {
				Proc currProc = array[i];
				proceedingRunTime = proceedingRunTime + currProc.getRunTime();
			}
		} else
			proceedingRunTime = -1;

		return proceedingRunTime;
	} // end of precedingProcessTime()

	@Override
	public void printAllProcesses(PrintWriter os) {
		for (int i = 0; i < array.length; i++) {
			os.print(array[i].getProcLabel() + " ");
		}
		os.print("\n");
	} // end of printAllProcesses()

	// 
	public Integer getPosition(String procLabel) {
		for (int i = 0; i < array.length; i++) {
			if (procLabel.equalsIgnoreCase(array[i].getProcLabel())) {
				return i;
			}
		}

		return null;
	}

	public Integer getPosition(int vt) {
		
        int first = 0;
        int last = array.length - 1;
        
        if(array.length == 1) {
        	if(array[0].getRunTime() <= vt)
        		return 1;
        	else
        		return 0;
        } else {
            while(first <= last) {
            	int mid = (int)Math.ceil((first + last) / 2);
            	
            	if(array[first].getRunTime() <= vt && array[last].getRunTime() < vt) {
            		return last+1;
            	} else if(array[mid].getRunTime() < vt) {
            		first = mid + 1;
            	} else if(array[mid].getRunTime() > vt) {
            		last = mid - 1;
            	}
            }
        }
        
        return first;
	
	}

	private class Proc {
		/** Process Label. */
		private String mProcLabel;

		/** Process RunTime. */
		private int vt;

		public Proc(String procLabel, int vt) {
			this.mProcLabel = procLabel;
			this.vt = vt;
		}

		public String getProcLabel() {
			return mProcLabel;
		}

		public int getRunTime() {
			return vt;
		}
	}

} // end of class OrderedArrayRQ