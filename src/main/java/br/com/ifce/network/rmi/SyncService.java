package br.com.ifce.network.rmi;

import br.com.ifce.mediator.AgendaMediator;
import br.com.ifce.model.Agenda;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class SyncService {

    private static final int TIMER_INTERVAL = 2000;

    private SyncService() {
    }

    private static AgendaService lookupService(Agenda agenda) throws MalformedURLException, NotBoundException, RemoteException {
        return (AgendaService) Naming.lookup(agenda + "/" + AgendaService.name());
    }

    public static boolean checkConnection(final Agenda agenda) {
        try {
            final var service = lookupService(agenda);
            return "OK".equals(service.checkConnection());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static AgendaService getRemoteService() throws RemoteException {
        try {
            final var agenda = AgendaMediator.getInstance().getAgenda();
            if (!checkConnection(agenda)) throw new RemoteException("Can't connect to agenda");
            return lookupService(agenda);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Can't connect to agenda");
        }
    }

    public static Agenda getAvailableAgenda() {
        final var agendas = Agenda.getAll();
        Collections.shuffle(agendas);

        return agendas.stream()
            .filter(SyncService::checkConnection)
            .findFirst()
            .orElse(null);
    }

    private static TimerTask syncAgendaTask() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    final var repo = AgendaMediator.getInstance();
                    if (repo.getAgenda() == null || !checkConnection(repo.getAgenda())) {
                        repo.setAgenda(SyncService.getAvailableAgenda());
                        if (repo.getAgenda() == null) return;
                    }
                    final var remoteService = SyncService.getRemoteService();
                    AgendaMediator.getInstance().updateAgenda(remoteService.getAll());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void periodicallyUpdateAgenda() {
        final var timer = new Timer();
        timer.scheduleAtFixedRate(syncAgendaTask(), 0, TIMER_INTERVAL);
    }
}
