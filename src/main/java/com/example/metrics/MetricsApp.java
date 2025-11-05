package com.example.metrics;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

import java.io.IOException;

public class MetricsApp {
    // Counter 메트릭 - 일정한 증가량
    private static final Counter testCounter = Counter.build()
            .name("test_requests_total")
            .help("테스트용 요청 카운터 (일정한 값)")
            .labelNames("method", "status")
            .register();

    // Gauge 메트릭 - 고정된 값
    private static final Gauge testGauge = Gauge.build()
            .name("test_active_connections")
            .help("테스트용 활성 연결 수 (고정된 값)")
            .labelNames("server")
            .register();

    // Histogram 메트릭 - 고정된 분포
    private static final Histogram testHistogram = Histogram.build()
            .name("test_request_duration_seconds")
            .help("테스트용 요청 지속 시간 (초) - 고정된 분포")
            .labelNames("endpoint")
            .buckets(0.1, 0.5, 1.0, 2.5, 5.0, 10.0)
            .register();

    // Summary 메트릭 - 고정된 분포
    private static final Summary testSummary = Summary.build()
            .name("test_response_size_bytes")
            .help("테스트용 응답 크기 (바이트) - 고정된 분포")
            .labelNames("api")
            .quantile(0.5, 0.05)
            .quantile(0.95, 0.01)
            .quantile(0.99, 0.001)
            .register();

    public static void main(String[] args) throws IOException, InterruptedException {
        // 기본 JVM/프로세스 메트릭 등록: process_cpu_seconds_total 등
        DefaultExports.initialize();

        int port = 9404;
        HTTPServer server = new HTTPServer(port);
        System.out.println("Prometheus metrics listening on /metrics port " + port);
        System.out.println("테스트용 고정 메트릭이 노출됩니다.");

        // 초기 고정값 설정
        initializeFixedMetrics();

        // 주기적으로 Counter 증가 (일정한 증가량)
        while (true) {
            // Counter: 매 5초마다 일정하게 증가
            testCounter.labels("GET", "200").inc(10.0);
            testCounter.labels("GET", "404").inc(2.0);
            testCounter.labels("POST", "200").inc(5.0);
            testCounter.labels("POST", "500").inc(1.0);

            // Gauge는 이미 고정값으로 설정되어 있으므로 변경하지 않음

            // Histogram: 일정한 분포로 관찰값 기록
            testHistogram.labels("/api/users").observe(0.3);
            testHistogram.labels("/api/users").observe(0.7);
            testHistogram.labels("/api/users").observe(1.2);
            testHistogram.labels("/api/products").observe(0.5);
            testHistogram.labels("/api/products").observe(2.0);

            // Summary: 일정한 분포로 관찰값 기록
            testSummary.labels("user-api").observe(1024.0);
            testSummary.labels("user-api").observe(2048.0);
            testSummary.labels("user-api").observe(4096.0);
            testSummary.labels("product-api").observe(512.0);
            testSummary.labels("product-api").observe(8192.0);

            Thread.sleep(5000);
        }
    }

    private static void initializeFixedMetrics() {
        // Gauge: 고정된 값 설정
        testGauge.labels("web-server").set(42.0);
        testGauge.labels("api-server").set(15.0);
        testGauge.labels("db-server").set(8.0);
    }
}
