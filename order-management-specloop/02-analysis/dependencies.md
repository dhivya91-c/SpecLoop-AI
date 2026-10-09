# Order Management Dependencies

## Module and runtime dependencies

| Concern | Shopizer module / dependency | Role in order flow |
|---|---|---|
| Persistence model | `sm-core-model` | JPA `Order`, line items, totals, attributes, status history, embedded billing/delivery, merchant and currency references. |
| Domain services | `sm-core` | `OrderService` / `OrderServiceImpl`, custom order repositories, customer/cart services, total calculation, payment/shipping/tax collaborators. |
| API and DTOs | `sm-shop` | Spring MVC `OrderApi`, `OrderStatusHistoryApi`, facades, request/response models, authorization and locale/store context. |
| Persistence framework | Spring Boot 2.5.12, Spring Data JPA, Hibernate | Repository implementation and entity mapping. |
| API framework | Spring MVC / Spring Security | `/api/v1` endpoints, validation, authentication principal, role authorization. |
| Mapping and validation | Shopizer facades/populators, Bean Validation | Maps API DTOs to domain entities; applies request/entity validation where annotations are present. |
| Test stack | Maven, JUnit (upstream tests include JUnit 4), Spring Boot test | Builds the multi-module project and runs module tests; inspected order-specific upstream tests are ignored/incomplete. |

## Source boundaries

The order feature is not a single Maven module. `sm-shop` depends on `sm-core` and `sm-core-model`; `sm-core` also depends on `sm-core-model` and `sm-core-modules`. The working source checkout is tag `3.2.7` / commit `6a4a0a65a3408ee8f62597b51d1b3aac24b77dee`, while its POM versions declare `3.2.5`.

## External and cross-cutting collaborators

Order processing can invoke payment, shipping, tax, inventory/catalog, cart, customer, currency, and invoice functionality. This pack records these as collaborators only; their transaction boundaries, retry guarantees, failure compensation, and external service configuration must be derived from the corresponding implementations before changing checkout behavior.