
/**
 * Interface entre os escalonadores para consultar a carga atual do escalondador
 * de curto prazo e adicionar processos
 */
public interface InterSchedulerInterface {

    public void addProcess(Process process);

    public int getProcessLoad();
}
