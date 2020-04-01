import java.io.PrintWriter;
import java.lang.String;


/**
 * Implementation of the Runqueue interface using an Ordered Array.
 *
 * Your task is to complete the implementation of this class.
 * You may add methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan
 */
public class OrderedArrayRQ implements Runqueue {

    protected Proc array[];

    /**
     * Constructs empty queue
     */
    public OrderedArrayRQ() {
        // Implement Me
        array = null;
    }  // end of OrderedArrayRQ()


    @Override
    public void enqueue(String procLabel, int vt) {
    	
    	Proc enqueuedProc = new Proc(procLabel, vt);
     	int newArrayLength;
    	if (array == null)
    		newArrayLength = 1;
    	else 
    		newArrayLength = array.length + 1;
        Proc tempArray[] = new Proc[newArrayLength];
        
        if(newArrayLength == 1) {
        	tempArray[0] = enqueuedProc;
        } else {
        	
        	Integer position = getPosition(vt);
        	
        	for(int i = 0; i < position; i++) {
        		tempArray[i] = array[i];
        	}
        	
        	tempArray[position] = enqueuedProc;
        	
        	for(int i = position+1; i < newArrayLength; i++) {
        		tempArray[i] = array[i-1];
        	}
        }

        array = tempArray;
    } // end of enqueue()


    @Override
    public String dequeue() throws NullPointerException {
        // Implement me
        if(array==null)
    		throw new NullPointerException("Nothing to dequeue.");
  
        Proc dequeuedProc = array[0];
        int newArrayLength = array.length - 1;
        Proc tempArray[] = new Proc[newArrayLength];

    	for(int i=1; i < array.length; i++) {
    		tempArray[i-1] = array[i];
    	}
    	
    	if(newArrayLength == 0)
    		array = null;
    	else
    		array = tempArray;

        return dequeuedProc.getProcLabel() + " has left the queue.";
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel){
        // Implement me
    	boolean found;
    	Integer position = getPosition(procLabel);
    	
    	if(position == null)
    		found = false;
    	else
    		found = true;
    	
    	return found;
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) throws NullPointerException {

    	if(array == null)
    		throw new NullPointerException("Queue is empty");
    	
    	boolean removedProc = false;
        Integer position = getPosition(procLabel);
        int newArrayLength = array.length - 1;
		Proc tempArray[] = new Proc[newArrayLength];    	
		
    	if(position == null) {
    		removedProc = false;
    	} else {    		
    		for(int i = 0; i < position; i++) {
    			tempArray[i] = array[i];
    		}
    		for(int j = position+1; j < array.length; j++) {
    			tempArray[j-1] = array[j];
    		}
    		
    		if(newArrayLength == 0)
    			array = null;
    		else
    			array = tempArray;
    		
    		removedProc = true;
    	}
    	
    	return removedProc;
    } // end of removeProcess()


    @Override
    public int precedingProcessTime(String procLabel) {
 
    	int preceedingRunTime = 0;
        Integer position = this.getPosition(procLabel);
    	
    	if(position != null) {    		
	    	for(int i=0; i < position; i++) {
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
    	
    	if(position != null) {    		
	    	for(int i=position+1; i < array.length; i++) {
	    		Proc currProc = array[i];
	    		proceedingRunTime = proceedingRunTime + currProc.getRunTime();
	    	}
    	} else
    		proceedingRunTime = -1;
    		
    	return proceedingRunTime;
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
 
    	String processes = "";
    	
    	for(int i=0; i < array.length; i++) {
    		Proc currProc = array[i];
    		processes = processes.concat(currProc.getProcLabel() + " ");
    	}
    	os.print(processes.trim());

    } // end of printAllProcesses()
    
    
    public Integer getPosition(String procLabel) {
    	
    	Integer position = null;
        
    	for(int i=0; i < array.length; i++) {
    		Proc currProc = array[i];
    		if(procLabel.equalsIgnoreCase(currProc.getProcLabel())) {
    			position = i;
    			break;
    		}
    	}
    	
    	return position;
    }
    
    public Integer getPosition(int vt) {
    	
    	Integer position = 0;
        
    	for(int i = array.length-1; i >= 0; i--) {
    		Proc currProc = array[i];
        	if(vt >= currProc.getRunTime()) {
        		position = i+1;
        		break;
        	}
    	}
        		   		
    	return position;
    }

} // end of class OrderedArrayRQ
