package net.koofr.api.auth;

import net.koofr.api.http.Request;

import java.io.IOException;

public interface Authenticator {

  void arm(Request request) throws IOException;

}
