package cn.dunn.im.model;

import java.io.Serializable;

//@XmlRootElement
public class LoginResponseMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private String reason;
	private String msg;
	private Boolean success;
	private User user;

	public LoginResponseMessage() {
	}

	public LoginResponseMessage(Boolean success) {
		this.success = success;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	// @XmlElement
	public String getMsg() {
		return msg;
	}

	// @XmlElement
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	// @XmlElement
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// @XmlElement
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
