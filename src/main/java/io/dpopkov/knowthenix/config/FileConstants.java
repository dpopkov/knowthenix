package io.dpopkov.knowthenix.config;

public class FileConstants {
    /**
     * Absolute path to directory containing profile images of users.
     * This directory contains directories for every user which has non-temporary profile images.
     */
    public static final String USER_FOLDER = System.getProperty("user.home") + "/apps/knowthenix/user/";
    /**
     * Base relative part for profile image url.
     * It is stored with user and used in web request for user image.
     * For example: "http:server.org/user/image/{username}/{image-filename}.
     */
    public static final String USER_IMAGE_PATH = "/user/image/";
    /** Extension of jpg image files. */
    public static final String JPG_EXTENSION = "jpg";
    /** Extension of png image files. */
    public static final String PNG_EXTENSION = "png";
}
