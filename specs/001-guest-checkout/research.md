# Research: Guest Checkout and Confirmation

## Decision 1: Reuse the existing v1 guest checkout route

- **Decision**: Verify and preserve `POST /api/v1/cart/{code}/checkout` with `PersistableAnonymousOrder`; do not add another route.
- **Rationale**: `OrderApi.checkout` already resolves the cart using its code and merchant context, passes the guest/customer/order data to the v1 order facade, and returns `ReadableOrderConfirmation` with HTTP 200.
- **Evidence**: Shopizer `sm-shop/.../store/api/v1/order/OrderApi.java`; DTOs in `sm-shop-model/.../order/v1/`.
- **Alternative considered**: Add a new guest-order endpoint. Rejected because it would duplicate an existing public contract.

## Decision 2: Derive payment amount from the cart and use the local money-order module in tests

- **Decision**: The integration test will submit the amount returned by the server-created cart and use `MONEYORDER` with module code `moneyorder`.
- **Rationale**: The v1 facade recalculates the order total and rejects a mismatched payment amount. `moneyorder` is registered in `shopizer-core-modules.xml` and creates a local transaction without a remote gateway.
- **Evidence**: `OrderFacadeImpl.processOrder(PersistableOrder, ...)`; `MoneyOrderPayment`; `shopizer-core-modules.xml`.
- **Alternative considered**: Hard-code the cart amount or call a live card processor. Rejected: a hard-coded amount can drift; a remote processor adds network credentials and nondeterminism unrelated to this feature.

## Decision 3: Exercise the real HTTP path with the existing integration harness

- **Decision**: Add a JUnit 4 Spring Boot integration test using `ServicesTestSupport.sampleCart()` and `TestRestTemplate`.
- **Rationale**: Existing helpers create a product and cart against the test application and H2 database. `OrderApiIntegrationTest` is currently ignored and contains no active test method.
- **Alternative considered**: Only unit-test the DTO or mock the facade. Rejected because neither proves the guest route-to-persistence-to-confirmation path.

## Runtime and source notes

- Source is pinned to Shopizer tag `3.2.7` / commit `6a4a0a65a3408ee8f62597b51d1b3aac24b77dee`; the POM version is `3.2.5`.
- Use Java 11 or 17. Infinispan 9.4 fails to start on JDK 25 because `Subject.getSubject` is unsupported there.
- The order model stores `customerId` as a scalar and requires an email snapshot; account creation during guest checkout is a separate optional path, excluded from this feature.
- No source evidence establishes idempotency, payment compensation, or transactional guarantees across external integrations. This spec makes no claims about them.
