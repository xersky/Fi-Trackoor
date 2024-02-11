import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Utils {

    public static Map<String, String> transactionParser(String transactionString) {
        Map<String,String> transactionMap = new HashMap<>();
        int fieldIndex = 0;

        if(transactionString.isEmpty()) return transactionMap;

        for(int i = 0; i < transactionString.length(); i++) {
            int indexOfDelimiter = transactionString.indexOf(",", i);

            if(indexOfDelimiter != - 1) {
                transactionMap.put(Transaction.fromInt(fieldIndex).name(), transactionString.substring(i, indexOfDelimiter));
                i = indexOfDelimiter;
                fieldIndex++;
            } else {
                transactionMap.put(Transaction.fromInt(fieldIndex).name(), transactionString.substring(i, transactionString.length()));
                break;
            }
        }
        return transactionMap;
    }

    public static List<Map<String, String>> allTransactionsParser(String transactionsString) {
        List<Map<String,String>> transactions = new ArrayList<Map<String,String>>();
        for(int i = 0; i < transactionsString.length(); i++) {
            int indexOfDelimiter = transactionsString.indexOf(";", i);
            transactions.add(transactionParser(transactionsString.substring(i, indexOfDelimiter)));
            i = indexOfDelimiter;
        }
        return transactions;
    }

    public static float calculateTotalIncome(List<Map<String,String>> transactions) {
        float totalIncome = 0;

        for (Map<String,String> transaction : transactions) {
            if(transaction.get(Transaction.TYPE.name()).equals("INCOME")) totalIncome += Float.parseFloat(transaction.get(Transaction.AMOUNT.name()));
        }

        return totalIncome;
    }

    public static float calculateTotalExpense(List<Map<String,String>> transactions) {
        float totalExpense = 0;

        for (Map<String,String> transaction : transactions) {
            if(transaction.get(Transaction.TYPE.name()).equals("EXPENSE")) totalExpense += Float.parseFloat(transaction.get(Transaction.AMOUNT.name()));
        }

        return totalExpense;
    }

    public static float calculateNetSavings(List<Map<String,String>> transactions) {
        return calculateTotalIncome(transactions) - calculateTotalExpense(transactions);
    }

    public static String generateReport(List<Map<String,String>> transactions) {
        Set<String> categories = new HashSet<>();
        Map<String,Float> expensesByCategory = new HashMap<>();
        StringBuffer reportBuffer = new StringBuffer();

        float totalIncome = calculateTotalIncome(transactions);
        float totalExpense = calculateTotalExpense(transactions);
        float netSavings = totalIncome - totalExpense;

        reportBuffer.append("----------------------- Financial Activity -----------------------");

        for (Map<String,String> transaction : transactions) {
            reportBuffer.append("\n");
            for (int i = 0; i < 4; i++) {
                reportBuffer.append(Transaction.fromInt(i).name() + ": " + transaction.get(Transaction.fromInt(i).name()) + " | ");
            }
            categories.add(transaction.get(Transaction.CATEGORY.name()));
        }

        for (String category : categories) {
            float expenses = 0;
            for (Map<String,String> transaction : transactions) {
                if(transaction.get(Transaction.CATEGORY.name()).equals(category) && transaction.get(Transaction.TYPE.name()).equals("EXPENSE")) expenses += Float.parseFloat(transaction.get(Transaction.AMOUNT.name()));
            }
            expensesByCategory.put(category, expenses);
        }

        reportBuffer.append("\n---------------------- Expenses By Category ----------------------");
        expensesByCategory.forEach((k,v) -> reportBuffer.append("\n<" + k + "> Total Expenses: " + v));
        reportBuffer.append("\n---------------------------- Summary -----------------------------");
        reportBuffer.append("\nTotal Income: " + totalIncome);
        reportBuffer.append("\nTotal Expenses: " + totalExpense);
        reportBuffer.append("\nNet Savings: " + netSavings);
        reportBuffer.append("\n-----------------------------------------------------------------");

        return reportBuffer.toString();
    }

    public static String readFromFile(String filename){
        StringBuilder fileContent = new StringBuilder();
        try {
            File file = new File(filename);
            if(file.exists()){
                Scanner sc = new Scanner(new FileReader(filename));
                while (sc.hasNextLine()) {
                    fileContent.append(sc.nextLine());
                    fileContent.append(";");
                }
            } else file.createNewFile();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }

    public static void updateFile(String filename, String newFileContent) {
        try {
            File file = new File(filename);

            if(!file.exists()) file.createNewFile();

            PrintWriter out = new PrintWriter(file);
            out.println(newFileContent);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Map<String,String>> transactions = allTransactionsParser(readFromFile("Transactions.txt"));
        updateFile("Report.txt", generateReport(transactions));
    }

}