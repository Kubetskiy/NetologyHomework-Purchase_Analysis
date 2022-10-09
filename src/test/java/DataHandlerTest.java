import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class DataHandlerTest {
    static DataHandler dh;

    @Test
    void conversionJSON_toJavaObjectShouldWorkCorrectly() throws IOException {

//        Map<String, String> ctgs = Map.of("булка", "еда", "курица", "еда");

        DataHandler dh = DataHandler.getInstance();
//        dh.setGoodsByCategory(DataManagement.loadCategories());
        // {"maxCategory":{"category": "другое","sum": 333}}
        String s1 = "{\"title\": \"рулька\", \"date\": \"2022.02.08\", \"sum\": 333}";
        String r1 = "{\"maxCategory\":{\"category\":\"другое\",\"sum\":333}}";
        dh.addSale(s1);
//        Assertions.assertEquals(r1, dh.generateAnalysisResults());
        // {"maxCategory":{"category":"еда","sum": 444}}
        String s2 = "{\"title\": \"булка\", \"date\": \"2022.02.08\", \"sum\": 222}";
        String s3 = "{\"title\": \"курица\", \"date\": \"2022.02.08\", \"sum\": 222}";
        String r2 = "{\"maxCategory\":{\"category\":\"еда\",\"sum\":444}}";
        dh.addSale(s2);
        dh.addSale(s3);
        Assertions.assertEquals(r2, dh.generateAnalysisResults());
    }
}
