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
    	
        Proc newProc = new Proc(procLabel, vt);

        // If head is empty, then list is empty and head reference need to be initialised.
        if (mLength == 0) {
            mHead = newProc;
            mTail = newProc;
        } 
        // otherwise distinguish where Proc needs to be placed, and insert it there.
        else {
        	int insertIndex = getIndex(vt);

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
    	
    	String dequeuedProcess = mHead.getProcLabel();
    	if(mLength == 1) {
    		mHead = null;
    		mTail = null;
    	} else {
        	Proc currProc = mHead.mNext;
	    	currProc.setPrev(null);
	    	mHead = currProc;
    	}
    	
    	mLength--;
    	
        return dequeuedProcess;
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
        return result;
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {
        boolean processRemoved = false;
        
        Integer removeIndex = getIndex(procLabel);
        if(removeIndex != null) {
        	
        	Proc currProc = mHead;
            for (int i = 0; i < removeIndex; i++) {
            	currProc = currProc.getNext();
            }
            
            if(removeIndex == 0 && mLength == 1) {
            	mHead = null;
            	mTail = null;
            } else if(removeIndex == 0 && mLength == 2) {
            	mHead = currProc.mNext;
            	mTail = currProc.mNext;
            	currProc.mNext.setPrev(null);
            } else if(removeIndex == 1 && mLength == 2) {
            	mHead = currProc.mPrev;
            	mTail = currProc.mPrev;
            	currProc.mPrev.setNext(null);
            } else if(removeIndex == 0 && mLength > 2) {
            	mHead = currProc.mNext;
            	currProc.mNext.setPrev(null);
            } else if(removeIndex == mLength-1 && mLength > 2) {
            	mTail = currProc.mPrev;
            	currProc.mPrev.setNext(null);
            } else {
            	currProc.mPrev.setNext(currProc.mNext);
            	currProc.mNext.setPrev(currProc.mPrev);
            }
            
        	processRemoved = true;
        	mLength--;
        	
        } 
        
        return processRemoved;
    } // End of removeProcess()

    
    @Override
    public int precedingProcessTime(String procLabel) {
        
    	int time = -1;
        Integer index = getIndex(procLabel);
        if(index != null) {
        	time = 0;
        	Proc currProc = mHead;
            for (int i = 0; i < index; i++) {
            	time = time + currProc.getRunTime();
            	currProc = currProc.getNext();
            }

        }

        return time;
    } // end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
    	
    	int time = -1;
        Integer index = getIndex(procLabel);
        if(index != null) {
        	time = 0;
        	Proc currProc = mTail;
            for (int i = mLength-1; i > index; i--) {
            	time = time + currProc.getRunTime();
            	currProc = currProc.getPrev();
            }
        }

        return time;
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
        
    	String processes = "";
    	Proc currProc = mHead;
    	for(int i = 0; i < mLength; i++) {
    		processes = processes.concat(currProc.getProcLabel() + " ");
    		currProc = currProc.getNext();
    	}
    	System.out.println(processes.trim());
    	
    } // end of printAllProcess()
    
    
    public Integer getIndex(int vt) {
    	
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
    
    
    public Integer getIndex(String procLabel) {
    	
    	Integer index = null;
    	int i = mLength;
    	
    	Proc currProc = mTail;
    	if(mLength > 1) {
    		while(currProc != null) { 
    			i--;
		    	if(procLabel.equalsIgnoreCase(currProc.getProcLabel())) {
		    		index = i;
		    		break;
		    	} else {
		    		currProc = currProc.getPrev();
		    	}			
	    	} 	
    	} else {
    		if(procLabel.equalsIgnoreCase(mHead.getProcLabel()))
    			index = 0;
    	}
    	
    	return index;
    	
    }

} // end of class OrderedLinkedListRQ
