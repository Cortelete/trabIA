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
        try {
            // Cria o JFileChooser para selecionar o arquivo Excel
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecione o arquivo Excel desejado");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivo Excel", "xlsx", "xls"));

            int userSelection = fileChooser.showOpenDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                System.out.println("Arquivo selecionado: " + filePath);

                // Inicializa o ExcelReader e carrega o arquivo selecionado
                ExcelReader er = new ExcelReader();
                er.open(filePath); // Usa o caminho selecionado pelo usuário

                InputLayer il = er.getIl(); // Obtém a camada de entrada
                ArrayList<Double> y = er.getOl(); // Obtém as saídas esperadas

                // Criação do neurônio
                Neuron n = new Neuron(il, 0.0000001, 90, y, Neuron.LINEAR, 305);
                System.out.println("Treinamento do neurônio iniciado...");

                // Executa o treinamento
                Double weight = n.Training();
                System.out.println("Pesos finais: " + weight);

            } else {
                System.out.println("Nenhum arquivo foi selecionado.");
            }

        } catch (Exception e) {
            System.err.println("Erro durante a execução: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
