package writers;

public class EmptyTranslationWriter extends TranslationWriter {

    public EmptyTranslationWriter(String title, String translationCode) {
        super(title, translationCode);
    }

    @Override
    public void write(int sura, int aya, String translationText) {
        /*ignored*/
    }
}
