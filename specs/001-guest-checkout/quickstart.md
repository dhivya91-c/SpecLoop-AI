# Quickstart: Verify Guest Checkout

## Prerequisites

- Shopizer source at the pinned `3.2.7` commit.
- Temurin/OpenJDK 17 (Java 11 is also supported); do not use JDK 25 with this Infinispan 9.4 release.
- Maven wrapper and dependencies available.

## Focused test

From the Shopizer repository root, after placing `GuestCheckoutIntegrationTest.java` at `sm-shop/src/test/java/com/salesmanager/test/shop/integration/order/`:

```powershell
$env:JAVA_HOME = 'C:\path\to\jdk-17'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
.\mvnw.cmd -pl sm-shop -am '-Dtest=com.salesmanager.test.shop.integration.order.GuestCheckoutIntegrationTest' '-Dsurefire.failIfNoSpecifiedTests=false' test
```

Expected result: the H2-backed test application creates the sample cart, submits guest checkout without a customer auth token, receives `200 OK`, and returns a confirmation with a populated order ID and product/total summary.

Verified on 2026-10-12 with Temurin JDK 17.0.20.1 using the pinned temporary Shopizer checkout: **3 tests passed, 0 failures, 0 errors, 0 skipped**. The fixture enables the bundled `moneyorder` method for the test merchant and disables outgoing order email only in the test context.

## Broad feature verification

```powershell
.\mvnw.cmd -pl sm-shop -am test
```

Record skipped upstream tests separately; this test does not claim the full application or all payment/shipping integrations are verified.

Verified on 2026-10-12 with Temurin JDK 17.0.20.1: **66 tests, 0 failures, 0 errors, 21 skipped across 21 suites** for the `sm-shop` reactor and required modules. Skips are existing upstream skipped/ignored tests; the upstream `OrderApiIntegrationTest` is still `@Ignore`.

## Manual API smoke test

1. Start Shopizer using the repository's documented local profile and test database.
2. Create a cart through the existing cart API and read its server-calculated total.
3. Submit `POST /api/v1/cart/{code}/checkout` without an Authorization header, using the supported `moneyorder` payment module in a local test environment.
4. Confirm a successful response contains an order ID and the cart's order summary.

Do not use real payment credentials or customer data for this smoke test.
