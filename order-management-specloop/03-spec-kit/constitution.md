# Order Management SDD Constitution

These principles govern this brownfield feature study and any implementation derived from it.

1. **Preserve source truth.** Document behavior from the pinned Shopizer source. Mark proposed requirements and unverified behavior as such; do not invent business policy.
2. **Protect tenant and customer boundaries.** Preserve merchant scoping and principal/role checks. Any change to these boundaries requires explicit security review and tests.
3. **Keep money and order state auditable.** Use the existing decimal-safe pricing pipeline. Status and history changes must remain traceable; do not define new transitions without product approval.
4. **Respect the brownfield architecture.** Keep API, facade, service, model, and repository ownership in their existing Shopizer modules and follow the Java/Spring versions declared by the source project.
5. **Verify claims with executable evidence.** Distinguish source inspection, generated test specifications, and tests actually run. Never report an unexecuted test as passing.
6. **Avoid unsafe payment-data changes.** Treat the deprecated embedded card model as a security concern; do not expand its use or expose secrets while documenting the current baseline.

The constitution is local to this Order Management SpecLoop pack, not a claim that upstream Shopizer has adopted these project-wide principles.