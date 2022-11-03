package snr1s.osuscores;

public class HttpResponse {
	public final int code;
	public final String msg;

	public HttpResponse(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
