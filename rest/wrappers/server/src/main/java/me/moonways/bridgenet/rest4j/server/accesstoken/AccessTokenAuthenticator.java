package me.moonways.bridgenet.rest4j.server.accesstoken;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.rest.model.authentication.*;

@RequiredArgsConstructor
public final class AccessTokenAuthenticator implements HttpAuthenticator {
    private final Bridgenet4jAccessTokenService accessTokenService;

    @Override
    public ApprovalResult authenticate(UnapprovedRequest request) {
        if (request.getAuthentication() != Authentication.BEARER) {
            return ApprovalResult.forbidden();
        }

        HttpCredentials credentials = request.getRequestCredentials();
        Token token = credentials.getToken();

        if (token != null && token.getValue() != null && accessTokenService.hasAccessToken(token.getValue())) {
            return ApprovalResult.approve();
        }

        return ApprovalResult.forbidden();
    }
}
