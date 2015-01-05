package parsers;

import org.junit.Test;
import writers.EmptyTranslationWriter;

import java.io.File;
import java.sql.*;

import static org.junit.Assert.*;

public class QuranComParserTest {

    private final String CONNECTION_FORMAT = "jdbc:sqlite:src" + File.separator +"test"
            + File.separator + "resources" + File.separator +"quran.ensi.db";

    @Test
    public void testGetAvailableTranslatorsTr() throws Exception {
        String[] expectedTrTranslators
                = {"transliteration", "vakfi", "golpinarli", "diyanet", "bulac"
                , "yuksel", "yildirim", "yazir", "ozturk", "ates"};
        String[] trTranslators = QuranComParser.getAvailableTranslators("tr");
        assertArrayEquals(expectedTrTranslators, trTranslators);
    }

    @Test
    public void testGetAvailableTranslatorsRu() throws Exception {
        String[] expectedRuTranslators
                = {"krachkovsky", "porokhova", "kuliev", "muntahab", "abuadel"
                , "sablukov", "osmanov"};
        String[] rusTranslators = QuranComParser.getAvailableTranslators("ru");
        assertArrayEquals(expectedRuTranslators, rusTranslators);
    }

    @Test
    public void testGetAvailableTranslatorsTt() throws Exception {
        String[] expectedTtTranslators = {"nugman"};
        String[] ttTranslators = QuranComParser.getAvailableTranslators("tt");
        assertArrayEquals(expectedTtTranslators, ttTranslators);
    }

    @Test
    public void testGetDocument() throws Exception {
        Parser sdAmrotiParser = new QuranComParser("amroti", "sd", new EmptyTranslationWriter("amroti", "sd"));
        assertNotNull(sdAmrotiParser.getDocument(1, 1, 2));
    }

    @Test
    public void testGetSuraTextKoKoreanFatiha() throws Exception {
        Parser koKoreanParser = new QuranComParser("korean", "ko", new EmptyTranslationWriter("korean", "ko"));
        String[] koKorean
                = {"자비로우시고 자애로우신 하나님의 이름으로"
                ,"온 우주의 주님이신 하나님께찬미를 드리나이다",
                "그분은 자애로우시고 자비로 우시며"
                ,"심판의 날을 주관하시도다"
                ,"우리는 당신만을 경배하오며 당신에게만 구원을 비노니"
                ,"저희들을 올바른 길로 인도하여 주시옵소서"
                ,"그 길은 당신께서 축복을 내리신 길이며 노여움을 받은 자나방황하는 자들이 걷지않는 가장 올바른 길이옵니다"};
        String[] parsedKoKorean = koKoreanParser.getSuraText(1);
        assertArrayEquals(koKorean, parsedKoKorean);
    }

    @Test
    public void testGetSuraTextRuMuntahabAyahKursi() throws Exception {
        Parser ruMuntahabParser = new QuranComParser("muntahab", "ru", new EmptyTranslationWriter("muntahab", "ru"));
        String ruMuntahab
                = "Аллах - нет божества, кроме Него, и только Ему мы должны поклоняться." +
                " Аллах - Живой, Сущий и хранит существование всех людей. Его не объемлет ни дремота, ни сон;" +
                " Он один владеет тем, что в небесах и на земле; и нет Ему равных." +
                " Кто заступится за другого перед Ним без Его дозволения? Аллах - слава Ему Всевышнему!" +
                " - знает всё, что было и что будет. Никто не может постигнуть ничего из Его мудрости и знания," +
                " кроме того, что Он дозволит. Трон Аллаха, Его знания и Его власть обширнее небес и земли," +
                " и не тяготит Его охрана их. Поистине, Он - Всевышний, Единый и Великий!";
        String parsedRuMuntahab = ruMuntahabParser.getSuraText(2)[254];
        assertEquals(ruMuntahab, parsedRuMuntahab);
    }

    @Test
    public void testGetSuraTextDeBubenheimIkhlas() throws Exception {
        Parser deBubenheimParser = new QuranComParser("bubenheim", "de", new EmptyTranslationWriter("bubenheim", "de"));
        String[] deBubenheim
                = {"Sag: Er ist Allah, ein Einer,"
                ,"Allah, der Überlegene."
                ,"Er hat nicht gezeugt und ist nicht gezeugt worden,"
                ,"und niemand ist Ihm jemals gleich."};
        String[] parsedDeBubenheim = deBubenheimParser.getSuraText(112);
        assertArrayEquals(deBubenheim, parsedDeBubenheim);
    }

    @Test
    public void testGetSuraTextEnSahihInternationalFullQuran() throws Exception {
        Parser enSahihInternationalParser = new QuranComParser("saheeh", "en", new EmptyTranslationWriter("transliteration", "en"));
        String ayahSelector = "SELECT text FROM verses WHERE sura=? ORDER BY ayah";
        for (int suraCount = 1; suraCount <= 114; suraCount++) {
            String[] parsedEnSahihInternational = enSahihInternationalParser.getSuraText(suraCount);
            try(Connection connection = DriverManager.getConnection(CONNECTION_FORMAT);
                PreparedStatement statement = connection.prepareStatement(ayahSelector)) {
                statement.setInt(1, suraCount);
                ResultSet resultSet = statement.executeQuery();
                for (String parsedText : parsedEnSahihInternational) {
                    resultSet.next();
                    assertEquals(suraCount + ":" + resultSet.getRow(), resultSet.getString("text"), parsedText);
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}