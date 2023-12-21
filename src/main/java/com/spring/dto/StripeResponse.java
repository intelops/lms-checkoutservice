package com.spring.dto;

public class StripeResponse {
    private String sessionId;
private String Message;

    public String getMessage() {
	return Message;
}

public void setMessage(String message) {
	Message = message;
}

	public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

  
    public StripeResponse(String sessionId, String message) {
		super();
		this.sessionId = sessionId;
		Message = message;
	}

	public StripeResponse() {
    }
}

