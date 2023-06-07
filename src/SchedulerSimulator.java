
public class SchedulerSimulator {
    public static void main(String[] args) {
        // primeiro argumento é a carga máxima de processos, segundo argumento é o
        // quantum
        int maxProcessLoad = 10;
        int quantum = 200;
        UserInterface userInterface = new UserInterface();
        LongTermScheduler longTermScheduler = new LongTermScheduler();
        ShortTermScheduler shortTermScheduler = new ShortTermScheduler();

    }
}