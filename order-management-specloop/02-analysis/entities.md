# Entity Analysis

## Aggregate root: `Order`

Source: `sm-core-model/.../order/Order.java`; JPA table `ORDERS`. The entity is merchant-owned and retains a scalar `customerId` rather than a customer association, explicitly allowing an order to remain after its customer is deleted. `customerEmailAddress` is non-null and limited to 50 characters.

| Concern | Fields / mapping |
|---|---|
| Identity and lifecycle | `id`, `status`, `lastModified`, `datePurchased`, `orderDateFinished`, `orderType` |
| Ownership and customer snapshot | `merchant` many-to-one; scalar `customerId`; required `customerEmailAddress` |
| Totals and currency | `total`, `currencyValue`, `currency` many-to-one |
| Checkout and fulfillment | `shoppingCartCode`, `channel`, `paymentType`, `paymentModuleCode`, `shippingModuleCode`, `customerAgreement`, `confirmedAddress`, embedded `billing` and `delivery` |
| Other metadata | `ipAddress`, `locale`, deprecated embedded `creditCard` |
| Cascaded collections | `orderProducts`, `orderTotal`, `orderHistory`, `orderAttributes`; each is one-to-many with cascade all |

`orderTotal` is ordered by `sort_order`; `orderHistory` is ordered by history ID. Monetary amounts use `BigDecimal`. The order model's embedded credit-card data is deprecated; payment security and tokenization policy are not established by this source and require a separate security review.

## Related persisted entities

- `OrderProduct`: SKU/name snapshot, quantity, one-time charge, owning order; cascades product attributes, prices, and download records.
- `OrderTotal`: persisted total components such as subtotal, shipping, tax, and grand total; the exact calculation is delegated to order-total and checkout services.
- `OrderStatusHistory`: required order reference and `dateAdded`, status enum, optional customer notification flag and comments.
- `OrderAttribute`: additional order metadata.
- Embedded `Billing` and `Delivery`; references to `MerchantStore` and `Currency`.

## Status values

`ORDERED`, `PROCESSED`, `DELIVERED`, `REFUNDED`, and `CANCELED` (`OrderStatus`). The enum alone does not define allowed transitions; do not infer a transition graph from these values.