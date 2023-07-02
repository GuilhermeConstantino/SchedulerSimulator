
import java.util.List;

/**
 * Classe que armazena os processos simulados, contém seus dados e os meios de
 * acessá-los.
 * 
 * @author Guilherme Constantion
 */
public class Process {
    private String fileName; // nome do arquivo do processo
    private List<BehaviourStatement> programBehaviour; // lista de instruções do processo
    private int processPriority; // prioridade do processo
    private long processSubmitionTime;
    private long processTerminationTime;

    /**
     * Método construtor do processo
     * 
     * @author Guilherme Constantino
     * @param fileName         Nome do arquivo
     * @param programBehaviour Lista de instruções de execução do processo
     * @param processPriority  Prioridade do processo
     */
    public Process(String fileName, List<BehaviourStatement> programBehaviour, int processPriority) {
        this.fileName = fileName;
        this.programBehaviour = programBehaviour;
        this.processPriority = processPriority;
    }

    /**
     * Remove e retorna a próxima instrução do processo
     *
     * @author Guilherme Constantino
     * @return Retorna a instrução que foi removida.
     */
    public BehaviourStatement removeNextBehaviourStatement() {
        if (programBehaviour.isEmpty()) {
            return new BehaviourStatement("end");
        } else {
            BehaviourStatement extracted = programBehaviour.get(0);
            programBehaviour.remove(0);
            return extracted;
        }
    }

    // Abaixo getters e setters
    public long getProcessSubmitionTime() {
        return processSubmitionTime;
    }

    public void setProcessSubmitionTime(long processSubmitTime) {
        this.processSubmitionTime = processSubmitTime;
    }

    public long getProcessTerminationTime() {
        return processTerminationTime;
    }

    public void setProcessTerminationTime(long processTerminationTime) {
        this.processTerminationTime = processTerminationTime;
    }

    public int getCiclesOnSubmition() {
        return ciclesOnSubmition;
    }

    public void setCiclesOnSubmition(int ciclesOnSubmition) {
        this.ciclesOnSubmition = ciclesOnSubmition;
    }

    public int getCiclesOnTermination() {
        return ciclesOnTermination;
    }

    public void setCiclesOnTermination(int ciclesOnTermination) {
        this.ciclesOnTermination = ciclesOnTermination;
    }

    private int ciclesOnSubmition;
    private int ciclesOnTermination;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<BehaviourStatement> getProgramBehaviour() {
        return programBehaviour;
    }

    public void setProgramBehaviour(List<BehaviourStatement> programBehaviour) {
        this.programBehaviour = programBehaviour;
    }

    public int getProcessPriority() {
        return processPriority;
    }

    public void setProcessPriority(int processPriority) {
        this.processPriority = processPriority;
    }

    public String getFileName() {
        return fileName;
    }

    public BehaviourStatement getBehaviourStatement(int i) {
        if (programBehaviour.isEmpty()) {
            return new BehaviourStatement("end");
        } else {
            return programBehaviour.get(i);
        }
    }

}