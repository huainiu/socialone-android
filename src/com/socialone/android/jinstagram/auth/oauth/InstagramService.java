package com.socialone.android.jinstagram.auth.oauth;

import com.socialone.android.jinstagram.auth.InstagramApi;
import com.socialone.android.jinstagram.auth.exceptions.OAuthException;
import com.socialone.android.jinstagram.auth.model.OAuthConfig;
import com.socialone.android.jinstagram.auth.model.OAuthConstants;
import com.socialone.android.jinstagram.auth.model.OAuthRequest;
import com.socialone.android.jinstagram.auth.model.Token;
import com.socialone.android.jinstagram.auth.model.Verifier;
import com.socialone.android.jinstagram.http.Response;

import java.io.IOException;

public class InstagramService {
	private static final String VERSION = "1.0";

	private final InstagramApi api;

	private final OAuthConfig config;

	/**
	 * Default constructor
	 * 
	 * @param api OAuth2.0 api information
	 * @param config OAuth 2.0 configuration param object
	 */
	public InstagramService(InstagramApi api, OAuthConfig config) {
		this.api = api;
		this.config = config;
	}

	/**
	 * {@inheritDoc}
	 */
	public Token getAccessToken(Token requestToken, Verifier verifier) {
		OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(),
				api.getAccessTokenEndpoint());

		// Add the oauth parameter in the body
		request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
		request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
		request.addBodyParameter("grant_type", "authorization_code");
		request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
		request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());

		if (config.hasScope()) {
			request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
		}

		if (config.getDisplay() != null) {
			request.addBodyParameter(OAuthConstants.DISPLAY, config.getDisplay());
		}

        Response response;
        try {
            response = request.send();
        } catch (IOException e) {
            throw new OAuthException("Could not get access token", e);
        }

		return api.getAccessTokenExtractor().extract(response.getBody());
	}

	/**
	 * {@inheritDoc}
	 */
	public Token getRequestToken() {
		throw new UnsupportedOperationException(
				"Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
	}

	/**
	 * {@inheritDoc}
	 */
	public String getVersion() {
		return VERSION;
	}

	/**
	 * {@inheritDoc}
	 */
	public void signRequest(Token accessToken, OAuthRequest request) {
		request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN,
				accessToken.getToken());
	}

	/**
	 * {@inheritDoc}
	 */
	public String getAuthorizationUrl(Token requestToken) {
		return api.getAuthorizationUrl(config);
	}
}
