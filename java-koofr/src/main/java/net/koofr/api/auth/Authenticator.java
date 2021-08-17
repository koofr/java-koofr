package net.koofr.api.auth;

import java.io.IOException;

import net.koofr.api.http.Request;

public interface Authenticator {

  void arm(Request request) throws IOException;

}
