package parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import writers.DatabaseTranslationWriter;
import writers.TranslationWriter;

import java.io.IOException;

public class QuranComParser extends Parser {

    public QuranComParser(String translatorName, String languageCode, TranslationWriter writer) {
        super(translatorName, languageCode, writer);
    }

    String[] getSuraText(int suraNumber) {
        String[] result = new String[ayahQuantity[suraNumber-1]];
        int firstAyahIndex = 1;
        int lastAyahIndex = ayahQuantity[suraNumber-1]%47;
        int ayahCount = 0;
        for ( ;lastAyahIndex <= ayahQuantity[suraNumber-1]; firstAyahIndex=lastAyahIndex+1, lastAyahIndex += 47) {
            for (int i = 0; i < 10; i++) {
                try {
                    Document document = getDocument(suraNumber, firstAyahIndex, lastAyahIndex);
                    String className = translatorName.equals("transliteration") ? translatorName
                            : "translation "  + translatorName;
                    Elements transText = document.select("tr[class=" + className + "]");

                    for (Element ayahTranslation : transText) {
                        result[ayahCount++] = ayahTranslation.select("td").text();
                    }
                    break;
                } catch (IOException e) {
                    /*try again with for*/
                }
            }
        }
        return result;
    }

    Document getDocument(int sura, int firstAyah, int lastAyah) throws IOException {
        String urlFormat = "http://beta.quran.com/%s/%d/%d-%d#%d/";
        return Jsoup.connect(String.format(urlFormat, languageCode, sura, firstAyah, lastAyah, firstAyah))
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0")
                .get();
    }

    public static String[] getAvailableTranslators(String languageCode) throws IOException {
        Document document = Jsoup.connect("http://beta.quran.com/" + languageCode + "/1/1-2#1/")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0")
                .get();
        Elements translatorsClasses = document.select("table[class=content]").first().select("tr[data-priority]");
        String[] availableTranslators = new String[translatorsClasses.size()];
        int index = 0;
        for (Element title : translatorsClasses) {
            String translatorName = title.className();
            availableTranslators[index++] = translatorName.equalsIgnoreCase("transliteration")? "transliteration"
                : translatorName.split(" ")[1];
        }
        return availableTranslators;
    }

}
