package core;

public enum CardSymbol {

    DIAMOND("Diamond"),
    CLUB("Club"),
    HEART("Heart"),
    SPADE("Spade"),
    NO_SYMBOL("NoSymbol");

    private String name;

    CardSymbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getShort() {
        return name.substring(0,1);
    }
}
