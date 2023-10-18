import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class LedgerFacade {
    private Ledger ledger = null;
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private static LedgerFacade facade = null;
    private static boolean loadFile = false;

    private LedgerFacade() throws ParseException {
        this.ledger = Ledger.getInstance();
    }

    private LedgerFacade(boolean loadFile) throws ParseException {
        loadFile = true;
        this.ledger = Ledger.getInstance(loadFile);
    }

    public static LedgerFacade getInstance() throws ParseException {
        if (facade == null)
            facade = new LedgerFacade();
        return facade;
    }

    public static LedgerFacade getInstance(boolean loadFile) throws ParseException {
        if (facade == null && loadFile != false)
            facade = new LedgerFacade(loadFile);
        return facade;
    }

    public static boolean getLoadFile() {
        return loadFile;
    }

    public boolean addTransaction(String tType, String tDate, String tAmount) {
        try {
            Date date = df.parse(tDate);
            BigDecimal amount = new BigDecimal(tAmount);
            Transactions aT = new Transactions(tType, date, amount);
            ledger.addTransaction(aT);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeTransaction(String tID) {
        if (isEmpty() || !ledger.searchTransaction(tID))
            return false;
        ledger.removeTransaction(tID);
        return true;
    }

    public boolean clearLedger(String answer) {
        if (answer.equalsIgnoreCase("no"))
            return false;
        ledger.clearLedger();
        return true;
    }

    public boolean isEmpty() {
        if (ledger.isEmpty())
            return true;
        return false;
    }

    public boolean searchTransaction(String tID) {
        if (isEmpty() || !ledger.searchTransaction(tID))
            return false;
        return true;
    }

    public boolean searchTransaction(String tType, String tDate, String tAmount) {
        try {
            Date date = df.parse(tDate);
            BigDecimal amount = new BigDecimal(tAmount);
            if (ledger.isEmpty() || !ledger.searchTransaction(tType, date, amount))
                return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean editTransaction(String tID, String valueType, String valueToEdit) {
        if (isEmpty() || !ledger.searchTransaction(tID))
            return false;
        try {
            if (valueType.equalsIgnoreCase("Type")) {
                ledger.editType(tID, valueToEdit);
                return true;
            } else if (valueType.equalsIgnoreCase("Date")) {
                Date newDate = df.parse(valueToEdit);
                ledger.editDate(tID, newDate);
                return true;
            } else if (valueType.equalsIgnoreCase("Amount")) {
                BigDecimal newAmount = new BigDecimal(valueToEdit);
                ledger.editAmount(tID, newAmount);
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean printLedger() {
        if (isEmpty())
            return false;
        ledger.print();
        return true;
    }

    public boolean printTotal() {
        if (isEmpty())
            return false;
        System.out.println("The total amount of money you have is: $"
                + (totalUp() == null ? "0.00" : totalUp()));
        return true;
    }

    public BigDecimal totalUp() {
        if (isEmpty())
            return null;
        return ledger.totalUp();
    }

    public boolean readFile(boolean loadFile) throws ParseException {
        if (loadFile == false)
            return false;
        ledger = Ledger.getInstance(loadFile);
        return true;
    }

    public boolean writeFile() {
        HashMap<String, Transactions> writeToFile = ledger.getListOfTransactions();
        DataWriter.saveTransactions(writeToFile);
        return true;
    }

    public boolean clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
