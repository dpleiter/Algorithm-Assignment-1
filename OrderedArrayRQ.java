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
        int new_array_length = array.length + 1;
        String temp_array[] = new String[new_array_length];
        String enqueued_node = procLabel + "," + String.valueOf(vt); // could use Integer.toString() here

        temp_array[0] = enqueued_node;
        for(int i=1; i < new_array_length; i++) {
            temp_array[i] = array[i];
        }
        array = temp_array;

    } // end of enqueue()


    @Override
    public String dequeue() throws NullPointerException {
        // Implement me
        if(array==null) {
    		throw new NullPointerException("The queue is already empty.");
    	} else {

            String dequeued_node = array[array.length];
            int new_array_length = array.length - 1
            String temp_array[] = new String[new_array_length];

        	for(int i=0; i < new_array_length; i++) {
        		temp_array[i] = array[i];
        	}
        	array = temp_array;
    	}

        return dequeued_node + "; has left the queue."; // placeholder,modify this
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel){
        // Implement me

        return false; // placeholder, modify this
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {
        // Implement me

        return false; // placeholder, modify this
    } // end of removeProcess()


    @Override
    public int precedingProcessTime(String procLabel) {
        // Implement me

        return -1; // placeholder, modify this
    }// end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
        // Implement me

        return -1; // placeholder, modify this
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
        //Implement me

    } // end of printAllProcesses()

} // end of class OrderedArrayRQ
