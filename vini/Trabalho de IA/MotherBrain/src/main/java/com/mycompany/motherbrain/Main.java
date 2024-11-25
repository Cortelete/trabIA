/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.motherbrain;

import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author willi
 */
public class Main {

    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione o arquivo Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivo Excel", "xlsx", "xls"));

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Cria o ExcelReader e abre o arquivo selecionado
            ExcelReader er = new ExcelReader();
            er.open(filePath);  // Usa o caminho selecionado pelo usuário

            InputLayer il = er.getIl();
            ArrayList<Double> y = er.getOl();

            Neuron n = new Neuron(il, 0.0000001, 97, y, Neuron.LINEAR,305);

            Double weight = n.Training();
            System.out.println("Pesos: " + weight);
            
            
        }
    }
}
