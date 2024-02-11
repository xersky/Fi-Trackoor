import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

    public static void main(String[] args) {
        System.out.println(readFromFile("Transactions.txt"));
    }

}