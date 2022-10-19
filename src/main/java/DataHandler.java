import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Данная реализация использует явную автоматическую инициализацию МАПы продукт - категория
 * путем вызова процедуры загрузки МАПы в конструкторе.
 */
public class DataHandler {
    private static final DataHandler INSTANCE;
    // Чтобы знать, за какую дату возвращать отчет
    private String lastSaleDate;

    static {
        try {
            INSTANCE = new DataHandler();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //  Справочник соответствия категорий товарам
    private final Map<String, String> goodsByCategory;
    //  Продажи по категориям товаров общие, по годам, месяцам и дням
    private final AllSalesData allSalesData;

    // Конструктор и инициализация данных
    private DataHandler() throws IOException, ClassNotFoundException {
        // Загрузка справочников товаров и категорий
        this.goodsByCategory = DataManagement.loadCategories();
        // Загрузка/Инициализация структур данных
        if (DataManagement.existSavedData()) {
//            this.allSalesData = DataManagement.loadAllDataFromBinFile();
            this.allSalesData = (AllSalesData) DataManagement.loadAllDataFromBinFile();
        } else {
            this.allSalesData = new AllSalesData();
            allSalesData.maxCategory = new HashMap<>();
            allSalesData.yearlySales = new HashMap<>();
            allSalesData.monthlySales = new HashMap<>();
            allSalesData.dailySales = new HashMap<>();
        }
    }

    // Внешний "конструктор"
    public static DataHandler getInstance() throws IOException {
        return INSTANCE;
    }

    /**
     * Получаем запись о продаже и добавляем к данным
     *
     * @param newSaleForAdd Строка формата JSON ({"title": "булка", "date": "2022.02.08", "sum": 200})
     */
    public void addSale(String newSaleForAdd) throws IOException {
        var gson = new Gson();
        // Распихиваем строку JSON в JAVA объект
        var salesRecord = gson.fromJson(newSaleForAdd, SalesRecord.class);
        // Получаем категорию, соответствующая товару из МАПы
        var category = goodsByCategory.get(salesRecord.title);
        if (category == null) {
            category = "другое";
        }
        var date = salesRecord.date;
        lastSaleDate = date;
        var saleSum = salesRecord.sum;
        // Плюсуем общие продажи (без учета даты)
        int currentSales = allSalesData.maxCategory.getOrDefault(category, 0);
        allSalesData.maxCategory.put(category, currentSales + saleSum);
        // ----------------------------------------------
        Map<String, Integer> localMap;
        // Годовые продажи
        var year = date.substring(0, 4);
        localMap = allSalesData.yearlySales.getOrDefault(year, new HashMap<>());
        currentSales = localMap.getOrDefault(category, 0);
        localMap.put(category, currentSales + saleSum);
        allSalesData.yearlySales.put(year, localMap);
        // Месячные продажи
        var month = date.substring(0, 7);
        localMap = allSalesData.monthlySales.getOrDefault(month, new HashMap<>());
        currentSales = localMap.getOrDefault(category, 0);
        localMap.put(category, currentSales + saleSum);
        allSalesData.monthlySales.put(month, localMap);
        // Дневные продажи
        localMap = allSalesData.dailySales.getOrDefault(date, new HashMap<>());
        currentSales = localMap.getOrDefault(category, 0);
        localMap.put(category, currentSales + saleSum);
        allSalesData.dailySales.put(date, localMap);

        // Сохраняем данные
        DataManagement.saveAllDataToBinFile(allSalesData);
    }

    /**
     * @return Строка JSON {"maxCategory": {"category": "еда","sum": 350000}}
     */
    public String generateAnalysisResults() {
//        var gson = new Gson();

        String key = getEntryWithMaximumSales(allSalesData.maxCategory);
        var maxInCategory = new SalesData(key, allSalesData.maxCategory.get(key));
        var result = new AllAnalysisResults();
        result.maxCategory = maxInCategory;

        Map<String, Integer> localMap;
        // Годовые продажи
        var year = lastSaleDate.substring(0, 4);
        localMap = allSalesData.yearlySales.get(year);
        key = getEntryWithMaximumSales(localMap);
        result.maxYearCategory = new SalesData(key, localMap.get(key));
        // Месячные продажи
        var month = lastSaleDate.substring(0, 7);
        localMap = allSalesData.monthlySales.get(month);
        key = getEntryWithMaximumSales(localMap);
        result.maxMonthCategory = new SalesData(key, localMap.get(key));
        // Дневные продажи
        var date = lastSaleDate;
        localMap = allSalesData.dailySales.get(date);
        key = getEntryWithMaximumSales(localMap);
        result.maxDayCategory = new SalesData(key, localMap.get(key));

        var gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(result);
    }

    //  Вернуть категорию с наибольшим объемом продаж в переданной МАПе
    private String getEntryWithMaximumSales(Map<String, Integer> salesMap) {
        int maxSale = -1;
        String key = null;
        for (Map.Entry<String, Integer> entryMap : salesMap.entrySet()) {
            if (entryMap.getValue() > maxSale) {
                key = entryMap.getKey();
                maxSale = entryMap.getValue();
            }
        }
        return key;
    }

    // Вспомогательный класс для получения записей о продажах
    private class SalesRecord {
        String title;
        String date;
        int sum;
    }

    // Вспомогательные классы для вывода результатов в JSON
    private class SalesData {
        String category;
        int sum;

        public SalesData(String category, int sum) {
            this.category = category;
            this.sum = sum;
        }
    }
    /**
     * Ключами МАПов являются подстроки даты<br>
     * "YYYY"<br>
     * "YYYY.MM"<br>
     * "YYYY.MM.DD"
     */
    private static class AllSalesData implements Serializable {
        @Serial
        private static final long serialVersionUID = 3L;
        Map<String, Integer> maxCategory;
        Map<String, Map<String, Integer>> yearlySales;
        Map<String, Map<String, Integer>> monthlySales;
        Map<String, Map<String, Integer>> dailySales;
    }

    private class AllAnalysisResults {
        SalesData maxCategory;
        SalesData maxYearCategory;
        SalesData maxMonthCategory;
        SalesData maxDayCategory;
    }
}
