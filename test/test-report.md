# 🧪 Test Coverage & Resilience Report

## 1. 📌 Overview

**Purpose**: Provide a summary of test efforts to validate the correctness, robustness, and resilience of the Real-Time Fraud Detection System.

**Scope**: Covers unit testing, integration testing, fraud scenario simulation, and resilience (failure recovery) testing.

---

## 2. 🧾 Precondition & Environment Setup

- Unit tests use `ReflectionTestUtils` or `configmap.yaml` to set up fraud detection rules.
- **Rule 1**: Transactions exceeding a threshold (10,000 USD)
- **Rule 2**: Suspicious accounts: ACC-001, ACC-002, ACC-003
- Project is packaged and compiled (`fraud-detection-system-1.0-SNAPSHOT.jar`)
- Integration tests run against localstack or AWS cloud
- Resilience tests run on AWS EKS environment
- All logs, JSON test messages, and screenshots are stored in `./test` folder

> Notes: The current rule engine is extensible. Future enhancements may include DB-driven rule updates, ML-based pattern recognition, and visual rule configuration interfaces.

---

## 3. 🧪 Test Case Overview

| Category          | ID    | Name                             | Expectation Summary                                                 |
|-------------------|-------|----------------------------------|----------------------------------------------------------------------|
| Unit Test         | 1.1   | Normal transaction               | Marked COMPLETED, log success                                       |
| Unit Test         | 1.2   | High amount transaction          | Marked FRAUDULENT, log threshold breach                             |
| Unit Test         | 1.3   | Suspicious account               | Marked FRAUDULENT, log suspicious match                             |
| Unit Test         | 1.4   | Invalid threshold fallback       | Marked FRAUDULENT, default rule applies                             |
| Unit Test         | 1.5   | No suspicious accounts configured| Marked COMPLETED, rule engine fallback verified                     |
| Integration Test  | 2.1   | Localstack SQS send/receive      | Message exchange succeeded, ID `it-test-001`                        |
| Integration Test  | 2.2   | CloudWatch log test              | Log group/stream created, message logged `it-test-002`              |
| Resilience Test   | 3.1   | Pod/node failures                | System continued processing uninterrupted, self-healing validated   |

---

## 4. ✅ Unit Testing

**Tool**: JUnit  
**Command**: `mvn test -Dtest=FraudDetectionServiceTest`

All test cases passed successfully.

📂 See: `./test/01.unit test 1.1-1.5.log`  
🖼 Snapshot: `./test/01.unit test 1.1-1.5.log.snapshot.png`

---

## 5. 🔗 Integration Testing

**Tools**: JUnit + LocalStack  
**Components Tested**: SQS, CloudWatch

- Verified sending/receiving to SQS
- Verified log stream writing to CloudWatch

📂 See: `./test/02.integration test 2.1-2.2.log`  
🖼 Snapshot: `./test/02.integration test 2.1-2.2.log.snapshot.png`

---

## 6. 🔁 Resilience Testing

**Tools**:
- AWS EKS
- `TransactionSenderClient.java`
- `AlertConsumerClient.java`
- `kubectl` (manual pod/node failure)

**Simulated Failures**:
- Single and multiple pod deletions
- Node drain

**Expectation**:
- Transactions continue to be processed
- No alert loss
- All clients continue 1 tx/sec without interruption

📂 Logs:
- `./test/03.resilience test-run TransactionSenderClient.log`
- `./test/03.resilience test-run AlertConsumerClient.log`
- `./test/03.resilience test-run kubectl to kill Pod and Node.log`

---

## 7. 📊 Test Summary

| Test Type                  | Tools Used           | Status  | Notes                                                   |
|----------------------------|----------------------|---------|----------------------------------------------------------|
| Unit Tests (incl. fraud)   | JUnit                | ✅ Pass | Rule engine tested in isolation                          |
| Integration Tests          | JUnit + LocalStack   | ✅ Pass | Verified SQS & CloudWatch connectivity                   |
| Resilience Testing         | EKS, AWS, kubectl    | ✅ Pass | Self-healing under pod/node failure, traffic uninterrupted |
