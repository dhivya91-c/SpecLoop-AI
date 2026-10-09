# Order Management Requirements

## Functional Requirements

| ID | Requirement | Priority |
|---|---|---|
| REQ-01 | The system shall persist an order with merchant ownership, customer identifier/email snapshot, status, dates, currency, total, and checkout/payment/shipping metadata. | Must |
| REQ-02 | The system shall persist line items, price/attribute/download details, totals, order attributes, and status history as order-associated records. | Must |
| REQ-03 | The system shall create an order from a cart for an authenticated customer and return an order confirmation. | Must |
| REQ-04 | The system shall support cart checkout for an anonymous customer through a distinct request contract. | Must |
| REQ-05 | The system shall allow an authenticated customer to list and read only their orders within the current merchant context. | Must |
| REQ-06 | The system shall allow authorized merchant staff to list orders using pagination and supported customer/status criteria. | Must |
| REQ-07 | The system shall allow authorized merchant staff to read an order only within the current merchant context. | Must |
| REQ-08 | The system shall allow authorized merchant staff to update order customer details. | Should |
| REQ-09 | The system shall allow authorized merchant staff to update status using a recognized order status. | Must |
| REQ-10 | The system shall expose an order's status history and allow authorized staff to append a history entry. | Must |
| REQ-11 | The system shall preserve an order when its customer record is removed. | Must |
| REQ-12 | The system shall reject order persistence without a customer email address. | Must |

## Non-Functional Requirements

| ID | Requirement |
|---|---|
| NFR-01 | All order access shall enforce merchant isolation and applicable customer/role authorization. |
| NFR-02 | Monetary values shall use decimal-safe representations; order total calculation shall remain delegated to the existing pricing/tax/shipping pipeline. |
| NFR-03 | List operations shall support bounded pagination and avoid returning the entire merchant history by default. |
| NFR-04 | Sensitive payment data shall not be exposed in readable order responses; the deprecated embedded credit-card model requires explicit security review. |
| NFR-05 | Status changes and corresponding history entries should be consistent and auditable; transaction and notification guarantees require confirmation. |

## User Stories

| ID | Story |
|---|---|
| US-01 | As a shopper, I want checkout to create an order from my cart so that I can receive confirmation of my purchase. |
| US-02 | As a guest shopper, I want to check out without creating an account so that I can complete a purchase with the anonymous checkout contract. |
| US-03 | As an authenticated customer, I want to list and view my orders so that I can review my purchase history. |
| US-04 | As an authorized store operator, I want to search and page through my store's orders so that I can process fulfillment work. |
| US-05 | As an authorized store operator, I want to update order status and retain its history so that lifecycle changes are auditable. |
| US-06 | As a store operator, I want order records to remain available if a customer account is removed so that financial/fulfillment records are retained. |

## Acceptance Criteria

These are verification targets; several are not covered by currently enabled upstream tests.

| ID | Given / When | Then |
|---|---|---|
| AC-01 | A valid cart and authenticated customer submit checkout | The API returns a confirmation for an order associated with that customer and current merchant. |
| AC-02 | A valid cart submits the anonymous checkout contract | Checkout does not require an authenticated principal and returns an order confirmation. |
| AC-03 | A customer requests `/auth/orders` or `/auth/orders/{id}` | Results are scoped to the principal's customer and current merchant; another customer's order is not disclosed. |
| AC-04 | A user without an allowed staff role calls a private administrative endpoint | The request is denied without revealing another merchant's order data. |
| AC-05 | Authorized staff request a merchant order list with supported criteria and page bounds | Only matching merchant orders are returned, with a consistent count/page contract. |
| AC-06 | Staff request an order ID belonging to a different merchant | The lookup returns no order and does not expose its details. |
| AC-07 | Authorized staff submit a recognized status | The order status is updated and the resulting history is available; atomicity is verified by integration test. |
| AC-08 | Staff submit an unknown/case-mismatched status value | The API returns a client error and leaves the order unchanged. |
| AC-09 | An order is persisted without customer email | Validation/persistence rejects the order. |
| AC-10 | The associated customer record is removed | The order remains readable to authorized staff with its customer ID/email snapshot. |
| AC-11 | Readable order responses are serialized | Deprecated card number/CVV and other prohibited payment secrets are absent. |
| AC-12 | Checkout is retried after a timeout | The agreed idempotency policy prevents duplicate charge/order effects. |

AC-07 through AC-12 require explicit implementation/operational guarantees not fully specified by the inspected source; close their open decisions before treating them as current behavior.

## Source Alignment

These requirements document capabilities found in the inspected source. Expectations such as “only their orders,” bounded pagination, and payment-data non-exposure are security/quality targets to validate, not all proven by the controller excerpts alone. The test and validation artifacts label these gaps explicitly.