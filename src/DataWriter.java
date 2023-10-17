import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class DataWriter {
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private static final String DELIM = "\t";

    public static void writeToFile(String name, HashMap<String, Transactions> ledger) {
        try {
            PrintWriter fileWrite = new PrintWriter(new FileOutputStream(new File(name)));
            fileWrite.println("TRANSACTION ID" + DELIM + "TYPE" + DELIM + "DATE" + DELIM + "AMOUNT");
            for (HashMap.Entry<String, Transactions> entry : ledger.entrySet())
                fileWrite.println(entry.getValue().getID() + DELIM + entry.getValue().getType() + DELIM
                        + df.format(entry.getValue().getDate()) + DELIM + entry.getValue().getAmount());
            fileWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
