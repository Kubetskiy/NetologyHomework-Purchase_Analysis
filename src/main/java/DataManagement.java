import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataManagement {
    private static final String DATA_FILE = "data.bin";

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

    //    public static void saveAllDataToBinFile(DataHandler.AllSalesData allSalesData) throws IOException {
    public static void saveAllDataToBinFile(Object allSalesData) throws IOException {
        File file = new File(DATA_FILE);
        try (var fos = new FileOutputStream(file);
             var oos = new ObjectOutputStream(fos)) {
            oos.writeObject(allSalesData);
        }
    }

    // Загружаем структуру данных
//    public static DataHandler.AllSalesData loadAllDataFromBinFile() throws IOException, ClassNotFoundException {
    public static Object loadAllDataFromBinFile() throws IOException, ClassNotFoundException {
        File file = new File(DATA_FILE);
        try (var fis = new FileInputStream(file);
             var ois = new ObjectInputStream(fis)) {
//                return (DataHandler.AllSalesData) ois.readObject();
            return ois.readObject();
        }
    }

    // Проверка наличия сохраненных данных
    public static boolean existSavedData() {
        File file = new File(DATA_FILE);
        return file.exists();
    }
}