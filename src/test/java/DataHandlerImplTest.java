import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class DataHandlerImplTest {

    @Test
    void responsesToQueriesShouldMeetExpectations() throws IOException {

        DataHandlerImpl dh = DataHandlerImpl.getInstance();
        // Запрещаем действия с файлом данных
        dh.setLoadAndSaveDataDisabled();

        // {"maxCategory":{"category": "другое","sum": 333}}
        String s1 = "{\"title\": \"рулька\", \"date\": \"2022.02.08\", \"sum\": 333}";
//        String r1 = "{\"maxCategory\":{\"category\":\"другое\",\"sum\":333}}";
        dh.addSale(s1);
//        Assertions.assertEquals(r1, dh.generateAnalysisResults());
        // {"maxCategory":{"category":"еда","sum": 444}}
        String s2 = "{\"title\": \"булка\", \"date\": \"2022.02.08\", \"sum\": 222}";
        String s3 = "{\"title\": \"курица\", \"date\": \"2022.02.08\", \"sum\": 222}";
        String s4 = "{\"title\": \"шапка\", \"date\": \"2022.03.08\", \"sum\": 555}";
        String r2 = """
                {
                  "maxCategory": {
                    "category": "одежда",
                    "sum": 555
                  },
                  "maxYearCategory": {
                    "category": "одежда",
                    "sum": 555
                  },
                  "maxMonthCategory": {
                    "category": "еда",
                    "sum": 444
                  },
                  "maxDayCategory": {
                    "category": "еда",
                    "sum": 444
                  }
                }""";
        dh.addSale(s2);
        dh.addSale(s4);
        dh.addSale(s3);
        Assertions.assertEquals(r2, dh.generateAnalysisResults());
    }
}
