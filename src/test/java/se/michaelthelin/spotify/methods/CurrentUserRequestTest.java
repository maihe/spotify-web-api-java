package se.michaelthelin.spotify.methods;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import org.junit.Test;
import se.michaelthelin.spotify.Api;
import se.michaelthelin.spotify.TestUtil;
import se.michaelthelin.spotify.models.Product;
import se.michaelthelin.spotify.models.User;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.*;
import static junit.framework.TestCase.fail;

public class CurrentUserRequestTest {

  @Test
  public void shouldGetCurrentUser_Aasync() throws Exception {
    final Api api = Api.DEFAULT_API;

    final CurrentUserRequest request = api.getCurrentUser()
            .accessToken("myLongAccessToken")
            .httpManager(TestUtil.MockedHttpManager.returningJson("current-user.json"))
            .build();

    final CountDownLatch asyncCompleted = new CountDownLatch(1);

    final SettableFuture<User> userFuture = request.getAsync();

    Futures.addCallback(userFuture, new FutureCallback<User>() {
      @Override
      public void onSuccess(User user) {
        assertNotNull(user);
        assertNull(user.getDisplayName());
        assertEquals("thelinmichael@gmail.com", user.getEmail());
        assertEquals("https://open.spotify.com/user/thelinmichael", user.getExternalUrls().get("spotify"));
        assertEquals("https://api.spotify.com/v1/users/thelinmichael", user.getHref());
        assertEquals("thelinmichael", user.getId());
        assertNull(user.getImages().get(0).getHeight());
        assertNull(user.getImages().get(0).getWidth());
        assertEquals("http://media.giphy.com/media/Aab07O5PYOmQ/giphy.gif", user.getImages().get(0).getUrl());
        assertEquals(Product.PREMIUM, user.getProduct());
        assertEquals("spotify:user:thelinmichael", user.getUri());

        asyncCompleted.countDown();
      }

      @Override
      public void onFailure(Throwable throwable) {
        fail("Failed to resolve future: " + throwable.getMessage());
      }
    });

    asyncCompleted.await(1, TimeUnit.SECONDS);
  }

  @Test
  public void shouldGetCurrentUser_sync() throws Exception {
    final Api api = Api.DEFAULT_API;

    final CurrentUserRequest request = api.getCurrentUser()
            .accessToken("myLongAccessToken")
            .httpManager(TestUtil.MockedHttpManager.returningJson("current-user.json"))
            .build();

    final User user = request.get();

    assertNotNull(user);
    assertNull(user.getDisplayName());
    assertEquals("thelinmichael@gmail.com", user.getEmail());
    assertEquals("https://open.spotify.com/user/thelinmichael", user.getExternalUrls().get("spotify"));
    assertEquals("https://api.spotify.com/v1/users/thelinmichael", user.getHref());
    assertEquals("thelinmichael", user.getId());
    assertNull(user.getImages().get(0).getHeight());
    assertNull(user.getImages().get(0).getWidth());
    assertEquals("http://media.giphy.com/media/Aab07O5PYOmQ/giphy.gif", user.getImages().get(0).getUrl());
    assertEquals(Product.PREMIUM, user.getProduct());
    assertEquals("spotify:user:thelinmichael", user.getUri());
  }
}