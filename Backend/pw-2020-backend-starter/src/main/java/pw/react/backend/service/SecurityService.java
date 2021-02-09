package pw.react.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
class SecurityService implements SecurityProvider {

    private static final String SECURITY_HEADER = "security-header";
    private final Logger logger = LoggerFactory.getLogger(FlatMainService.class);

    private final UserService userService;

    public SecurityService(UserService userService)
    {
        this.userService = userService;
    }

    @Override
    public boolean isAuthenticated(HttpHeaders headers) {
        if (headers == null) {
            return false;
        }

        return headers.containsKey(SECURITY_HEADER) && userService.containsToken(headers.getFirst(SECURITY_HEADER).toString());
    }

    @Override
    public boolean isAuthorized(HttpHeaders headers) {
        return isAuthenticated(headers);
    }

    @Override
    public String generateNewUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
