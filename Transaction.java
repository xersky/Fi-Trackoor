public enum Transaction {

    DATE(0),
    TYPE(1),
    CATEGORY(2),
    AMOUNT(3);

    private int transactionField;

    Transaction(int transactionField){
        this.transactionField = transactionField;
    }

    public static Transaction fromInt(int n) {
        switch (n) {
            case 0:
                return DATE;

            case 1:
                return TYPE;

            case 2:
                return CATEGORY;

            case 3:
                return AMOUNT;
                
            default:
            throw new ExceptionInInitializerError();
        }
    }
}
