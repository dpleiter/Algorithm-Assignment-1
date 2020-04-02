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
        Node node = this.head.getNode(procLabel);

        if (node == null) {
            return false;
        }

        Node newNode = node.removeProcess(procLabel);

        if (node == this.head) {
            this.head = newNode;
        } else if (node.getParent().getLeft() == node) {
            node.getParent().setLeft(newNode);
        } else {
            node.getParent().setRight(newNode);
        }

        this.head.calcHeight();

        return true;
    } // end of removeProcess()

    @Override
    public int precedingProcessTime(String procLabel) {
        Node node = this.head.getNode(procLabel);

        if (node == null) {
            return -1;
        }

        return node.calcTimeOfPreceeding(procLabel);
    } // end of precedingProcessTime()

    @Override
    public int succeedingProcessTime(String procLabel) {
        Node node = this.head.getNode(procLabel);

        if (node == null) {
            return -1;
        }

        return node.calcTimeOfSucceeding(procLabel);
    } // end of precedingProcessTime()

    @Override
    public void printAllProcesses(PrintWriter os) {
        this.head.printTree(os);
        os.print("\n");
    }

    private class Node {
        private int vt;
        private String[] procLabels;
        private int numLabels;

        private Node parent;
        private Node left;
        private Node right;

        private int height;

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

        public String dequeue() {
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

        public Boolean findProcLabel(String procLabel) {
            for (int i = 0; i < this.numLabels; i++) {
                if (procLabels[i].compareTo(procLabel) == 0) {
                    return true;
                }
            }

            return (this.left != null && this.left.findProcLabel(procLabel))
                    || (this.right != null && this.right.findProcLabel(procLabel));
        }

        public Node removeProcess(String procLabel) {
            if (this.numLabels == 1) {
                // delete node
                int leftHeight = this.left == null ? 0 : this.left.getHeight();
                int rightHeight = this.right == null ? 0 : this.right.getHeight();

                Node replacementNode;

                if (this.height == 0) {
                    // leaf node
                    return null;
                } else if (leftHeight > rightHeight) {
                    replacementNode = this.left.findMaximumVt();

                    if (this.left != replacementNode) {
                        replacementNode.getParent().setRight(replacementNode.getLeft());
                        replacementNode.setLeft(this.left);
                    }

                    replacementNode.setRight(this.right);
                } else {
                    replacementNode = this.right.findMinimumVt();

                    if (this.right != replacementNode) {
                        replacementNode.getParent().setLeft(replacementNode.getRight());
                        replacementNode.setRight(this.right);
                    }

                    replacementNode.setLeft(this.left);
                }

                replacementNode.setParent(this.parent);

                return replacementNode;
            } else {
                for (int i = 0; i < this.numLabels; i++) {
                    if (this.procLabels[i].compareTo(procLabel) == 0) {
                        for (int j = i; j < this.numLabels - 1; j++) {
                            procLabels[j] = procLabels[j + 1];
                        }
                        numLabels--;

                        break;
                    }
                }

                return this;
            }
        }

        public int calcTimeOfPreceeding(String procLabel) {
            int leftChildTime;
            int parentTime;
            int thisTime = 0;

            if (procLabel == null) {
                thisTime = this.vt * this.numLabels;
            } else {
                for (int i = 0; i < this.numLabels; i++) {
                    if (this.procLabels[i].compareTo(procLabel) == 0) {
                        thisTime = this.vt * i;
                        break;
                    }
                }
            }

            if (this.left == null) {
                leftChildTime = 0;
            } else {
                leftChildTime = this.left.findTimeOfTree();
            }

            if (this.parent == null || this.parent.getVt() > this.vt) {
                parentTime = 0;
            } else {
                parentTime = this.parent.calcTimeOfPreceeding(null);
            }

            return thisTime + leftChildTime + parentTime;
        }

        public int calcTimeOfSucceeding(String procLabel) {
            int rightChildTime;
            int parentTime;
            int thisTime = 0;

            if (procLabel == null) {
                thisTime = this.vt * this.numLabels;
            } else {
                for (int i = 0; i < this.numLabels; i++) {
                    if (this.procLabels[i].compareTo(procLabel) == 0) {
                        thisTime = this.vt * (this.numLabels - i - 1);
                        break;
                    }
                }
            }

            if (this.right == null) {
                rightChildTime = 0;
            } else {
                rightChildTime = this.right.findTimeOfTree();
            }

            if (this.parent == null || this.parent.getVt() < this.vt) {
                parentTime = 0;
            } else {
                parentTime = this.parent.calcTimeOfSucceeding(null);
            }

            return thisTime + rightChildTime + parentTime;
        }

        public void printTree(PrintWriter os) {
            if (this.left != null) {
                this.left.printTree(os);
            }

            for (int i = 0; i < this.numLabels; i++) {
                os.print(this.procLabels[i] + " ");
            }

            if (this.right != null) {
                this.right.printTree(os);
            }
        }

        // ***** Helper functions *****
        public Node getNode(String procLabel) {
            Node findInLeftChild;
            Node findInRightChild;

            for (int i = 0; i < this.numLabels; i++) {
                if (this.procLabels[i].compareTo(procLabel) == 0) {
                    return this;
                }
            }

            if (this.left == null) {
                findInLeftChild = null;
            } else {
                findInLeftChild = this.left.getNode(procLabel);
            }

            if (this.right == null) {
                findInRightChild = null;
            } else {
                findInRightChild = this.right.getNode(procLabel);
            }

            return findInLeftChild == null ? findInRightChild : findInLeftChild;
        }

        private Node findMinimumVt() {
            // finds minimum vt in tree and returns that node
            if (this.left == null) {
                return this;
            } else {
                return this.left.findMinimumVt();
            }
        }

        private Node findMaximumVt() {
            // finds maximum vt in tree and returns that node
            if (this.right == null) {
                return this;
            } else {
                return this.right.findMaximumVt();
            }
        }

        private int findTimeOfTree() {
            int timeOfLeftChild;
            int timeOfRightChild;

            if (this.left == null) {
                timeOfLeftChild = 0;
            } else {
                timeOfLeftChild = this.left.findTimeOfTree();
            }

            if (this.right == null) {
                timeOfRightChild = 0;
            } else {
                timeOfRightChild = this.right.findTimeOfTree();
            }

            return timeOfLeftChild + timeOfRightChild + this.numLabels * this.vt;
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

        // ***** Getters and Setters *****
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

        public int getNumProcLabels() {
            return this.numLabels;
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
    } // end of class Node

} // end of class BinarySearchTreeRQ