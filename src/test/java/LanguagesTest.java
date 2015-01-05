import org.junit.Test;

import static org.junit.Assert.*;

public class LanguagesTest {

    @Test
    public void testIsCodeValidEn() throws Exception {
        assertTrue(Languages.isCodeValid("en"));
    }

    @Test
    public void testIsCodeValidRu() throws Exception {
        assertTrue(Languages.isCodeValid("ru"));
    }

    @Test
    public void testIsCodeValidEmptyString() throws Exception {
        assertFalse(Languages.isCodeValid(""));
    }

    @Test
    public void testIsCodeValidTooLength() throws Exception {
        assertFalse(Languages.isCodeValid("zzz"));
    }

    @Test
    public void testIsCodeValidWithDigit() throws Exception {
        assertFalse(Languages.isCodeValid("e1"));
    }
}