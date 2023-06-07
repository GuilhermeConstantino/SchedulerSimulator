import java.util.ArrayList;

public class ShortTermScheduler extends Thread implements InterSchedulerInterface {
    Process[] processesQueue; // fila de processos onde a posição 0 representa um processo em execução
    String newEvent = "stable"; // sinalizador de novos eventos
    static UserInterface userInterface = UserInterface.getUserInterface(); // instância da interface de usuário
    private static ShortTermScheduler shortTermScheduler; // instância do escalonador de curto prazo
    private int maxProcessLoad;
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
        while (true) { // mantem a thread sempre em execução

        }
    }

    public void addProcess(Process process) { // método para envio de um novo processo
        userInterface.display("Novo processo " + process.getFileName() + " adicionado!");
        processesQueue[currentProcessLoad] = process; // adiciona o processo ao fim da fila
        currentProcessLoad++;
    }

    public int getProcessLoad() { // método para obter
        return processesQueue.length;
    }

    public String getProcessQueue() { // método para obter a fila de processos
        String queueDescription = " bop";
        System.out.println(processesQueue.length);
        for (int i = 0; i < processesQueue.length; i++) {
            if (processesQueue[i] != null) {
                queueDescription = queueDescription + " [posicao " + i + "] " + processesQueue[i].getFileName();
            } else {
                break;
            }

        }
        return queueDescription;
    }

    public void setMaxProcessLoad(int maxLoad) { // método para estabelecer a carga máxima de processos
        processesQueue = new Process[maxLoad];
    }

}
