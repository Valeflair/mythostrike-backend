package Test;
    /**
     * the collection of set containing the information, representing which kind of inputs are allowed
     *
     * @author uzjip
     * @version 1.0.0
     */
    public class InputConditions {
        private int amountMin = 0;
        private int amountMax = 0;
        private int rangeMax = 0;
        private int rangeMin = 0;
        private boolean allowDuplicate = false;
        private boolean allowEmpty = false;
        private String hint = null;

        public void update(int amountMin, int amountMax,int rangeMin, int rangeMax, String hint, boolean allowDuplicate, boolean allowEmpty) {
            this.amountMin = amountMin;
            this.amountMax = amountMax;
            this.rangeMin = rangeMin;
            this.rangeMax = rangeMax;
            this.hint = hint;
            this.allowDuplicate = allowDuplicate;
            this.allowEmpty = allowEmpty;
        }

        public int getAmountMin() {
            return amountMin;
        }

        public int getAmountMax() {
            return amountMax;
        }

        public int getRangeMax() {
            return rangeMax;
        }

        public int getRangeMin() {
            return rangeMin;
        }

        public boolean isAllowDuplicate() {
            return allowDuplicate;
        }

        public boolean isAllowEmpty() {
            return allowEmpty;
        }

        public String getHint() {
            return hint;
        }

        public String getAllowance(){
            return "you are allowed to enter:"+amountMin+"~"+amountMax+" number(s) between "+ rangeMin +"~"+ rangeMax +",duplicate:"+allowDuplicate+",empty:"+allowEmpty+",hint:"+hint;
        }

        public boolean match(String s){



            return false;
        }

    }


