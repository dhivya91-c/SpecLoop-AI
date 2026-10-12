# Shopizer Order Management SpecLoop

An online order touches several parts of a store's software. For this hackathon project, we followed one order through Shopizer and wrote down what the existing code does, how its parts connect, and what still needs testing.

This is a guide to Shopizer's order feature, not a new store or replacement order service. We used Spec-Driven Development (SDD): first describe expected behavior, then map the design and tasks to the existing code, and finally record test evidence and gaps.

## Follow an Order

For a short presentation, start with the shopper's checkout requirements in `03-spec-kit/requirements.md`. Follow the request through the diagrams in `03-spec-kit/design.md`, then point to the API, service, repository, and order model in `01-source/`. The matrix in `06-traceability/traceability-matrix.md` connects those requirements to tests. Finish with `07-validation/convergence-report.md` for what passed and what remains open.

For an interactive storefront/admin demo, Shopizer has a [Docker Compose guide](https://shopizer-ecommerce.github.io/documentation/docker.html). We did not run that external demo as part of this work, so check its image versions before presenting it as the same version analyzed here. The JSON in `demo/sample-output/` is an example shape, not a captured response; no live screenshots are included.

## What We Checked

We analyzed Shopizer tag `3.2.7`, commit `6a4a0a65a3408ee8f62597b51d1b3aac24b77dee`. Its Maven POMs say `3.2.5`, so both version numbers are recorded.

On Temurin JDK 17.0.20.1, the focused order test passed (2 tests). The `sm-core` Maven reactor also passed: 30 tests, no failures or errors, and 13 skipped across 16 suites. Some upstream order tests are ignored. We did not run the API/Cucumber scenarios or a live storefront order, so the convergence status is **Partial**. See `07-validation/` for the full results and remaining checks.
On Temurin JDK 17.0.20.1, the original `sm-core` checks passed: 30 tests, no failures or errors, and 13 skipped across 16 suites. The focused guest checkout feature now has canonical Spec Kit artifacts under [`specs/001-guest-checkout`](../specs/001-guest-checkout/spec.md) and a three-case API integration test that passes. The broader `sm-shop` reactor passed 66 tests with no failures/errors and 21 skipped across 21 suites. Some upstream order tests remain ignored. The API/Cucumber specs outside the new test were not run, nor was a live storefront demo; see the feature quickstart and convergence report for exact scope.

Use Java 11 or 17 for this Shopizer release. Its Infinispan 9.4 dependency fails to start on JDK 25.

## Where Things Live

- `01-source/` has Shopizer source snapshots. The file `OrderController.java` is renamed from upstream `OrderApi.java` for this pack; don't deploy it alongside the original API because it would register duplicate routes.
- `02-analysis/` explains the order model, API, business rules, and dependencies.
- `03-spec-kit/` contains the project principles, requirements, design, plan, clarifications, and tasks.
- `04-generated-code/` contains reference copies of existing code, not deployable generated replacements.
- `05-generated-tests/` contains the JUnit test and proposed Cucumber/API checks.
- `06-traceability/` maps requirements to design and tests.
- `07-validation/` contains analysis, coverage, test results, and convergence status.
- `demo/` contains presentation examples and screenshot guidance.

Shopizer is licensed under Apache License 2.0. Its license is included in `01-source/LICENSE.md`; retain the license and upstream attribution when redistributing copied source.