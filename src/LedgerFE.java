import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.math.BigDecimal;

public class LedgerFE {
    public static Scanner k = new Scanner(System.in);
    public static Ledger ledger = new Ledger();
    public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    // The following Strings are commands that are available to be used in the program
    // I added these in case I wanted to make changes in the wording + it's easier to reference in code
    public static final String ADD = "ADD";
    public static final String REM = "REMOVE";
    public static final String PRINT = "PRINT";
    public static final String READ = "READ";
    public static final String WRITE = "WRITE";
    public static final String DELETE = "DELETE";
    public static final String QUIT = "QUIT";
    public static final String YES = "YES";
    public static final String NO = "NO";

    public static void main(String[] args) throws ParseException {
        printGreetings();
        System.out.println("Do you have a file you would like to add?\nType [Y/n]");
        String answer = k.nextLine();
        if (answer.equalsIgnoreCase(YES) || answer.equalsIgnoreCase("Y")) {
            System.out.println("Great!");
            readFile();
            System.out.println("The total amount of money you have is: $"+ledger.totalUp());
            menu();
        } else {
            System.out.println("That's okay.");
            menu();
        }
    }

    public static void printGreetings() {
        System.out.println("Welcome to your online ledger!\n");
    }

    public static void printChoices() {
        System.out.println("\nWelcome to the menu!\nType "+ADD+" to add a transaction\n"+
            REM+" to remove a transaction by date and amount\n"+
            PRINT+" to print your current transactions listed\n"+
            READ+" to read a file with pre-existing transactions\n"+
            WRITE+" to write a file with your current transactions\n"+
            DELETE+" to remove all transactions from your ledger\n"+
            QUIT+" to end the program");
    }

    public static void menu() throws ParseException { // This method utilizes a switch-case to make it easier to navigate choices
        boolean quit = false;
        while(!quit) {
            printChoices();
            System.out.println();
            String choice = k.nextLine();

            switch(choice.toUpperCase()) { // case-insensitive
                case ADD:
                    addTrans();
                    printTrans();
                    break;
                case REM:
                    removeTrans();
                    printTrans();
                    break;
                case PRINT:
                    printTrans();
                    break;
                case READ:
                    readFile();
                    printTrans();
                    break;
                case WRITE:
                    writeToFile();
                    break;
                case DELETE:
                    delete();
                    break;
                case QUIT:
                    quit();
                    quit = true;
                    break;
            }
        }
    }

    public static void addTrans() {
        try {
            System.out.println("What type of transaction are you adding? [Type D for DEPOSIT OR W for WITHDRAWAL]");
            String type = k.nextLine();

            System.out.println("When did this transaction occur? [Type date using yyyy-MM-dd format]");
            String sDate = k.nextLine();
            Date date = df.parse(sDate);

            System.out.println("What was the amount for this transaction? [Use only positive numbers]");
            System.out.print("$");
            String amt = k.nextLine();
            BigDecimal amount = new BigDecimal(amt);

            Transactions aT = new Transactions(type, date, amount);
            ledger.addTrans(aT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void removeTrans() {
        System.out.println("Type the ID of the transaction you want to remove.");
        String pID = k.nextLine();
        ledger.removeTrans(pID);
    }

    public static void delete() {
        // Present warning before letting user delete ledger value
        System.out.println("Are you sure you want to clear the ledger of all transactions?"+
            "\nWARNING: This action cannot be undone. Type [Y/n]");
        String choice = k.nextLine();
        if (choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase(YES))
            ledger.clearTrans();
    }

    public static void printTrans() {
        ledger.print();
        System.out.println("The total amount of money you have is: $"+ledger.totalUp()+"\n");
    }

    public static void readFile() throws ParseException {
        System.out.println("Type the name of the file you wish to use.");
        String fName = k.nextLine();
        ledger.readFile(fName);
    }

    public static void writeToFile() {
        System.out.println("Type the name of the file that you want to write to.");
        String wName = k.nextLine();
        ledger.writeToFile(wName);
    }

    public static void quit() {
        System.out.println("Would you like to save your session? Type [Y/n]");
        String saveChoice = k.nextLine();

        if (saveChoice.equalsIgnoreCase("Y") || saveChoice.equalsIgnoreCase(YES)) {
            writeToFile();
            System.out.println("Goodbye!");
        } else {
            System.out.println("Goodbye!");
        }
    }
}
