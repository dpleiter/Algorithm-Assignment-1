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

    protected String array[];

    /**
     * Constructs empty queue
     */
    public OrderedArrayRQ() {
        // Implement Me
        array = null;
    }  // end of OrderedArrayRQ()


    @Override
    public void enqueue(String procLabel, int vt) {
    	
        // Implement me
    	int new_array_length;
    	if (array == null)
    		new_array_length = 1;
    	else 
    		new_array_length = array.length + 1;
    	
        String temp_array[] = new String[new_array_length];
        String enqueued_node = procLabel + "," + Integer.toString(vt);
        
        if(new_array_length == 1) {
        	temp_array[0] = enqueued_node;
        } else {
        	
        	Integer position = this.getPosition(vt);
        	
        	for(int i = 0; i < position; i++) {
        		temp_array[i] = array[i];
        	}
        	
        	temp_array[position] = enqueued_node;
        	
        	for(int i = position+1; i < new_array_length; i++) {
        		temp_array[i] = array[i-1];
        	}
        }

        array = temp_array;
    } // end of enqueue()


    @Override
    public String dequeue() throws NullPointerException {
        // Implement me
        if(array==null)
    		throw new NullPointerException("Nothing to dequeue.");
  
        String dequeued_node = array[0];
        int new_array_length = array.length - 1;
        String temp_array[] = new String[new_array_length];

    	for(int i=1; i < array.length; i++) {
    		temp_array[i-1] = array[i];
    	}
    	
    	if(new_array_length == 0)
    		array = null;
    	else
    		array = temp_array;

        return dequeued_node.split(",")[0].trim() + " has left the queue.";
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel){
        // Implement me
    	boolean found;
    	Integer position = this.getPosition(procLabel);
    	
    	if(position == null)
    		found = false;
    	else
    		found = true;
    	
    	return found;
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) throws NullPointerException {
        // Implement me
    	if(array == null)
    		throw new NullPointerException("Queue is empty");
    	
    	boolean removed = false;
        Integer position = this.getPosition(procLabel);
        int new_array_length = array.length - 1;
		String temp_array[] = new String[new_array_length];    	
		
    	if(position == null) {
    		removed = false;
    	} else {    		
    		for(int i = 0; i < position; i++) {
    			temp_array[i] = array[i];
    		}
    		for(int j = position+1; j < array.length; j++) {
    			temp_array[j-1] = array[j];
    		}
    		
    		if(new_array_length == 0)
    			array = null;
    		else
    			array = temp_array;
    		
    		removed = true;
    	}
    	
    	return removed;
    } // end of removeProcess()


    @Override
    public int precedingProcessTime(String procLabel) {
        // Implement me

    	int preceeding_run_time = 0;
        Integer position = this.getPosition(procLabel);
    	
    	if(position != null) {    		
	    	for(int i=0; i < position; i++) {
	    		preceeding_run_time = preceeding_run_time + Integer.parseInt(array[i].split(",")[1].trim());
	    	}
    	} else
    		preceeding_run_time = -1;
    		
    	return preceeding_run_time;
    }// end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
        // Implement me
    	
    	int proceeding_run_time = 0;
        Integer position = this.getPosition(procLabel);
    	
    	if(position != null) {    		
	    	for(int i=position+1; i < array.length; i++) {
	    		proceeding_run_time = proceeding_run_time + Integer.parseInt(array[i].split(",")[1].trim());
	    	}
    	} else
    		proceeding_run_time = -1;
    		
    	return proceeding_run_time;
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
        //Implement me
    	String queue = "";
    	
    	for(int i=0; i < array.length; i++) {
    		queue = queue.concat(array[i].split(",")[0].trim().concat(" "));
    	}
    	queue = queue.trim();
    	System.out.println(queue);

    } // end of printAllProcesses()
    
    
    public Integer getPosition(String procLabel) {
    	
    	Integer position = null;
        
    	for(int i=0; i < array.length; i++) {
    		if(procLabel.equalsIgnoreCase(array[i].split(",")[0].trim())) {
    			position = i;
    			break;
    		}
    	}
    	
    	return position;
    }
    
    public Integer getPosition(int vt) {
    	
    	Integer position = 0;
        
    	for(int i = array.length-1; i >= 0; i--) {
        	if(vt >= Integer.parseInt(array[i].split(",")[1].trim())) {
        		position = i+1;
        		break;
        	}
    	}
        		   		
    	return position;
    }

} // end of class OrderedArrayRQ
