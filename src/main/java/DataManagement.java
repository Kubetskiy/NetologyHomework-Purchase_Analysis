import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataManagement {


    /**
     * loadCategories() - формирование мапы категорий из файла categories.tsv
     */
    static Map<String, String> loadCategories() throws IOException {

//        Map<String, String> ctgs = Map.of("булка", "еда", "курица", "еда");

        var file = new File("categories.tsv");
        Map<String, String> goodsByCategory = new HashMap<>();
        try (var fis = new FileInputStream(file)) {
            var scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                var s = scanner.nextLine();
                var productAndCategory = s.split("\t");
                goodsByCategory.put(productAndCategory[0], productAndCategory[1]);
            }
//            goodsByCategory.put("сарделька", "еда");
        }
        return goodsByCategory;
/*
        var outFile = new File("TEST.txt");
        try (var writer = new FileWriter(outFile)) {
            for (Map.Entry<String, String> ent : goodsByCategory.entrySet()) {
                writer.write(ent.getKey()+" "+ent.getValue()+"\n");
            }
*/
/*
            for (Map.Entry<String, String> entry : ctgs.entrySet()) {
                writer.write(entry.getKey()+" "+entry.getValue()+"\n");
            }
        }
*/
//        return goodsByCategory;
    }
}
