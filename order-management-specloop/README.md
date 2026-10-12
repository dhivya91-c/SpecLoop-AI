**Shopizer Order Management SpecLoop**
When a customer places an order, that request travels through multiple layers of an e-commerce application. Understanding that journey can be difficult, especially in a large codebase with APIs, services, repositories, business rules, and tests spread across different modules.

This project follows a guest checkout order through Shopizer and documents how the existing implementation works. The goal is not to build a new order service, but to create a clear link between requirements, design, source code, tests, and validation results.

**Why This Project**
Documentation, code, and tests often evolve separately over time, making it harder to understand how a business requirement is actually implemented and verified.

Using Specification-Driven Development (SDD), this project starts with expected behavior, maps it to the existing design and code, and records the evidence needed to show whether the requirement has been validated. The result is a traceable view of the order flow from requirement to verification.

**Following an Order**
A good starting point is 03-spec-kit/requirements.md, which describes the checkout requirements.

**From there you can:**

Review the flow and architecture in 03-spec-kit/design.md
Explore the API, services, repositories, and domain models in 01-source/
See how requirements map to tests in 06-traceability/traceability-matrix.md
Review validation results in 07-validation/convergence-report.md
Together, these artifacts show how a requirement is implemented, tested, and validated within the Shopizer order flow.

**What Was Analyzed**
This analysis is based on:

Shopizer tag 3.2.7
Commit 6a4a0a65a3408ee8f62597b51d1b3aac24b77dee
The Maven POM files reference version 3.2.5, so both version identifiers have been documented.

**Validation Summary**
Testing was performed using Temurin JDK 17.0.20.1.

**Results included:**

Focused order tests: 2 passed
sm-core reactor: 30 tests passed, 0 failures, 0 errors
sm-shop reactor: 66 tests passed, 0 failures, 0 errors
Several upstream tests remain skipped or ignored
Guest checkout API integration tests passed
The project does not include:

A live storefront checkout execution
Full API/Cucumber test execution
Production deployment validation
Because of these limitations, the overall convergence status is currently Partial.

**Runtime Notes**
Use Java 11 or Java 17 with this Shopizer release.

Java 25 is not supported because the Infinispan 9.4 dependency fails during startup.

**Repository Structure**
**01-source/**
Snapshots of the Shopizer source code used for analysis.

Note: OrderController.java is a renamed copy of the upstream OrderApi.java file for documentation purposes. It should not be deployed alongside the original Shopizer application because it would create duplicate API routes.

**02-analysis/**
Explanations of the order domain, business rules, architecture, and dependencies.

**03-spec-kit/**
Requirements, design documents, plans, tasks, and supporting specification artifacts.

**04-generated-code/**
Reference copies of existing Shopizer code used for traceability and analysis. These are not generated replacements and are not intended for deployment.

**05-generated-tests/**
JUnit tests, integration tests, and proposed validation scenarios.

**06-traceability/**
Mappings between requirements, design decisions, implementation, and tests.

**07-validation/**
Coverage analysis, test results, convergence reports, and known gaps.

**demo/**
Sample outputs, presentation assets, and guidance for demonstrations.

**Project Scope**
This repository is intended to help readers understand how Shopizer's order management functionality works and how specification artifacts can be connected to implementation and testing evidence.

It is an analysis and traceability project, not a replacement implementation of the Shopizer order service.

**License**
Shopizer is licensed under the Apache License 2.0.

A copy of the license is included in 01-source/LICENSE.md. If source files are redistributed, please retain the original license and upstream attribution.
