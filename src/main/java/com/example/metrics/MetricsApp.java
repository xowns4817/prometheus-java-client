package com.example.metrics;

import com.sun.management.OperatingSystemMXBean;
import io.prometheus.client.Counter;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

import java.io.IOException;
import java.lang.management.ManagementFactory;

public class MetricsApp {
    private static final Counter myCpuSeconds = Counter.build()
            .name("my_process_cpu_seconds_total")
            .help("Alias of process CPU time in seconds.")
            .register();

    public static void main(String[] args) throws IOException, InterruptedException {
        // 기본 JVM/프로세스 메트릭 등록: process_cpu_seconds_total 등
        DefaultExports.initialize();

        int port = 9404;
        HTTPServer server = new HTTPServer(port);
        System.out.println("Prometheus metrics listening on /metrics port " + port);

        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long lastNs = os.getProcessCpuTime();

        while (true) {
            long nowNs = os.getProcessCpuTime();
            long deltaNs = Math.max(0, nowNs - lastNs);
            myCpuSeconds.inc(deltaNs / 1_000_000_000.0); // ns -> s
            lastNs = nowNs;
            Thread.sleep(5000);
        }
    }
}
