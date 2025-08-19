package ht.exception;

public class BizException extends Exception {

	/**
	 * BizException 에러 코드 및 메세지 처리
	 */
	private static final long serialVersionUID = -4143036775072016306L;

	/** Error code */
	private String errCode;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}


	public BizException() {
		super();
	}

	public BizException(String errCode, String message, Throwable cause) {
		super(message, cause);
		this.errCode = errCode;
	}

	public BizException(String message) {
		super(message);
	}

	public BizException(String errCode, String message) {
		super(message);
		this.errCode = errCode;
	}

	public BizException(Throwable cause) {
		super(cause);
	}

}
