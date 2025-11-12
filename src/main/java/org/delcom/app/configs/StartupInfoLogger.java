package org.delcom.app.configs;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartupInfoLogger implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Ambil environment
        Environment env = event.getApplicationContext().getEnvironment();

        // Ambil port dan context-path
        String port = env.getProperty("server.port", "8080");
        
        // --- INI BAGIAN YANG DIPERBAIKI ---
        // Kita gunakan "/" sebagai nilai default, sesuai penjelasan sebelumnya.
        String contextPath = env.getProperty("server.servlet.context-path", "/");
        
        // Cek null sudah tidak perlu lagi karena ada nilai default.
        // Kita hanya perlu mengubah "/" menjadi "" agar log URL lebih bersih.
        if (contextPath.equals("/")) {
            contextPath = "";
        }
        

        // Deteksi LiveReload dari DevTools
        boolean liveReloadEnabled = env.getProperty("spring.devtools.livereload.enabled", Boolean.class, false);
        String liveReloadPort = env.getProperty("spring.devtools.livereload.port", "35729");

        // Ambil host (default localhost)
        String host = env.getProperty("server.address", "localhost");

        // Warna ANSI untuk konsol
        String GREEN = "\u001B[32m";
        String CYAN = "\u001B[36m";
        String YELLOW = "\u001B[33m";
        String RESET = "\u001B[0m";

        // Cetak log ke konsol
        System.out.println();
        System.out.println(GREEN + "Application started successfully!" + RESET);
        System.out.println(CYAN + "> URL: http://" + host + ":" + port + contextPath + RESET);
        System.out.println();
        System.out.println(
                liveReloadEnabled
                        ? (YELLOW + "> LiveReload: ENABLED (port " + liveReloadPort + ")" + RESET)
                        : (YELLOW + "> LiveReload: DISABLED" + RESET)
        );
        System.out.println();
    }
}