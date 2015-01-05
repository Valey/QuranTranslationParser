package writers;

import java.io.File;

public abstract class TranslationWriter {

    protected final String translatorName;
    protected final String languageCode;
    protected final String LANGUAGE_FOLDER;

    protected TranslationWriter(String translatorName, String languageCode) {
        this.translatorName = translatorName;
        this.languageCode = languageCode;
        String resultFolder = System.getProperty("user.dir") + File.separator + "result";
        LANGUAGE_FOLDER = resultFolder + File.separator + languageCode;

        File fileResultFolder = new File(resultFolder);
        if (!fileResultFolder.exists()) {
            fileResultFolder.mkdir();
        }
        File fileLanguageFolder = new File(LANGUAGE_FOLDER);
        if (!fileLanguageFolder.exists()) {
            fileLanguageFolder.mkdir();
        }
    }

    public abstract void write(int sura, int aya, String translationText);
    

}
