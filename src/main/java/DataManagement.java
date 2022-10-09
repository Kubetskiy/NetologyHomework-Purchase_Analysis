import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataManagement {


    /**
     * loadCategories() - формирование мапы категорий из файла categories.tsv
     */
    static Map<String, String> loadCategories() throws IOException {
        var file = new File("categories.tsv");
        Map<String, String> goodsByCategory = new HashMap<>();
        try (var fis = new FileInputStream(file)) {
            var scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                var s = scanner.nextLine();
                var productAndCategory = s.split("\t");
                goodsByCategory.put(productAndCategory[0], productAndCategory[1]);
            }
        }
        return goodsByCategory;
    }
}
