import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class DataLoader {
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private static final String DELIM = "\t";
    private static final String FIRST_CHECK = "TRANSACTION ID";
    private static final int HEADER_FIELD = 4, BODY_FIELD = 4;

    public static HashMap<String, Transactions> readFile(String name) throws ParseException {
        HashMap<String, Transactions> ledger = new HashMap<String, Transactions>();
        try {
            Scanner read = new Scanner(new File(name));
            // Read header
            String firstLine = read.nextLine();
            String[] headerLine = firstLine.split(DELIM);
            if (headerLine.length != HEADER_FIELD && headerLine[0] != FIRST_CHECK) {
                read.close();
                readFileWithoutHeader(name);
            } else {
                while (read.hasNextLine()) {
                    String fileLine = read.nextLine();
                    String[] splitLine = fileLine.split(DELIM);
                    if (splitLine.length != BODY_FIELD) // Check to make sure there are four pieces of info
                        continue;
                    String id = splitLine[0];
                    String type = splitLine[1];
                    Date date = df.parse(splitLine[2]);
                    BigDecimal amount = new BigDecimal(splitLine[3]);
                    Transactions aT = new Transactions(type, date, amount, id);
                    ledger.put(id, aT);
                }
                read.close();
                return ledger;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Added interoperablity for older file formats (aka ones without a header)
    public static HashMap<String, Transactions> readFileWithoutHeader(String name) throws ParseException {
        HashMap<String, Transactions> ledger = new HashMap<String, Transactions>();
        try {
            Scanner read = new Scanner(new File(name));
            while (read.hasNextLine()) {
                String fileLine = read.nextLine();
                String[] splitLine = fileLine.split(DELIM);
                if (splitLine.length != BODY_FIELD) // Check to make sure there are four pieces of info
                    continue;
                String id = splitLine[0];
                String type = splitLine[1];
                Date date = df.parse(splitLine[2]);
                BigDecimal amount = new BigDecimal(splitLine[3]);
                Transactions aT = new Transactions(type, date, amount, id);
                ledger.put(id, aT);
            }
            read.close();
            return ledger;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
