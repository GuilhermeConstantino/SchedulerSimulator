
public class SchedulerSimulator {
    public static void main(String[] args) {
        // primeiro argumento é a carga máxima de processos, segundo argumento é o
        // quantum
        Helpers helpers = new Helpers();

        System.out.println(helpers.isNumeric(args[0]));
    }
}