package views;

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


public class indexTest {

 
    @Test
    public void render_correctForm_NullMap() {
    	Content html = views.html.main.render(null, null);
        assertThat("text/html", is(equalTo(html.contentType())));
        assertThat(html.body(), stringContainsInOrder(Arrays.asList("Welcome to Freelancelot")));
    }
}