---

description: "Task list for the Shopizer guest checkout feature"
---

# Tasks: Guest Checkout and Confirmation

**Input**: Design documents from `specs/001-guest-checkout/`

**Prerequisites**: `plan.md`, `spec.md`, `research.md`, `data-model.md`, and `contracts/`

**Tests**: Integration tests are required by the P1 feature's independent acceptance test.

**Organization**: Tasks follow the single P1 guest-checkout story. No second story is in scope.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Parallelizable when it touches a distinct file and has no unfinished dependency.
- **[Story]**: The user story served by the task.
- Paths are relative to this SpecLoop repository unless explicitly marked as the temporary Shopizer checkout.

## Phase 1: Setup

**Purpose**: Pin source/runtime and prepare the brownfield test target.

- [x] T001 Confirm Shopizer tag `3.2.7`, commit `6a4a0a65a3408ee8f62597b51d1b3aac24b77dee`, and Java 17 test runtime; record the POM version discrepancy in `specs/001-guest-checkout/research.md`.
- [x] T002 Confirm `sm-shop` fixtures provide `ServicesTestSupport.sampleCart()`, H2 persistence, and bundled `moneyorder`; document their use in `specs/001-guest-checkout/research.md`.

## Phase 2: Foundational

**Purpose**: Align the executable fixture with the existing public v1 contract.

- [x] T003 Specify the existing request, success response, and error behavior in `specs/001-guest-checkout/contracts/guest-checkout.md` and `guest-checkout.openapi.yaml`.
- [x] T004 Add feature data entities and runnable test setup guidance in `specs/001-guest-checkout/data-model.md` and `quickstart.md`.

## Phase 3: User Story 1 - Place an order without signing in (P1)

**Goal**: A guest submits a valid cart without a customer auth token and receives a confirmation for the persisted order.

**Independent Test**: Run the focused Spring Boot integration test against the H2-backed `sm-shop` test application; assert `200 OK`, a positive confirmation ID, and order products/total breakdown.

### Tests for User Story 1

- [x] T005 [US1] Implement `guestCheckoutWithoutAuthenticationReturnsPersistedOrderConfirmation` in `specs/001-guest-checkout/tests/GuestCheckoutIntegrationTest.java`; create a cart with `ServicesTestSupport.sampleCart()`, call `/api/v1/cart/{code}/checkout` without an Authorization header, and assert the confirmation and authorized order readback.
- [x] T006 [US1] Configure the bundled `moneyorder` method and cover unknown-cart and missing-customer-object rejection in `specs/001-guest-checkout/tests/GuestCheckoutIntegrationTest.java`.
- [x] T007 [US1] Run the focused integration tests on Java 17 in the pinned Shopizer checkout; record the 3 passing cases in `specs/001-guest-checkout/quickstart.md`.

## Final Phase: Polish and Cross-Cutting

- [x] T008 Run the `sm-shop` Maven reactor on Java 17 and record passed, failed, and skipped counts in `specs/001-guest-checkout/quickstart.md`.
- [ ] T009 Run a read-only consistency analysis of `spec.md`, `plan.md`, and `tasks.md`; report findings in the session without modifying artifacts.
- [ ] T010 Run Spec Kit convergence after implementation; append new, source-traceable tasks to this file only if verified gaps remain, otherwise leave it unchanged and report Converged.

## Dependencies and Execution Order

T001-T002 establish the source/runtime and fixtures. T003-T004 define the contract, data model, and test guide. T005-T006 implement the P1 integration coverage. T007 validates the focused story. T008 runs broad validation. T009-T010 are the final Spec Kit quality gates.

## Parallel Opportunities

T001 and T002 were independent source/runtime research tasks. No implementation tasks are marked parallel because the integration cases share one Spring fixture and test class.

## MVP

Deliver US1 only: a successful anonymous checkout with confirmation plus rejection of an unknown cart or missing customer object. Shopizer's production endpoint already exists; do not add duplicate production routes. The test source is stored in this feature and staged into the pinned temporary Shopizer checkout for execution. Account registration, real gateway behavior, compensation, idempotency, and transaction guarantees remain out of scope until specified.
