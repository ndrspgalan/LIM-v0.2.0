
import application.GameApplication;
import bootstrap.DemoBootstrap;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        GameApplication application = DemoBootstrap.createApplication(System.in, System.out);
        application.run();
    }
}
