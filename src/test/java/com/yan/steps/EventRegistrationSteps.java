package com.yan.steps;

import io.cucumber.java.en.*;
import java.util.*;

public class EventRegistrationSteps {
    private String loggedInUser = null;
    private String loggedInRole = null;
    private Map<String, Boolean> events = new HashMap<>();
    private Set<String> registered = new HashSet<>();
    private String lastMessage = null;
    private boolean registrationSuccess = false;

    @Given("a club member {string} is logged in")
    public void a_club_member_is_logged_in(String member) {
        loggedInUser = member;
        loggedInRole = "member";
    }

    @Given("there is an upcoming event {string}")
    public void there_is_an_upcoming_event(String event) {
        events.put(event, false);
    }

    @Given("a blocked club member {string} is logged in")
    public void a_blocked_club_member_is_logged_in(String member) {
        loggedInUser = member;
        loggedInRole = "blocked";
    }

    @Given("there is no event named {string}")
    public void there_is_no_event_named(String event) {
        events.remove(event);
    }

    @Given("{string} is already registered for {string}")
    public void is_already_registered_for(String member, String event) {
        registered.add(member + ":" + event);
    }

    @Given("an admin user {string} is logged in")
    public void an_admin_user_is_logged_in(String admin) {
        loggedInUser = admin;
        loggedInRole = "admin";
    }

    @Given("the event {string} is full")
    public void the_event_is_full(String event) {
        events.put(event, true);
    }

    @Given("no user is logged in")
    public void no_user_is_logged_in() {
        loggedInUser = null;
        loggedInRole = null;
    }

    @When("{string} registers for {string}")
    public void registers_for(String member, String event) {
        attemptRegistration(member, event);
    }

    @When("{string} tries to register for {string}")
    public void tries_to_register_for(String member, String event) {
        attemptRegistration(member, event);
    }

    @When("someone tries to register for {string}")
    public void someone_tries_to_register_for(String event) {
        attemptRegistration(null, event);
    }

    private void attemptRegistration(String user, String event) {
        String actualUser = user != null ? user : loggedInUser;
        if (actualUser == null) {
            lastMessage = "You must be logged in.";
            registrationSuccess = false;
            return;
        }
        if (!events.containsKey(event)) {
            lastMessage = "Event does not exist.";
            registrationSuccess = false;
            return;
        }
        if (loggedInRole == null || (!loggedInUser.equals(actualUser))) {
            lastMessage = "Invalid user.";
            registrationSuccess = false;
            return;
        }
        if (loggedInRole.equals("blocked")) {
            lastMessage = "User is blocked.";
            registrationSuccess = false;
            return;
        }
        if (loggedInRole.equals("admin")) {
            lastMessage = "Admins cannot register for events.";
            registrationSuccess = false;
            return;
        }
        if (events.get(event)) {
            lastMessage = "Event is full.";
            registrationSuccess = false;
            return;
        }
        if (registered.contains(actualUser + ":" + event)) {
            lastMessage = "Already registered for this event.";
            registrationSuccess = false;
            return;
        }

        registered.add(actualUser + ":" + event);
        lastMessage = "Registration successful!";
        registrationSuccess = true;
    }

    @Then("{string} should see a confirmation message")
    public void should_see_a_confirmation_message(String member) {
        assert registrationSuccess;
        assert lastMessage != null && lastMessage.contains("successful");
    }

    @Then("the event should appear in {string}'s profile")
    public void the_event_should_appear_in_profile(String member) {
        assert registered.contains(member + ":" + getRegisteredEventForUser(member));
    }

    private String getRegisteredEventForUser(String member) {
        for (String key : registered) {
            if (key.startsWith(member + ":")) {
                return key.split(":", 2)[1];
            }
        }
        return null;
    }

    @Then("{string} should see an error message")
    public void should_see_an_error_message(String member) {
        assert !registrationSuccess;
        assert lastMessage != null && !lastMessage.contains("successful");
    }

    @Then("the event should not appear in {string}'s profile")
    public void the_event_should_not_appear_in_profile(String member) {
        String event = getRegisteredEventForUser(member);
        assert event == null || !registered.contains(member + ":" + event);
    }

    @Then("they should see an error message")
    public void they_should_see_an_error_message() {
        assert !registrationSuccess;
        assert lastMessage != null && !lastMessage.contains("successful");
    }
}

