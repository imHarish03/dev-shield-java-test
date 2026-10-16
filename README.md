# accounts-api: a deliberately vulnerable Spring Boot API

**Do not deploy this.** It exists to test [DevShield AI](https://github.com/imHarish03/dev-shield-ai): every
problem below is planted on purpose, and every secret in it is made up.

A small accounts API (Spring Boot 3.3, Java 17, H2) with the mistakes real projects make.

## What DevShield should find

| Kind | Where | What |
|---|---|---|
| SQL injection (CWE-89) | `AccountRepository.findByOwner`, `listSortedBy`; `LegacyReports.countFor` | Input concatenated or formatted into SQL |
| Command injection (CWE-78) | `AdminController.ping` | `Runtime.exec("ping -c 1 " + host)` |
| Path traversal (CWE-22) | `AdminController.report` | `REPORTS.resolve(name)` without a check |
| XXE (CWE-611) | `AdminController.importXml` | `DocumentBuilderFactory` with external entities on |
| Unsafe deserialization (CWE-502) | `AdminController.restore` | `ObjectInputStream.readObject` on uploads |
| Open redirect (CWE-601) | `AdminController.continueTo` | `"redirect:" + next` |
| Weak cryptography (CWE-327/328/338) | `AdminController.export`, `TokenService` | DES, MD5 passwords, `java.util.Random` tokens |
| Hard-coded credentials (CWE-798) | `TokenService.signingKey`, `LegacyReports.connect`, `application.properties`, `integrations.properties` | Signing key, database passwords, partner API keys |
| Insecure Spring configuration | `SecurityConfig`, `application.properties` | CSRF off, everything `permitAll`, CORS `*`, security debug, all actuator endpoints open, H2 console, stack traces in errors, insecure cookie |
| Vulnerable dependencies | `pom.xml` | commons-text 1.9 (Text4Shell), log4j-core 2.14.1 (Log4Shell), commons-collections 3.2.1, guava 29.0-jre |

## Build

```bash
mvn test
```

The tests pass, so DevShield can verify a fix by building and testing the project before and after it.
