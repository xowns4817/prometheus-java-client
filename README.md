# prom-java-metrics

- /metrics 포트 9404에서 Prometheus 포맷 메트릭 노출
- 포함 메트릭
  - 기본: process_cpu_seconds_total 등(DefaultExports)
  - 커스텀: my_process_cpu_seconds_total (동일 값 별칭)

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
