package io.dpopkov.knowthenix;

public class Profiles {
    /**
     * Profile used for development using H2 in-memory database.
     * See properties for datasource in the application-dev.yml file.
     */
    public static final String DEV = "dev";
    /**
     * Profile used for initialization of production PostgreSQL database.
     * See properties for datasource in the application-init.yml file.
     */
    public static final String INIT = "init";
    /**
     * Profile used for initialization of production MySQL database (mainly for researching purposes).
     * See properties for datasource in the application-mysqlinit.yml file.
     */
    public static final String MYSQL_INIT = "mysqlinit";
    /**
     * Profile used for production PostgreSQL database after it is initialized.
     * See properties for datasource in the application-prod.yml file.
     */
    public static final String PROD = "prod";
}
