
/**
 * Interface de submissao de processos e vizualizacao da fila de submissao
 */
public interface SubmissionInterface {
    public boolean submitJob(String job);

    public void displaySubmissionQueue();
}
