package gr.iserm.java.camel.errorhandling;

public class ExceptionHandlingBeanImpl implements ExceptionHandlingBean {
	@Override
	public void handleException(Object body, Exception e) {
		System.out.printf("Handled exception [%s] for message body [%s]", body, e.getMessage());
	}
}
