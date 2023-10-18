import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataWriter extends DataConstants {
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public static void saveTransactions(HashMap<String, Transactions> listOfTransactions) {
        /*
         * Ledger ledger = Ledger.getInstance("Yes");
         * HashMap<String, Transactions> listOfTransactions =
         * ledger.getListOfTransactions();
         */
        JSONArray jsonTransactions = new JSONArray();

        for (HashMap.Entry<String, Transactions> entry : listOfTransactions.entrySet())
            jsonTransactions.add(getTransactionsJSON(entry.getValue()));

        try (FileWriter file = new FileWriter(FILE_NAME)) {
            file.write(jsonTransactions.toJSONString());
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getTransactionsJSON(Transactions transaction) {
        JSONObject transactionDetails = new JSONObject();
        transactionDetails.put(ID, transaction.getID());
        transactionDetails.put(TYPE, transaction.getType());
        transactionDetails.put(DATE, df.format(transaction.getDate()));
        transactionDetails.put(AMOUNT, transaction.getAmount().toString());
        return transactionDetails;
    }
}
