package net.koofr.api.v2.util;

public interface Log {
	public void debug(String tag, String msg);
	public void debug(String tag, String msg, Throwable t);
}
