# API Analysis

All paths below are relative to `/api/v1`. The controller is `OrderApi` in upstream Shopizer; the local snapshot is named `OrderController.java` and its class declaration is normalized to `OrderController`.

| Method | Path | Access / behavior |
|---|---|---|
| GET | `/private/orders/customers/{id}` | Lists a selected customer's orders for the merchant context; optional `start`, `count` (default 0, 100). Private route. |
| GET | `/auth/orders` | Lists the authenticated customer's orders; optional `page`, `count` (controller fallback 0, 100). Customer is resolved from the authenticated principal. |
| GET | `/private/orders` | Lists merchant orders with criteria/pagination; restricted to configured administrator/order/retail groups. Default `count=25`, `page=0`. |
| GET | `/private/orders/{id}` | Reads one order in merchant context; restricted to configured administrator/order/retail groups. |
| GET | `/auth/orders/{id}` | Reads an order in merchant context after resolving the authenticated customer. |
| POST | `/auth/cart/{code}/checkout` | Authenticated cart checkout using `PersistableOrder`; customer comes from principal. |
| POST | `/cart/{code}/checkout` | Anonymous cart checkout using `PersistableAnonymousOrder`. |
| PATCH | `/private/orders/{id}/customer` | Updates order customer details; administrator/order/retail group authorization. |
| PUT | `/private/orders/{id}/status` | Updates order status from a status string; administrator/order/retail group authorization. |
| GET | `/private/orders/{id}/history` | Lists localized/readable status history; administrator/order/retail group authorization. Implemented in `OrderStatusHistoryApi`. |
| POST | `/private/orders/{id}/history` | Adds status history; same authorization; responds `201 Created`. |

The code uses Spring MVC `@RequestMapping`; checkout and update responses are mapped by the implementation, not inferred here as a stable external OpenAPI contract. The checkout mappings currently declare `200 OK`. Some handlers pass errors through `HttpServletResponse`; verify actual error payloads against deployed configuration before treating them as a contract.

## Authorization observations

Private administrative routes call `AuthorizationUtils.authorizeUser` with superadmin, admin, admin-order, and admin-retail groups. Authenticated customer routes use the request principal to locate a customer. Anonymous checkout intentionally has no authenticated customer. Merchant and language are resolved as request context. The inspected single-order repository lookup includes both order ID and merchant ID.