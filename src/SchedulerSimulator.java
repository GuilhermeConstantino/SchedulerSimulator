import java.util.Scanner;

public class SchedulerSimulator {
    public static void main(String[] args) {
        // primeiro argumento é a carga máxima de processos, segundo argumento é o
        // quantum
        int maxProcessLoad = 10;
        int quantum = 200;
        String userInput;
        UserInterface userInterface = UserInterface.getUserInterface();
        LongTermScheduler longTermScheduler = new LongTermScheduler();
        ShortTermScheduler shortTermScheduler = ShortTermScheduler.getShortTermScheduler();
        Scanner scanner = new Scanner(System.in);
        shortTermScheduler.setMaxProcessLoad(maxProcessLoad);
        while (true) {
            System.out.println("Menu de comandos (digite o número para executar)");
            System.out.println("1 - adicionar processo");
            userInput = scanner.nextLine();
            if (userInput == "1") {
                userInput = scanner.nextLine();
                Process process = new Process(userInput, new String[] { "execute", "execute" });
                shortTermScheduler.addProcess(process);
                userInterface.display("Nova fila de processos: " + shortTermScheduler.getProcessQueue());
            }
        }
    }
}