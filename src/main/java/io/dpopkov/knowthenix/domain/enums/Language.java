package io.dpopkov.knowthenix.domain.enums;

/**
 * Language of translation.
 * New languages must be added to the end of the list only because or ordinal representation in database.
 */
public enum Language {
    BE("Belarusian"),
    DE("German"),
    EN("English"),
    ES("Spanish"),
    FR("French"),
    IT("Italian"),
    JA("Japanese"),
    KO("Korean"),
    RU("Russian"),
    TR("Turkish"),
    UK("Ukrainian"),
    ZH("Chinese")
    ;
    private final String name;

    Language(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
