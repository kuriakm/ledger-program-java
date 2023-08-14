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
    private static String userFile = null;

    public static void main(String[] args) throws ParseException {
        printGreetings();
        System.out.println("Do you have a file you would like to add?\nType [Y/n]");
        String answer = k.nextLine();
        if (answer.equalsIgnoreCase(YES) || answer.equalsIgnoreCase("Y")) {
            System.out.println("Great! Enter the name of the file you wish to use.");
            userFile = k.nextLine();
            ledger.readFile(userFile);
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
            REM+" to remove a transaction by ID\n"+
            EDIT+" to edit a pre-existing transaction\n"+
            SEARCH+" to search for a transaction in the system\n"+
            PRINT+" to print your current transactions listed\n"+
            READ+" to read a file with pre-existing transactions\n"+
            WRITE+" to write a file with your current transactions\n"+
            CLEAR+" to clear the console\n"+
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
                case EDIT:
                    editTrans();
                    printTrans();
                    break;
                case SEARCH:
                    searchTrans();
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
        }  catch (Exception e) {
            System.out.println("There was an error when trying to add your transaction. Please try again.");
        }
    }

    public static void removeTrans() {
        if (ledger.isEmpty() == true)
            System.out.println("There are no transactions to edit. Add a transaction first before attempting to edit something.");
        else {
            System.out.println("Type the ID of the transaction you want to remove.");
            String pID = k.nextLine();
            ledger.removeTrans(pID);
        }
    }

    public static void editTrans() {
        try {
            if (ledger.isEmpty() == true) {
                System.out.println("There are no transactions to edit. Add a transaction first before attempting to edit something.");
            } else {
                System.out.println("Type the ID of the transaction you want to edit.");
                String pID = k.nextLine();
                System.out.println("What would you like to edit? Type \"T\" or TYPE to edit transaction type,"+
                "\"D\" or DATE to edit the date, \"A\" or AMOUNT to edit the amount, or \"MORE\" to edit more than one field.");
                String editChoice = k.nextLine();

                if (editChoice.equalsIgnoreCase("TYPE") || editChoice.equalsIgnoreCase("T")) {
                    System.out.println("What type of transaction are you adding? [Type D for DEPOSIT OR W for WITHDRAWAL]");
                    String type = k.nextLine();
                    ledger.editType(pID, type);
                } else if (editChoice.equalsIgnoreCase("DATE") || editChoice.equalsIgnoreCase("D")) {
                    System.out.println("When did this transaction occur? [Type date using yyyy-MM-dd format]");
                    String dateS = k.nextLine();
                    Date date = df.parse(dateS);
                    ledger.editDate(pID, date);
                } else if (editChoice.equalsIgnoreCase("AMOUNT") || editChoice.equalsIgnoreCase("A")) {
                    System.out.println("What was the amount for this transaction? [Use only positive numbers]");
                    String amountS = k.nextLine();
                    BigDecimal amount = new BigDecimal(amountS);
                    ledger.editAmount(pID, amount);
                } else if (editChoice.equalsIgnoreCase("MORE")) {
                    System.out.println("What type of transaction are you adding? [Type D for DEPOSIT, W for WITHDRAWAL or K to keep the current value]");
                    String typeChoice = k.nextLine();
                    if (!typeChoice.equalsIgnoreCase("K")) {
                        ledger.editType(pID, typeChoice);
                    }
                    System.out.println("When did this transaction occur? [Type date using yyyy-MM-dd format or K to keep the current value]");
                    String dateChoice = k.nextLine();
                    if (!dateChoice.equalsIgnoreCase("K")) {
                        Date dateDecided = df.parse(dateChoice);
                        ledger.editDate(pID, dateDecided);
                    }
                    System.out.println("What was the amount for this transaction? [Use only positive numbers or type K to keep the current value]");
                    String amountChoice = k.nextLine();
                    if (!amountChoice.equalsIgnoreCase("K")) {
                        BigDecimal amountDecided = new BigDecimal(amountChoice);
                        ledger.editAmount(pID, amountDecided);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("There was an error when trying to edit your transaction. Please try again.");
        }
    }

    public static void searchTrans() {
        try {
            if (ledger.isEmpty() == true) {
                System.out.println("There are no transactions to search for. Add a transaction first before attempting to search something.");
            } else {
                System.out.println("Are you searching by ID or by transaction details?\n"
                    + "Type \"ID\" to search by ID or \"Details\" to search by transaction details");
                String option = k.nextLine();
                if (option.equalsIgnoreCase("ID")) {
                    System.out.println("Type the ID of the transaction you want to search for.");
                    String pID = k.nextLine();
                    //ledger.searchTrans(pID);
                    if (ledger.searchTrans(pID) == false) // If the boolean method returns false,
                                                          // the user will be notified that the transaction does not exist in the system
                        System.out.println("\nThe transaction could not be found in the system.");
                }
                else if (option.equalsIgnoreCase("Details")) {
                    System.out.println("Write the type of the transaction you want to search for (use D or deposit and W or withdrawal).");
                    String searchType = k.nextLine();
                    System.out.println("Write the date of your transaction in the format of yyyy-MM-dd.");
                    String sSearchDate = k.nextLine();
                    System.out.println("Write the amount of your transaction.");
                    System.out.print("$");
                    String sSearchAmount = k.nextLine();
                    Date searchDate = df.parse(sSearchDate);
                    BigDecimal searchAmount = new BigDecimal(sSearchAmount);
                    //ledger.searchTrans(searchType, searchDate, searchAmount);
                    if (ledger.searchTrans(searchType, searchDate, searchAmount) == false)
                        System.out.println("\nThe transaction could not be found in the system.");
                }
            }
        } catch (Exception e) {
            System.out.println("There was an error when trying to search for your transaction. Please try again.");
        }
    }

    public static void delete() {
        // Present warning before letting user delete ledger value
        System.out.println("Are you sure you want to clear the ledger of all transactions?"+
            "\nWARNING: This action cannot be undone. Type [Y/n]");
        String choice = k.nextLine();
        if (choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase(YES))
            ledger.clearLedger();
    }

    public static void printTrans() {
        ledger.print();
        System.out.println("The total amount of money you have is: $"+ledger.totalUp()+"\n");
    }

    public static void readFile() {
        try {
            System.out.println("Type the name of the file you wish to use.");
            String fName = k.nextLine();
            // If a file does not meet the current file format via this check
            // the readFileWithoutHeader() method in the Ledger class is utilized
            ledger.readFile(fName);
        } catch (Exception e) {
            System.out.println("There was an error when trying to read your file. Please try again.");
        }
    }

    public static void writeToFile() {
        System.out.println("Type the name of the file that you want to write to.");
        String wName = k.nextLine();
        ledger.writeToFile(wName);
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
                ledger.writeToFile(userFile);
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
}
