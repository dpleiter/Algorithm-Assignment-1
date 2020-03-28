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

        public void addChild(Node nodeToAdd) {
            if (nodeToAdd.getVt() > this.vt) {
                if (this.parent != null && nodeToAdd.getVt() == this.parent.getVt()) {
                    // Insert duplicate key
                    nodeToAdd.setParent(this.parent);
                    nodeToAdd.setLeft(this);

                    this.parent.setLeft(nodeToAdd);
                    this.parent = nodeToAdd;
                } else if (this.right == null) {
                    this.right = nodeToAdd;
                    nodeToAdd.setParent(this);
                } else {
                    this.right.addChild(nodeToAdd);
                }
            } else {
                if (this.left == null) {
                    this.left = nodeToAdd;
                    nodeToAdd.setParent(this);
                } else {
                    this.left.addChild(nodeToAdd);
                }
            }
        }

        public Node getParent() {
            return this.parent;
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

        public void setParent(Node node) {
            this.parent = node;
        }

        public void setLeft(Node node) {
            this.left = node;
        }

        public void printTree() {
            if (this.left != null) {
                this.left.printTree();
            }

            System.out.print(this.procLabel + " ");

            if (this.right != null) {
                this.right.printTree();
            }
        }

        public Node findMinimumVt() {
            if (this.left == null) {
                return this;
            } else {
                return this.left.findMinimumVt();
            }
        }

        public Boolean findProcLabel(String procLabel) {
            if (procLabel.compareTo(this.procLabel) == 0) {
                return true;
            } else {
                return (this.left != null && this.left.findProcLabel(procLabel))
                        || (this.right != null && this.right.findProcLabel(procLabel));
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
        Node nodeToDequeue = head.findMinimumVt();

        if (nodeToDequeue == this.head) {
            // dequeue the head
            // as no nodes to left, set head as the right node
            this.head = this.head.getRight();
        } else {
            nodeToDequeue.getParent().setLeft(null);
        }

        return nodeToDequeue.getProcLabel();
    } // end of dequeue()

    @Override
    public boolean findProcess(String procLabel) {
        return this.head.findProcLabel(procLabel);
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

        System.out.print("\n");
    }

} // end of class BinarySearchTreeRQ
