import java.io.PrintWriter;

import java.lang.String;



/**
 * Implementation of the run queue interface using an Ordered Link List.
 *
 * Your task is to complete the implementation of this class.
 * You may add methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan.
 */
public class OrderedLinkedListRQ implements Runqueue {
	
    /** Reference to head node. */
    protected Proc mHead;
    
    /** Reference to tail of list. */
    protected Proc mTail;
    
    /** Length of list. */
    protected int mLength;

    
    /**
     * Constructs empty linked list
     */
    public OrderedLinkedListRQ() {
        mHead = null;
        mTail = null;
        mLength = 0;
    }  // end of OrderedLinkedList()


    @Override
    public void enqueue(String procLabel, int vt) {
        // Implement me
        Proc newProc = new Proc(procLabel, vt);

        // If head is empty, then list is empty and head reference need to be initialised.
        if (mLength == 0) {
            mHead = newProc;
            mTail = newProc;
        } 
        // otherwise distinguish where Proc needs to be placed, and insert it there.
        else {
        	int insertIndex = getInsertIndex(vt);

            // if index = 0, we should replace mHead with newNode
            if (insertIndex == 0) {
            	newProc.setNext(mHead);
            	newProc.mNext.setPrev(newProc);
                mHead = newProc;
            } else if(insertIndex == mLength) {
            	newProc.setPrev(mTail);
            	newProc.mPrev.setNext(newProc);
            	mTail = newProc;
            } else {
                Proc currProc = mHead;
                for (int i = 0; i < insertIndex-1; ++i) {
                	currProc = currProc.getNext();
                }

                newProc.setNext(currProc.mNext);
                newProc.setPrev(currProc);
                currProc.setNext(newProc);
                newProc.mNext.setPrev(newProc);
            }
        }

        mLength++;

    } // end of enqueue()
    
    


    @Override
    public String dequeue() {
        // Implement me
    	
    	if(mLength == 1) {
    		mHead = null;
    		mTail = null;
    	} else {
        	Proc currProc = mHead.mNext;
	    	currProc.setPrev(null);
	    	mHead = currProc;
    	}
    	
    	mLength--;
    	
        return ""; // placeholder, modify this
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel) {
        // Implement me
    	boolean result = false;
    	Proc currProc = mHead;
    	
    	while(currProc != null) {
	    	if(currProc.getProcLabel().equalsIgnoreCase(procLabel)) {
	    		result = true;
	    		break;
	    	} else
	    		currProc = currProc.mNext;
    	}
        return result; // placeholder, modify this
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {
        // Implement me

        return false; // placeholder, modify this
    } // End of removeProcess()


    @Override
    public int precedingProcessTime(String procLabel) {
        // Implement me

        return -1; // placeholder, modify this
    } // end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
        // Implement me

        return -1; // placeholder, modify this
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
        //Implement me
    	Proc currProc = mHead;
    	for(int i = 0; i < mLength; i++) {
    		System.out.print(currProc.getProcLabel() + " ");
    		currProc = currProc.getNext();
    	}

    } // end of printAllProcess()
    
    
    public int getInsertIndex(int vt) {
    	
    	int index = 0;
    	int i = mLength;
    	
    	Proc currProc = mTail;
    	if(mLength > 1) {
    		while(currProc != null) {   			
		    	if(vt >= currProc.getRunTime()) {
		    		index = i;
		    		break;
		    	} else {
		    		currProc = currProc.getPrev();
		    		i--;
		    	}			
	    	} 	
    	} else {
    		if(vt >= currProc.getRunTime())
    			index = 1;
    	}
    	return index;
    	
    }
    
    
    
    /**
     * Proc type, inner private class.
     */
	private class Proc {

        /** Process Label. */
        protected String mProcLabel;
        
        /** Process RunTime. */
        protected int mVt;
        
        /** Reference to next node. */
        protected Proc mNext;
        
        /** Reference to previous node. */
        protected Proc mPrev;

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

        public void setProcess(String procLabel) {
        	mProcLabel = procLabel;
        }

        public void setRunTime(int vt) {
        	mVt = vt;
        }


        public void setNext(Proc next) {
            mNext = next;
        }
        
        public void setPrev(Proc prev) {
            mPrev = prev;
        }
    } 

} // end of class OrderedLinkedListRQ
