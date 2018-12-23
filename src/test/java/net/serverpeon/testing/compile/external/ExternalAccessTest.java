package net.serverpeon.testing.compile.external;

import net.serverpeon.testing.compile.CompilationExtension;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExternalAccessTest {
    @Test
    void testCreate() {
        assertNotNull(CompilationExtension.create());
    }

    @Test
    void testCreateWithExecutor() {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        assertNotNull(CompilationExtension.createWithExecutor(ex));
        ex.shutdown();
    }
}
