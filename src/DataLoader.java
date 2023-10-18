import java.io.FileReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataLoader extends DataConstants {
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public static HashMap<String, Transactions> loadTransactions() {
        HashMap<String, Transactions> transactions = new HashMap<String, Transactions>();
        try {
            FileReader reader = new FileReader(FILE_NAME);
            JSONParser parser = new JSONParser();
            JSONArray listJSON = (JSONArray) new JSONParser().parse(reader);

            for (int i = 0; i < listJSON.size(); i++) {
                JSONObject transactionJSON = (JSONObject) listJSON.get(i);
                Date date = df.parse((String) transactionJSON.get(DATE));
                String id = (String) transactionJSON.get(ID);
                BigDecimal amount = new BigDecimal((String) transactionJSON.get(AMOUNT));
                String type = (String) transactionJSON.get(TYPE);
                Transactions aT = new Transactions(type, date, amount, id);
                transactions.put(id, aT);
            }
            return transactions;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
