package net.koofr.api.v2.transfer;

public interface ProgressListener {
	void transferred(long bytes);

	void setTotal(long bytes);
	
	boolean isCanceled();
}
