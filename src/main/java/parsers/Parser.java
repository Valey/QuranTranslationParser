package parsers;

import writers.TranslationWriter;
import org.jsoup.nodes.Document;

import java.io.IOException;

abstract class Parser implements Runnable {

    final String languageCode;
    final String translatorName;
    TranslationWriter writer;
    static final int[] ayahQuantity =
            {7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111, 43, 52, 99, 128, 111, 110, 98, 135, 112, 78, 118, 64, 77, 227
            , 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75, 85, 54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55, 78
            , 96, 29, 22, 24, 13, 14, 11, 11, 18, 12, 12, 30, 52, 52, 44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42, 29, 19, 36, 25
            , 22, 17, 19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11, 11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5, 6};

    Parser(String translatorName, String languageCode, TranslationWriter writer) {
        this.languageCode = languageCode;
        this.translatorName = translatorName;
        this.writer = writer;
    }

    public void run() {
        for (int i = 1; i <= ayahQuantity.length; i++) {
            String[] suraText = getSuraText(i);
            for (int j = 0; j < suraText.length; j++) {
                writer.write(i, j+1, suraText[j]);
            }
            if (i == 8) {
                System.out.println(String.format("Translation %s:%s 25%% downloaded.", languageCode, translatorName));
            }
            if (i == 14) {
                System.out.println(String.format("Translation %s:%s 50%% downloaded.", languageCode, translatorName));
            }
            if (i == 35) {
                System.out.println(String.format("Translation %s:%s 75%% downloaded.", languageCode, translatorName));
            }
        }
        System.out.println(String.format("Translation %s:%s successfully downloaded.", languageCode, translatorName));
    }

    abstract String[] getSuraText(int suraNumber);

    abstract Document getDocument(int sura, int firstAya, int lastAya) throws IOException;
}
