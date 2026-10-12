# Feature Specification: Guest Checkout and Confirmation

**Feature Branch**: `001-guest-checkout`

**Created**: 2026-10-12

**Status**: Draft

**Input**: User request: implement a focused Spec Kit feature for Shopizer guest checkout, based on its existing order-management behavior.

## User Scenarios & Testing

### User Story 1 - Place an order without signing in (Priority: P1)

As a guest shopper, I want to submit my cart and contact/order details without signing in, so I can buy items and receive an order confirmation.

**Why this priority**: This is the essential guest-checkout journey and the smallest independently demonstrable slice of the feature.

**Independent Test**: Submit a valid guest checkout for an existing cart without an authenticated session; verify that one order is created for the cart's merchant and a confirmation is returned with the order summary.

**Acceptance Scenarios**:

1. **Given** an existing non-empty cart for the selected merchant and valid guest/order details, **When** the guest submits checkout without signing in, **Then** the system processes the cart and returns an order confirmation containing the order summary.
2. **Given** the guest has not asked to create an account, **When** checkout succeeds, **Then** the purchase does not require creating a reusable customer login.
3. **Given** a cart code that does not exist for the selected merchant, **When** checkout is submitted, **Then** the request fails and no successful order confirmation is returned.
4. **Given** the guest customer-details object is absent, **When** checkout is submitted, **Then** the request is rejected and no successful order confirmation is returned.

## Edge Cases

- A missing, unknown, or merchant-mismatched cart code must not produce a successful confirmation.
- The checkout payload must contain a customer-details object; guest status means sign-in is omitted, not the order's contact snapshot.
- This feature submits no account credentials; account registration during checkout is outside this feature's scope.
- Payment, shipping, inventory compensation, retry/idempotency, and persistence atomicity behavior are not specified by this feature unless separately verified in the implementation and approved requirements.

## Requirements

### Functional Requirements

- **FR-001**: The system MUST accept a checkout request for a cart code without requiring the shopper to be signed in.
- **FR-002**: The system MUST require a customer-details object in the guest checkout request.
- **FR-003**: The system MUST resolve the cart using both its code and the selected merchant context before processing checkout.
- **FR-004**: The system MUST process a valid guest checkout and return an order confirmation with the resulting order summary.
- **FR-005**: The system MUST reject checkout when the cart cannot be found for the selected merchant and MUST NOT return a success confirmation.
- **FR-006**: The confirmation MUST represent the processed order and its purchase summary; it MUST NOT claim success when checkout processing fails.

### Key Entities

- **Shopping cart**: A merchant-scoped collection of purchasable items, identified by a cart code and consumed as checkout input.
- **Guest customer details**: Contact and billing/delivery details supplied with checkout. This feature does not submit credentials or create an account.
- **Order**: The persisted purchase associated with the selected merchant, cart, customer snapshot, line items, totals, and lifecycle status.
- **Order confirmation**: The shopper-facing result describing the created order and its purchase summary.

## Success Criteria

### Measurable Outcomes

- **SC-001**: In the guest checkout integration test, a valid existing cart can be checked out without an authenticated session; its successful confirmation identifies an order that can be read back by that ID.
- **SC-002**: The returned confirmation identifies the processed purchase and includes the order summary fields defined by the current confirmation contract.
- **SC-003**: An unknown cart or absent customer-details object produces no successful confirmation.
- **SC-004**: The guest checkout flow completes without requiring sign-in or account credentials.

## Assumptions

- This feature documents and tests the existing Shopizer v1 guest checkout contract at `/api/v1/cart/{code}/checkout`; it does not introduce a new endpoint.
- Account registration during checkout is an existing adjacent capability but is explicitly out of scope for this focused feature.
- The current request model supplies order/payment/shipping inputs supported by Shopizer, and checkout continues to use the existing pricing, payment, shipping, and order-processing pipeline.
- Merchant and language are supplied by the existing request context; this feature does not allow the request body to override merchant ownership.
- Product owners have not specified transaction rollback, payment compensation, retry/idempotency, or notification guarantees; no such guarantees are implied here.
- The analyzed source is Shopizer tag `3.2.7`, commit `6a4a0a65a3408ee8f62597b51d1b3aac24b77dee`; the Maven POMs declare version `3.2.5`.
