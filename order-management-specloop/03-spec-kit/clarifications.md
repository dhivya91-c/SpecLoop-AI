# Clarifications

## Scope decisions

| ID | Question | Resolution | Effect |
|---|---|---|---|
| C-01 | Is this a new order service or a brownfield specification of Shopizer? | Brownfield specification of Shopizer 3.2.7 source. | No parallel production implementation is introduced. |
| C-02 | Which module owns Order Management? | The feature crosses `sm-core-model`, `sm-core`, and `sm-shop`. | Requirements and tests follow those ownership boundaries. |
| C-03 | Are `ORDERED`, `PROCESSED`, `DELIVERED`, `REFUNDED`, and `CANCELED` a defined transition graph? | They are the observed enum values only; transition policy is unspecified. | No new legal-transition rules are claimed. |
| C-04 | Are checkout idempotency, payment compensation, and inventory rollback specified? | Not established by the inspected API/service contracts. | Excluded from baseline requirements; listed as follow-up risks. |
| C-05 | Does this pack prove authorization and payment-secret non-disclosure? | No; source paths/DTO boundaries are documented, but dedicated executable security tests are absent. | These remain verification risks, not claimed guarantees. |
| C-06 | Does “implementation” mean replacing Shopizer's order subsystem? | No; the upstream subsystem already exists. | This effort captures, tests, and traces the existing implementation; production redesign requires a separate approved feature. |

## Deferred decisions

Product/security owners must decide transition policy, idempotency keys, retry/compensation behavior, PII retention, and payment-secret handling before a hardening feature is specified. These questions are intentionally not answered by the code-generation agent.