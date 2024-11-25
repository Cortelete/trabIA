/*
 * License: Alterado para manter os direitos autorais e o uso ético do código.
 */
package com.mycompany.motherbrain;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;

/**
 * Classe principal para executar o programa.
 *
 * @author willi
 */
public class Main {

    public static void main(String[] args) {
        // Instancia e configura o seletor de arquivos
        JFileChooser fileChooser = configurarSeletorDeArquivos();

        // Abre o diálogo para o usuário escolher um arquivo
        int userSelection = fileChooser.showOpenDialog(null);

        // Processa o arquivo escolhido, se confirmado
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            processarArquivoSelecionado(fileChooser);
        }
    }

    /**
     * Configura o seletor de arquivos para aceitar arquivos Excel.
     *
     * @return Um JFileChooser configurado.
     */
    private static JFileChooser configurarSeletorDeArquivos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione o arquivo Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivo Excel", "xlsx", "xls"));
        return fileChooser;
    }

    /**
     * Processa o arquivo selecionado pelo usuário e executa o treinamento do neurônio.
     *
     * @param fileChooser O seletor de arquivos com o arquivo selecionado.
     */
    private static void processarArquivoSelecionado(JFileChooser fileChooser) {
        String filePath = fileChooser.getSelectedFile().getAbsolutePath();

        // Criação e configuração do leitor de Excel
        ExcelReader er = new ExcelReader();
        er.open(filePath); // Usa o caminho selecionado pelo usuário

        // Obtém dados de entrada e saída do ExcelReader
        InputLayer il = er.getIl();
        ArrayList<Double> y = er.getOl();

        // Instancia e treina o neurônio
        Neuron n = new Neuron(il, 0.0000001, 80, y, Neuron.LINEAR, 305); // learningRate = 0.01, minimumAccuracy = 80%
        Double weight = n.Training();

        // Exibe os pesos treinados
        System.out.println("Pesos: " + weight);
    }
}
