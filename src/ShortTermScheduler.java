
import java.util.ArrayList;
import java.util.List;

public class ShortTermScheduler extends Thread implements InterSchedulerInterface {
    private List<Process> processesQueue = new ArrayList<>(); // fila de processos onde a posição 0 representa um
                                                              // processo em execução
    String newEvent = "stable"; // sinalizador de novos eventos
    static UserInterface userInterface = UserInterface.getUserInterface(); // instância da interface de usuário
    private static ShortTermScheduler shortTermScheduler; // instância do escalonador de curto prazo
    private int maxProcessLoad = 10;
    private int currentProcessLoad = 0;

    private ShortTermScheduler() {

    }

    public static ShortTermScheduler getShortTermScheduler() { // método de instanciação de um singleton
        if (shortTermScheduler == null) {
            return new ShortTermScheduler();
        } else {
            return shortTermScheduler;
        }
    }

    public void run() {

    }

    public void addProcess(Process process) { // método para envio de um novo processo
        // if (processesQueue.size() == maxProcessLoad) {
        // throw new Error("ShortScheduler overflow");
        // }
        userInterface.display("Novo processo " + process.getFileName() + " adicionado!");
        processesQueue.add(process); // adiciona o processo ao fim da fila
        currentProcessLoad++;
    }

    public int getProcessLoad() { // método para obter
        return processesQueue.size();
    }

    public String getProcessQueue() { // método para obter a fila de processos
        String queueDescription = "Fila [" + processesQueue.size() + "]:";

        for (int i = 0; i < processesQueue.size(); i++) {
            if (processesQueue.get(i) != null) {
                queueDescription = queueDescription + " [" + i + "] " + processesQueue.get(i).getFileName();
            } else {
                break;
            }

        }
        return queueDescription;
    }

    public void setMaxProcessLoad(int maxLoad) { // método para estabelecer a carga máxima de processos
        maxProcessLoad = maxLoad;
    }

}
