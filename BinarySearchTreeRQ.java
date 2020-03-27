import java.io.PrintWriter;
import java.lang.String;

/**
 * Implementation of the Runqueue interface using a Binary Search Tree.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan
 */
public class BinarySearchTreeRQ implements Runqueue {
    @SuppressWarnings("unused")
    private class Node {
        String procLabel;
        int vt;

        private Node parent;

        private Node left;
        private Node right;

        public Node(String procLabel, int vt) {
            this.procLabel = procLabel;
            this.vt = vt;

            this.left = null;
            this.right = null;
        }

        public void addChild(Node node) {
            if (node.getVt() > this.vt) {
                if (this.right == null) {
                    this.right = node;
                    node.setParent(this);
                } else {
                    this.right.addChild(node);
                }
            } else {
                if (this.left == null) {
                    this.left = node;
                    node.setParent(this);
                } else {
                    this.left.addChild(node);
                }
            }
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getLeft() {
            return this.left;
        }

        public Node getRight() {
            return this.right;
        }

        public int getVt() {
            return this.vt;
        }

        public String getProcLabel() {
            return this.procLabel;
        }

        public void printTree() {
            if (this.left != null) {
                this.left.printTree();
            }

            System.out.println(this.vt);

            if (this.right != null) {
                this.right.printTree();
            }
        }
    }

    Node head;

    /**
     * Constructs empty queue
     */
    public BinarySearchTreeRQ() {
        this.head = null;
    } // end of BinarySearchTreeRQ()

    @Override
    public void enqueue(String procLabel, int vt) {
        Node nodeToAdd = new Node(procLabel, vt);

        if (head == null) {
            this.head = nodeToAdd;
        } else {
            this.head.addChild(nodeToAdd);
        }
    }

    @Override
    public String dequeue() {
        // Implement me

        return ""; // placeholder, modify this
    } // end of dequeue()

    @Override
    public boolean findProcess(String procLabel) {
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
    } // end of precedingProcessTime()

    @Override
    public int succeedingProcessTime(String procLabel) {
        // Implement me

        return -1; // placeholder, modify this
    } // end of precedingProcessTime()

    @Override
    public void printAllProcesses(PrintWriter os) {
        head.printTree();
    }

} // end of class BinarySearchTreeRQ
