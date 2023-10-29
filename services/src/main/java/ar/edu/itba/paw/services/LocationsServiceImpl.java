package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.LocationNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.LocationsService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.userContext.implementations.Behaviour;
import ar.edu.itba.paw.models.userContext.implementations.LocationImpl;
import ar.edu.itba.paw.models.userContext.implementations.UserImpl;
import ar.itba.edu.paw.persistenceinterfaces.LocationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationsServiceImpl implements LocationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserReviewsServiceImpl.class);

    private final LocationDao locationsDao;
    private final UserService userService;

   @Autowired
    public LocationsServiceImpl(LocationDao locationsDao, UserService userService) {
        this.locationsDao = locationsDao;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void addLocation(LocationImpl lc) {
         locationsDao.addLocation(lc);
    }

    @Override
    @Transactional
    public void addLocation(int id, String name, String locality, String province, String country, String zipcode, UserImpl user) throws UserNotFoundException {
       if (user.getBehavior().equals(Behaviour.BORROWER)) {
           userService.changeRole(user.getEmail(), Behaviour.LENDER);
           LOGGER.info("User {} changed role to Lender", user.getId());
       }
        if(id == -1) {
            LocationImpl newLocation = new LocationImpl(name, zipcode, locality, province, country, user);
            addLocation(newLocation);
            LOGGER.info("Location {} added for user {}", newLocation.getId(), user.getId());
        } else {
            LocationImpl newLocation = new LocationImpl(id, name, zipcode, locality, province, country, user);
            editLocation(newLocation);
            LOGGER.info("Location {} modified for user {}", newLocation.getId(), user.getId());
        }
    }

    @Override
    @Transactional
    public LocationImpl getLocation(int locationId) throws LocationNotFoundException {
        LocationImpl location= locationsDao.getLocation(locationId);
        if (location == null) {
            throw new LocationNotFoundException("Location not found");
        }
        return location;
    }

    @Override
    @Transactional
    public List<LocationImpl> getLocations(UserImpl user) {
        return locationsDao.getLocations(user);
    }

    @Override
    @Transactional
    public void editLocation(LocationImpl lc) {
         locationsDao.editLocation(lc);
    }

    @Override
    @Transactional
    public void deleteLocation(LocationImpl lc) {
            locationsDao.deleteLocation(lc);
            LOGGER.info("Location {} deleted for user {}", lc.getId(), lc.getUser().getId());
    }

    @Override
    @Transactional
    public List<LocationImpl> getLocationsById(int userId) throws UserNotFoundException {
        return locationsDao.getLocations(userService.getUserById(userId));
    }

    @Override
    @Transactional
    public LocationImpl editLocationById(int locationId) {
        LocationImpl location = locationsDao.editLocation(locationsDao.getLocation(locationId));
        LOGGER.info("Location {} modified for user {}", location.getId(), location.getUser().getId());
        return  location;
    }

    @Override
    @Transactional
    public void deleteLocationById(int locationId) throws UserNotFoundException, LocationNotFoundException {
        if(locationId != -1) {
            deleteLocation(getLocation(locationId));
            LOGGER.info("Location {} deleted", locationId);
        }
    }
}
