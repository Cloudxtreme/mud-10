@wip
Feature: Authentication
  Definition of Authentication into the system in various forms
  # Enter feature description here

  Scenario: Log in as an unknown user
    Given I have connected to the MUD
    When I authenticate as "graham@grahamcox.co.uk"
    Then I am told that the user does not exist

  Scenario: Log in as a banned user
    Given that a user exists with details:
    | Email    | graham@grahamcox.co.uk |
    | Password | password               |
    | Enabled  | False                  |
    And I have connected to the MUD
    When I authenticate as "graham@grahamcox.co.uk"
    Then I am told that the user is banned

  Scenario: Log in as a wrong password
    Given that a user exists with details:
      | Email    | graham@grahamcox.co.uk |
      | Password | password               |
      | Enabled  | False                  |
    And I have connected to the MUD
    When I authenticate as "graham@grahamcox.co.uk" with password "wrong"
    Then I am told that the password is wrong

  Scenario: Log in as a valid user
    Given that a user exists with details:
      | Email    | graham@grahamcox.co.uk |
      | Password | password               |
      | Enabled  | False                  |
    And I have connected to the MUD
    When I authenticate as "graham@grahamcox.co.uk" with password "password"
    Then I log in successfully
