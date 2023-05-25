package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.exceptions.AssetInstanceNotFoundException;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.AssetInstanceReview;
import ar.edu.itba.paw.models.assetExistanceContext.interfaces.AssetInstance;

import java.util.List;

public interface AssetInstanceReviewsService {

    void addReview(final AssetInstanceReview assetInstanceReview);

    double getRating(final AssetInstance assetInstance);

    double getRatingById(final int assetInstanceId) throws AssetInstanceNotFoundException;

    List<AssetInstanceReview> getAssetInstanceReviews(final AssetInstance assetInstance);

    List<AssetInstanceReview> getAssetInstanceReviewsById(final int assetInstanceId) throws AssetInstanceNotFoundException;
}
