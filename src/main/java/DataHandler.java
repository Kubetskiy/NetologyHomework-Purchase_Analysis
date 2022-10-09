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
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //  Справочник соответствия категорий товарам
    private final Map<String, String> goodsByCategory;
    //  Продажи по категориям товаров
    private final Map<String, Integer> salesByCategory;

    // Конструктор и инициализация данных
    private DataHandler() throws IOException, ClassNotFoundException {
        // Загрузка справочников товаров и категорий
        this.goodsByCategory = DataManagement.loadCategories();
        this.salesByCategory = DataManagement.loadDataFromBinFile();
    }

    // Вместо конструктора
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
        // Категория, соответствующая товару
        var category = goodsByCategory.get(salesRecord.title);
        if (category == null) {
            category = "другое";
        }
        // Плюсуем продажи
        int currentSales = salesByCategory.getOrDefault(category, 0);
        salesByCategory.put(category, currentSales + salesRecord.sum);
        // Сохраняем данные
        DataManagement.saveDataToBinFile(salesByCategory);
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
