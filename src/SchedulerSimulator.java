import java.util.Scanner;

public class SchedulerSimulator {
    public static void main(String[] args) {
        // primeiro argumento é a carga máxima de processos, segundo argumento é o
        // quantum
        int maxProcessLoad = 10;
        int quantum = 200;
        UserInterface userInterface = UserInterface.getUserInterface();
        LongTermScheduler longTermScheduler = new LongTermScheduler();
        ShortTermScheduler shortTermScheduler = new ShortTermScheduler();
        Scanner scanner = new Scanner(System.in);
        // while (true) {
        // String name = scanner.nextLine();
        // System.out.println(name);
        // }
        Process process = new Process("akuakuexe", new String[] { "execute", "execute" });

    }
}