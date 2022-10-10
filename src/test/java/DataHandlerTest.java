import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class DataHandlerTest {
    static DataHandler dh;

    @Test
    void conversionJSON_toJavaObjectShouldWorkCorrectly() throws IOException {

        File file = new File("data.bin");
        if (file.exists()) {
            file.delete();
        }

        DataHandler dh = DataHandler.getInstance();
        // {"maxCategory":{"category": "другое","sum": 333}}
        String s1 = "{\"title\": \"рулька\", \"date\": \"2022.02.08\", \"sum\": 333}";
        String r1 = "{\"maxCategory\":{\"category\":\"другое\",\"sum\":333}}";
        dh.addSale(s1);
//        Assertions.assertEquals(r1, dh.generateAnalysisResults());
        // {"maxCategory":{"category":"еда","sum": 444}}
        String s2 = "{\"title\": \"булка\", \"date\": \"2022.02.08\", \"sum\": 222}";
        String s3 = "{\"title\": \"курица\", \"date\": \"2022.02.08\", \"sum\": 222}";
        String s4 = "{\"title\": \"шапка\", \"date\": \"2022.03.08\", \"sum\": 555}";
        String r2 = "{\n" +
                "  \"maxCategory\": {\n" +
                "    \"category\": \"одежда\",\n" +
                "    \"sum\": 555\n" +
                "  },\n" +
                "  \"maxYearCategory\": {\n" +
                "    \"category\": \"одежда\",\n" +
                "    \"sum\": 555\n" +
                "  },\n" +
                "  \"maxMonthCategory\": {\n" +
                "    \"category\": \"еда\",\n" +
                "    \"sum\": 444\n" +
                "  },\n" +
                "  \"maxDayCategory\": {\n" +
                "    \"category\": \"еда\",\n" +
                "    \"sum\": 444\n" +
                "  }\n" +
                "}";
        dh.addSale(s2);
        dh.addSale(s4);
        dh.addSale(s3);
        Assertions.assertEquals(r2, dh.generateAnalysisResults());
    }
}
