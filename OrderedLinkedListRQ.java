import java.io.PrintWriter;

import java.lang.String;

/**
 * Implementation of the run queue interface using an Ordered Link List. *
 * Originally written by @author Sajal Halder, Minyi Li, Jeffrey Chan
 *
 * Appropiated for Assignment 1 by @author Oliver Eaton
 */
public class OrderedLinkedListRQ implements Runqueue {


	/** Reference to head node. */
	private Proc mHead;


	/** Reference to tail of list. */
	private Proc mTail;


	/** Constructs empty linked list */
	public OrderedLinkedListRQ() {
		this.mHead = null;
		this.mTail = null;
	} // end of OrderedLinkedList()


	/**
	 * Enqueue process into linked list.
	 *
     * @param process label and it's runtime to enqueue.
     */
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


	/**
	 * Dequeue process from linked list.
	 *
	 * @return label of dequeued process.
     */
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


	/**
	 * Find process in linked list.
	 * Utlises getProcByLabel() method.
	 *
     * @param process label to find.
	 * @return true if label found, false if not found.
     */
	@Override
	public boolean findProcess(String procLabel) {
		return getProcByLabel(procLabel) != null;
	} // end of findProcess()


	/**
	 * Remove process from linked list.
	 *
     * @param process label to remove.
	 * @return true if process removed, false if not removed.
     */
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


	/**
	 * Calculate total preceeding runtime
	 *
     * @param process label to calculate runtime prior to.
	 * @return value of total preceeding runtime if label found, otherwise -1.
     */
	@Override
	public int precedingProcessTime(String procLabel) {
		Proc currProc = this.mHead;
		int sum = 0;

		while (currProc != null) {
			if (currProc.getProcLabel().compareTo(procLabel) == 0) {
				return sum;
			}

			sum += currProc.getRunTime();
			currProc = currProc.getNext();
		}

		return -1;
	} // end of precedingProcessTime()


	/**
	 * Calculate total succeeding runtime
	 *
     * @param process label to calculate runtime succeeding from.
	 * @return value of total preceeding runtime if label found, otherwise -1.
     */
	@Override
	public int succeedingProcessTime(String procLabel) {
		Proc currProc = this.mTail;
		int sum = 0;

		while (currProc != null) {
			if (currProc.getProcLabel().compareTo(procLabel) == 0) {
				return sum;
			}

			sum += currProc.getRunTime();
			currProc = currProc.getPrev();
		}

		return -1;
	} // end of precedingProcessTime()


	/**
	 * Print processes in linked list.
	 *
     * @param print writer object.
     */
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



	/**
	 * Helper method: get a Proc obejct by it's label
	 * Used in conjunction with removeProcess(String procLabel) and findProcess(String procLabel).
	 *
     * @param process label.
	 * @return Proc object with label = procLabel.
     */
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


	/**
	 * Inner class: Proc.
	 * Proc object used in conjuction with OrderedArrayRQ class.
     */
	private class Proc {
		
		/** Process Label. */
		protected String mProcLabel;

		/** Process RunTime. */
		private int mVt;

		/** Reference to next node. */
		private Proc mNext;

		/** Reference to previous node. */
		private Proc mPrev;

		/** Proc constrcutor. */
		public Proc(String procLabel, int vt) {
			mProcLabel = procLabel;
			mVt = vt;
			mNext = null;
			mPrev = null;
		}

		/** Return process label. */
		public String getProcLabel() {
			return mProcLabel;
		}

		/** Return process run time. */
		public int getRunTime() {
			return mVt;
		}

		/** Return process's proceeding process  */
		public Proc getNext() {
			return mNext;
		}

		/** Return process's preceeding process  */
		public Proc getPrev() {
			return mPrev;
		}

		/** Set process's proceeding process  */
		public void setNext(Proc next) {
			mNext = next;
		}

		/** Set process's preceeding process  */
		public void setPrev(Proc prev) {
			mPrev = prev;
		}
	}
} // end of class OrderedLinkedListRQ