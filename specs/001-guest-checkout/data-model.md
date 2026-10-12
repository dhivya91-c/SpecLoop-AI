# Data Model: Guest Checkout

This feature reuses Shopizer's existing entities and does not add tables.

## Shopping Cart

- Identified by a cart code and owned by the current merchant context.
- Contains the server-side line items used to calculate the authoritative subtotal and total.
- Is resolved before order processing; a code that cannot be resolved for the merchant is rejected.

## Guest Checkout Request

The API request is a `PersistableAnonymousOrder`, which extends `PersistableOrder` and adds guest customer details.

- **Customer details**: email/contact and optional billing/delivery address. A password is not required for the guest path; account registration is outside scope.
- **Payment details**: payment type, transaction type, payment-module code, and amount. The amount must match the total recalculated from the cart.
- **Order metadata**: currency, customer-agreement flag, comments, and optional shipping quote, as supported by the current DTO.
- **Context**: cart code is in the route; merchant and language are resolved by the existing request context.

## Order Aggregate

- Persists merchant ownership, customer ID/email snapshot, status, currency, purchase dates, and total.
- Owns line items, persisted total rows, status history, and order attributes.
- Customer ID is stored as a scalar; the model intentionally does not require a live customer foreign-key relationship.
- Checkout cascades order child records using the existing persistence mapping.

## Order Confirmation

The API returns `ReadableOrderConfirmation`: order ID, billing/delivery summary, payment/shipping labels when present, total summary, and readable products. The test should assert only fields guaranteed by the current contract and created fixture.
