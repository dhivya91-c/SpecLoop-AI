package com.salesmanager.test.shop.integration.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.core.business.utils.CoreConfiguration;
import com.salesmanager.shop.application.ShopApplication;
import com.salesmanager.shop.model.customer.PersistableCustomer;
import com.salesmanager.shop.model.customer.address.Address;
import com.salesmanager.shop.model.order.transaction.PersistablePayment;
import com.salesmanager.shop.model.order.v0.ReadableOrder;
import com.salesmanager.shop.model.order.v1.PersistableAnonymousOrder;
import com.salesmanager.shop.model.order.v1.ReadableOrderConfirmation;
import com.salesmanager.shop.model.shoppingcart.ReadableShoppingCart;
import com.salesmanager.shop.model.store.ReadableMerchantStore;
import com.salesmanager.shop.model.system.IntegrationModuleConfiguration;
import com.salesmanager.test.shop.common.ServicesTestSupport;

@SpringBootTest(classes = ShopApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class GuestCheckoutIntegrationTest extends ServicesTestSupport {

        @org.springframework.beans.factory.annotation.Autowired
        private CoreConfiguration coreConfiguration;

        @Before
        public void disableExternalOrderEmailForTest() {
                coreConfiguration.getProperties().setProperty("ORDER_EMAIL_API", "false");
        }

    @Test
    public void guestCheckoutWithoutAuthenticationReturnsPersistedOrderConfirmation() {
                configureMoneyOrderPayment();
        ReadableMerchantStore store = fetchStore();
        ReadableShoppingCart cart = sampleCart();
        assertNotNull(cart.getCode());
        assertNotNull(cart.getTotal());

        PersistableAnonymousOrder checkout = guestOrder(store.getCurrency(), cart.getTotal().toPlainString());
        String checkoutUrl = "/api/v1/cart/" + cart.getCode() + "/checkout?store="
                + Constants.DEFAULT_STORE + "&lang=en";

        ResponseEntity<ReadableOrderConfirmation> response = testRestTemplate.postForEntity(
                checkoutUrl, new HttpEntity<>(checkout), ReadableOrderConfirmation.class);

        assertEquals(OK, response.getStatusCode());
        ReadableOrderConfirmation confirmation = response.getBody();
        assertNotNull(confirmation);
        assertNotNull(confirmation.getId());
        assertTrue(confirmation.getId() > 0);
        assertNotNull(confirmation.getTotal());
        assertNotNull(confirmation.getTotal().getTotals());
        assertFalse(confirmation.getTotal().getTotals().isEmpty());
        assertNotNull(confirmation.getProducts());
        assertEquals(1, confirmation.getProducts().size());

        HttpEntity<String> adminRequest = new HttpEntity<>(getHeader());
        ResponseEntity<ReadableOrder> persistedOrder = testRestTemplate.exchange(
                "/api/v1/private/orders/" + confirmation.getId() + "?store="
                        + Constants.DEFAULT_STORE + "&lang=en",
                GET,
                adminRequest,
                ReadableOrder.class);

        assertEquals(OK, persistedOrder.getStatusCode());
        assertNotNull(persistedOrder.getBody());
        assertEquals(confirmation.getId(), persistedOrder.getBody().getId());
        assertNotNull(persistedOrder.getBody().getProducts());
        assertEquals(1, persistedOrder.getBody().getProducts().size());
    }

    @Test
    public void guestCheckoutWithUnknownCartDoesNotReturnSuccess() {
        PersistableAnonymousOrder checkout = guestOrder("USD", null);
        String unknownCartCode = "missing-" + UUID.randomUUID();
        String checkoutUrl = "/api/v1/cart/" + unknownCartCode + "/checkout?store="
                + Constants.DEFAULT_STORE + "&lang=en";

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                checkoutUrl, new HttpEntity<>(checkout), String.class);

        assertFalse(response.getStatusCode().is2xxSuccessful());
    }

        @Test
        public void guestCheckoutWithoutCustomerDetailsDoesNotReturnSuccess() {
                PersistableAnonymousOrder checkout = guestOrder("USD", null);
                checkout.setCustomer(null);

                ResponseEntity<String> response = testRestTemplate.postForEntity(
                                "/api/v1/cart/unused/checkout?store=" + Constants.DEFAULT_STORE + "&lang=en",
                                new HttpEntity<>(checkout),
                                String.class);

                assertFalse(response.getStatusCode().is2xxSuccessful());
        }

    private PersistableAnonymousOrder guestOrder(String currency, String amount) {
        PersistableCustomer customer = new PersistableCustomer();
        customer.setEmailAddress("guest-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com");
        customer.setFirstName("Guest");
        customer.setLastName("Shopper");
        customer.setLanguage("en");
        customer.setStoreCode(Constants.DEFAULT_STORE);

        Address billing = new Address();
        billing.setFirstName("Guest");
        billing.setLastName("Shopper");
        billing.setAddress("100 Test Street");
        billing.setCity("Test City");
        billing.setPostalCode("10000");
        billing.setCountry("US");
        customer.setBilling(billing);
        customer.setDelivery(billing);

        PersistablePayment payment = new PersistablePayment();
        payment.setAmount(amount);
        payment.setPaymentType("MONEYORDER");
        payment.setTransactionType("AUTHORIZECAPTURE");
        payment.setPaymentModule("moneyorder");

        PersistableAnonymousOrder checkout = new PersistableAnonymousOrder();
        checkout.setCurrency(currency);
        checkout.setCustomerAgreement(true);
        checkout.setCustomer(customer);
        checkout.setPayment(payment);
        return checkout;
    }

        private void configureMoneyOrderPayment() {
                IntegrationModuleConfiguration configuration = new IntegrationModuleConfiguration();
                configuration.setCode("moneyorder");
                configuration.setActive(true);
                configuration.setDefaultSelected(true);
                Map<String, String> integrationKeys = new HashMap<>();
                integrationKeys.put("address", "SpecLoop test payment address");
                configuration.setIntegrationKeys(integrationKeys);

                ResponseEntity<Void> response = testRestTemplate.postForEntity(
                                "/api/v1/private/modules/payment?store=" + Constants.DEFAULT_STORE,
                                new HttpEntity<>(configuration, getHeader()),
                                Void.class);

                assertEquals(OK, response.getStatusCode());
        }
}
