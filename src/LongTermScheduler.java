
import java.util.ArrayList;
import java.util.List;

/**
 * Classe simula o escalonador de longo prazo
 * 
 * @author Raphael Morraye
 * @author Guilherme Constantino
 */
public class LongTermScheduler extends Thread implements SubmissionInterface {

    private List<Process> newProcessesQueue;
    private UserInterface userInterface;
    private ShortTermScheduler shortTermScheduler;
    private int totalSubmittedProcesses;
    private int maxProcessLoad;

    /**
     * Construtor privado para forcar o uso do getLongTermScheduler
     * 
     * @author Raphael Morraye
     * @author Guilherme Constantino
     */
    public LongTermScheduler(UserInterface userInterface, ShortTermScheduler shortTermScheduler, int maxProcessLoad) {
        this.userInterface = userInterface;
        this.shortTermScheduler = shortTermScheduler;
        this.maxProcessLoad = maxProcessLoad;
        newProcessesQueue = new ArrayList<>();
        totalSubmittedProcesses = 0;
    }

    /**
     * Execucao do escalonador de longo prazo
     * 
     * @author Raphael Morraye
     * @author Guilherme Constantino
     */
    public void run() {

        while (true) {
            try {
                Thread.sleep(shortTermScheduler.getTimeSlice() / 2);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            if (shortTermScheduler.getCurrentSimulationStatus() == "running") {
                if (shortTermScheduler.getProcessLoad() < maxProcessLoad
                        && !newProcessesQueue.isEmpty()) {
                    Process sendProcess = newProcessesQueue.remove(0);
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
        int submitionCicles = shortTermScheduler.getTotalCicles();
        newProcess.setCiclesOnSubmition(submitionCicles);

        newProcessesQueue.add(newProcess);
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

        for (int i = 0; i < newProcessesQueue.size(); i++) {

            queueDescription = queueDescription + "[" + i + "] " + newProcessesQueue.get(i).getFileName() + "\n";

        }

        userInterface.displayLongQueue(queueDescription);
        System.out.println(queueDescription);
    }

    public int getTotalSubmittedProcesses() {
        return totalSubmittedProcesses;
    }

    public int getNewProcessesQueueSize() {
        return newProcessesQueue.size();
    }

}
