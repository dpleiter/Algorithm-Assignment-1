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
    private Node head;

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

        this.head = nodeToAdd.rebalanceTree2(null, null, false);
    }

    @Override
    public String dequeue() {
        Node nodeToDequeue = head.findMinimumVt();

        if (nodeToDequeue == this.head) {
            this.head = this.head.getRight();

            if (this.head != null) {
                this.head.setParent(null);
            } else {
                // dequeue last item in tree
                return nodeToDequeue.dequeue();
            }
        }

        this.head = this.head.rebalanceTree();

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

        this.head = this.head.rebalanceTree();

        return true;
    } // end of removeProcess()

    @Override
    public int precedingProcessTime(String procLabel) {
        Node node = this.head.getNode(procLabel);

        if (node == null) {
            return -1;
        }

        return node.calcTimeOfPreceeding(null);
    } // end of precedingProcessTime()

    @Override
    public int succeedingProcessTime(String procLabel) {
        Node node = this.head.getNode(procLabel);

        if (node == null) {
            return -1;
        }

        return node.calcTimeOfSucceeding(null);
    } // end of precedingProcessTime()

    @Override
    public void printAllProcesses(PrintWriter os) {
        this.head.printTree(os);
        os.print("\n");
    }

    private class Node {
        private int vt;
        private String procLabel;

        private Node parent;
        private Node left;
        private Node right;

        private int height;

        public Node(String procLabel, int vt) {
            this.vt = vt;

            this.procLabel = procLabel;

            this.left = null;
            this.right = null;
        }

        public void addChild(Node nodeToAdd) {
            if (nodeToAdd.getVt() == this.vt) {
                addDuplicateProc(nodeToAdd);
            } else if (nodeToAdd.getVt() < this.vt) {
                if (this.left == null) {
                    this.left = nodeToAdd;
                    this.left.setParent(this);
                } else {
                    this.left.addChild(nodeToAdd);
                }
            } else {
                if (this.right == null) {
                    this.right = nodeToAdd;
                    this.right.setParent(this);
                } else {
                    this.right.addChild(nodeToAdd);
                }
            }
        }

        public void addDuplicateProc(Node newNode) {
            if (this.vt != newNode.getVt()) {
                newNode.setRight(this);
                newNode.setParent(this.parent);

                this.parent.setRight(newNode);
                this.parent = newNode;
            } else if (this.right == null) {
                newNode.setParent(this);
                this.right = newNode;
            } else {
                this.right.addDuplicateProc(newNode);
            }
        }

        public String dequeue() {
            if (this.parent != null) {
                // If not head node
                this.parent.setLeft(this.right);
            }

            if (this.right != null) {
                this.right.setParent(this.parent);
            }

            return this.procLabel;
        }

        public Boolean findProcLabel(String procLabel) {
            if (this.procLabel.compareTo(procLabel) == 0) {
                return true;
            }

            return (this.left != null && this.left.findProcLabel(procLabel))
                    || (this.right != null && this.right.findProcLabel(procLabel));
        }

        public Node removeProcess(String procLabel) {
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
        }

        public int calcTimeOfPreceeding(Node callingNode) {
            int leftChildTime;
            int parentTime;
            int thisTime;

            if (callingNode == null) {
                // Initial call of method
                thisTime = 0;
                leftChildTime = this.left == null ? 0 : this.left.findTimeOfTree();
            } else if (callingNode == this.left) {
                thisTime = 0;
                leftChildTime = 0;
            } else {
                thisTime = this.vt;
                leftChildTime = this.left == null ? 0 : this.left.findTimeOfTree();
            }

            if (this.parent == null) {
                return thisTime + leftChildTime;
            } else {
                parentTime = this.parent.calcTimeOfPreceeding(this);
                return leftChildTime + parentTime + thisTime;
            }
        }

        public int calcTimeOfSucceeding(Node callingNode) {
            int rightChildTime;
            int parentTime;
            int thisTime;

            if (callingNode == null) {
                // Initial call of method
                thisTime = 0;
                rightChildTime = this.right == null ? 0 : this.right.findTimeOfTree();
            } else if (callingNode == this.right) {
                thisTime = 0;
                rightChildTime = 0;
            } else {
                thisTime = this.vt;
                rightChildTime = this.right == null ? 0 : this.right.findTimeOfTree();
            }

            if (this.parent == null) {
                return thisTime + rightChildTime;
            } else {
                parentTime = this.parent.calcTimeOfSucceeding(this);
                return rightChildTime + parentTime + thisTime;
            }
        }

        public void printTree(PrintWriter os) {
            if (this.left != null) {
                this.left.printTree(os);
            }

            os.print(this.procLabel + " ");

            if (this.right != null) {
                this.right.printTree(os);
            }
        }

        // ***** Helper functions *****
        public Node getNode(String procLabel) {
            Node findInLeftChild;
            Node findInRightChild;

            if (this.procLabel.compareTo(procLabel) == 0) {
                return this;
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

            return timeOfLeftChild + timeOfRightChild + this.vt;
        }

        public Node rebalanceTree() {
            Node newRoot = this;

            if (this.left != null) {
                this.left = this.left.rebalanceTree();
            }

            if (this.right != null) {
                this.right = this.right.rebalanceTree();
            }

            int leftHeight = getLeftHeight();
            int rightHeight = getRightHeight();

            if (leftHeight - rightHeight > 1) {
                newRoot = rightRotate();
            } else if (rightHeight - leftHeight > 1) {
                newRoot = leftRotate();
            }

            this.height = Math.max(getLeftHeight(), getRightHeight()) + 1;

            return newRoot;
        }

        @SuppressWarnings({ "unused" })
        public Node rebalanceTree2(Node oldNode, Node newNode, Boolean test) {
            int leftHeight = getLeftHeight();
            int rightHeight = getRightHeight();

            Node newRoot = this;
            Node oldRoot = this;

            Boolean performedRebalancing = test;
            Boolean isHead = this.parent == null;

            if (oldNode != newNode) {
                this.left = this.left == oldNode ? newNode : this.left;
                this.right = this.right == oldNode ? newNode : this.right;
            }

            if (performedRebalancing) {
                if (this.parent == null) {
                    return this;
                } else {
                    return this.parent.rebalanceTree2(null, null, true);
                }
            }

            if (leftHeight - rightHeight > 1) {
                newRoot = rightRotate();
                performedRebalancing = true;
            }

            if (rightHeight - leftHeight > 1) {
                newRoot = leftRotate();
                performedRebalancing = true;
            }

            this.height = Math.max(getLeftHeight(), getRightHeight()) + 1;

            return isHead ? newRoot : newRoot.getParent().rebalanceTree2(oldRoot, newRoot, performedRebalancing);
        }

        private Node rightRotate() {
            if (this.left.getRightHeight() > this.left.getLeftHeight()) {
                this.left = this.left.leftRotate();
            }

            // new root of subtree to be pointed to by parent
            Node newRoot = this.left;

            // Move pointers around
            this.left = newRoot.getRight();

            if (this.left != null) {
                this.left.setParent(this);
            }

            newRoot.setParent(this.parent);

            this.parent = newRoot;

            newRoot.setRight(this);

            // Set new heights
            this.height = Math.max(getLeftHeight(), getRightHeight()) + 1;
            newRoot.setHeight(Math.max(newRoot.getLeftHeight(), newRoot.getRightHeight()) + 1);

            return newRoot;
        }

        private Node leftRotate() {
            if (this.right.getLeftHeight() > this.right.getRightHeight()) {
                this.right = this.right.rightRotate();
            }

            // new root of subtree to be pointed to by parent
            Node newRoot = this.right;

            // Move pointers around
            this.right = newRoot.getLeft();

            if (this.right != null) {
                this.right.setParent(this);
            }

            newRoot.setParent(this.parent);

            this.parent = newRoot;
            newRoot.setLeft(this);

            // Set new heights
            this.height = Math.max(getLeftHeight(), getRightHeight()) + 1;
            newRoot.setHeight(Math.max(newRoot.getLeftHeight(), newRoot.getRightHeight()) + 1);

            return newRoot;
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

        public int getHeight() {
            return this.height;
        }

        public int getLeftHeight() {
            if (this.left == null) {
                return -1;
            } else {
                return this.left.getHeight();
            }
        }

        public int getRightHeight() {
            if (this.right == null) {
                return -1;
            } else {
                return this.right.getHeight();
            }
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

        public void setHeight(int height) {
            this.height = height;
        }
    } // end of class Node
} // end of class BinarySearchTreeRQ