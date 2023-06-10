import java.util.Date;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.util.Random;

public class Transactions implements Comparable<Transactions>{
    private String type;
    private BigDecimal amt;
    private Date date;
    private String id;
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    public static final String DEPOSIT = "Deposit";
    public static final String WITHDRAWAL = "Withdrawal";
    public static final String KEY_CHARS = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ0123456789";

    // This accounts for read-in files with pre-existing IDs
    public Transactions(String aT, Date aD, BigDecimal aA, String ID) {
        this.setType(aT);
        this.setAmount(aA);
        this.setDate(aD);
        this.setID(ID);
    }

    public Transactions(String aT, Date aD, BigDecimal aA) {
        this.setType(aT);
        this.setAmount(aA);
        this.setDate(aD);
        this.setID();
    }

    public String getType() {
        return this.type;
    }

    public void setType(String aT) {
        if(aT.equalsIgnoreCase("D") || aT.equalsIgnoreCase(DEPOSIT))
            this.type = "Deposit";
        else if(aT.equalsIgnoreCase("W") || aT.equalsIgnoreCase(WITHDRAWAL))
            this.type = "Withdrawal";
        else
            System.out.println("Invalid transaction type.");
    }

    public BigDecimal getAmount() throws ArithmeticException {
        return this.amt.setScale(2);
    }

    public void setAmount(BigDecimal aA) {
        BigDecimal def = new BigDecimal(0.00);
        if(aA != null)
           this.amt = aA.add(def);
        else
           System.out.println("Invalid amount."); 
    }

    public Date getDate() {
        return this.date;
    }    

    public void setDate(Date aD) {
        if(aD != null)
            this.date = aD;
        else
            System.out.println("Invalid date.");
    }

    public String getID() {
        return this.id;
    }

    public void setID() {
        this.id = generateKey();
    }

    public void setID(String aID) { // For read-in files that have an ID
        if (aID != null)
            this.id = aID;
    }

    public String generateKey() {
        Random rng = new Random();
        char[] text = new char[5]; // key is bounded
        for (int i = 0; i < 5; i++)
            text[i] = KEY_CHARS.charAt(rng.nextInt(KEY_CHARS.length()));
        return new String(text);
    }

    public String toString() {
        if(this.type.equals("Deposit"))
            return "["+this.type+"]: \n"+
                "   Transaction ID: "+this.id+" \n"+
                "   Date: "+df.format(this.date)+" \n"+
                "   Amount: $"+this.amt.setScale(2);
        else
            return "["+this.type+"]: \n"+
                "   Transaction ID: "+this.id+" \n"+
                "   Date: "+df.format(this.date)+" \n"+
                "   Amount: -$"+this.amt.setScale(2);
    }

    public boolean equals(Transactions aT) {
        return aT != null &&
            this.type.equalsIgnoreCase(aT.getType()) &&
            this.date.equals(aT.getDate()) &&
            this.amt.compareTo(aT.getAmount()) == 0;
    }

    public int compareTo(Transactions aT) {
        return (this.date.compareTo(aT.date) < 0) ? -1 : ((this.date == aT.date) ? (this.type.compareTo(aT.type)) : 1);
    }

    /*
    public int compareTo(Transactions aT) {
        if(this.date.compareTo(aT.date) < 0)
            return -1;
        else if(this.date.compareTo(aT.date) > 0)
            return 1;
        else if(this.date.compareTo(aT.date) == 0)
            return this.type.compareTo(aT.type);
        else
            return 0;
    }

    public static void main(String[] args) {
        try {
            Date date1 = df.parse("2022-12-23");
            Date date2 = df.parse("2022-12-03");    
    
            Transactions t1 = new Transactions("Deposit", date1, 50.0);
            Transactions t2 = new Transactions("Withdrawal", date2, 20);

            System.out.println(t1);
            System.out.println(t2);
            System.out.println("\n");
            System.out.println(t1.compareTo(t2));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    */
}
