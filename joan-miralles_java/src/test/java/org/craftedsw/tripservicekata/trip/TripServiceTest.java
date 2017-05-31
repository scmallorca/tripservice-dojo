package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.craftedsw.tripservicekata.user.UserSession;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class TripServiceTest {

    private TripService service;

    @Mock
    UserSession userSession;

    @Mock
    TripDAO tripDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new TripService(tripDAO);
        when(tripDAO.findTripsBy(any(User.class))).thenReturn(Arrays.asList(new Trip(), new Trip()));
        service.setUserSession(userSession);
    }

    @Test(expected = UserNotLoggedInException.class)
    public void given_user_not_logged_in_then_throws_exception() {
        when(userSession.getLoggedUser()).thenReturn(null);
        service.getTripsByUser(new User());
    }

    @Test
    public void given_logged_in_user_when_is_not_a_friend_then_return_empty_trip_list() {
        User loggedInButNotFriendUser = new User();
        when(userSession.getLoggedUser()).thenReturn(loggedInButNotFriendUser);
        assertThat(
                service.getTripsByUser(createUserWithFriends(new User())),
                is(Collections.<Trip>emptyList()));
    }

    @Test
    public void given_logged_in_user_when_is_a_friend_then_return_not_empty_trip_list() {
        User loggedInAndFriendUser = new User();
        when(userSession.getLoggedUser()).thenReturn(loggedInAndFriendUser);
        assertThat(
                service.getTripsByUser(createUserWithFriends(loggedInAndFriendUser)),
                is(not(Collections.<Trip>emptyList())));
    }

    private User createUserWithFriends(User... friends) {
        User userWithFriends = new User();
        for (User friend : friends) {
            userWithFriends.addFriend(friend);
        }
        return userWithFriends;
    }

}
