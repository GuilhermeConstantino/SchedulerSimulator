import java.util.Scanner;

public class SchedulerSimulator {
    public static void main(String[] args) {

        int maxProcessLoad = 10; // carga máxima de processos simultâneos
        int quantum = 200; // fatia de tempo dos processos
        int cpuBurstTime = 50; // tempo de cada ciclo de CPU
        String userInput;
        UserInterface userInterface = UserInterface.getUserInterface();
        LongTermScheduler longTermScheduler = new LongTermScheduler();
        ShortTermScheduler shortTermScheduler = ShortTermScheduler.getShortTermScheduler();
        Scanner scanner = new Scanner(System.in);
        shortTermScheduler.setMaxProcessLoad(maxProcessLoad);
        userInterface.run();

    }
}