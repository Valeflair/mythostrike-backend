package Test;
    /**
     * the collection of set containing the information, representing which kind of inputs are allowed
     *
     * @author uzjip
     * @version 1.0.0
     */
    public class InputConditions {
        private int amountNumber = 0;
        private int range = 0;
        private boolean allowDuplicate = false;
        private boolean allowEmpty = false;
        private String hint = null;

        /**
         * update the whole information through the InputCondition,
         * which means system should allow complete another type of inputs
         *
         * @param amountNumber   -1 represent allow any possible number of inputs, other number represent the length of
         *                       array inputs[] should be
         * @param range          the maximum number allowed for input can have.
         * @param hint           the hint for user when ask for input
         * @param allowDuplicate true means that the multiple inputs can be same number, for example inputs[] = [1,1,1]
         *                       false will in that case not allowed
         * @param allowEmpty     true will allow that use just don't give any input.
         */
        public void update(int amountNumber, int range, String hint, boolean allowDuplicate, boolean allowEmpty) {
            this.amountNumber = amountNumber;
            this.range = range;
            this.hint = hint;
            this.allowDuplicate = allowDuplicate;
            this.allowEmpty = allowEmpty;
        }

        /**
         * -1 represent allow any possible number of inputs
         *
         * @return the size of inputs allowed
         */
        public int getAmountNumber() {
            return amountNumber;
        }

        /**
         * getter for range maximum number input allowed
         *
         * @return range of input allowed
         */
        public int getRange() {
            return range;
        }

        /**
         * getter for if current condition allow duplicated number on multiple input
         *
         * @return true, if inputs should be allowed to put multiple same number in.
         * otherwise false
         */
        public boolean isAllowDuplicate() {
            return allowDuplicate;
        }

        /**
         * getter for if current condition allow duplicated number on multiple input
         *
         * @return true, if inputs should be allowed to put multiple same number in.
         * otherwise false
         */
        public boolean isAllowEmpty() {
            return allowEmpty;
        }

        /**
         * getter for hint should display to user
         *
         * @return the hint display
         */
        public String getHint() {
            return hint;
        }


    }

}
