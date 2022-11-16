package io.dpopkov.knowthenix.services.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.domain.repositories.AuthUserRepository;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.TemporaryProfileImagesService;
import io.dpopkov.knowthenix.services.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static io.dpopkov.knowthenix.services.impl.AuthUserServiceImplConstants.NO_USER_FOUND_BY_USERNAME;

@Slf4j
@Component
public class TemporaryProfileImagesServiceImpl implements TemporaryProfileImagesService {

    public static final String AVATARS_API_BASE_URL = "https://ui-avatars.com/api/";

    private LoadingCache<String, byte[]> imagesCache;
    private final AuthUserRepository authUserRepository;

    public TemporaryProfileImagesServiceImpl(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
        initializeCache();
    }

    private void initializeCache() {
        imagesCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(100)
                .build(new CacheLoader<>() {
                    @SuppressWarnings("NullableProblems")
                    @Override
                    public byte[] load(String key) {
                        return new byte[0];
                    }
                });
    }

    @Override
    public byte[] getImage(String username) {
        log.trace("Requested temporary image for {}", username);
        try {
            byte[] image = imagesCache.get(username);
            if (image.length == 0) {
                AuthUserEntity authUser = this.authUserRepository.findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
                String url = buildUrlToUiAvatars(authUser.getFirstName(), authUser.getLastName());
                image = load(url);
                imagesCache.put(username, image);
                log.trace("Loaded and cached temporary image for {}", username);
            }
            return image;
        } catch (ExecutionException | IOException e) {
            throw new AppServiceException("Error getting cached temporary image", e);
        }
    }

    private byte[] load(String u) throws IOException {
        URL url = new URL(u);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream in = url.openStream()) {
            int numRead;
            byte[] buffer = new byte[1024];
            while ((numRead = in.read(buffer)) != -1) {
                outputStream.write(buffer, 0, numRead);
            }
            return outputStream.toByteArray();
        }
    }

    private String buildUrlToUiAvatars(String firstName, String lastName) {
        return AVATARS_API_BASE_URL
                + "?name=" + firstName + "+" + lastName
                + "&size=40"
                + "&background=random";
    }
}
