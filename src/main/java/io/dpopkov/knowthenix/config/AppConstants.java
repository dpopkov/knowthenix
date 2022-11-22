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
    public static final String BASIC_AUTH_URL = API + "/basicAuth";

    /* Security-2 user urls. */
    public static final String USER_URL = "/user";
    public static final String REGISTER_URL = "/register";
    public static final String LOGIN_URL = "/login";
    public static final String RESET_PASSWORD_URL = "/resetPassword";
    public static final String IMAGE_URL = "/image";
    public static final String IMAGE_PROFILE_URL = IMAGE_URL + "/profile";
    public static final String UPDATE_PROFILE_IMAGE_URL = "/updateProfileImage";
}
