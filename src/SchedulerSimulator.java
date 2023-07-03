
import javax.swing.JOptionPane;

/**
 * Metodo main para a inicializacao do programa e definicao de variaveis
 * 
 * @author Guilherme Constantino
 * @author Gabriela Pereira
 */
public class SchedulerSimulator {
    public static void main(String[] args) {

        int userStage = 0; // acompanha quantos dos passos para usar o simulador o usuario concluiu
        String userInput;
        int maxProcessLoad = -1;
        int timeSlice = -1;
        UserInterface userInterface = UserInterface.getUserInterface();
        ShortTermScheduler shortTermScheduler = ShortTermScheduler.getShortTermScheduler();
        LongTermScheduler longTermScheduler = LongTermScheduler.getLongTermScheduler();
        // estabelecer os ponteiros das instancias da forma abaixo ao inves de no
        // construtor das
        // classes serve para evitar um loop infinito onde uma tenta obter a instancia
        // da outra ao estabelecer suas variaveis

        userInterface.setShortTermScheduler(shortTermScheduler);
        userInterface.setLongTermScheduler(longTermScheduler);
        shortTermScheduler.setLongTermScheduler(longTermScheduler);
        StringBuilder message = new StringBuilder();

        while (userStage == 0) {
            userInput = JOptionPane.showInputDialog("Passo 1: Digite a carga maxima de processos",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            try {
                maxProcessLoad = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                message.append("Entrada invalida, a carga maxima deve ser um numero inteiro");
                JOptionPane.showMessageDialog(null, message, "ERRO", JOptionPane.ERROR_MESSAGE);

                continue;
            }
            if (maxProcessLoad > 0) {

                userStage = 1;

            } else {
                message.append("Entrada invalida, a carga maxima deve ser maior que 0");
                JOptionPane.showMessageDialog(null, message, "ERRO", JOptionPane.ERROR_MESSAGE);

            }

        }
        while (userStage == 1) {
            userInput = JOptionPane.showInputDialog("Passo 2: Digite o a fatia de tempo / quantum (ms)",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            try {
                timeSlice = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                message.append("Entrada invalida, o quantum deve ser um numero inteiro");
                JOptionPane.showMessageDialog(null, message, "ERRO", JOptionPane.ERROR_MESSAGE);

                continue;
            }
            if (timeSlice > 0) {
                userStage = 2;

            } else {
                message.append("Entrada invalida, fatia de tempo deve ser maior que 0");
                JOptionPane.showMessageDialog(null, message, "ERRO", JOptionPane.ERROR_MESSAGE);

            }

        }
        while (userStage == 2) {
            userInput = (String) JOptionPane.showInputDialog(null, "Passo 3: Escolha o algoritmo de escalonamento",
                    "Opcoes de algoritmo de escalonamento", JOptionPane.QUESTION_MESSAGE, null,
                    new Object[] { "FIFO", "Alternancia circular" }, "FIFO");
            if (userInput != null) {
                if (userInput.equals("FIFO")) {
                    shortTermScheduler.setSelectedAlgorithm("FIFO");
                    userStage = 3;
                } else if (userInput.equals("Alternancia circular")) {
                    shortTermScheduler.setSelectedAlgorithm("RR"); // não uso userInput pois as variaveis nesse código
                                                                   // são por padrão em inglês, devido a isso também é
                                                                   // necessário 2 'if'
                    userStage = 3;
                }
            } else {
                userInterface.shutdown();
            }
        }

        shortTermScheduler.setMaxProcessLoad(maxProcessLoad);
        shortTermScheduler.setTimeSlice(timeSlice);

        userInterface.start();

        longTermScheduler.start();
        shortTermScheduler.start();
    }
}