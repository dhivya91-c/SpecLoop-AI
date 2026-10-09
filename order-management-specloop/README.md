# Shopizer Order Management SpecLoop

Source-backed requirements, design, traceability, and test artifacts for the order flow in [Shopizer](https://github.com/shopizer-ecommerce/shopizer), release tag `3.2.7` (commit `6a4a0a65a3408ee8f62597b51d1b3aac24b77dee`). The repository's Maven POMs report version `3.2.5`; the source snapshot and tag are authoritative for this pack, and this version discrepancy is recorded rather than silently reconciled.

## Scope

Order Management crosses three Shopizer Maven modules: `sm-core-model` owns the JPA order model, `sm-core` owns its service and repository, and `sm-shop` exposes the `/api/v1` order flow. This pack documents order checkout, listing, reads, customer/status changes, and order status history. It does not claim to implement a new standalone order service.

## Artifact map

- `01-source/`: source snapshots from upstream modules. `OrderController.java` normalizes the upstream `OrderApi.java` class name for the requested tree; do not deploy it alongside `OrderApi` because it would duplicate route mappings.
- `02-analysis/`: entity, API, business-rule, and dependency findings.
- `03-spec-kit/`: constitution, clarifications, requirements/user stories/acceptance criteria, technical plan, design, and ordered tasks. Crosswalk: `requirements.md` = specify output, `plan.md` = plan output, and `tasks.md` = tasks output.
- `04-generated-code/`: source-derived baseline snapshots, not deployable generated replacements.
- `05-generated-tests/`: JUnit, Cucumber, and API test specifications; actual test status is recorded under validation.
- `06-traceability/`: requirement-to-design-to-test mapping.
- `07-validation/`: consistency analysis, build/test evidence, limitations, and convergence status.
- `demo/`: sample order-flow output; no Shopizer server screenshots were captured.

## Source provenance

Upstream files are under `sm-core-model/src/main/java/com/salesmanager/core/model/order/Order.java`, `sm-core/src/main/java/com/salesmanager/core/business/services/order/OrderService.java`, `sm-core/src/main/java/com/salesmanager/core/business/repositories/order/OrderRepository.java`, and `sm-shop/src/main/java/com/salesmanager/shop/store/api/v1/order/OrderApi.java`. Status history is exposed by `OrderStatusHistoryApi`.

Shopizer is licensed under Apache License 2.0. The upstream license is included in `01-source/LICENSE.md`; preserve it and the upstream attribution when redistributing copied source.

## Reading order

This is a brownfield Spec-Driven Development pack: constitution/clarifications → requirements → technical plan/design → dependency-ordered tasks → existing implementation/test artifacts → analysis, verification, and convergence. Start with `03-spec-kit/constitution.md`. The Spec Kit stages are mapped into this requested folder structure rather than initialized as a standalone `specify` project. Mermaid diagrams are embedded in `03-spec-kit/design.md` and `tasks.md`.