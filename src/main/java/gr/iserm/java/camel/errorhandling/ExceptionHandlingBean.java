package gr.iserm.java.camel.errorhandling;

public interface ExceptionHandlingBean {
	void handleException(Object body, Exception e);
}
