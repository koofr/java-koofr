package net.koofr.api.v2;

import org.restlet.Request;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.engine.header.ChallengeWriter;
import org.restlet.engine.header.Header;
import org.restlet.engine.security.AuthenticatorHelper;
import org.restlet.util.Series;

public class KoofrAuthenticator extends AuthenticatorHelper {
	private String token;
	
	public static final ChallengeScheme KOOFR_CHALLENGE_SCHEME =
			new ChallengeScheme("Koofr Token Challenge Scheme", "Token");
	
	public KoofrAuthenticator() {
		super(KOOFR_CHALLENGE_SCHEME, true, false);
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public void formatResponse(ChallengeWriter cw,
			ChallengeResponse challenge, Request request,
			Series<Header> httpHeaders) {
		if(null != token) {
			cw.append(token);
		}
	}		

}
