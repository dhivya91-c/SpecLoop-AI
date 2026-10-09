Feature: Order management

  Scenario: Guest checks out a valid cart
    Given a valid cart exists for the current merchant
    And the guest checkout request is valid
    When the guest submits checkout for that cart
    Then the response contains an order confirmation
    And the order belongs to the current merchant

  Scenario: Customer reads an order owned by another customer
    Given an order belongs to a different customer in the current merchant
    And I am authenticated as a customer
    When I request that order through the authenticated order API
    Then the response does not disclose the order

  Scenario: Staff updates order status
    Given I have the order-management staff role for the current merchant
    And an order exists in that merchant
    When I update its status to "PROCESSED"
    Then the order status is "PROCESSED"
    And the status history records the change