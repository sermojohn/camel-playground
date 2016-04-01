package gr.iserm.java.camel.kai_tutorial;

public class TransformationBean {

	public String makeUpperCase(String body) {

		String transformedBody = body.toUpperCase();

		return transformedBody;

	}

}
