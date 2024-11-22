package com.mycompany.motherbrain;

import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione o arquivo Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivo Excel", "xlsx", "xls"));

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Inicializa o leitor de Excel e abre o arquivo
            ExcelReader er = new ExcelReader();
            er.open(filePath);

            // Obtém dados de entrada e saída
            InputLayer inputLayer = er.getIl();
            ArrayList<Double> outputList = er.getOl();

            // Converte dados de entrada para o formato double[][]
            double[][] inputData = new double[inputLayer.getInputs().size()][];
            for (int i = 0; i < inputLayer.getInputs().size(); i++) {
                ArrayList<Double> inputValues = inputLayer.getInputs().get(i).getInput();
                inputData[i] = new double[inputValues.size()];

                for (int j = 0; j < inputValues.size(); j++) {
                    inputData[i][j] = inputValues.get(j);
                }
            }

            // Converte dados de saída para o formato double[]
            double[] outputData = new double[outputList.size()];
            for (int i = 0; i < outputList.size(); i++) {
                outputData[i] = outputList.get(i);
            }

            // Inicializa o neurônio para regressão linear
            Neuron neuron = new Neuron(inputData[0].length, 0.01, "linear");

            // Treina o neurônio
            neuron.train(inputData, outputData, 10000, 0.99);
            System.out.println("Treinamento concluído.");
        }
    }
}