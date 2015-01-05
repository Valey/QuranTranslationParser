
import parsers.QuranComParser;
import writers.DatabaseTranslationWriter;
import writers.TranslationWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        System.out.println("Please, enter language code of the translation of the Quran:");
        System.out.println("LANGUAGE: code");
        System.out.println("----------------------------------------");
        for (Languages translation : Languages.values()) {
            System.out.println(translation + ": " + translation.getCode());
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                try {
                    String translationCode = reader.readLine();
                    if (!Languages.isCodeValid(translationCode)) {
                        throw new IllegalArgumentException();
                    }
                    String[] availableTranslators = QuranComParser.getAvailableTranslators(translationCode);
                    int indexTranslators = 1;
                    for (String translatorName : availableTranslators) {
                        TranslationWriter writer = new DatabaseTranslationWriter(translatorName, translationCode);
                        threadPool.execute(new QuranComParser(translatorName, translationCode, writer));
                        System.out.println(String.format("%d: start downloading - %s:%s", indexTranslators++, translationCode, translatorName));
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Illegal language code, try again.");
                    continue;
                }
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
    }
}
