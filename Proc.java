
public class Proc {

        /** Process Label. */
        protected String mProcLabel;
        
        /** Process RunTime. */
        protected int mVt;
        
        /** Reference to next node. */
        protected Proc mNext;
        
        /** Reference to previous node. */
        protected Proc mPrev;

        public Proc(String procLabel, int vt) {
        	mProcLabel = procLabel;
        	mVt = vt;
            mNext = null;
            mPrev = null;
        }

        public String getProcLabel() {
            return mProcLabel;
        }
        
        public int getRunTime() {
            return mVt;
        }


        public Proc getNext() {
            return mNext;
        }
        
        public Proc getPrev() {
            return mPrev;
        }

        public void setProcess(String procLabel) {
        	mProcLabel = procLabel;
        }

        public void setRunTime(int vt) {
        	mVt = vt;
        }

        public void setNext(Proc next) {
            mNext = next;
        }
        
        public void setPrev(Proc prev) {
            mPrev = prev;
        }
    } 
