import java.io.IOException;

public interface DataHandler {

    /**
     * Получаем запись о продаже и добавляем к данным
     *
     * @param newSaleForAdd Строка JSON ({"title": "булка", "date": "2022.02.08", "sum": 200})
     */
    void addSale(String newSaleForAdd) throws IOException;

    /**
     * @return Строка JSON {"maxCategory": {"category": "еда","sum": 350000}}
     */
    String generateAnalysisResults();
}
