import java.io.*;
import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.util.HashMap;

public class Ledger {
    private HashMap<String, Transactions> ledger = new HashMap<String, Transactions>();;
    public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    public static final int LINE = 4;
    public static final String DELIM = "\t";

    public Ledger() { // Since the HashMap is already instantiated, I left this method blank
    }

    public void addTrans(Transactions aT) {
        if (aT == null) // Precursor check
            return;
        ledger.put(aT.getID(), aT);
    }

    public void removeTrans(String key) {
        if (ledger.containsKey(key))
            ledger.remove(key);
    }

    public BigDecimal totalUp() {
        BigDecimal total = new BigDecimal(0.00);
        for (HashMap.Entry<String, Transactions> entry : ledger.entrySet()) {
            if (entry.getValue().getType().equals("Deposit"))
                total = total.add(entry.getValue().getAmount());
            else
                total = total.subtract(entry.getValue().getAmount());
        }
        return total;
    }

    public void print() {
        // TODO: fix sorted print issues
        /* Transactions[] sorted = sortByDate();
        for (Transactions t : sorted)
            System.out.println(t); */
        for (HashMap.Entry<String, Transactions> entry : ledger.entrySet())
            System.out.println(entry.getValue());
    }

    /* private Transactions[] sortByDate() {
        // Converted HashMap into sortable array for printing
        Transactions[] sortedLedger = new Transactions[ledger.size()];
        mergeSort(sortedLedger, ledger.size());
        return sortedLedger;
    } */

    // Big O Time Complexity: O(nlogn)
    // Big O Space Complexity: O(n)
    /* private static void mergeSort(Transactions arr[], int size) {
        if (size < 2)
            return;
        int mid = size / 2;
        Transactions[] left = new Transactions[mid];
        Transactions[] right = new Transactions[size - mid];

        for (int i = 0; i < mid; i++)
            left[i] = arr[i];

        for (int i = mid; i < size; i++)
            right[i - mid] = arr[i];
    
        mergeSort(left, mid);
        mergeSort(right, size - mid);
        merge(arr, left, right, mid, (size - mid));
    }

    private static void merge(Transactions arr[], Transactions[] l, Transactions[] r, int left, int right) {
        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (l[i].getDate().compareTo(r[j].getDate()) <= 0)
                arr[k++] = l[i++];
            else
                arr[k++] = r[j++];
        }

        while (i < left)
            arr[k++] = l[i++];
        while (j < right)
            arr[k++] = r[j++];
    } */

    public void readFile(String name) throws ParseException {
        try {
            Scanner read = new Scanner(new File(name));
            while(read.hasNextLine()) {
                String fileLine = read.nextLine();
                String[] splitLine = fileLine.split(DELIM);
                if (splitLine.length != LINE) // Check to make sure there are four pieces of info
                    continue;
                String id = splitLine[0];
                String type = splitLine[1];
                Date date = df.parse(splitLine[2]);
                BigDecimal amount = new BigDecimal(splitLine[3]);
                Transactions aT = new Transactions(type, date, amount, id);
                this.addTrans(aT);
            }
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String name) {
        try {
            PrintWriter fileWrite = new PrintWriter(new FileOutputStream(new File(name)));
            // Transactions[] sorted = sortByDate();
            // for (Transactions t : sorted)
            for (HashMap.Entry<String, Transactions> entry : ledger.entrySet())
                fileWrite.println(entry.getValue().getID()+DELIM+entry.getValue().getType()+DELIM+df.format(entry.getValue().getDate())+DELIM+entry.getValue().getAmount());
            fileWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearTrans() {
        ledger.clear();
    }
}
