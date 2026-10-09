# Order Management Technical Plan

## Feature

Document and verify Shopizer's existing order-management baseline at upstream tag `3.2.7` / commit `6a4a0a65a3408ee8f62597b51d1b3aac24b77dee`. This is a brownfield feature study, not a replacement implementation. `requirements.md` is the specification artifact; this file is the Spec Kit plan; `design.md` carries the detailed architecture, sequence, and data model; `tasks.md` is the execution plan.

## Technical Context

- Java 11 target, Spring Boot 2.5.12, Maven multi-module reactor.
- `sm-core-model`: persisted order aggregate and child entities.
- `sm-core`: order service, repository, and business collaborations.
- `sm-shop`: Spring MVC v1 APIs, facades, DTO conversion, authorization/context.
- Parent POM version is `3.2.5` although this source is checked out from tag `3.2.7`; preserve both identifiers in evidence.

## Implementation Approach

1. Preserve current module boundaries and public API v1 routes.
2. Treat upstream implementation as the baseline; make SpecLoop source copies auditable and do not place duplicate controller mappings into Shopizer.
3. Use the current principal, merchant context, facade/service/repository, and total-calculation pipeline for order behavior.
4. Validate through a JUnit 4 order-model test in the upstream `sm-core` Maven test context. Run the broader `sm-core` reactor separately and report unrelated failures.
5. Keep Cucumber and API request artifacts as follow-up test specifications unless their step definitions, runtime, server, auth, and fixtures are supplied.

## Design Decisions and Constraints

- Authenticated and anonymous checkout remain distinct request paths/contracts.
- Order retrieval must remain merchant-scoped; customer routes derive identity from the authenticated principal.
- Staff endpoints retain existing role authorization.
- Do not invent status-transition, idempotency, rollback, notification, or payment-retention policy. Those require product/security decisions and separate executable tests.
- Do not claim payment-secret non-disclosure or bounded maximum pagination without validating response serialization and list limits.

## Verification Plan

- Run the focused order test with `mvn -pl sm-core -am -Dtest=com.salesmanager.test.order.specloop.OrderTest -Dsurefire.failIfNoSpecifiedTests=false test`.
- Run `mvn -pl sm-core -am test` to assess the wider core reactor.
- Check requirement-to-test traceability, source/version provenance, and internal links.
- Evaluate convergence only against this declared brownfield documentation-and-verification scope; report production-hardening gaps separately.