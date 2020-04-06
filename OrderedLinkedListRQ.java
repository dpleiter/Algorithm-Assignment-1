import java.io.PrintWriter;

import java.lang.String;

/**
 * Implementation of the run queue interface using an Ordered Link List.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan.
 */
public class OrderedLinkedListRQ implements Runqueue {

	/** Reference to head node. */
	private Proc mHead;

	/** Reference to tail of list. */
	private Proc mTail;

	/** Length of list. */

	/**
	 * Constructs empty linked list
	 */
	public OrderedLinkedListRQ() {
		this.mHead = null;
		this.mTail = null;
	} // end of OrderedLinkedList()

	@Override
	public void enqueue(String procLabel, int vt) {
		Proc newProc = new Proc(procLabel, vt);

		if (this.mHead == null) {
			// First item in list
			this.mHead = newProc;
			this.mTail = newProc;
		} else if (newProc.getRunTime() < this.mHead.getRunTime()) {
			// Set new head
			newProc.setNext(this.mHead);
			this.mHead.setPrev(newProc);

			this.mHead = newProc;
		} else if (newProc.getRunTime() >= this.mTail.getRunTime()) {
			// Set new tail
			newProc.setPrev(this.mTail);
			this.mTail.setNext(newProc);

			this.mTail = newProc;
		} else {
			// Insert somewhere between head and tail
			Proc currProc = this.mHead.getNext();

			while (true) {
				if (newProc.getRunTime() < currProc.getRunTime()) {
					newProc.setPrev(currProc.getPrev());
					newProc.getPrev().setNext(newProc);

					newProc.setNext(currProc);
					currProc.setPrev(newProc);

					break;
				}

				currProc = currProc.getNext();
			}
		}
	} // end of enqueue()

	@Override
	public String dequeue() {
		Proc procToDequeue = this.mHead;

		if (this.mHead == this.mTail) {
			// Last item in list
			this.mHead = null;
			this.mTail = null;
		} else {
			this.mHead = this.mHead.getNext();
			this.mHead.setPrev(null);
		}

		return procToDequeue.getProcLabel();
	} // end of dequeue()

	@Override
	public boolean findProcess(String procLabel) {
		return getProcByLabel(procLabel) != null;
	} // end of findProcess()

	@Override
	public boolean removeProcess(String procLabel) {
		Proc procToRemove = getProcByLabel(procLabel);

		if (procToRemove == null) {
			// Proc label not found
			return false;
		} else if (this.mHead == this.mTail) {
			// Last item in list
			this.mHead = null;
			this.mTail = null;
		} else if (procToRemove == this.mHead) {
			// Remove head
			this.mHead = this.mHead.getNext();
			this.mHead.setPrev(null);
		} else if (procToRemove == this.mTail) {
			// Remove tail
			this.mTail = this.mTail.getPrev();
			this.mTail.setNext(null);
		} else {
			// remove other
			procToRemove.getPrev().setNext(procToRemove.getNext());
			procToRemove.getNext().setPrev(procToRemove.getPrev());
		}

		return true;
	} // End of removeProcess()

	@Override
	public int precedingProcessTime(String procLabel) {
		Proc proc = getProcByLabel(procLabel);
		int sum = 0;

		if (proc == null) {
			return -1;
		}

		while (proc.getPrev() != null) {
			proc = proc.getPrev();

			sum += proc.getRunTime();
		}

		return sum;
	} // end of precedingProcessTime()

	@Override
	public int succeedingProcessTime(String procLabel) {
		Proc proc = getProcByLabel(procLabel);
		int sum = 0;

		if (proc == null) {
			return -1;
		}

		while (proc.getNext() != null) {
			proc = proc.getNext();

			sum += proc.getRunTime();
		}

		return sum;
	} // end of precedingProcessTime()

	@Override
	public void printAllProcesses(PrintWriter os) {
		String processes = "";
		Proc currProc = this.mHead;

		while (currProc != null) {
			processes = processes.concat(currProc.getProcLabel() + " ");
			currProc = currProc.getNext();
		}

		os.println(processes.trim());
	} // end of printAllProcess()

	// Helper method
	private Proc getProcByLabel(String proclabel) {
		Proc currentProc = this.mHead;

		while (currentProc != null) {
			if (currentProc.getProcLabel().compareTo(proclabel) == 0) {
				return currentProc;
			}

			currentProc = currentProc.getNext();
		}

		return null;
	}

	private class Proc {
		/** Process Label. */
		protected String mProcLabel;

		/** Process RunTime. */
		private int mVt;

		/** Reference to next node. */
		private Proc mNext;

		/** Reference to previous node. */
		private Proc mPrev;

		public Proc(String procLabel, int vt) {
			mProcLabel = procLabel;
			mVt = vt;
			mNext = null;
			mPrev = null;
		}

		public String getProcLabel() {
			return mProcLabel;
		}

		public int getRunTime() {
			return mVt;
		}

		public Proc getNext() {
			return mNext;
		}

		public Proc getPrev() {
			return mPrev;
		}

		public void setNext(Proc next) {
			mNext = next;
		}

		public void setPrev(Proc prev) {
			mPrev = prev;
		}
	}
} // end of class OrderedLinkedListRQ