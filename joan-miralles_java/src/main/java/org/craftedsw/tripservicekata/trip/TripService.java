package org.craftedsw.tripservicekata.trip;

import java.util.ArrayList;
import java.util.List;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.craftedsw.tripservicekata.user.UserSession;

public class TripService {

    private UserSession userSession;

    private TripDAO tripDAO;

    public TripService(TripDAO tripDAO) {
        this.userSession = UserSession.getInstance();
        this.tripDAO = tripDAO;
    }

    protected void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public List<Trip> getTripsByUser(User user) throws UserNotLoggedInException {
        User loggedInUser = getLoggedUser();
        return user.friendOf(loggedInUser)
                ? tripDAO.findTripsBy(user)
                : new ArrayList<Trip>();
    }

    private User getLoggedUser() {
        User loggedUser = this.userSession.getLoggedUser();
        if (loggedUser == null) {
            throw new UserNotLoggedInException();
        }
        return loggedUser;
    }

}
