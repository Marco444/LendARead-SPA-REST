package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.userContext.implementations.Behaviour;
import ar.edu.itba.paw.models.userContext.implementations.PasswordResetTokenImpl;
import ar.edu.itba.paw.models.userContext.implementations.UserImpl;
import ar.itba.edu.paw.persistenceinterfaces.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;

    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(LanguagesServiceImpl.class);

    private static final String BORROWER_ROLE = "ROLE_BORROWER";

    @Autowired
    public UserServiceImpl(final PasswordEncoder passwordEncoder, final UserDao userDao, final EmailService emailService, final AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
    }


    @Transactional(readOnly = true)
    @Override
    public UserImpl getUser(final String email) throws UserNotFoundException {
        Optional<UserImpl> user = userDao.getUser(email);
        if (!user.isPresent()) {
            LOGGER.error("Failed to get user {}", email);
            throw new UserNotFoundException("The user was not found");
        }
        return user.get();
    }

    @Override
    public UserImpl getUserById(int id) throws UserNotFoundException {
        Optional<UserImpl> user = userDao.getUser(id);
        if (!user.isPresent()) {
            LOGGER.error("User with id {} not found", id);
            throw new UserNotFoundException("The user with id " + id + " not found");
        }

        return user.get();
    }

    @Transactional
    @Override
    public UserImpl createUser(final String email, String name, final String telephone, final String password) {

        return userDao.addUser(Behaviour.BORROWER, email, name, telephone, passwordEncoder.encode(password));
    }

    @Transactional
    @Override
    public void changeRole(final String email, final Behaviour behaviour) throws UserNotFoundException {
        boolean changed = userDao.changeRole(email, behaviour);
        if (!changed)
            throw new UserNotFoundException("The user was not founded");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HashSet<GrantedAuthority> actualAuthorities = new HashSet<>();
        actualAuthorities.add(new SimpleGrantedAuthority("ROLE_" + behaviour.toString()));
        Authentication newAuth = new
                UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), actualAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean getCurrentUserIsBorrower() {
        return getCurrentRoles().contains(new SimpleGrantedAuthority(BORROWER_ROLE));
    }

    @Override
    public String getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails.getUsername();
        }
        return "";
    }

    @Override
    public Collection<? extends GrantedAuthority> getCurrentRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    @Transactional
    @Override
    public void createChangePasswordToken(final String email) {
        String token = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Optional<UserImpl> user = userDao.getUser(email);
        if (!user.isPresent()) {
            LOGGER.error("User not found");
            return;
        }
        PasswordResetTokenImpl passwordResetToken = new PasswordResetTokenImpl(token, user.get().getId(), LocalDate.now().plusDays(1));
        emailService.sendForgotPasswordEmail(email, passwordResetToken.getToken(), LocaleContextHolder.getLocale());
        userDao.setForgotPasswordToken(passwordResetToken);
    }

    @Transactional
    @Override
    public void changePassword(final String token, final String password) {
        Optional<PasswordResetTokenImpl> passwordResetToken = userDao.getPasswordRestToken(token);
        if (!passwordResetToken.isPresent())
            return;
        if (!isTokenValid(token))
            return;
        userDao.deletePasswordRestToken(token);
        userDao.changePassword(passwordResetToken.get(), passwordEncoder.encode(password));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isTokenValid(final String token) {
        Optional<PasswordResetTokenImpl> passwordResetToken = userDao.getPasswordRestToken(token);
        return passwordResetToken.map(resetToken -> resetToken.getExpiryDate().isAfter(LocalDate.now())).orElse(false);
    }

    @Override
    public void logInUser(final String email, final String password) {
        Optional<UserImpl> user = userDao.getUser(email);
        if (!user.isPresent()) {
            LOGGER.error("User not found");
            return;
        }
        final Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.get().getBehavior().toString()));
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public boolean isCurrent(final int userId) {
        Optional<UserImpl> user = userDao.getUser(userId);
        if (!user.isPresent()) {
            LOGGER.error("User not found");
            return false;
        }

        String current = getCurrentUser();
        LOGGER.debug("Current User: {}", current);
        return current.equals(user.get().getEmail());
    }
}
