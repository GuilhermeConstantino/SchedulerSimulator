import java.util.Scanner;

public class UserInterface extends Thread implements NotificationInterface {
    String[] notifications;
    private static UserInterface userInterface;
    Scanner scanner = new Scanner(System.in);
    String userInput = "0";
    ShortTermScheduler shortTermScheduler = ShortTermScheduler.getShortTermScheduler();

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
        while (!userInput.equals("6")) {
            System.out.println("Menu de comandos (digite o numero para executar)"); // TODO: colocar condicionais para o
                                                                                    // que aparece ou não
            System.out.println("1 - adicionar processo");
            System.out.println("2 - iniciar simulacao");
            System.out.println("3 - suspender simulacao");
            System.out.println("4 - continuar simulacao");
            System.out.println("5 - encerrar simulacao");
            System.out.println("6 - fechar simulador");
            userInput = scanner.nextLine();
            if (userInput.equals("1")) {

                userInput = scanner.nextLine();
                Process process = new Process(userInput, new String[] { "execute", "execute" });
                shortTermScheduler.addProcess(process);
                display("Nova fila de processos: " + shortTermScheduler.getProcessQueue());
            }
        }
    }

}