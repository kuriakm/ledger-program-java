import java.util.Date;
import java.text.ParseException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

public class Ledger {
    private static Ledger ledger = null;
    private static HashMap<String, Transactions> listOfTransactions = null;

    private Ledger() {
        listOfTransactions = new HashMap<String, Transactions>();
    }

    private Ledger(boolean loadFile) {
        listOfTransactions = DataLoader.loadTransactions();
    }

    protected static Ledger getInstance(boolean loadFile) {
        if (ledger == null)
            ledger = new Ledger(loadFile);
        return ledger;
    }

    protected static Ledger getInstance() {
        if (ledger == null)
            ledger = new Ledger();
        return ledger;
    }

    protected HashMap<String, Transactions> getListOfTransactions() {
        return listOfTransactions;
    }

    protected boolean isEmpty() {
        if (listOfTransactions.isEmpty())
            return true;
        else
            return false;
    }

    protected void addTransaction(Transactions aT) {
        if (aT == null) // Precursor check
            return;
        listOfTransactions.put(aT.getID(), aT);
    }

    protected void removeTransaction(String key) {
        if (listOfTransactions.containsKey(key))
            listOfTransactions.remove(key);
    }

    public void clearLedger() {
        listOfTransactions.clear();
    }

    protected BigDecimal totalUp() {
        BigDecimal total = new BigDecimal(0.00);
        for (HashMap.Entry<String, Transactions> entry : listOfTransactions.entrySet()) {
            if (entry.getValue().getType().equals("Deposit"))
                total = total.add(entry.getValue().getAmount());
            else
                total = total.subtract(entry.getValue().getAmount());
        }
        return total;
    }

    protected void print() {
        ArrayList<Transactions> sorted = sortTransactions();
        for (Transactions t : sorted)
            System.out.println(t);
    }

    protected void editType(String key, String updatedType) {
        listOfTransactions.put(key, new Transactions(updatedType, listOfTransactions.get(key).getDate(),
                listOfTransactions.get(key).getAmount(), key));
    }

    protected void editDate(String key, Date updatedDate) {
        listOfTransactions.put(key, new Transactions(listOfTransactions.get(key).getType(), updatedDate,
                listOfTransactions.get(key).getAmount(), key));
    }

    protected void editAmount(String key, BigDecimal updatedAmount) throws ParseException {
        listOfTransactions.put(key, new Transactions(listOfTransactions.get(key).getType(),
                listOfTransactions.get(key).getDate(), updatedAmount, key));
    }

    protected boolean searchTransaction(String key) {
        if (listOfTransactions.containsKey(key)) {
            System.out.println("\nThe transaction was found!\n" + listOfTransactions.get(key));
            return true;
        }
        return false;
    }

    protected boolean searchTransaction(String type, Date date, BigDecimal amount) throws ParseException {
        if (type != null && date != null && amount != null) {
            Transactions searchValue = new Transactions(type, date, amount);
            for (HashMap.Entry<String, Transactions> entry : listOfTransactions.entrySet()) {
                if (entry.getValue().equals(searchValue)) {
                    System.out.println("\nThe transaction was found!\n" + entry.getValue());
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<Transactions> sortTransactions() {
        ArrayList<Transactions> sortedTrans = new ArrayList<Transactions>();
        for (HashMap.Entry<String, Transactions> entry : listOfTransactions.entrySet())
            sortedTrans.add(entry.getValue());
        Collections.sort(sortedTrans);
        return sortedTrans;
    }
}