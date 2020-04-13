import java.io.PrintWriter;
import java.lang.String;

/**
 * AVL Binary Search Tree by @author Dylan Pleiter
 */
public class BinarySearchTreeRQ implements Runqueue {
    private Proc head;

    // No constructer needed. Initialise upon first enqueue

    @Override
    public void enqueue(String procLabel, int vt) {
        Proc procToAdd = new Proc(procLabel, vt);
        if (head == null) {
            this.head = procToAdd;
        } else {
            this.head.addChild(procToAdd);
        }

        this.head = procToAdd.rebalanceTree(null, null, false);
    } // end of enqueue

    @Override
    public String dequeue() {
        Proc procToDequeue = this.head.findMinimumVt();

        if (procToDequeue == this.head) {
            // Guaranteed to have no left children
            this.head = this.head.getRight();

            if (this.head != null) {
                // if not dequeueing last item in tree
                this.head.setParent(null);
            }

            return procToDequeue.getProcLabel();
        }

        procToDequeue.dequeue();

        this.head = procToDequeue.getParent().rebalanceTree(null, null, false);

        return procToDequeue.getProcLabel();
    } // end of dequeue()

    @Override
    public boolean findProcess(String procLabel) {
        return this.head.findProcLabel(procLabel);
    } // end of findProcess()

    @Override
    public boolean removeProcess(String procLabel) {
        Proc procToRemove = this.head.getProcByLabel(procLabel);

        if (procToRemove == null) {
            return false;
        }

        // detatch node and return node to take its place as root of subtree
        Proc newProc = procToRemove.removeProcess();

        if (procToRemove == this.head) {
            this.head = newProc;
        } else if (procToRemove.getParent().getLeft() == procToRemove) {
            procToRemove.getParent().setLeft(newProc);
        } else {
            procToRemove.getParent().setRight(newProc);
        }

        this.head = this.head.rebalanceTree();

        return true;
    } // end of removeProcess()

    @Override
    public int precedingProcessTime(String procLabel) {
        Proc proc = this.head.getProcByLabel(procLabel);

        if (proc == null) {
            return -1;
        }

        return proc.calcTimeOfPreceding(null);
    } // end of precedingProcessTime()

    @Override
    public int succeedingProcessTime(String procLabel) {
        Proc proc = this.head.getProcByLabel(procLabel);

        if (proc == null) {
            return -1;
        }

        return proc.calcTimeOfSucceeding(null);
    } // end of precedingProcessTime()

    @Override
    public void printAllProcesses(PrintWriter os) {
        this.head.printTree(os);
        os.print("\n");
    }

    private class Proc {
        private int vt;
        private String procLabel;

        private Proc parent;
        private Proc left;
        private Proc right;

        private int height;

        public Proc(String procLabel, int vt) {
            this.vt = vt;
            this.procLabel = procLabel;
        }

        public void addChild(Proc procToAdd) {
            // Add new process as a leaf ndoe
            if (procToAdd.getVt() < this.vt) {
                if (this.left == null) {
                    this.left = procToAdd;
                    this.left.setParent(this);
                } else {
                    this.left.addChild(procToAdd);
                }
            } else {
                // IMPORTANT: An equal process should be added to right subtree
                if (this.right == null) {
                    this.right = procToAdd;
                    this.right.setParent(this);
                } else {
                    this.right.addChild(procToAdd);
                }
            }
        } // end of addChild()

        public void dequeue() {
            if (this.parent != null) {
                // If not head node
                this.parent.setLeft(this.right);
            }

            if (this.right != null) {
                this.right.setParent(this.parent);
            }
        }// end of dequeue

        public Boolean findProcLabel(String procLabel) {
            if (this.procLabel.compareTo(procLabel) == 0) {
                return true;
            }

            return (this.left != null && this.left.findProcLabel(procLabel))
                    || (this.right != null && this.right.findProcLabel(procLabel));
        } // end of findProcLabel

        public Proc removeProcess() {
            int leftHeight = this.left == null ? -1 : this.left.getHeight();
            int rightHeight = this.right == null ? -1 : this.right.getHeight();

            Proc replacementProc;

            // replace as root a process from the larger subtree (in terms of height)
            if (this.height == 0) {
                // leaf node
                return null;
            } else if (leftHeight > rightHeight) {
                replacementProc = this.left.findMaximumVt();

                if (this.left != replacementProc) {
                    replacementProc.getParent().setRight(replacementProc.getLeft());
                    replacementProc.setLeft(this.left);
                }

                replacementProc.setRight(this.right);
            } else {
                replacementProc = this.right.findMinimumVt();

                if (this.right != replacementProc) {
                    replacementProc.getParent().setLeft(replacementProc.getRight());
                    replacementProc.setRight(this.right);
                }

                replacementProc.setLeft(this.left);
            }

            replacementProc.setParent(this.parent);

            return replacementProc;
        } // end of remove process

        public int calcTimeOfPreceding(Proc callingNode) {
            int leftChildTime;
            int parentTime;
            int thisTime;

            // determine if we should include this process and subtree in calculation
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

            // now trace up to the head process
            if (this.parent == null) {
                return thisTime + leftChildTime;
            } else {
                parentTime = this.parent.calcTimeOfPreceding(this);
                return leftChildTime + parentTime + thisTime;
            }
        } // end of calcTimeOfPreceding

        public int calcTimeOfSucceeding(Proc callingNode) {
            int rightChildTime;
            int parentTime;
            int thisTime;

            // determine if we should include this process and subtree in calculation
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

            // now trace up to the head process
            if (this.parent == null) {
                return thisTime + rightChildTime;
            } else {
                parentTime = this.parent.calcTimeOfSucceeding(this);
                return rightChildTime + parentTime + thisTime;
            }
        } // end of calcTimeOfSucceeding

        public void printTree(PrintWriter os) {
            if (this.left != null) {
                this.left.printTree(os);
            }

            os.print(this.procLabel + " ");

            if (this.right != null) {
                this.right.printTree(os);
            }
        } // end of printTree

        // ***** Helper functions *****
        public Proc getProcByLabel(String procLabel) {
            // return the process with a given procLabel
            Proc findInLeftChild;
            Proc findInRightChild;

            if (this.procLabel.compareTo(procLabel) == 0) {
                return this;
            }

            if (this.left == null) {
                findInLeftChild = null;
            } else {
                findInLeftChild = this.left.getProcByLabel(procLabel);
            }

            if (findInLeftChild != null) {
                // avoids exploring right subtree if procLabel found in left
                return findInLeftChild;
            } else if (this.right == null) {
                findInRightChild = null;
            } else {
                findInRightChild = this.right.getProcByLabel(procLabel);
            }

            return findInRightChild;
        }

        private Proc findMinimumVt() {
            // finds minimum vt in tree and returns that process
            if (this.left == null) {
                return this;
            } else {
                return this.left.findMinimumVt();
            }
        }

        private Proc findMaximumVt() {
            // finds maximum vt in tree and returns that process
            if (this.right == null) {
                return this;
            } else {
                return this.right.findMaximumVt();
            }
        }

        private int findTimeOfTree() {
            // recursively find time of subtree with this process as the root
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

        public Proc rebalanceTree() {
            // Rebalance from top down (visits every process in tree)

            // New root to be referenced by calling Proc
            Proc newRoot = this;

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

        public Proc rebalanceTree(Proc oldChild, Proc newChild, Boolean performedRebalancing) {
            // Rebalance tree from bottom up (visits only parents up to head)
            int leftHeight = getLeftHeight();
            int rightHeight = getRightHeight();

            // Node to replace this one (to be read by parent)
            Proc newRoot = this;

            Boolean isHead = this.parent == null;

            if (oldChild != newChild) {
                // Reset relevant child reference if new and old are different
                this.left = this.left == oldChild ? newChild : this.left;
                this.right = this.right == oldChild ? newChild : this.right;
            }

            if (performedRebalancing) {
                // Only one rebalancing needed. Now fetch the head node to return to initial
                // calling method
                if (isHead) {
                    return this;
                } else {
                    return this.parent.rebalanceTree(null, null, true);
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

            return isHead ? newRoot : newRoot.getParent().rebalanceTree(this, newRoot, performedRebalancing);
        } // end of rebalanceTree()

        private Proc leftRotate() {
            // check if right child needs to be rotated before proceeding with this rotation
            if (this.right.getLeftHeight() > this.right.getRightHeight()) {
                this.right = this.right.rightRotate();
            }

            // new root of subtree to be pointed to by parent
            Proc newRoot = this.right;

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
        } // end of leftRotate()

        private Proc rightRotate() {
            // check if right child needs to be rotated before proceeding with this rotation
            if (this.left.getRightHeight() > this.left.getLeftHeight()) {
                this.left = this.left.leftRotate();
            }

            // new root of subtree to be pointed to by parent
            Proc newRoot = this.left;

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
        } // end of rightRotate()

        // ***** Getters and Setters *****
        public Proc getParent() {
            return this.parent;
        }

        public Proc getLeft() {
            return this.left;
        }

        public Proc getRight() {
            return this.right;
        }

        public String getProcLabel() {
            return this.procLabel;
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

        public void setParent(Proc proc) {
            this.parent = proc;
        }

        public void setLeft(Proc proc) {
            this.left = proc;
        }

        public void setRight(Proc proc) {
            this.right = proc;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    } // end of class Proc
} // end of class BinarySearchTreeRQ