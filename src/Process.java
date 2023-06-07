// classe que interpreta os arquivos contendo programas simulados
public class Process {
    private String fileName; // nome do arquivo do processo
    private String[] programBehaviour; // lista de instruções do processo
    private int processPriority; // prioridade do processo
    // método construtor do processo com prioridade

    public Process(String fileName, String[] programBehaviour, int processPriority) {
        this.fileName = fileName;
        this.programBehaviour = programBehaviour;
        this.processPriority = processPriority;
    }

    // método construtor do processo com prioridade
    public Process(String fileName, String[] programBehaviour) {
        this.fileName = fileName;
        this.programBehaviour = programBehaviour;
        this.processPriority = processPriority;
    }

}
