
import java.util.ArrayList;
import java.util.List;

/**
 * Classe representante do escalonador de curto prazo.
 * 
 * @author Guilherme Constantino
 */
public abstract class ShortTermScheduler extends Thread implements InterSchedulerInterface, ControllInterface {
    public enum Status {
        UNINITIALIZED, RUNNING, BLOCKED, SUSPENDED, FINISHED
    }

    protected Status currentSimulationStatus; // representa o estado atual da simulação
    protected List<Process> readyProcessesQueue; // fila de processos prontos
    protected List<Process> blockedProcesses; // fila de processos bloqueados
    protected List<Process> concludedProcesses; // processos concluidos
    protected UserInterface userInterface; // instância de interface de usuario

    protected int timeSlice; // fatia de tempo do escalonador, valores negativos indicam que ainda nao foi

    public int getTimeSlice() {
        return timeSlice;
    }

    // inicializado
    protected int totalCicles; // contador de ciclos totais
    protected int executionCicles; // contador de operacoes de execute
    protected Process executingProcess; // variavel que armazena o processo em execucao

    public ShortTermScheduler(UserInterface userInterface, int timeSlice) {
        this.userInterface = userInterface;
        this.timeSlice = timeSlice;

        currentSimulationStatus = Status.UNINITIALIZED;
        totalCicles = 0;
        executionCicles = 0;
        readyProcessesQueue = new ArrayList<>();
        blockedProcesses = new ArrayList<>();
        concludedProcesses = new ArrayList<>();
    }

    /**
     * Metodo que simula a execução do escalonador de curto prazo
     * 
     * @author Guilherme Constantino
     */
    public abstract void run();

    /**
     * Metodo que reduz em um o tempo de bloqueio dos processos bloqueados e
     * reinsere os processos com tempo de bloqueio
     * igual a 0 no escalonador
     * 
     * @author Guilherme Constantino
     */

    protected void tickBlockedProcesses() {

        for (int i = blockedProcesses.size() - 1; i >= 0; i--) {
            Process process = blockedProcesses.get(i);
            BehaviourStatement statement = process.getBehaviourStatement(0);

            if (--statement.blockPeriod == 0) {
                userInterface.displayNotification("Termino de bloqueio de " + process.getFileName());
                process.removeNextBehaviourStatement();
                readyProcessesQueue.add(process);
                blockedProcesses.remove(i);
            } else {
                userInterface.displayNotification(
                        process.getFileName() + " tempo de bloqueio restante: " + statement.blockPeriod);
            }
        }
    }

    public void addProcess(Process process) { // método para envio de um novo processo

        userInterface.displayNotification("Novo processo " + process.getFileName() + " adicionado!");

        readyProcessesQueue.add(process); // adiciona o processo ao fim da fila

    }

    /**
     * Inicializa a simulacao
     * 
     * @author Guilherme Constantino
     */
    @Override
    public void startSimulation() {
        if (currentSimulationStatus == Status.UNINITIALIZED) {
            userInterface.displayNotification("Simulacao iniciada");
            currentSimulationStatus = Status.RUNNING;
        } else {
            userInterface.displayNotification("Erro: simulação já foi inicializada");
        }
    }

    /**
     * Suspense a simulacao
     * 
     * @author Guilherme Constantino
     */
    @Override
    public synchronized void suspendSimulation() {
        if (currentSimulationStatus == Status.RUNNING) {
            currentSimulationStatus = Status.SUSPENDED;
            userInterface.displayNotification("Simulação suspensa");
            try {
                wait(); // Wait until notified to continue
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            userInterface.displayNotification("Erro: simulação não está em execução");
        }
    }

    /**
     * Continua a simulacao
     * 
     * @author Guilherme Constantino
     */
    @Override
    public void resumeSimulation() {
        if (currentSimulationStatus == Status.BLOCKED) {
            currentSimulationStatus = Status.RUNNING;
            notify();
            userInterface.displayNotification(
                    "Simulacao retomada");
        } else {
            userInterface.displayNotification("Erro: simulacao nao se encontra suspensa");
        }
    }

    /**
     * Encerra a simulacao
     * 
     * @author Guilherme Constantino
     */
    @Override
    public void stopSimulation() {
        if (currentSimulationStatus == Status.RUNNING || currentSimulationStatus == Status.BLOCKED) {
            currentSimulationStatus = Status.FINISHED;
            userInterface.displayNotification("Encerrando simulacao");
            readyProcessesQueue.clear();
        } else {
            userInterface.displayNotification("Erro: simulacao nao esta suspensa ou em execucao");
        }
    }

    /**
     * Exibe o processo em execucao e as filas de processos prontos e bloquados em
     * uma String
     * 
     * @author Guilherme Constantino
     */
    @Override
    public void displayProcessesQueues() {
        String queueDescription = "";
        if (executingProcess != null) {
            queueDescription = "Processo em execução: \n" + executingProcess.getFileName() + "\n";
        }
        queueDescription = queueDescription + "Fila de prontos: \n";
        if (!readyProcessesQueue.isEmpty()) {
            for (int i = 0; i < readyProcessesQueue.size(); i++) {

                queueDescription = queueDescription + " [" + i + "] " + readyProcessesQueue.get(i).getFileName() + "\n";

            }
        }

        queueDescription = queueDescription + "\nFila de bloquados:\n";

        for (int i = 0; i < blockedProcesses.size(); i++) {

            queueDescription = queueDescription + " ["
                    + blockedProcesses.get(i).getBehaviourStatement(0).command + "] "
                    + blockedProcesses.get(i).getFileName() + "\n";

        }

        userInterface.displayShortQueue(queueDescription);
    }

    public int getTotalCicles() {
        return totalCicles;
    }

    public String getCurrentSimulationStatus() {
        if (currentSimulationStatus == Status.UNINITIALIZED) {
            return "uninitialized";
        } else if (currentSimulationStatus == Status.RUNNING) {
            return "running";
        } else if (currentSimulationStatus == Status.SUSPENDED) {
            return "suspended";
        } else {
            return "finished";
        }
    }

    public int getProcessLoad() { // método para obter
        int load = readyProcessesQueue.size() + blockedProcesses.size();
        if (executingProcess != null) // caso algum processo esteja em execução e portanto fora da fila ele é
                                      // acrescentado a contagem
            load++;
        return load;
    }

    public void setTimeSlice(int time) {
        this.timeSlice = time;
    }

    public int getTotalConcludedProcesses() {
        return concludedProcesses.size();
    }

    public int getExecutionCicles() {
        return executionCicles;
    }

}
