import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Данная реализация использует явную автоматическую инициализацию МАПы продукт - категория
 * путем вызова процедуры загрузки МАПы в конструкторе.
 */
public class DataHandler {
    private static final DataHandler INSTANCE;

    static {
        try {
            INSTANCE = new DataHandler();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //  Продажи по категориям товаров
    private static final Map<String, Integer> salesByCategory = new HashMap<>();
    //  Соответствие категорий товарам
    private Map<String, String> goodsByCategory = new HashMap<>();


    private DataHandler() throws IOException {
        // Загрузка справочников товаров и категорий
        setGoodsByCategory(DataManagement.loadCategories());
    }
    // Вместо конструктора
    public static DataHandler getInstance() throws IOException {
//        goodsByCategory = DataLoad.loadCategories();
        return INSTANCE;
    }

    public void setGoodsByCategory(Map<String, String> goodsByCategory) {
        this.goodsByCategory = goodsByCategory;
    }

    /**
     * @param newSaleForAdd Строка формата JSON ({"title": "булка", "date": "2022.02.08", "sum": 200})
     */
    public void addSale(String newSaleForAdd) {
        var gson = new Gson();
        // Распихиваем строку JSON в JAVA объект
        var salesRecord = gson.fromJson(newSaleForAdd, SalesRecord.class);
        // Категория, соответствующая товару
        var category = goodsByCategory.get(salesRecord.title);
        if (category == null) {
            category = "другое";
        }
        // Плюсуем продажи
        int currentSales = salesByCategory.getOrDefault(category, 0);
        salesByCategory.put(category, currentSales + salesRecord.sum);
    }

    /**
     * @return Строка JSON {"maxCategory": {"category": "еда","sum": 350000}}
     */
    public String generateAnalysisResults() {
        var gson = new Gson();
        String key = getEntryWithMaximumSales();
        var salesData = new SalesData(key, salesByCategory.get(key));
        var result = new AnalysisResult(salesData);
        return gson.toJson(result);
    }

    //  Вернуть категорию с наибольшим объемом продаж
    private String getEntryWithMaximumSales() {
        int maxSale = -1;
        String key = null;
        for (Map.Entry<String, Integer> entryMap : salesByCategory.entrySet()) {
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
    private class AnalysisResult {
        SalesData maxCategory;

        public AnalysisResult(SalesData salesData) {
            this.maxCategory = salesData;
        }
    }
    private class SalesData {
        String category;
        int sum;

        public SalesData(String category, int sum) {
            this.category = category;
            this.sum = sum;
        }
    }
}
