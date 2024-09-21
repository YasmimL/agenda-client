package br.com.ifce;

import br.com.ifce.model.Agenda;
import br.com.ifce.network.rmi.SyncService;
import br.com.ifce.view.MainView;

import javax.swing.*;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        SyncService.getInstance().setAgenda(randomAgenda());

        SwingUtilities.invokeLater(() -> {
            try {
                var view = new MainView();
                view.show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static Agenda randomAgenda() {
        return Agenda.getAll().get(new Random().nextInt(Agenda.getAll().size()));
    }
}