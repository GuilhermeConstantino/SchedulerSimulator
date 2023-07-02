
import java.util.ArrayList;

import java.util.List;

/**
 * Classe representante do escalonador de curto prazo.
 * 
 * @author Guilherme Constantino
 */
public class ShortTermScheduler extends Thread implements InterSchedulerInterface, ControllInterface {
    private List<Process> readyProcesses = new ArrayList<>(); // fila de processos onde a posição 0 representa um
                                                              // processo em execução
    private List<Process> blockedProcesses = new ArrayList<>(); // fila de processos que aguardando a
                                                                // conclusão de
    // seu tempo de block
    private UserInterface userInterface = UserInterface.getUserInterface(); // instância de interface de usuario
    private LongTermScheduler longTermScheduler; // instância do escalonador

    // de longo prazo
    String status = "uninitialized"; // sinalizador inicio, pausa, continuação ou encerramento
    private static ShortTermScheduler shortTermScheduler; // instância do escalonador de curto prazo
    private int timeSlice = -1; // fatia de tempo do escalonador, valores negativos indicam que ainda não foi
                                // inicializado
    private int totalCicles = 0; // contador de ciclos totais

    public int getTotalCicles() {
        return totalCicles;
    }

    private String selectedAlgorithm = "";
    boolean preempt = false; // variavel que sinaliza a preempção do processo
    Process executingProcess;

    private int maxProcessLoad = -1; // carga máxima de processos, valores negativos indicam que ainda não foi
                                     // estabelecida

    private ShortTermScheduler() {
    }

    /**
     * Método de instanciação seguindo o padrão de design singleton
     * 
     * @return Retorna instancia unica da classe ou caso nao haja cria uma nova
     * @author Guilherme Constantino
     */
    public static ShortTermScheduler getShortTermScheduler() { // método de instanciação de um singleton
        if (shortTermScheduler == null) {
            shortTermScheduler = new ShortTermScheduler();
        }
        return shortTermScheduler;

    }

    /**
     * Metodo que simula a execução do escalonador de curto prazo
     * 
     * @author Guilherme Constantino
     */
    public void run() {
        while (status != "shutdown") { // mantém o escalonador ativo até que seja desligado
            try {
                Thread.sleep(timeSlice); // simula o tempo gasto por cada processo para executar uma instrução
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

            while (status.equals("running")) {

                if (readyProcesses.isEmpty()) { // verifica se há processos a serem executados
                    if (blockedProcesses.isEmpty()) { // verifica se há processos bloqueados em espera
                        if (longTermScheduler.getProcessQueue().isEmpty()) { // verifica se há processos submetidos mas
                                                                             // ainda não admitidos
                            userInterface.display("Fila de processos vazia, encerrando simulacao.");
                            stopSimulation(); // encerra a simulação
                        } else {
                            continue; // reinicia o loop para aguardar os novos processos
                        }
                    } else {
                        tickBlockedProcesses(); // simula a contagem de tempo dos processos bloquados e reinicia o ciclo
                        try {
                            Thread.sleep(timeSlice); // simula o tempo gasto por cada processo para executar uma
                                                     // instrução
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        totalCicles++;

                        continue;
                    }
                } else {
                    totalCicles++;
                    executingProcess = readyProcesses.remove(0);
                    preempt = false;
                    while (!preempt) {
                        BehaviourStatement nextStatement = executingProcess.getBehaviourStatement(0);
                        tickBlockedProcesses();
                        try {
                            Thread.sleep(timeSlice); // simula execução da instrução
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }

                        if (nextStatement.getCommand().equals("block")) {

                            blockedProcesses.add(executingProcess);

                            userInterface.display("Block " + "["
                                    + nextStatement.getBlockPeriod() + "] " + executingProcess.getFileName());
                            executingProcess = null;
                            preempt = true;
                        } else if (nextStatement.getCommand().equals("execute")) {

                            userInterface.display("Execute " + executingProcess.getFileName());
                            executingProcess.removeNextBehaviourStatement();
                            if (selectedAlgorithm == "RR") { // caso o algoritmo seja RR, considera-se que o processo
                                                             // utilizou seu quantum e portanto o mesmo é preemptado
                                                             // para o fim da fila
                                readyProcesses.add(executingProcess);
                                executingProcess = null;
                                preempt = true;
                            } else if (selectedAlgorithm == "FIFO") {

                                continue;
                            }

                        } else { // caso o próximo comando não seja execute ou block, entende-se que o processo
                                 // chegou ao fim
                            executingProcess = null;
                            preempt = true;
                        }
                    }
                }

            }
        }

    }

    /**
     * Método que reduz em um o tempo de bloqueio dos processos bloqueados e
     * reinsere os processos com tempo de bloqueio
     * igual a 0 no escalonador
     * 
     * @author Guilherme Constantino
     */
    private void tickBlockedProcesses() {

        for (int i = blockedProcesses.size() - 1; i >= 0; i--) {
            Process process = blockedProcesses.get(i);
            BehaviourStatement statement = process.getBehaviourStatement(0);

            if (--statement.blockPeriod == 0) {
                userInterface.display("Termino de bloqueio de " + process.getFileName());
                process.removeNextBehaviourStatement();
                readyProcesses.add(process);
                blockedProcesses.remove(i);
            } else {
                userInterface.display(process.getFileName() + " tempo de bloqueio restante: " + statement.blockPeriod);
            }
        }
    }

    public void addProcess(Process process) { // método para envio de um novo processo

        userInterface.display("Novo processo " + process.getFileName() + " adicionado!");

        readyProcesses.add(process); // adiciona o processo ao fim da fila

    }

    @Override
    public void startSimulation() {
        if (maxProcessLoad < 1) {
            userInterface.display(
                    "Nao foi possivel iniciar a simulacao: carga máxima invalida");
            return;
        } else if (readyProcesses.isEmpty() && longTermScheduler.getProcessQueue().isEmpty()) {
            userInterface.display("Nao foi possivel iniciar a simulacao: fila de processo vazia");
            return;
        } else if (timeSlice < 1) {
            userInterface.display("Nao foi possivel iniciar a simulacao: fatia de tempo invalida");
            return;
        }
        userInterface.display("Simulacao iniciada");
        status = "running";
    }

    @Override
    public void suspendSimulation() {
        status = "suspended";
        userInterface.display(
                "Simulacao suspensa");
    }

    @Override
    public void resumeSimulation() {
        status = "running";
        userInterface.display(
                "Simulacao retomada");
    }

    @Override
    public void stopSimulation() {
        userInterface.display("Encerrando simulacao");
        readyProcesses.clear();
        status = "uninitialized";
    }

    @Override
    public void displayProcessesQueues() {
        String queueDescription = "";
        if (executingProcess != null) {
            queueDescription = " [Executando] " + executingProcess.getFileName() + "\n";
        }
        if (!readyProcesses.isEmpty()) {
            for (int i = 0; i < readyProcesses.size(); i++) {

                queueDescription = queueDescription + " [" + i + "] " + readyProcesses.get(i).getFileName() + "\n";

            }
        }

        queueDescription = queueDescription + "\nProcessos bloquados:\n";

        for (int i = 0; i < blockedProcesses.size(); i++) {

            queueDescription = queueDescription + " ["
                    + blockedProcesses.get(i).getBehaviourStatement(0).getBlockPeriod() + "] "
                    + blockedProcesses.get(i).getFileName() + "\n";

        }

        userInterface.displayShortQueue(queueDescription);
    }

    public void shutDown() {
        status = "shutdown";
    }

    // getters e setters
    public String getStatus() {
        return status;
    }

    public String getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public void setSelectedAlgorithm(String selectedAlgorithm) {
        this.selectedAlgorithm = selectedAlgorithm;
    }

    public List<Process> getReadyProcesses() {
        return readyProcesses;
    }

    public void setReadyProcesses(List<Process> processesQueue) {
        this.readyProcesses = processesQueue;
    }

    public List<Process> getBlockedProcesses() {
        return blockedProcesses;
    }

    public void setBlockedProcesses(List<Process> blockedProcesses) {
        this.blockedProcesses = blockedProcesses;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static void setShortTermScheduler(ShortTermScheduler shortTermScheduler) {
        ShortTermScheduler.shortTermScheduler = shortTermScheduler;
    }

    public int getTimeSlice() {
        return timeSlice;
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public int getProcessLoad() { // método para obter
        int load = readyProcesses.size() + blockedProcesses.size();
        if (executingProcess != null) // caso algum processo esteja em execução e portanto fora da fila ele é
                                      // acrescentado a contagem
            load++;
        return load;
    }

    public void setMaxProcessLoad(int maxLoad) { // método para estabelecer a carga máxima de processos
        maxProcessLoad = maxLoad;
    }

    public int getMaxProcessLoad() {
        return maxProcessLoad;
    }

    public void setTimeSlice(int time) {
        this.timeSlice = time;
    }

    public void setLongTermScheduler(LongTermScheduler longTermScheduler) {
        this.longTermScheduler = longTermScheduler;
    }

}
