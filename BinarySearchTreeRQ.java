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
    Node head;

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
            // dequeue existing head and set new head as right child
            this.head = this.head.getRight();
            this.head.setParent(null);
        } else {
            nodeToDequeue.getParent().setLeft(nodeToDequeue.getRight());
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
        this.head.printTree();

        System.out.print("\n");
    }

    @SuppressWarnings("unused")
    private class Node {
        private String procLabel;
        private int vt;

        private int height;

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
            if (nodeToAdd.getVt() >= this.vt) {
                if (this.right == null) {
                    this.right = nodeToAdd;
                    nodeToAdd.setParent(this);
                } else {
                    this.right.addChild(nodeToAdd);
                }
            } else {
                if (this.parent != null && this.parent.getVt() == nodeToAdd.getVt()) {
                    // Add duplicate key here
                    nodeToAdd.setParent(this.parent);
                    nodeToAdd.setRight(this);

                    this.parent.setRight(nodeToAdd);
                    this.setParent(nodeToAdd);
                } else if (this.left == null) {
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

        public int getHeight() {
            return this.height;
        }

        public void setParent(Node node) {
            this.parent = node;
        }

        public void setLeft(Node node) {
            this.left = node;
        }

        public void setRight(Node node) {
            this.right = node;
        }

        public void printTree() {
            if (this.left != null) {
                this.left.printTree();
            }

            System.out.print(this.procLabel + "=" + Integer.toString(this.vt) + " ");

            if (this.right != null) {
                this.right.printTree();
            }
        }

        public Node findMinimumVt() {
            // finds minimum vt in tree and returns that node
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

        public int calcHeight() {
            int nodeHeight;

            if (this.left != null) {
                if (this.right != null) {
                    nodeHeight = Math.max(this.left.calcHeight(), this.right.calcHeight()) + 1;
                } else {
                    nodeHeight = this.left.calcHeight() + 1;
                }
            } else {
                if (this.right != null) {
                    nodeHeight = this.right.calcHeight() + 1;
                } else {
                    nodeHeight = 0;
                }
            }

            this.height = nodeHeight;

            return nodeHeight;
        }

        public void printDetails() {
            // For testing
            System.out.println("\nDetails of node " + procLabel);

            if (this.parent == null) {
                System.out.println("HEAD NODE");
            } else {
                System.out.println("Parent: " + parent.getProcLabel());
            }

            if (left != null) {
                System.out.println("Left Child: " + left.getProcLabel());
            }

            if (right != null) {
                System.out.println("Right Child: " + right.getProcLabel());
            }
        }
    } // end of class Node

} // end of class BinarySearchTreeRQ
