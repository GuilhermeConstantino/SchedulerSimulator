
/**
 * Classo que simula uma instrucao do programa simulado.
 * 
 * 
 * @author Guilherme Constantino
 */
public class BehaviourStatement {
    String command;
    int blockPeriod;

    /**
     * Este construtor apenas aceita block como comando, pois um execute nao tem
     * periodo de bloqueio.
     * 
     * @author Guilherme Constantino
     * @param command     Comando de bloqueio
     * @param blockPeriod Período de bloqueio do comando
     */
    public BehaviourStatement(String command, int blockPeriod) {
        if (!command.equals("block")) {
            throw new IllegalArgumentException("Comando invalido: apenas comandos de 'block' para este construtor");
        } else if (blockPeriod < 1 || blockPeriod > 5) {
            throw new IllegalArgumentException("Comando invalido: periodo de bloqueio invalido");
        }
        this.command = command;
        this.blockPeriod = blockPeriod;
    }

    /**
     * Este construtor apenas aceita execute como comando, pois um block necessita
     * de um periodo de bloqueio
     * 
     * @author Guilherme Constantino
     * @param command Comando de execute
     */
    public BehaviourStatement(String command) {
        if (!command.equals("execute") && !command.equals("end")) {
            throw new IllegalArgumentException("Comando invalido: apenas comandos de 'execute' para este construtor");
        }
        this.command = command;
    }
}
