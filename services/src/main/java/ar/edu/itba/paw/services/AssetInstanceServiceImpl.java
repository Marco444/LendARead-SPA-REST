package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.AssetInstanceNotFoundException;
import ar.edu.itba.paw.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.exceptions.LocationNotFoundException;
import ar.edu.itba.paw.interfaces.AssetInstanceService;
import ar.edu.itba.paw.interfaces.LocationsService;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.AssetInstance;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.PhysicalCondition;
import ar.edu.itba.paw.models.assetLendingContext.implementations.AssetState;
import ar.edu.itba.paw.models.viewsContext.implementations.PagingImpl;
import ar.edu.itba.paw.models.viewsContext.implementations.SearchQueryImpl;
import ar.edu.itba.paw.models.viewsContext.interfaces.AbstractPage;
import ar.edu.itba.paw.models.viewsContext.interfaces.SearchQuery;
import ar.edu.itba.paw.utils.HttpStatusCodes;
import ar.itba.edu.paw.persistenceinterfaces.AssetInstanceDao;
import ar.itba.edu.paw.persistenceinterfaces.ImagesDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssetInstanceServiceImpl implements AssetInstanceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LanguagesServiceImpl.class);


    private final AssetInstanceDao assetInstanceDao;


    private final ImagesDao imagesDao;

    private final LocationsService locationsService;


    @Autowired
    public AssetInstanceServiceImpl( final AssetInstanceDao assetInstanceDao, final ImagesDao imagesDao,final LocationsService locationsService) {
        this.assetInstanceDao = assetInstanceDao;
        this.imagesDao = imagesDao;
        this.locationsService = locationsService;
    }

    @Transactional(readOnly = true)
    @Override
    public AssetInstance getAssetInstance(final int id) throws AssetInstanceNotFoundException {
        Optional<AssetInstance> assetInstanceOpt = this.assetInstanceDao.getAssetInstance(id);
        if (!assetInstanceOpt.isPresent()) {
            LOGGER.error("Failed to find the asset instance");
            throw new AssetInstanceNotFoundException(HttpStatusCodes.NOT_FOUND);
        }
        return assetInstanceOpt.get();
    }


    @Transactional(readOnly = true)
    @Override
    public AbstractPage<AssetInstance> getAllAssetsInstances(final int pageNum, final int itemsPerPage, SearchQuery searchQuery) {

        if (pageNum < 0 || itemsPerPage <= 0)
            return new PagingImpl<>(new ArrayList<>(), 1, 1);

        if (searchQuery == null)
            searchQuery = new SearchQueryImpl(new ArrayList<>(), new ArrayList<>(), "", 1, 5,-1);


        Optional<AbstractPage<AssetInstance>> optionalPage = assetInstanceDao.getAllAssetInstances(pageNum, itemsPerPage, searchQuery);

        AbstractPage<AssetInstance> page;

        if (optionalPage.isPresent()) {
            page = optionalPage.get();
            return page;
        }
        return new PagingImpl<AssetInstance>(new ArrayList<>(), 1, 1);
    }

    @Transactional
    @Override
    public void removeAssetInstance(final int id) throws AssetInstanceNotFoundException {
        AssetInstance assetInstance = getAssetInstance(id);
        if (assetInstance.getAssetState() == AssetState.DELETED)
            throw new AssetInstanceNotFoundException(HttpStatusCodes.NOT_FOUND);
        assetInstanceDao.changeStatus(assetInstance,AssetState.DELETED);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isOwner(final AssetInstance assetInstance, final String email) {
        return assetInstance.getOwner().getEmail().equals(email);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isOwner(final int id, final String email) throws AssetInstanceNotFoundException {
        AssetInstance assetInstance = getAssetInstance(id);
        return assetInstance.getOwner().getEmail().equals(email);
    }
    @Transactional
    @Override
    public void changeAssetInstance(final int id, final Optional<PhysicalCondition> physicalCondition, final Optional<Integer> maxLendingDays, final Optional<Integer> location,final byte[] image,final Optional<String> description,final Optional<Boolean> isReservable,final Optional<String> state) throws AssetInstanceNotFoundException, LocationNotFoundException, ImageNotFoundException {
        AssetInstance assetInstance = getAssetInstance(id);
        if (location.isPresent())
            assetInstance.setLocation(locationsService.getLocation(location.get()));
        if (image != null)
            assetInstance.setImage(imagesDao.addPhoto(image));
        description.ifPresent(assetInstance::setDescription);
        physicalCondition.ifPresent(assetInstance::setPhysicalCondition);
        maxLendingDays.ifPresent(assetInstance::setMaxLendingDays);
        isReservable.ifPresent(assetInstance::setIsReservable);
        state.ifPresent(s -> assetInstance.setAssetState(AssetState.fromString(s)));

    }

}
