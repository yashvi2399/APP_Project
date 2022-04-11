package views;

import models.Tweet;
import models.UserProfile;
import org.junit.Test;
import play.Application;
import play.data.Form;
import play.data.FormFactory;
import play.test.Helpers;
import play.twirl.api.Content;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;

/**
 * Implements JUnit test cases for index page.
 *
 * @author Nikita Baranov
 * @version 1.0.0
 */
public class indexTest {

    /**
     * FakeApplication invoke play application for testing purpose.
     */
    Application application = Helpers.fakeApplication();

    /**
     * Form Factory object
     */
    @Inject
    private FormFactory formFactory;

    /**
     * Correct view rendering with empty Form and without User Profile and Tweets.
     */
    @Test
    public void render_correctForm_NullMap() {
        formFactory = application.injector().instanceOf(FormFactory.class);

        Form<String> searchForm = formFactory.form(String.class);

        Content html = views.html.index.render(searchForm, null);
        assertThat("text/html", is(equalTo(html.contentType())));
        stringContainsInOrder(Arrays.asList("<form", "</form>"));
    }

    /**
     * Correct view rendering with User Profile and Tweets.
     */
    @Test
    public void render_correctForm_correctMap() {
        formFactory = application.injector().instanceOf(FormFactory.class);

        Form<String> searchForm = formFactory.form(String.class);
        UserProfile up = new UserProfile();
        up.setName("testName");
        up.setScreen_name("testSname");
        up.setCreated_at("createdAt");
        up.setDescription("testDesc");
        up.setFavourites_count(123123);
        up.setFollowers_count(123123);
        up.setFriends_count(123);
        up.setTime_zone("zone");
        up.setStatuses_count("sc");

        Tweet tweet1 = new Tweet();
        tweet1.setFull_text("tweet1");
        tweet1.setCreated_at("tw1createdAt");
        tweet1.setUser(up);
        Map<String, List<Tweet>> searchResult = new HashMap<>();
        searchResult.put("keyword1", Arrays.asList(tweet1));

        Content html = views.html.index.render(searchForm, searchResult);
        assertThat("text/html", is(equalTo(html.contentType())));
        stringContainsInOrder(Arrays.asList("testSname", "tweet1"));
    }

    /**
     * Correct view rendering without Form User Profile and Tweets
     */
    @Test
    public void render_nullForm_nullMap() {
        Content html = views.html.index.render(null, null);
        assertThat("text/html", is(equalTo(html.contentType())));
        stringContainsInOrder(Arrays.asList("<form", "</form>"));
    }
}