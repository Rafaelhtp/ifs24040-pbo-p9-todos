package org.delcom.app.configs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class StartupInfoLoggerTests {

    private StartupInfoLogger logger;

    private ConfigurableEnvironment environment;
    private ConfigurableApplicationContext context;
    private ApplicationReadyEvent event;

    // Menyiapkan stream untuk "menangkap" output dari System.out
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out; // Simpan System.out yang asli

    @BeforeEach
    void setup() {
        // Arahkan System.out ke ByteArrayOutputStream kita
        System.setOut(new PrintStream(outContent));

        logger = new StartupInfoLogger();

        environment = mock(ConfigurableEnvironment.class);
        context = mock(ConfigurableApplicationContext.class);
        event = mock(ApplicationReadyEvent.class);

        when(event.getApplicationContext()).thenReturn(context);
        when(context.getEnvironment()).thenReturn(environment);

        // default property mock
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        // Atur default livereload ke true, tes spesifik bisa override ini
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(true);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");
    }

    @AfterEach
    void restoreStreams() {
        // Kembalikan System.out ke kondisi semula setelah setiap tes
        System.setOut(originalOut);
    }

    @Test
    void testOnApplicationEvent_withSpecificContextPath() {
        // Tes ini menyimulasikan context-path kustom, misal "/app/home"
        // PENTING: Nilai default (argumen ke-2) HARUS SAMA PERSIS dengan di source code ("/")
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/app/home");
        
        logger.onApplicationEvent(event);

        String output = outContent.toString();

        assertTrue(output.contains("Application started successfully!"));
        // Pastikan URL yang tercetak menyertakan context-path kustom
        assertTrue(output.contains("> URL: http://localhost:8080/app/home"));
        assertTrue(output.contains("> LiveReload: ENABLED (port 35729)"));
    }

    @Test
    void testLiveReloadDisabled() {
        // Tes ini fokus untuk memastikan log "DISABLED" muncul
        // Kita biarkan context-path menggunakan skenario default (root)
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/");
        
        // Ini adalah mock utama untuk tes ini
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(false);

        logger.onApplicationEvent(event);

        String output = outContent.toString();
        
        // Pastikan log "DISABLED" yang muncul
        assertTrue(output.contains("> LiveReload: DISABLED"));
    }

    
    @Test
    void testOnApplicationEvent_withRootContextPath() {
        // Tes ini menyimulasikan skenario default di mana context-path adalah "/"
        // Ini akan menguji baris `if (contextPath.equals("/"))`
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/");

        logger.onApplicationEvent(event);

        String output = outContent.toString();

        // Kode Anda seharusnya mengubah "/" menjadi "" saat logging
        // Jadi, pastikan URL *tidak* memiliki garis miring di akhir
        assertTrue(output.contains("> URL: http://localhost:8080"));
        assertFalse(output.contains("> URL: http://localhost:8080/")); // Pastikan tidak ada trailing slash
        assertTrue(output.contains("> LiveReload: ENABLED"));
    }
}