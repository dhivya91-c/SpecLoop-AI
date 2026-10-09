# Order Management Design

## Plan: Baseline and Technical Constraints

Keep Shopizer's existing layered implementation: `sm-shop` owns REST adapters and authorization; facade/populator code maps API DTOs to the `sm-core-model` aggregate; `sm-core` owns order business operations and persistence contracts. Checkout delegates to `OrderFacade`, which coordinates customer/cart and order services rather than constructing persistence rows in the controller.

The `Order` aggregate is merchant-scoped and cascades line items, totals, history, and attributes. `OrderService` exposes lookup/list, save/update, checkout processing, total calculation, invoice generation, downloadable-item detection, and capturable-order queries. `OrderRepository` provides the JPA lookup plus custom listing queries.

## Request Design

- Keep authenticated and anonymous checkout DTOs separate; never accept a customer identity from an authenticated request body as the authority.
- Resolve merchant and locale/language from established request context.
- Enforce the authenticated principal/merchant/role scope before returning or mutating order data.
- Persist status and status-history changes through the service/facade boundary.
- Reuse the established total-calculation pipeline rather than recomputing amounts in a controller.

## Hardening Decisions

1. Define the legal status-transition graph and reject invalid transitions.
2. Define checkout idempotency and payment/inventory compensation.
3. Audit response DTOs and logs for payment data and customer PII.
4. Confirm status/history atomicity, event/notification behavior, and authorization coverage for customer routes.
5. Confirm API pagination conventions; the source contains `start`/`count` and `page`/`count` variants.

No new production implementation is introduced by this analysis pack. Proposed design improvements remain decisions, not baseline guarantees.

### Implementation constraints

- Upstream parent POM targets Java 11 and Spring Boot 2.5.12; source is pinned to tag `3.2.7` even though POM artifact versions report `3.2.5`.
- Keep persistence in `sm-core-model` / `sm-core`, API adapters in `sm-shop`, and use the existing facade/service/repository boundary.
- Preserve API v1 paths and DTO contracts when documenting the baseline; no endpoint migration is part of this scope.
- Validation must run against the upstream Maven reactor. The SpecLoop artifact folder is not itself a Maven project, so its test specifications are not independently executable.
- Treat the generated-code folder as a source-derived snapshot, not deployable generated output; in particular, the normalized controller class must not be added alongside upstream `OrderApi` because it would register duplicate mappings.

## Architecture

```mermaid
flowchart LR
    Client[Storefront or Admin Client] --> API[sm-shop OrderApi and OrderStatusHistoryApi]
    API --> Auth[Principal and AuthorizationUtils]
    API --> Facade[Order Facade and DTO Mappers]
    Facade --> Customer[Customer and ShoppingCart Services]
    Facade --> Service[sm-core OrderService]
    Service --> Repo[OrderRepository and Custom Queries]
    Repo --> DB[(Relational Database)]
    Service --> Totals[Order Total, Tax, Shipping, Payment Modules]
    Repo --> Model[sm-core-model Order Aggregate]
    Model --> DB
```

The diagram represents observed module boundaries. Payment, shipping, and tax are collaborators; their transaction or failure semantics are not asserted here.

## Checkout Sequence

```mermaid
sequenceDiagram
    actor Buyer
    participant API as OrderApi
    participant Auth as Principal and CustomerService
    participant Cart as ShoppingCartService
    participant Facade as OrderFacade
    participant Service as OrderService
    participant Repo as OrderRepository
    Buyer->>API: POST /cart/{code}/checkout or /auth/cart/{code}/checkout
    opt authenticated checkout
        API->>Auth: resolve principal to customer
        Auth-->>API: customer
    end
    API->>Cart: load cart by code and merchant context
    Cart-->>API: cart
    API->>Facade: process order(request, customer?, cart context)
    Facade->>Service: calculate/process order
    Service->>Repo: persist order aggregate
    Repo-->>Service: persisted order
    Service-->>Facade: order
    Facade-->>API: order confirmation
    API-->>Buyer: confirmation (200 OK in current mappings)
```

The diagram captures the API-to-domain flow. Payment authorization, stock updates, rollback, and notification steps are omitted because their ordering and guarantees were not established by the inspected controller and service contract.

## Data Model

```mermaid
classDiagram
    class Order {
        Long id
        OrderStatus status
        Long customerId
        String customerEmailAddress
        BigDecimal total
        BigDecimal currencyValue
        String shoppingCartCode
        PaymentType paymentType
    }
    class MerchantStore
    class Currency
    class Billing
    class Delivery
    class OrderProduct {
        Long id
        String sku
        String productName
        int productQuantity
        BigDecimal oneTimeCharge
    }
    class OrderTotal
    class OrderStatusHistory {
        Long id
        OrderStatus status
        Date dateAdded
        Integer customerNotified
        String comments
    }
    class OrderAttribute
    class OrderProductAttribute
    class OrderProductPrice
    class OrderProductDownload
    Order "many" --> "1" MerchantStore
    Order "many" --> "1" Currency
    Order *-- "0..1" Billing
    Order *-- "0..1" Delivery
    Order "1" *-- "0..*" OrderProduct
    Order "1" *-- "0..*" OrderTotal
    Order "1" *-- "0..*" OrderStatusHistory
    Order "1" *-- "0..*" OrderAttribute
    OrderProduct "1" *-- "0..*" OrderProductAttribute
    OrderProduct "1" *-- "0..*" OrderProductPrice
    OrderProduct "1" *-- "0..*" OrderProductDownload
```

`customerId` is deliberately scalar in the inspected entity. The relationship from `Order` to `MerchantStore` and `Currency` is many-to-one; the embedded values and cascaded child records follow the JPA mappings.