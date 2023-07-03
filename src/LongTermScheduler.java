
import java.util.ArrayList;
import java.util.List;

/**
 * Classe simula o escalonador de longo prazo
 * 
 * @author Raphael Morraye
 * @author Guilherme Constantino
 */
public class LongTermScheduler extends Thread implements SubmissionInterface {

    private List<Process> processQueue = new ArrayList<>();
    private UserInterface userInterface = UserInterface.getUserInterface();
    private ShortTermScheduler shortTermScheduler = ShortTermScheduler.getShortTermScheduler();
    private static LongTermScheduler longTermScheduler;
    private String status = "dormant";
    private int totalSubmittedProcesses = 0;

    /**
     * Construtor privado para forcar o uso do getLongTermScheduler
     * 
     * @author Raphael
     * 
     */
    private LongTermScheduler() {
    }

    /**
     * Execucao do escalonador de longo prazo
     * 
     * @author Raphael Morraye
     * @author Guilherme Constantino
     */
    public void run() {

        while (status != "shutdown") {
            try {
                Thread.sleep(shortTermScheduler.getTimeSlice() / 2);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            if (shortTermScheduler.status.equals("running")) {
                if (shortTermScheduler.getProcessLoad() < shortTermScheduler.getMaxProcessLoad()
                        && !processQueue.isEmpty()) {
                    Process sendProcess = processQueue.remove(0);
                    shortTermScheduler.addProcess(sendProcess);
                    userInterface.displayNotification("Processo " + sendProcess.getFileName() + " aceito");
                }
            }
        }
    }

    /**
     * Transforma uma String em process e a envia para a fila do escalonador de
     * longo prazo
     * 
     * @author Raphael Morraye
     * @author Guilherme Constantino
     */
    @Override
    public boolean submitJob(String job) {
        String fileName;
        int priority;
        List<BehaviourStatement> instructions = new ArrayList<>();

        String[] parts = job.split("\n");

        String[] firstLine = parts[0].split(" ");

        priority = Integer.parseInt(firstLine[2].substring(0, 1)); // usando substring pois outras funcoes não retornam
                                                                   // string
        fileName = firstLine[1];
        for (int i = 2; i < parts.length - 1; i++) {

            BehaviourStatement behaviour;

            if (parts[i].substring(0, 5).equals("execu")) {

                behaviour = new BehaviourStatement("execute");
            } else {

                behaviour = new BehaviourStatement("block",
                        Integer.parseInt(parts[i].substring(6, 7)));
            }

            instructions.add(behaviour); // getting instructions (begin ... end)
        }

        Process newProcess = new Process(fileName, instructions, priority);
        // for (int i = 0; i < instructions.size(); i++) {
        // userInterface.display(instructions.get(i).getCommand());
        // }
        long submitionTime = System.currentTimeMillis();
        newProcess.setProcessSubmitionTime(submitionTime);
        processQueue.add(newProcess);
        totalSubmittedProcesses++;
        return true;
    }

    /**
     * Exibe a fila de submissao
     * 
     * @author Raphael Morraye
     */
    @Override
    public void displaySubmissionQueue() {

        String queueDescription = "";

        for (int i = 0; i < processQueue.size(); i++) {

            queueDescription = queueDescription + "[" + i + "] " + processQueue.get(i).getFileName() + "\n";

        }

        userInterface.displayLongQueue(queueDescription);
    }

    /**
     * Sinaliza o encerramento do simulador
     * 
     * @author Guilherme Constantino
     */
    public void shutDown() {
        status = "shutdown";
    }

    // Getters e setters

    public List<Process> getProcessQueue() {
        return processQueue;
    }

    public void setProcessQueue(List<Process> processQueue) {
        this.processQueue = processQueue;
    }

    public UserInterface getUserInterface() {
        return userInterface;
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public ShortTermScheduler getShortTermScheduler() {
        return shortTermScheduler;
    }

    public void setShortTermScheduler(ShortTermScheduler shortTermScheduler) {
        this.shortTermScheduler = shortTermScheduler;
    }

    public static void setLongTermScheduler(LongTermScheduler longTermScheduler) {
        LongTermScheduler.longTermScheduler = longTermScheduler;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static LongTermScheduler getLongTermScheduler() {
        if (longTermScheduler == null) {
            longTermScheduler = new LongTermScheduler();
        }
        return longTermScheduler;

    }

    public int getTotalSubmittedProcesses() {
        return totalSubmittedProcesses;
    }

}
