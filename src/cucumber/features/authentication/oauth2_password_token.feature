Feature: OAuth2 Username Password Credentials Authentication
    Scenario: Successful authentication
        Given a user exists with details:
            | Email    | graham@grahamcox.co.uk |
            | Password | password               |
            | Enabled  | true                   |
        When I request a username password credentials grant for:
            | Username | graham@grahamcox.co.uk |
            | Password | password               |
        Then I get a successful response

    Scenario: Incorrect password
        Given a user exists with details:
            | Email    | graham@grahamcox.co.uk |
            | Password | password               |
            | Enabled  | true                   |
        When I request a username password credentials grant for:
            | Username | graham@grahamcox.co.uk |
            | Password | wrong                  |
        Then I get an error response of "invalid_grant"

        Scenario: Banned user
            Given a user exists with details:
                | Email    | banned@grahamcox.co.uk |
                | Password | password               |
                | Enabled  | false                  |
            When I request a username password credentials grant for:
                | Username | banned@grahamcox.co.uk |
                | Password | password               |
            Then I get an error response of "invalid_grant"

        Scenario: Unknown user
            When I request a username password credentials grant for:
                | Username | unknown@grahamcox.co.uk |
                | Password | password                |
            Then I get an error response of "invalid_grant"
