package io.dpopkov.knowthenix.config;

public class AppConstants {
    public static final String API = "/api";
    public static final String CATEGORIES_URL = API + "/categories";
    public static final String QUESTIONS_URL = API + "/questions";
    public static final String ANSWERS_URL = API + "/answers";
    public static final String QUESTIONS_ANSWERS_URL = QUESTIONS_URL + "/{questionId}/answers";
    public static final String KEYTERMS_URL = API + "/keyterms";
    public static final String SOURCES_URL = API + "/sources";
    public static final String USERS_URL = API + "/users";
}
