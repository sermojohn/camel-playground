package gr.iserm.java.camel.so;

public class Main {

	public static void main(String[] args) throws Exception {
		org.apache.camel.main.Main main = new org.apache.camel.main.Main();
		main.addRouteBuilder(new SoAnswerRouteBuilder());
		main.run();
	}

}
