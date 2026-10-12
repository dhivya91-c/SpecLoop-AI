# SpecLoop Constitution

## Core Principles

### I. Source Truth and Scope
Specs MUST distinguish observed behavior from requested changes and assumptions. Brownfield work MUST name the source revision it describes. A source snapshot is evidence, not proof that behavior is tested or safe to deploy.

### II. Tenant and Customer Safety
Order behavior MUST preserve merchant ownership and the applicable customer or staff authorization boundary. Changes affecting identity, access, payment information, or personal data MUST include a relevant security test or be explicitly reported as unverified.

### III. Testable Requirements and Traceability
Every Must requirement MUST have observable acceptance criteria. Implementation tasks and verification evidence MUST trace back to a requirement or an explicitly approved technical constraint. Unmapped tasks and untested requirements MUST be reported.

### IV. Brownfield Compatibility
Changes MUST fit the ownership boundaries and supported runtime of the pinned Shopizer revision. Existing public API behavior MUST NOT be duplicated or changed implicitly. Prefer the smallest change that satisfies an approved requirement.

### V. Evidence Over Claims
Completion claims MUST cite a fresh command, test, or source inspection. Reports MUST state runtime versions and skipped or unrun checks. A feature MUST NOT be called converged while required tasks or material verification gaps remain.

## Project Constraints

- The current study targets Shopizer source tag `3.2.7`, commit `6a4a0a65a3408ee8f62597b51d1b3aac24b77dee`; its Maven POMs declare `3.2.5`.
- This Shopizer revision targets Java 11 and is documented as tested with Java 11 and 17. Use Java 11 or 17 for validation; JDK 25 is not supported by the bundled Infinispan 9.4 runtime.
- Preserve Apache License 2.0 and upstream attribution for copied Shopizer source.
- Payment, inventory, status-transition, retry, and retention policies MUST NOT be invented when absent from the source; clarify them with the responsible owner.

## Delivery Workflow

For each feature, follow Spec Kit's specify, clarify, plan, checklist, tasks, analyze, implement, and converge stages as appropriate. Review generated artifacts before proceeding. Run focused tests after each implementation slice, then run the broadest relevant supported-runtime checks before reporting completion.

## Governance

This constitution governs work in this SpecLoop repository. Amendments MUST explain the reason, update the version and amendment date, and be checked against active feature specs and plans. Reviewers MUST verify requirement traceability, tenant/security boundaries, test evidence, and open convergence tasks before accepting a feature.

**Version**: 1.0.0 | **Ratified**: 2026-10-12 | **Last Amended**: 2026-10-12
