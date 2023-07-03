
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Interface gráfica de usuário permite interação com o programa, e visualização
 * das mecânicas internas do programa
 * 
 * @author Gabriela Pereira
 * @author Guilherme Constantino
 */
public class UserInterface extends Thread implements NotificationInterface {
    private static UserInterface userInterface; // instância da classe para implementação do singleton

    private ShortTermScheduler shortTermScheduler; // instância do escalonador de longo prazo
    private LongTermScheduler longTermScheduler; // instância do escalonador de curto prazo

    String statistics = ""; // armazena o corpo de texto com as estatisticas
    private String concludedProcessesData = ""; // dados de processos concluidos
    private int totalConcludedReturnCicles = 0; // quantidade de ciclos gasta por todos os processo concluidos

    // as variaveis de elementos da interface são instanciadas aqui ao invés de
    // dentro do startUI
    // para que se possa modificar
    // elas durante a execução do código
    private JFrame frame = new JFrame("Simulador de escalonamento"); // janela da interface
    private JPanel mainPanel = new JPanel(new GridBagLayout()); // painel onde serão inseridos os elementos da interface
    private JPanel sidebarPanel = new JPanel(); // painel que contém os botões
    private JTextArea notificationsDisplay = new JTextArea(15, 30); // area para exibir as notificacões
    private JTextArea longQueueDisplay = new JTextArea(15, 30); // area para exibir a fila de longo prazo
    private JTextArea shortQueueDisplay = new JTextArea(15, 30); // area para exibir a fila de curto prazo
    private JTextArea statisticsDisplay = new JTextArea(15, 30); // area para exibir as estatisticas
    private JButton addButton = new JButton("Adicionar processo"); // botão para adicionar processos
    private JButton startButton = new JButton("Iniciar simulacao"); // botão para iniciar a simulação
    private JButton suspendButton = new JButton("Suspender simulacao"); // botão para suspender a simulação
    private JButton continueButton = new JButton("Continuar simulacao"); // botão para continuar a simulação
    private JButton stopButton = new JButton("Encerrar simulacao"); // botão para encerrar a simulação
    private JScrollPane notificationsPanel = new JScrollPane(notificationsDisplay); // painel de rolagem das
                                                                                    // notificações
    private JScrollPane shortQueuePanel = new JScrollPane(shortQueueDisplay); // painel de rolagem da fila de curto
                                                                              // prazo
    private JScrollPane longQueuePanel = new JScrollPane(longQueueDisplay); // painel de rolagem da fila de longo prazo
    private JScrollPane statisticsPanel = new JScrollPane(statisticsDisplay); // painel de rolagem da exibição de
                                                                              // estatísticas

    private UserInterface() {
    }

    /**
     * Metodo para a obtencao da instancia unica de UserInterface
     * 
     * @return UserInterface
     */
    public static UserInterface getUserInterface() {
        if (userInterface == null) {
            userInterface = new UserInterface();
        }
        return userInterface;
    }

    public void run() {
        int totalCicles;
        int timeSlice = shortTermScheduler.getTimeSlice();
        int executeCicles;
        int totalConcludedProcesses;
        int totalSubmittedProcesses;
        String simulatedTime;
        String cpuSimulationTime;
        String cpuUsage;
        String throughPutPerCicle;
        String throughPutPerTime;
        boolean end = false;

        startUI();

        while (!end) {
            try {
                Thread.sleep(timeSlice / 2); // espera para poupar loops excessivos
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

            totalCicles = shortTermScheduler.getTotalCicles(); // ciclos totais decorridos
            executeCicles = shortTermScheduler.getExecutionCicles(); // ciclos de execucao totais decorridos
            timeSlice = shortTermScheduler.getTimeSlice(); // fatia de tempo determinada pelo usuario
            totalConcludedProcesses = shortTermScheduler.getTotalConcludedProcesses(); // quantidade de processos
                                                                                       // concluidos
            totalSubmittedProcesses = longTermScheduler.getTotalSubmittedProcesses(); // quantidade de processos
                                                                                      // submetidos
            simulatedTime = String.format("%.3f",
                    (Double.parseDouble(Integer.toString(totalCicles * timeSlice)) / 1000)); // tempo total simulado
            cpuSimulationTime = String.format("%.3f",
                    (Double.parseDouble(Integer.toString(executeCicles * timeSlice)) / 1000)); // tempo simulado de uso
                                                                                               // da CPU
            cpuUsage = String.format("%.2f", Double.parseDouble(Integer.toString(executeCicles))
                    / Double.parseDouble(Integer.toString(totalCicles)) * 100); // tempo simulado de uso da CPU em
                                                                                // ciclos
            throughPutPerCicle = String.format("%.2f",
                    totalConcludedProcesses / (Double.parseDouble(Integer.toString(totalCicles)))); // throughtput atual
                                                                                                    // total
            if (simulatedTime != "0,000") {
                throughPutPerTime = String.format("%.2f",
                        Double.parseDouble(Integer.toString(totalConcludedProcesses))
                                / (Double.parseDouble(Integer.toString(totalCicles * timeSlice)) / 1000));
            } else {
                throughPutPerTime = "0,00";
            }
            if (throughPutPerCicle.equals("NaN")) {
                throughPutPerCicle = "0,00";
            }
            if (throughPutPerTime.equals("NaN")) {
                throughPutPerTime = "0,00";
            }

            longTermScheduler.displaySubmissionQueue(); // requisicao de exibicao da fila de submissao
            shortTermScheduler.displayProcessesQueues(); // requisicao de exibicao do escalonador de curto prazo
            // abaixo construcao e exibicao das estatisticas calculadas acima
            statistics = "Simulacao: " + shortTermScheduler.getTranslatedStatus();

            statistics = statistics + "\nTempo simulado decorrido: "
                    + simulatedTime
                    + " seg ("
                    + totalCicles + " ciclos)";
            statistics = statistics + "\nProcessos concluidos: " + totalConcludedProcesses + " de "
                    + totalSubmittedProcesses;

            statistics = statistics + "\nUso da CPU simulado: "
                    + cpuSimulationTime
                    + " seg ("
                    + executeCicles + " ciclos)";
            if (totalCicles != 0) {

                statistics = statistics + "\nAproveitamento de CPU: "
                        + cpuUsage
                        + "%";
            } else {
                statistics = statistics + "\nAproveitamento de CPU: "
                        + "0,00";
            }
            statistics = statistics + "\nVazao: "
                    + throughPutPerTime
                    + " processos/seg ("
                    + throughPutPerCicle + " processos/ciclo)";
            // ao termino da simulacao, exibicao de dados relativos ao tempo de retorno dos
            // processos
            if (shortTermScheduler.status.equals("finished")) {
                Double ciclesPerProcess = Double.parseDouble(Integer.toString(totalConcludedReturnCicles))
                        / Double.parseDouble(Integer.toString(totalConcludedProcesses));
                String avarageTime = String.format("%.2f", ciclesPerProcess * timeSlice / 1000);
                String avarageCicles = String.format("%.2f", ciclesPerProcess);
                if (avarageTime.equals("NaN")) {
                    avarageTime = "0,00";
                }
                if (avarageCicles.equals("NaN")) {
                    avarageCicles = "0,00";
                }
                statistics += "\nTempo de retorno medio: " + avarageTime
                        + " segundos por processo\n("
                        + avarageCicles + " ciclos por processo)";
                statistics += "\n------------------";
                statistics += concludedProcessesData;
                end = true;
            }
            displayStatistics(statistics);

        }
    }

    @Override
    public void displayNotification(String text) {

        notificationsDisplay.append(text + "\n");
        notificationsDisplay.setCaretPosition(notificationsDisplay.getDocument().getLength());

    }

    public void displayLongQueue(String text) {

        longQueueDisplay.setText(text);
        longQueueDisplay.setCaretPosition(longQueueDisplay.getDocument().getLength());

    }

    public void displayShortQueue(String text) {

        shortQueueDisplay.setText(text);
        shortQueueDisplay.setCaretPosition(shortQueueDisplay.getDocument().getLength());

    }

    public void displayStatistics(String text) {

        statisticsDisplay.setText(text);
        statisticsDisplay.setCaretPosition(statisticsDisplay.getDocument().getLength());

    }

    public void shutdown() {
        shortTermScheduler.shutDown();
        longTermScheduler.shutDown();
        frame.dispose();

    }

    /**
     * Configura os elementos que compoem a interface de usuario
     * 
     * @author Gabriela Pereira
     * @author Guilherme Constantino
     */
    private void startUI() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // especifica que a aplicacao deve ser encerrada quando a
                                                              // janela for fechada
        GridBagConstraints constraints = new GridBagConstraints(); // coordenadas serão usadas para posicionar os
                                                                   // paineis

        constraints.fill = GridBagConstraints.BOTH; // faz com que o painel preencha toda area que ocupa para se
                                                    // posicionar melhor

        constraints.weightx = 1.0;
        constraints.weighty = 1.0;

        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createTitledBorder("Acoes"));

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (shortTermScheduler.getStatus() != "finished") {
                    searchTxtFile();
                } else {
                    displayNotification("Erro: simulacao encerrada");
                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shortTermScheduler.startSimulation();
            }
        });

        suspendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shortTermScheduler.suspendSimulation();
            }
        });

        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shortTermScheduler.resumeSimulation();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shortTermScheduler.stopSimulation();
            }
        });

        addButton.setPreferredSize(new Dimension(60, 20));
        startButton.setPreferredSize(new Dimension(60, 20));
        suspendButton.setPreferredSize(new Dimension(60, 20));
        continueButton.setPreferredSize(new Dimension(60, 20));
        stopButton.setPreferredSize(new Dimension(60, 20));

        mainPanel.setPreferredSize(new Dimension(1280, 920)); // Define o tamanho do painel
        sidebarPanel.setPreferredSize(new Dimension(10, 10));

        sidebarPanel.add(addButton);
        sidebarPanel.add(startButton);
        sidebarPanel.add(suspendButton);
        sidebarPanel.add(continueButton);
        sidebarPanel.add(stopButton);

        notificationsDisplay.setEditable(false);

        notificationsPanel.setBorder(BorderFactory.createTitledBorder("Notificacoes"));

        longQueueDisplay.setEditable(false);

        longQueuePanel.setBorder(BorderFactory.createTitledBorder("Escalonador de longo prazo"));

        shortQueueDisplay.setEditable(false);

        shortQueuePanel.setBorder(BorderFactory.createTitledBorder("Escalonador de curto prazo"));

        statisticsDisplay.setEditable(false);

        statisticsPanel.setBorder(BorderFactory.createTitledBorder("Estatisticas"));

        // Configura posicoes de cada painel
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainPanel.add(sidebarPanel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        mainPanel.add(notificationsPanel, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        mainPanel.add(longQueuePanel, constraints);

        constraints.gridx = 2;
        constraints.gridy = 1;
        mainPanel.add(shortQueuePanel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        mainPanel.add(statisticsPanel, constraints);

        frame.add(mainPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Método para a seleção de arquivos de texto através do explorador nativo do
     * sistema.
     * Após selecionados, os arquivos são lidos e enviados para o escalonador de
     * longo prazo.
     * 
     * @author Guilherme Constantino
     */
    private void searchTxtFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            File[] selectedFiles = fileChooser.getSelectedFiles();

            for (int i = 0; i < selectedFiles.length; i++) {
                File file = selectedFiles[i];
                try {
                    String fileContent = fileReader(file);

                    longTermScheduler.submitJob(fileContent);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    /**
     * Metodo para a leitura de conteúdo do arquivo
     * 
     * @param file arquivo de texto a ser lido
     * @return retorna uma String com o conteúdo do arquivo
     * @author Guilherme Constantino
     */
    private static String fileReader(File file) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    /**
     * Extrai o tempo de retorno dos processos e os armazena prontos para exibicao
     * 
     * @param concludedProcess processo do qual os dados serão extraidos
     */
    public void addConcludedProcessData(Process concludedProcess) {
        int turnAround = concludedProcess.getTurnaround();
        int timeSlice = shortTermScheduler.getTimeSlice();
        double turnAroundTime = Double.parseDouble(Integer.toString(turnAround * timeSlice)) / 1000;
        concludedProcessesData += "\n" + "- Tempo de retorno (" + concludedProcess.getFileName() + "): "
                + String.format("%.2f", turnAroundTime) + " (" + turnAround + " ciclos)";
        totalConcludedReturnCicles += concludedProcess.getTurnaround();
    }

    public void setShortTermScheduler(ShortTermScheduler shortTermScheduler) {
        this.shortTermScheduler = shortTermScheduler;
    }

    public void setLongTermScheduler(LongTermScheduler longTermScheduler) {
        this.longTermScheduler = longTermScheduler;
    }
}
