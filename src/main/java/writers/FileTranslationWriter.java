package writers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FileTranslationWriter extends TranslationWriter {

    private File outputFile;
    private final String TXT_FOLDER = LANGUAGE_FOLDER + File.separator + "txt";

    public FileTranslationWriter(String translatorName, String languageCode) {
        super(translatorName, languageCode);
        File fileTxtFolder = new File(TXT_FOLDER);
        if (!fileTxtFolder.exists()) {
            fileTxtFolder.mkdir();
        }
        outputFile = new File(TXT_FOLDER + File.separator + String.format("quran.%s.%s.txt", this.languageCode, translatorName));

    }

    @Override
    public void write(int sura, int ayah, String translationText) {
        if (translationText == null) {
            throw new NullPointerException("Translation text by" + translatorName + " for writing is null.");
        }
        try (FileWriter writer = new FileWriter(outputFile, true)) {
            writer.write(String.format("%d:%d %s%s", sura, ayah, translationText, System.getProperty("line.separator")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
