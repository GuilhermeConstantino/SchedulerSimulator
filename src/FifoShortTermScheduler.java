public class FifoShortTermScheduler extends ShortTermScheduler {

    public FifoShortTermScheduler(UserInterface userInterface, int timeSlice) {
        super(userInterface, timeSlice);

    }

    @Override
    public void run() {
        while (currentSimulationStatus != Status.FINISHED) { // mantém o escalonador ativo até que seja desligado
            try {
                Thread.sleep(timeSlice); // simula o tempo gasto por cada processo para executar uma instrução
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

            while (currentSimulationStatus != Status.RUNNING) {
                boolean preempt = false;
                if (readyProcessesQueue.isEmpty()) { // verifica se há processos a serem executados
                    if (blockedProcesses.isEmpty()) { // verifica se há processos bloqueados em espera
                        if (userInterface.getNewProcessesQueueSize() == 0) { // verifica se há processos submetidos
                                                                             // mas
                                                                             // ainda não admitidos
                            userInterface.displayNotification("Fila de processos vazia");
                            stopSimulation(); // encerra a simulação
                        } else {
                            continue; // reinicia o loop para aguardar os novos processos
                        }
                    } else { // este caso ocorre quando ha processos bloquados mas nao ha processos em
                             // execucao
                        tickBlockedProcesses(); // simula a contagem de tempo dos processos bloquados e reinicia o ciclo
                        try {
                            Thread.sleep(timeSlice); // simula o tempo gasto por cada processo para executar uma
                                                     // instrução
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        totalCicles++;

                        continue;
                    }
                } else {

                    executingProcess = readyProcessesQueue.remove(0);
                    preempt = false;
                    while (!preempt) {
                        BehaviourStatement nextStatement = executingProcess.getBehaviourStatement(0);
                        tickBlockedProcesses();
                        try {
                            Thread.sleep(timeSlice); // simula execução da instrução
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }

                        if (nextStatement.command.equals("block")) {

                            blockedProcesses.add(executingProcess);

                            userInterface.displayNotification("Block " + "["
                                    + nextStatement.blockPeriod + "] " + executingProcess.getFileName());
                            executingProcess = null;
                            totalCicles++;
                            executionCicles++;
                            preempt = true;
                        } else if (nextStatement.command.equals("execute")) {

                            userInterface.displayNotification("Execute " + executingProcess.getFileName());
                            totalCicles++;
                            executionCicles++;
                            executingProcess.removeNextBehaviourStatement();

                            continue;

                        } else { // caso o próximo comando não seja execute ou block, entende-se que o processo
                                 // chegou ao fim

                            userInterface.displayNotification(
                                    "Processo " + executingProcess.getFileName() + " terminado");

                            executingProcess.setCiclesOnTermination(totalCicles);
                            executingProcess.setTurnaround();
                            userInterface.addConcludedProcessData(executingProcess);
                            concludedProcesses.add(executingProcess);
                            executingProcess = null;
                            preempt = true;
                        }
                    }
                }

            }
        }

    }
}
