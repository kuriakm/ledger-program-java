import java.util.Scanner;
import java.text.ParseException;

public class LedgerFE {
    private static Scanner k;
    private static LedgerFacade facade;
    protected static String userFile;
    public static final String ADD = "ADD";
    public static final String REM = "REMOVE";
    public static final String EDIT = "EDIT";
    public static final String SEARCH = "SEARCH";
    public static final String PRINT = "PRINT";
    public static final String READ = "READ";
    public static final String WRITE = "WRITE";
    public static final String DELETE = "DELETE";
    public static final String CLEAR = "CLEAR";
    public static final String QUIT = "QUIT";
    public static final String YES = "YES";
    public static final String NO = "NO";

    public LedgerFE() {
        k = new Scanner(System.in);
    }

    public void run() throws ParseException {
        printGreetings();
        facade = askForFile();
        menu();
    }

    public static void printGreetings() {
        System.out.println("Welcome to your online ledger!\n");
    }

    public LedgerFacade askForFile() throws ParseException {
        System.out.println("Do you have a file you would like to add?\nType [Y/n]");
        String answer = k.nextLine();
        if (answer.equalsIgnoreCase(YES) || answer.equalsIgnoreCase("Y")) {
            LedgerFacade facade = LedgerFacade.getInstance(true);
            return facade;
        } else {
            System.out.println("That's okay.");
            LedgerFacade facade = LedgerFacade.getInstance();
            return facade;
        }
    }

    public static void menuOptions() {
        System.out.println("\nWelcome to the menu!\nType " + ADD + " to add a transaction\n" +
                REM + " to remove a transaction by ID\n" +
                EDIT + " to edit a pre-existing transaction\n" +
                SEARCH + " to search for a transaction in the system\n" +
                PRINT + " to print your current transactions listed\n" +
                READ + " to read a file with pre-existing transactions\n" +
                WRITE + " to write a file with your current transactions\n" +
                CLEAR + " to clear the console\n" +
                DELETE + " to remove all transactions from your ledger\n" +
                QUIT + " to end the program");
    }

    public static void menu() throws ParseException {
        printTotal();
        boolean quit = false;
        while (!quit) {
            menuOptions();
            System.out.println();
            String choice = k.nextLine();
            switch (choice.toUpperCase()) { // case-insensitive
                case ADD:
                    add();
                    printList();
                    break;
                case REM:
                    remove();
                    printList();
                    break;
                case EDIT:
                    edit();
                    printList();
                    break;
                case SEARCH:
                    search();
                    break;
                case PRINT:
                    printList();
                    break;
                case READ:
                    readFile();
                    printList();
                    break;
                case WRITE:
                    writeToFile();
                    break;
                case CLEAR:
                    clearConsole();
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

    public static void add() {
        try {
            System.out.println("What type of transaction are you adding? [Type D for DEPOSIT OR W for WITHDRAWAL]");
            String type = k.nextLine();

            System.out.println("When did this transaction occur? [Type date using yyyy-MM-dd format]");
            String date = k.nextLine();

            System.out.println("What was the amount for this transaction? [Use only positive numbers]");
            System.out.print("$");
            String amount = k.nextLine();

            boolean checkMethod = facade.addTransaction(type, date, amount);
            if (!checkMethod)
                System.out.println("There was an error when trying to add your transaction. Please try again later.");
        } catch (Exception e) {
            System.out.println("There was an error when trying to add your transaction. Please try again later.");
        }
    }

    public static void remove() {
        if (facade.isEmpty())
            System.out.println(
                    "There are no transactions to edit. Add a transaction first before attempting to edit something.");
        else {
            System.out.println("Type the ID of the transaction you want to remove.");
            String pID = k.nextLine();
            boolean checkMethod = facade.removeTransaction(pID);
            if (!checkMethod)
                System.out
                        .println("There was an error when trying to remove your transaction. Please try again later.");
        }
    }

    public static void edit() {
        if (facade.isEmpty())
            System.out.println(
                    "There are no transactions to edit. Add a transaction first before attempting to edit something.");
        else {
            editByID(); // TODO: Add editByDetails() method
        }
    }

    private static void editByID() {
        System.out.println("Type the ID of the transaction you want to edit.");
        String pID = k.nextLine();
        System.out.println("What would you like to edit? Type \"T\" or TYPE to edit transaction type," +
                "\"D\" or DATE to edit the date, \"A\" or AMOUNT to edit the amount.");
        String valueType = k.nextLine();
        System.out.println("Type the new value below.");
        if (valueType.equalsIgnoreCase("amount") || valueType.equalsIgnoreCase("a"))
            System.out.print("$");
        String valueToEdit = k.nextLine();
        boolean checkMethod = facade.editTransaction(pID, valueType, valueToEdit);
        if (!checkMethod)
            System.out.println("There was an error when trying to edit your transaction. Please try again later.");
    }

    public static void search() {
        try {
            if (facade.isEmpty()) {
                System.out.println(
                        "There are no transactions to search for. Add a transaction first before attempting to search something.");
            } else {
                System.out.println("Are you searching by ID or by transaction details?\n"
                        + "Type \"ID\" to search by ID or \"Details\" to search by transaction details");
                String option = k.nextLine();
                if (option.equalsIgnoreCase("ID"))
                    searchByID();
                else if (option.equalsIgnoreCase("Details"))
                    searchByDetails();
            }
        } catch (Exception e) {
            System.out
                    .println("There was an error when trying to search for your transaction. Please try again later.");
        }
    }

    private static void searchByID() {
        System.out.println("Type the ID of the transaction you want to search for.");
        String pID = k.nextLine();
        boolean checkMethod = facade.searchTransaction(pID);
        if (!checkMethod)
            System.out.println("\nThe transaction could not be found in the system.");
    }

    private static void searchByDetails() {
        System.out.println(
                "Write the type of the transaction you want to search for (use D or deposit and W or withdrawal).");
        String searchType = k.nextLine();
        System.out.println("Write the date of your transaction in the format of yyyy-MM-dd.");
        String searchDate = k.nextLine();
        System.out.println("Write the amount of your transaction.");
        System.out.print("$");
        String searchAmount = k.nextLine();
        // ledger.searchTrans(searchType, searchDate, searchAmount);
        boolean checkMethod = facade.searchTransaction(searchType, searchDate, searchAmount);
        if (!checkMethod)
            System.out.println("\nThe transaction could not be found in the system.");
    }

    public static void delete() {
        // Present warning before letting user delete ledger value
        System.out.println("Are you sure you want to clear the ledger of all transactions?" +
                "\nWARNING: This action cannot be undone. Type [Y/n]");
        String choice = k.nextLine();
        facade.clearLedger(choice);
    }

    public static void printList() {
        boolean checkMethod = facade.printLedger();
        if (checkMethod)
            System.out.println("The total amount of money you have is: $"
                    + (facade.totalUp() == null ? "0.00" : facade.totalUp()) + "\n");
        else
            System.out.println("There was a problem printing your transactions, please try again later.");
    }

    public static void printTotal() {
        boolean checkMethod = facade.printTotal();
        if (!checkMethod)
            System.out.println("There was a problem totaling up your transactions, please try again later.");
    }

    public static void readFile() throws ParseException {
        boolean checkMethod = facade.readFile(true);
        if (!checkMethod)
            System.out.println("There was an error when trying to read your file. Please try again later.");
    }

    public static void writeToFile() {
        boolean checkMethod = facade.writeFile();
        if (!checkMethod)
            System.out.println("There was an error when trying to read your file. Please try again later.");
    }

    public static void clearConsole() {
        boolean checkMethod = facade.clearConsole();
        if (!checkMethod)
            System.out.println("There was an error when trying to clear the console. Please try again later.");
    }

    public static void quit() {
        System.out.println("Would you like to save your session? Type [Y/n]");
        String saveChoice = k.nextLine();
        int counter = 0;
        if (saveChoice.equalsIgnoreCase("Y") || saveChoice.equalsIgnoreCase(YES)) {
            if (userFile == null) {
                writeToFile();
                clearConsole();
            } else {
                facade.writeFile();
                clearConsole();
            }

            while (counter < 10) {
                counter++;
            }
            if (counter == 10) {
                System.out.println("Goodbye!");
            }
        } else {
            clearConsole();
            counter = 0;
            while (counter < 10) {
                counter++;
            }
            if (counter == 10) {
                System.out.println("Goodbye!");
            }
        }
    }

    public static void main(String[] args) throws ParseException {
        LedgerFE fe = new LedgerFE();
        fe.run();
    }
}
