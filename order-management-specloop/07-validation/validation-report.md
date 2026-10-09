# Validation Report

## Scope and provenance checks

- Inspected upstream `shopizer-ecommerce/shopizer`, tag `3.2.7`, commit `6a4a0a65a3408ee8f62597b51d1b3aac24b77dee`.
- Confirmed the order entity, service interface, and repository snapshots match their upstream files byte-for-byte before the controller class-name normalization.
- Confirmed the repository lookup uses both order ID and merchant ID, and custom merchant listing filters by merchant code.
- Found a version discrepancy: tag/repository page is 3.2.7 while module POMs report 3.2.5.
- Confirmed `sm-core/src/test/.../OrderTest.java` is `@Ignore`; `sm-shop/.../OrderApiIntegrationTest.java` is `@Ignore` and its only test method is commented out.

## Execution status

| Check | Result |
|---|---|
| Source provenance / file mapping | Passed for inspected snapshots |
| Focused order JUnit test | Passed: `mvn -pl sm-core -am -Dtest=com.salesmanager.test.order.specloop.OrderTest -Dsurefire.failIfNoSpecifiedTests=false test`; 2 tests, 0 failures/errors/skips. Executed with available JDK 25. |
| Broad core Maven reactor | Failed: `mvn -pl sm-core -am test`; 28 tests, 1 error, 13 skipped. Unrelated `ProductTest.testCreateProduct` failed with `UnexpectedRollbackException`; the original order test was skipped. |
| Cucumber feature | Not executed; step definitions and Cucumber runtime are not included |
| API JSON | Not executed; requires a running Shopizer instance, authentication, merchant context, and valid cart/order fixtures |
| Shopizer source changes | None; this pack documents and verifies the pinned upstream baseline |

## Known coverage gaps

No enabled upstream order service/API tests were found in the inspected test classes. Required follow-up includes authenticated/anonymous checkout integration, cross-customer and cross-merchant denial, role authorization, list filtering/pagination, invalid status, history atomicity, required email validation, customer deletion retention, response payment-data exclusion, and checkout retry/idempotency.

This report records source analysis and focused verification evidence. It does not claim a green full reactor, production code changes, or complete requirement coverage. See `convergence-report.md` for the scoped verdict.