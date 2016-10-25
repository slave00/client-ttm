package view;

import core.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by slave00 on 19/10/16.
 */
public class ChatView extends JFrame implements Observer {
    private JList<String> listaUsuarios;
    private JTextArea areaMensagens;
    private JTextField campoMensagem;
    private JButton botaoEnviar;

    private Cliente cli;

    public ChatView(){
        build();
    }

    public void startUp(Cliente cli){
        this.cli = cli;
        cli.addObserver(this);
    }

    public void build(){
        DefaultListModel model = new DefaultListModel();
        listaUsuarios = new JList<String>(model);
        listaUsuarios.setFixedCellWidth(150);
        add(new JScrollPane(listaUsuarios), BorderLayout.LINE_START);

        areaMensagens = new JTextArea(30, 70);
        areaMensagens.setEditable(false);
        areaMensagens.setLineWrap(true);
        add(new JScrollPane(areaMensagens), BorderLayout.CENTER);

        Box box = Box.createHorizontalBox();
        add(box, BorderLayout.SOUTH);
        campoMensagem = new JTextField();
        botaoEnviar = new JButton("Enviar");
        box.add(campoMensagem);
        box.add(botaoEnviar);

        pack();

        setTitle("cliente chat talk to much");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        ActionListener sendListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String str = campoMensagem.getText();
                if (str != null && str.trim().length() > 0){
                    if(listaUsuarios.getSelectedValue() != null){
                        cli.enviarMensagem(str, listaUsuarios.getSelectedValue());
                    }else {
                        cli.enviarMensagem(str);
                    }
                }

                campoMensagem.selectAll();
                campoMensagem.requestFocus();
                campoMensagem.setText("");
            }
        };

        campoMensagem.addActionListener(sendListener);
        botaoEnviar.addActionListener(sendListener);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cli.stop();
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        final Object finalArg = arg;

        if(arg.toString().contains("@@u@@")){
            String uu = arg.toString().split(" ")[2];
            DefaultListModel dlm = (DefaultListModel) listaUsuarios.getModel();

            if(!dlm.contains(uu)){
                dlm.addElement(uu);
            } else {
                dlm.removeElement(uu);
            }
            //SwingUtilities.invokeLater(listaUsuarios.);
        }else {
            Runnable updateTxtMensagens = () -> {
                areaMensagens.append(finalArg.toString());
                areaMensagens.append("\n");
            };

            SwingUtilities.invokeLater(updateTxtMensagens);
        }
    }
}
