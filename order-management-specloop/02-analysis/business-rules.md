# Business Rules

These rules describe observed source behavior, not proposed policy. `Verified` means directly visible in the inspected 3.2.7-tag source; it does not imply the behavior is covered by an enabled test.

| ID | Rule | Evidence |
|---|---|---|
| BR-01 | An order belongs to a merchant. Single-order repository lookup matches both order ID and merchant ID. | `OrderRepository.findOne(id, merchantId)` |
| BR-02 | An order can outlive its customer; the model stores a scalar customer ID, not a JPA customer relation. | `Order` model comment and `customerId` field |
| BR-03 | Order email is required and limited to 50 characters at the persistence mapping. | `Order.customerEmailAddress` |
| BR-04 | Order reads/lists for a customer are resolved from the authenticated principal on `/auth` routes. | `OrderApi` principal/customer lookup |
| BR-05 | Checkout accepts an authenticated request model and a distinct anonymous request model. | `/auth/cart/{code}/checkout`, `/cart/{code}/checkout` |
| BR-06 | Administrative order operations require one of superadmin, admin, admin-order, or admin-retail roles. | Private list/get/update/history handlers |
| BR-07 | Order status is represented by the five enum values `ORDERED`, `PROCESSED`, `DELIVERED`, `REFUNDED`, `CANCELED`. | `OrderStatus` enum |
| BR-08 | Administrative status update converts the provided string with `OrderStatus.valueOf`; unknown/case-mismatched values are not normalized in this handler. | `OrderApi.updateOrderStatus` |
| BR-09 | Order status history belongs to an order, records a status and required date, and may include comments/customer-notified value. | `OrderStatusHistory` mapping |
| BR-10 | Order, line items, totals, history, and attributes are configured for cascade-all persistence from `Order`. | `Order` one-to-many mappings |
| BR-11 | Merchant order listing filters by merchant code and can filter by customer name/email/phone and status, with pagination. | `OrderRepositoryImpl.listOrders` |
| BR-12 | Authenticated and anonymous checkout delegates order creation to the order facade with cart, merchant, locale/language, and customer context as applicable. | `OrderApi.checkout` |

## Unspecified / follow-up decisions

- Legal status-transition rules and whether a cancelled/refunded order may move again.
- Idempotency behavior for repeated checkout requests, payment retries, and duplicate cart submission.
- Inventory reservation/release timing and compensation after payment failure.
- Transaction boundaries spanning cart, payment, inventory, and order persistence.
- PII retention, masking, and access auditing, especially for embedded deprecated credit-card fields.
- Exact rounding, tax, discount, shipping, and currency-conversion policy beyond delegated calculation services.
- Whether status history must be written atomically with each status change and whether customers are notified.