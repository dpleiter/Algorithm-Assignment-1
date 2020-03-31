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
        // Node nodeToAdd = new Node(procLabel, vt);

        if (head == null) {
            this.head = new Node(procLabel, vt);
        } else {
            this.head.addChild(procLabel, vt);
            // this.head.addChild(nodeToAdd);
        }

        this.head.calcHeight();
    }

    @Override
    public String dequeue() {
        Node nodeToDequeue = head.findMinimumVt();

        if (nodeToDequeue == this.head && this.head.getNumProcLabels() == 1) {
            if (this.head.getRight() != null) {
                this.head = this.head.getRight();
                this.head.setParent(null);
            } else {
                // dequeue last item in tree
                this.head = null;
            }
        }

        this.head.calcHeight();

        return nodeToDequeue.dequeue();
    } // end of dequeue()

    @Override
    public boolean findProcess(String procLabel) {
        return this.head.findProcLabel(procLabel);
    } // end of findProcess()

    @Override
    public boolean removeProcess(String procLabel) {
        Boolean removed;

        if (this.head.getHeight() == 0 && this.head.getNumProcLabels() == 1
                && this.head.getProcLabels()[0] == procLabel) {
            this.head = null;
            removed = true;
        } else {
            removed = this.head.removeProcess(procLabel);
            this.head.calcHeight();
        }

        return removed;
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
        private int vt;

        private int height;

        private String[] procLabels;
        private int numLabels;

        private Node parent;
        private Node left;
        private Node right;

        public Node(String procLabel, int vt) {
            this.vt = vt;

            this.procLabels = new String[] { procLabel };
            this.numLabels = 1;

            this.left = null;
            this.right = null;
        }

        public void addChild(String procLabel, int vt) {
            if (vt == this.vt) {
                // add duplicate key
                String[] newProcLabels = new String[this.numLabels + 1];

                for (int i = 0; i < this.numLabels; i++) {
                    newProcLabels[i] = procLabels[i];
                }

                newProcLabels[numLabels] = procLabel;

                procLabels = newProcLabels;
                this.numLabels++;
            } else if (vt < this.vt) {
                if (this.left == null) {
                    this.left = new Node(procLabel, vt);
                    this.left.setParent(this);
                } else {
                    this.left.addChild(procLabel, vt);
                }
            } else {
                if (this.right == null) {
                    this.right = new Node(procLabel, vt);
                    this.right.setParent(this);
                } else {
                    this.right.addChild(procLabel, vt);
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

        public int getHeight() {
            return this.height;
        }

        public String dequeue() {
            // handle dequeueing operation
            if (this.numLabels == 1) {
                if (this.parent != null) {
                    // If not head node
                    this.parent.setLeft(null);
                }

                return procLabels[0];
            } else {
                // pop first procLabel
                String[] newProcLabels = new String[this.numLabels - 1];
                String itemToDequeue = procLabels[0];

                for (int i = 1; i < this.numLabels; i++) {
                    newProcLabels[i - 1] = this.procLabels[i];
                }

                procLabels = newProcLabels;
                numLabels--;

                return itemToDequeue;
            }

        }

        public int getNumProcLabels() {
            return this.numLabels;
        }

        public String[] getProcLabels() {
            return this.procLabels;
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

            printDetails();

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

        public Node findMaximumVt() {
            // finds maximum vt in tree and returns that node
            if (this.right == null) {
                return this;
            } else {
                return this.right.findMaximumVt();
            }
        }

        public Boolean findProcLabel(String procLabel) {
            for (int i = 0; i < this.numLabels; i++) {
                if (procLabels[i].compareTo(procLabel) == 0) {
                    return true;
                }
            }

            return (this.left != null && this.left.findProcLabel(procLabel))
                    || (this.right != null && this.right.findProcLabel(procLabel));
        }

        private void removeReferences() {
            // Recursively replace node with details of children until leaf, then delete
            // Never called for a node with 2 non-null children
            if (this.left == null && this.right == null) {
                if (this.parent.getLeft() == this) {
                    this.parent.setLeft(null);
                } else {
                    this.parent.setRight(null);
                }
            } else if (this.left != null) {
                replaceNodeDetails(this.left);
                this.left.removeReferences();
            } else {
                replaceNodeDetails(this.right);
                this.right.removeReferences();
            }

        }

        private void replaceNodeDetails(Node replacementNode) {
            // replace this nodes details with that of another
            this.vt = replacementNode.getVt();
            this.procLabels = replacementNode.getProcLabels();
            this.numLabels = replacementNode.getNumProcLabels();
        }

        public Boolean removeProcess(String procLabel) {
            for (int i = 0; i < this.numLabels; i++) {
                if (this.procLabels[i].compareTo(procLabel) == 0) {
                    if (numLabels == 1) {
                        // node needs to be deleted
                        int leftHeight = this.left == null ? 0 : this.left.getHeight();
                        int rightHeight = this.right == null ? 0 : this.right.getHeight();

                        Node replacementNode;

                        if (this.height == 0) {
                            // leaf node means no replacement. Delete references
                            if (this.parent.getLeft() == this) {
                                this.parent.setLeft(null);
                            } else {
                                this.parent.setRight(null);
                            }

                            this.parent = null;

                            return true;
                        } else if (leftHeight > rightHeight) {
                            replacementNode = this.left.findMaximumVt();
                        } else {
                            replacementNode = this.right.findMinimumVt();
                        }

                        // Set this node's details to that of the replacement node
                        replaceNodeDetails(replacementNode);

                        // delete all references to replacement node
                        replacementNode.removeReferences();
                    } else {
                        for (int j = i; j < this.numLabels - 1; j++) {
                            procLabels[j] = procLabels[j + 1];
                        }
                        numLabels--;
                    }

                    return true;
                }
            }

            return (this.left != null && this.left.removeProcess(procLabel))
                    || (this.right != null && this.right.removeProcess(procLabel));
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

        private void printDetails() {
            // For testing
            System.out.print("\nDetails of nodes: ");
            for (int i = 0; i < this.numLabels; i++) {
                System.out.print(procLabels[i] + " ");
            }

            System.out.println("\nNumber of items: " + Integer.toString(numLabels));

            System.out.println("VT: " + Integer.toString(this.vt));

            System.out.println("Height: " + Integer.toString(height));

            if (this.parent == null) {
                System.out.println("HEAD NODE");
            } else {
                System.out.println("Parent: " + parent.getVt());
            }

            if (left != null) {
                System.out.println("Left Child: " + left.getVt());
            }

            if (right != null) {
                System.out.println("Right Child: " + right.getVt());
            }
        }
    } // end of class Node

} // end of class BinarySearchTreeRQ