package ar.itba.edu.paw.persistenceinterfaces;

import ar.edu.itba.paw.models.userContext.implementations.UserImpl;
import ar.edu.itba.paw.models.userContext.implementations.UserReview;
import ar.edu.itba.paw.models.viewsContext.implementations.PagingImpl;

import java.util.Optional;

public interface UserReviewsDao {

    void addReview(final UserReview newReview);

    double getRating(final UserImpl user);

    double getRatingAsLender(UserImpl user);

    double getRatingAsBorrower(UserImpl user);

    Optional<UserReview> getUserReviewsByLendingIdAndUser(final int lendingId, final String user);

    PagingImpl<UserReview> getUserReviewsAsBorrower(int pageNum, int itemsPerPage, final UserImpl recipient);

    PagingImpl<UserReview> getUserReviewsAsLender(int pageNum, int itemsPerPage, final UserImpl reviewer);

    Optional<UserReview> getUserReviewAsLender(final int userId, final int reviewId) ;

    Optional<UserReview> getUserReviewAsBorrower(final int userId, final int reviewId) ;

}
