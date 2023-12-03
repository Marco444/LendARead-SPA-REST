package ar.edu.itba.paw.webapp.auth.voters;

import ar.edu.itba.paw.interfaces.UserReviewsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LenderReviewVoter implements AccessDecisionVoter<FilterInvocation> {
    private final UserReviewsService userReviewsService;
    @Autowired
    public LenderReviewVoter(UserReviewsService userReviewsService) {
        this.userReviewsService = userReviewsService;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }


    @SneakyThrows
    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection<ConfigAttribute> attributes) {
        AtomicInteger vote = new AtomicInteger();
        vote.set(ACCESS_ABSTAIN);
        if(filterInvocation.getRequestUrl().toLowerCase().contains("/review/lender/")) {
            StringBuilder stringBuilder = new StringBuilder(filterInvocation.getRequestUrl());
            stringBuilder.delete(0, stringBuilder.lastIndexOf("/") + 1);
            try {
                if (userReviewsService.lenderCanReview(Integer.parseInt(stringBuilder.toString()))) {
                    vote.set(ACCESS_GRANTED);
                    return vote.get();
                }
                else {
                    vote.set(ACCESS_DENIED);
                    return vote.get();
                }
            } catch (Exception e) {
                vote.set(ACCESS_DENIED);
                return vote.get();
            }

        }

        return vote.get();
    }
}

