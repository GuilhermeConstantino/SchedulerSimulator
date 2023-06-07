public class ShortTermScheduler extends Thread {
    Process[] processesQueue; // fila de processos onde a posição 0 representa um processo em execução
    String newEvent = "stable"; // sinalizador de novos eventos
    UserInterface userInterface = UserInterface.getUserInterface(); // instância de userInterface

    public ShortTermScheduler() {

    }

    public void run() {
        while (true) { // mantem a thread sempre em execução
            if (newEvent == "addProcess") {
                System.out.println("Novo processo adicionado!");
                newEvent = "stable";
            }

        }
    }

    public void addProcess(Process process) { // método para envio de um novo processo
        newEvent = "addProcess";
    }

    public int getProcessLoad() {
        return processesQueue.length;
    }
}
