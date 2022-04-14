package actors;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;


public class SearchActorProtocolTest {

    @Test
    public void testProtocol() {
        SearchActorProtocol protocol = new SearchActorProtocol();
        assertThat(protocol, is(notNullValue()));
    }


    @Test
    public void testRefresh() {
        SearchActorProtocol.Refresh refresh = new SearchActorProtocol.Refresh();
        assertThat(refresh, is(notNullValue()));
    }

    @Test
    public void testSearch() {
        SearchActorProtocol.Search search = new SearchActorProtocol.Search();
        search.setSearchKey("test");
        assertThat(search.getSearchKey(), is(equalTo("test")));
    }
}
