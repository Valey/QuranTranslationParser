public enum Languages {
    ALBANIAN("sq"),
    AZERBAIJANI("az"),
    BENGALI("bn"),
    BOSNIAN("bs"),
    BULGARIAN("bg"),
    CHINESE("zh"),
    CZECH("cs"),
    DIVEHI("dv"),
    DEUTCH("nl"),
    ENGLISH("en"),
    FRENCH("fr"),
    GERMAN("de"),
    HAUSA("ha"),
    HINDI("hi"),
    INDONESIAN("id"),
    ITALIAN("it"),
    JAPANESE("ja"),
    KOREAN("ko"),
    KURDISH("ku"),
    MALAY("ms"),
    MALAYLAM("ml"),
    NORVEGIAN("no"),
    PERSIAN("fa"),
    POLISH("pl"),
    PORTGUESE("pt"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SINDHI("sd"),
    SOMALI("so"),
    SPANISH("es"),
    SWAHILI("sw"),
    SWEDISH("sv"),
    TAJIK("tg"),
    TAMIL("ta"),
    TATAR("tt"),
    THAI("th"),
    TURKISH("tr"),
    URDU("ur"),
    UYGHUR("ug"),
    UZBEK("uz");

    private String code;

    Languages(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static boolean isCodeValid(String code) {
        if (!code.matches("\\w\\w")) {
            return false;
        }
        for (Languages language : Languages.values()) {
            if (language.getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }
}
