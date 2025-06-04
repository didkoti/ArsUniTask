Feature: Game Rating
  As a club member
  I want to rate games after events
  So that others can see feedback and statistics

  Scenario: Successful game rating by a club member
    Given a club member "Ivan" attended the event "Board Game Night" with game "Catan"
    When "Ivan" rates the game "Catan" with 5 stars and comment "Great game!"
    Then the rating for "Catan" should be updated
    And the comment should be visible publicly

  Scenario: Rating fails if user did not attend event
    Given a club member "Petar" did not attend the event "Board Game Night" with game "Catan"
    When "Petar" tries to rate the game "Catan" with 4 stars
    Then "Petar" should see an error message
    And the rating should not be updated

  Scenario: Rating fails for non-existent game
    Given a club member "Ivan" attended the event "Board Game Night" with game "Catan"
    When "Ivan" tries to rate the game "UnknownGame" with 3 stars
    Then "Ivan" should see an error message

  Scenario: Rating fails for invalid rating value
    Given a club member "Ivan" attended the event "Board Game Night" with game "Catan"
    When "Ivan" tries to rate the game "Catan" with 6 stars
    Then "Ivan" should see an error message

  Scenario: Blocked user cannot rate games
    Given a blocked club member "Petar" attended the event "Board Game Night" with game "Catan"
    When "Petar" tries to rate the game "Catan" with 4 stars
    Then "Petar" should see an error message

  Scenario: Admin cannot rate games
    Given an admin user "Admin" attended the event "Board Game Night" with game "Catan"
    When "Admin" tries to rate the game "Catan" with 5 stars
    Then "Admin" should see an error message

  Scenario: User can leave a comment without rating
    Given a club member "Ivan" attended the event "Board Game Night" with game "Catan"
    When "Ivan" leaves a comment "Fun but too long" for the game "Catan"
    Then the comment should be visible publicly
