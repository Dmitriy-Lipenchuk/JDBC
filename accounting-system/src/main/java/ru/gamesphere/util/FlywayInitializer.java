package ru.gamesphere.util;

import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

public final class FlywayInitializer {

    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    private FlywayInitializer() {
    }

    public static void initDb() {
        final Flyway flyway = Flyway.configure()
                .dataSource(
                        CREDS.url(),
                        CREDS.login(),
                        CREDS.password()
                )
                .cleanDisabled(false)
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();
    }
}
