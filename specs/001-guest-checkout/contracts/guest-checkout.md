# Contract: Guest Checkout

## Request

- **Method/path**: `POST /api/v1/cart/{code}/checkout`
- **Authentication**: no customer bearer token required; this is the guest route.
- **Context**: `store` and `lang` are resolved by Shopizer request context and default to the configured store/language where omitted.
- **Body**: JSON `PersistableAnonymousOrder` (`PersistableOrder` fields plus a `customer` object).
- **Relevant fields**:
  - `customer.emailAddress`: guest contact email.
  - `customer.billing` / `customer.delivery`: address data when needed by the configured checkout flow.
  - `payment.amount`: amount must match Shopizer's server-side recalculation.
  - `payment.paymentType`, `payment.transactionType`, `payment.paymentModule`: selected configured payment method.
  - `shippingQuote`: optional quote identifier when checkout uses a shipping quote.
- **Guest behavior**: omit a password unless explicitly requesting the separate account-registration path; that path is out of scope here.

## Success response

- **Status**: `200 OK` in the current controller mapping.
- **Body**: `ReadableOrderConfirmation` with a nonzero order `id`, readable product list, total summary, and address/payment/shipping summaries when present.

## Failure behavior

- A missing customer object is rejected by the controller.
- An unknown cart is rejected; it must not return a successful confirmation.
- Invalid payload, mismatched payment amount, unavailable payment module, or checkout processing errors are returned as a failed request through Shopizer's runtime exception mapping. Exact error status/body depends on the global exception handler; tests should assert the agreed observable status after running against the application, not assume a payload shape from this controller alone.
- Duplicate-account and password-confirmation behavior belongs to the excluded optional account-registration path.

## Source of contract

Pinned Shopizer `3.2.7`: `sm-shop/.../store/api/v1/order/OrderApi.java`, `sm-shop-model/.../order/v1/PersistableAnonymousOrder.java`, `PersistableOrder.java`, and `ReadableOrderConfirmation.java`.
