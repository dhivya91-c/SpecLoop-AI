# Implementation Plan: Guest Checkout and Confirmation

**Branch**: `001-guest-checkout` | **Date**: 2026-10-12 | **Spec**: [spec.md](spec.md)

**Input**: Focused P1 journey: submit an existing cart as a guest and receive the resulting order confirmation. Optional account registration is out of scope.

## Summary

Shopizer already exposes guest checkout at `/api/v1/cart/{code}/checkout`. This feature adds executable integration coverage for the existing behavior rather than another checkout implementation. The test will create a product/cart using the existing Shopizer test helpers, submit the documented v1 guest request, use the bundled `moneyorder` payment module, and verify the HTTP confirmation, order ID, totals, and products.

## Technical Context

**Language/Version**: Java 11 source/target; validate on Temurin 17.0.20.1

**Primary Dependencies**: Shopizer 3.2.7 source; Spring Boot 2.5.12; Spring MVC; Spring Data JPA; JUnit 4; Spring Boot Test

**Storage**: H2 in-memory test database initialized by the existing `sm-shop` test profile

**Testing**: Maven reactor (`sm-shop` with required modules); JUnit 4 / Spring `TestRestTemplate`

**Target Platform**: Windows/Linux/macOS JVM running Java 11 or 17

**Project Type**: Existing Java multi-module commerce backend; this feature adds an integration test, not a new endpoint

**Performance Goals**: No new performance target; the existing request/response completes within the integration-test timeout

**Constraints**: Preserve API v1 route and DTOs; merchant context must scope cart lookup; do not use external gateways or real payment secrets; JDK 25 is unsupported by Infinispan 9.4 in this revision

**Scale/Scope**: One guest cart, one order, one merchant in an isolated integration-test database

## Constitution Check

- Source truth: PASS. The endpoint and DTOs are present in the pinned source; this plan adds verification only.
- Tenant/customer safety: PASS. The API resolves the cart with both cart code and merchant context; the test uses the default test merchant.
- Testability/traceability: PASS. The P1 scenarios map to a Spring API integration test and explicit FR/SC IDs.
- Brownfield compatibility: PASS. No route or production behavior changes are planned.
- Evidence discipline: PASS with a Java 17 run; skipped upstream tests and API-specific results will be reported separately.

## Design and Execution

1. Create the guest cart through `ServicesTestSupport.sampleCart()` so its product and total come from Shopizer's own fixture flow.
2. Build a `PersistableAnonymousOrder` with valid customer email/contact details and payment data derived from the server-returned cart total.
3. Select `MONEYORDER` and module code `moneyorder`, available in the bundled Spring configuration, to avoid remote payment calls.
4. POST to `/api/v1/cart/{cartCode}/checkout` without an authentication token.
5. Assert the response status is `200 OK`, confirmation ID is populated, and confirmation includes a non-empty product list and total.
5. Assert the response status is `200 OK`, confirmation ID is populated, and confirmation includes a non-empty product list and total breakdown.
6. Verify unknown-cart and missing-customer-object requests are rejected without a success response.

## Project Structure

### Documentation (this feature)

```text
specs/001-guest-checkout/
├── spec.md
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   ├── guest-checkout.md
│   ├── guest-checkout.openapi.yaml
│   └── README.md
├── checklists/
│   └── requirements.md
└── tasks.md
```

### Source Code (repository root)

```text
specs/001-guest-checkout/tests/GuestCheckoutIntegrationTest.java  # Test artifact in this repository
order-management-specloop/                                      # Existing source-backed analysis pack
<temporary Shopizer checkout>/sm-shop/src/test/java/com/salesmanager/test/shop/integration/order/
└── GuestCheckoutIntegrationTest.java                            # Staged copy used to run Maven
```
**Structure Decision**: Keep Spec Kit docs and the test source under `specs/001-guest-checkout`. Stage a copy of the test into the pinned temporary Shopizer checkout to compile and run it; no Shopizer production source is changed. Reuse Shopizer's fixture builders, test merchant, H2 profile, and local money-order provider.

**Structure Decision**: Keep Spec Kit docs under `specs/001-guest-checkout`. Add a self-contained integration-test source artifact under the feature, then stage that test into the pinned Shopizer checkout for execution. Preserve Shopizer module ownership, existing fixture builders, the default test merchant, H2 profile, and local money-order provider.

## Complexity Tracking

No constitution violations or new architectural abstractions are proposed.
