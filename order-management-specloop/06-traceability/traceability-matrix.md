# Traceability Matrix

| Requirement | Design | Acceptance criteria | Test artifact | Status |
|---|---|---|---|---|
| REQ-01 | Aggregate root and persistence layers | AC-01, AC-09 | JUnit `OrderTest`; API checkout | Partial: entity smoke test only |
| REQ-02 | Aggregate child mappings | AC-07 | Cucumber status scenario | Planned; not executed |
| REQ-03 | Authenticated checkout flow | AC-01 | API authenticated-order request; Cucumber guest flow is distinct | Planned |
| REQ-04 | Anonymous checkout contract | AC-02 | API anonymous checkout; Cucumber guest flow | Planned |
| REQ-05 | Principal-scoped customer reads | AC-03 | Cucumber cross-customer scenario | Planned; authorization integration gap |
| REQ-06 | Staff list and repository criteria | AC-05 | API test collection (detail request only) | Gap: list/filter assertions absent |
| REQ-07 | Merchant-scoped lookup | AC-06 | Cucumber/API detail scenario | Planned |
| REQ-08 | Customer detail update | - | No generated test | Gap |
| REQ-09 | Status update | AC-07, AC-08 | Cucumber staff scenario | Planned; invalid-value case absent |
| REQ-10 | Status-history API | AC-07 | Cucumber staff scenario | Planned |
| REQ-11 | Customer-independent order retention | AC-10 | No generated test | Gap |
| REQ-12 | Required customer email | AC-09 | JUnit smoke test does not cover validation | Gap |
| NFR-01 | Authorization and merchant context | AC-03 to AC-06 | Cucumber cross-customer scenario | Planned; needs integration tests |
| NFR-02 | Decimal-safe existing total pipeline | AC-01 | JUnit decimal smoke assertion | Partial |
| NFR-03 | Bounded pagination | AC-05 | API list test absent | Gap |
| NFR-04 | Payment secret exclusion | AC-11 | No generated serialization test | Gap |
| NFR-05 | Status/history consistency | AC-07 | Cucumber staff scenario | Planned; atomicity unverified |

"Planned" means a scenario has been specified, not run. See `07-validation/validation-report.md` for evidence and limitations.