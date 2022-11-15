package ru.gamesphere;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.gamesphere.util.FlywayInitializer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public abstract class AbstractTest {

    @NotNull
    private final PrintStream standardOut = System.out;

    @NotNull
    protected final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeAll
    static void initDb() {
        FlywayInitializer.initDb();
    }

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @AfterAll
    static void cleanDb() {
        FlywayInitializer.cleanDb();
    }
}
