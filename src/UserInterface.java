
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
 * @author Guilherme Constantino
 */
public class UserInterface extends Thread implements NotificationInterface {
    private static UserInterface userInterface; // instância da classe para implementação do singleton

    private ShortTermScheduler shortTermScheduler; // instância do escalonador de longo prazo
    private LongTermScheduler longTermScheduler; // instância do escalonador de curto prazo

    String statistics = ""; // armazena o corpo de texto com as estatisticas

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
        String simulatedTime;
        String cpuSimulationTime;
        String cpuUsage;

        startUI();

        while (true) {
            try {
                Thread.sleep(timeSlice / 2); // espera para poupar loops excessivos
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

            totalCicles = shortTermScheduler.getTotalCicles();
            executeCicles = shortTermScheduler.getExecutionCicles();
            timeSlice = shortTermScheduler.getTimeSlice();
            totalConcludedProcesses = shortTermScheduler.getTotalConcludedProcesses();
            simulatedTime = String.format("%.3f",
                    (Double.parseDouble(Integer.toString(totalCicles * timeSlice)) / 1000));
            cpuSimulationTime = String.format("%.3f",
                    (Double.parseDouble(Integer.toString(executeCicles * timeSlice)) / 1000));
            cpuUsage = String.format("%.2f", Double.parseDouble(Integer.toString(executeCicles))
                    / Double.parseDouble(Integer.toString(totalCicles)) * 100);

            longTermScheduler.displaySubmissionQueue();
            shortTermScheduler.displayProcessesQueues();

            statistics = "Simulacao: " + shortTermScheduler.getTranslatedStatus();

            statistics = statistics + "\nTempo simulado decorrido: "
                    + simulatedTime
                    + " seg ("
                    + totalCicles + " ciclos)";
            statistics = statistics + "\nProcessos concluidos: " + totalConcludedProcesses;
            statistics = statistics + "\nVazao: " + totalConcludedProcesses;

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
                        + "-";
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
     */
    private void startUI() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // especifica que a aplicacao deve ser encerrada quando a
                                                              // janela for fechada
        GridBagConstraints constraints = new GridBagConstraints(); // coordenadas serão usadas para posicionar os
                                                                   // paineis

        constraints.fill = GridBagConstraints.BOTH; // faz com que o painel preencha toda area que ocupa para se
                                                    // posicionar melhor

        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createTitledBorder("Acoes"));

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchTxtFile();
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
     * Método para a leitura de conteúdo do arquivo
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

    public void setShortTermScheduler(ShortTermScheduler shortTermScheduler) {
        this.shortTermScheduler = shortTermScheduler;
    }

    public void setLongTermScheduler(LongTermScheduler longTermScheduler) {
        this.longTermScheduler = longTermScheduler;
    }
}
