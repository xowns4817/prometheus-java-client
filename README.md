# prom-java-metrics

테스트용으로 일정한 값만 리턴하는 Prometheus 메트릭 수집기

- /metrics 포트 9404에서 Prometheus 포맷 메트릭 노출
- 포함 메트릭
  - 기본: process_cpu_seconds_total 등 (DefaultExports)
  - **테스트용 커스텀 메트릭**:
    - `test_requests_total`: Counter 메트릭 - 일정한 증가량 (method, status 라벨)
    - `test_active_connections`: Gauge 메트릭 - 고정된 값 (server 라벨)
    - `test_request_duration_seconds`: Histogram 메트릭 - 고정된 분포 (endpoint 라벨)
    - `test_response_size_bytes`: Summary 메트릭 - 고정된 분포 (api 라벨)

## 빌드 & 실행
```bash
cd /Users/taejunkim/prom-java-metrics
mvn -q -e -DskipTests package
java -jar target/prom-java-metrics-0.1.0-jar-with-dependencies.jar
```

## 스크랩 예시
```yaml
scrape_configs:
  - job_name: prom-java-metrics
    static_configs:
      - targets: [host.docker.internal:9404]
```
