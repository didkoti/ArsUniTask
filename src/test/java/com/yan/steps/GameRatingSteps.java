package com.yan.steps;

import io.cucumber.java.en.*;
import java.util.*;

public class GameRatingSteps {
    private String loggedInUser = null;
    private String loggedInRole = null; // "member", "admin", "blocked", null
    private Set<String> attended = new HashSet<>(); // "user:event:game"
    private Set<String> games = new HashSet<>(); // game names
    private Map<String, List<Integer>> ratings = new HashMap<>(); // game -> list of ratings
    private Map<String, List<String>> comments = new HashMap<>(); // game -> list of comments
    private String lastMessage = null;
    private boolean ratingSuccess = false;

    @Given("a club member {string} attended the event {string} with game {string}")
    public void a_club_member_attended_event_with_game(String user, String event, String game) {
        loggedInUser = user;
        loggedInRole = "member";
        attended.add(user + ":" + event + ":" + game);
        games.add(game);
    }

    @Given("a club member {string} did not attend the event {string} with game {string}")
    public void a_club_member_did_not_attend_event_with_game(String user, String event, String game) {
        loggedInUser = user;
        loggedInRole = "member";
        games.add(game);
    }

    @Given("a blocked club member {string} attended the event {string} with game {string}")
    public void a_blocked_club_member_attended_event_with_game(String user, String event, String game) {
        loggedInUser = user;
        loggedInRole = "blocked";
        attended.add(user + ":" + event + ":" + game);
        games.add(game);
    }

    @Given("an admin user {string} attended the event {string} with game {string}")
    public void an_admin_user_attended_event_with_game(String user, String event, String game) {
        loggedInUser = user;
        loggedInRole = "admin";
        attended.add(user + ":" + event + ":" + game);
        games.add(game);
    }

    @When("{string} rates the game {string} with {int} stars and comment {string}")
    public void rates_game_with_stars_and_comment(String user, String game, Integer stars, String comment) {
        attemptRating(user, game, stars, comment);
    }

    @When("{string} tries to rate the game {string} with {int} stars")
    public void tries_to_rate_game_with_stars(String user, String game, Integer stars) {
        attemptRating(user, game, stars, null);
    }

    @When("{string} leaves a comment {string} for the game {string}")
    public void leaves_comment_for_game(String user, String comment, String game) {
        attemptRating(user, game, null, comment);
    }

    private void attemptRating(String user, String game, Integer stars, String comment) {
        if (loggedInUser == null || !loggedInUser.equals(user)) {
            lastMessage = "You must be logged in.";
            ratingSuccess = false;
            return;
        }
        if (!games.contains(game)) {
            lastMessage = "Game does not exist.";
            ratingSuccess = false;
            return;
        }
        if (loggedInRole.equals("blocked")) {
            lastMessage = "User is blocked.";
            ratingSuccess = false;
            return;
        }
        if (loggedInRole.equals("admin")) {
            lastMessage = "Admins cannot rate games.";
            ratingSuccess = false;
            return;
        }
        boolean attendedEvent = attended.stream().anyMatch(s -> s.startsWith(user + ":") && s.endsWith(":" + game));
        if ((stars != null || (comment != null && !comment.isEmpty())) && !attendedEvent) {
            lastMessage = "You did not attend the event with this game.";
            ratingSuccess = false;
            return;
        }
        if (stars != null && (stars < 1 || stars > 5)) {
            lastMessage = "Rating must be between 1 and 5.";
            ratingSuccess = false;
            return;
        }
        if (stars != null) {
            ratings.computeIfAbsent(game, k -> new ArrayList<>()).add(stars);
        }
        if (comment != null && !comment.isEmpty()) {
            comments.computeIfAbsent(game, k -> new ArrayList<>()).add(comment);
        }
        lastMessage = "Rating saved successfully.";
        ratingSuccess = true;
    }

    @Then("the rating for {string} should be updated")
    public void the_rating_should_be_updated(String game) {
        assert ratingSuccess;
        assert ratings.containsKey(game);
    }

    @Then("the comment should be visible publicly")
    public void the_comment_should_be_visible_publicly() {
        assert ratingSuccess;
    }

    @Then("{string} should see a game rating error message")
    public void member_should_see_a_game_rating_error_message(String user) {
        assert !ratingSuccess;
        assert lastMessage != null && !lastMessage.contains("successfully");
    }

    @Then("the rating should not be updated")
    public void the_rating_should_not_be_updated() {
        assert !ratingSuccess;
    }
}

