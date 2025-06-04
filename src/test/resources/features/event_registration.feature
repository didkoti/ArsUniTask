Feature: Event Registration
  As a club member
  I want to register for upcoming events
  So that I can participate and track my attendance

  Scenario: Successful event registration by a club member
    Given a club member "Ivan" is logged in
    And there is an upcoming event "Board Game Night"
    When "Ivan" registers for "Board Game Night"
    Then "Ivan" should see a confirmation message
    And the event should appear in "Ivan"'s profile

  Scenario: Registration fails for blocked user
    Given a blocked club member "Petar" is logged in
    And there is an upcoming event "Board Game Night"
    When "Petar" tries to register for "Board Game Night"
    Then "Petar" should see an error message
    And the event should not appear in "Petar"'s profile

  Scenario: Registration fails for non-existent event
    Given a club member "Ivan" is logged in
    And there is no event named "Nonexistent Event"
    When "Ivan" tries to register for "Nonexistent Event"
    Then "Ivan" should see an error message
    And the event should not appear in "Ivan"'s profile

  Scenario: Registration fails if already registered
    Given a club member "Ivan" is logged in
    And "Ivan" is already registered for "Board Game Night"
    When "Ivan" tries to register for "Board Game Night"
    Then "Ivan" should see an error message

  Scenario: Admin cannot register for events
    Given an admin user "Admin" is logged in
    And there is an upcoming event "Board Game Night"
    When "Admin" tries to register for "Board Game Night"
    Then "Admin" should see an error message

  Scenario: Registration fails if event is full
    Given a club member "Ivan" is logged in
    And the event "Board Game Night" is full
    When "Ivan" tries to register for "Board Game Night"
    Then "Ivan" should see an error message

  Scenario: Registration fails if user is not logged in
    Given no user is logged in
    When someone tries to register for "Board Game Night"
    Then they should see an error message
