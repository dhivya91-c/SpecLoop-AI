package com.salesmanager.test.shop.integration.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.shop.application.ShopApplication;
import com.salesmanager.shop.model.customer.PersistableCustomer;
import com.salesmanager.shop.model.customer.address.Address;
import com.salesmanager.shop.model.order.transaction.PersistablePayment;
import com.salesmanager.shop.model.order.v0.ReadableOrder;
import com.salesmanager.shop.model.order.v1.PersistableAnonymousOrder;
import com.salesmanager.shop.model.order.v1.ReadableOrderConfirmation;
import com.salesmanager.shop.model.shoppingcart.ReadableShoppingCart;
import com.salesmanager.test.shop.common.ServicesTestSupport;

@SpringBootTest(classes = ShopApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class GuestCheckoutIntegrationTest extends ServicesTestSupport {

    @Test
    public void guestCheckoutWithoutAuthenticationReturnsPersistedOrderConfirmation() {
        ReadableShoppingCart cart = sampleCart();
        assertNotNull(cart.getCode());
        assertNotNull(cart.getTotal());

        PersistableAnonymousOrder checkout = guestOrder(cart.getTotal().toPlainString());
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
        assertNotNull(confirmation.getTotal().getGrandTotal());
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
        PersistableAnonymousOrder checkout = guestOrder(null);
        String unknownCartCode = "missing-" + UUID.randomUUID();
        String checkoutUrl = "/api/v1/cart/" + unknownCartCode + "/checkout?store="
                + Constants.DEFAULT_STORE + "&lang=en";

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                checkoutUrl, new HttpEntity<>(checkout), String.class);

        assertFalse(response.getStatusCode().is2xxSuccessful());
    }

    private PersistableAnonymousOrder guestOrder(String amount) {
        PersistableCustomer customer = new PersistableCustomer();
        customer.setEmailAddress("guest-" + UUID.randomUUID() + "@example.com");
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
        checkout.setCurrency("USD");
        checkout.setCustomerAgreement(true);
        checkout.setCustomer(customer);
        checkout.setPayment(payment);
        return checkout;
    }
}