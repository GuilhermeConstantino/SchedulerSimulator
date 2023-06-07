public class UserInterface extends Thread implements NotificationInterface {
    String[] notifications;
    private static UserInterface userInterface;

    private UserInterface() {

    }

    /// padrão de design singleton garante que haja apenas uma instância desta
    /// classe
    public static UserInterface getUserInterface() {
        if (userInterface == null) {
            return new UserInterface();
        } else {
            return userInterface;
        }
    }

    @Override
    public void display(String text) {
        System.out.println(text);

    }

    public void run() {
        while (true) {

        }
    }

}