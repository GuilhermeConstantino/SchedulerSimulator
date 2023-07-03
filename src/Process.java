
import java.util.List;

/**
 * Classe que armazena os processos simulados, contém seus dados e os meios de
 * acessa-los.
 * 
 * @author Guilherme Constantion
 */
public class Process {
    private String fileName; // nome do arquivo do processo
    private List<BehaviourStatement> programBehaviour; // lista de instruções do processo
    private int processPriority; // prioridade do processo
    private int ciclesOnSubmition = 0;// quantidade de ciclos totais no momento em que o processo foi submetido
    private int ciclesOnTermination = 0;// quantidade de ciclos totais no momento em que o processo foi terminado
    private int turnaround;// numero de ciclos total que levou desde a submissao ate o termino do processo

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

    public long getTurnaround() {
        return turnaround;
    }

    public void setTurnaround() {
        this.turnaround = this.ciclesOnTermination - this.ciclesOnSubmition;
    }

}