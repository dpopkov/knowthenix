package io.dpopkov.knowthenix.services;

public interface LoginAttemptService {

    /** Removes the entry of the specified user from the cache. */
    void evictUser(String username);

    /** Adds user's attempt and increments number of login attempts. */
    void addUserAttempt(String username);

    /** Checks whether the user exceeded the allowed number of attempts to login. */
    boolean exceededAttemptsLimit(String username);
}
