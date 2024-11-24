package com.mycompany.motherbrain;

import java.util.ArrayList;

/**
 * Classe para representar uma camada de entrada para um neurônio.
 * Gerencia as entradas e fornece métodos para acessar os dados.
 */
public class InputLayer {
    private ArrayList<Input> inputs; // Lista de objetos de entrada

    /**
     * Construtor para inicializar a camada de entrada.
     *
     * @param inputs Lista de entradas fornecida.
     */
    public InputLayer(ArrayList<Input> inputs) {
        this.inputs = inputs;
    }

    /**
     * Retorna a lista de entradas.
     *
     * @return Lista de objetos Input.
     */
    public ArrayList<Input> getInputs() {
        return inputs;
    }

    /**
     * Retorna os dados da camada de entrada no formato de uma lista de listas de Double.
     * Cada sublista representa os dados de uma entrada.
     *
     * @return Dados das entradas como ArrayList<ArrayList<Double>>.
     */
    public ArrayList<ArrayList<Double>> getData() {
        ArrayList<ArrayList<Double>> data = new ArrayList<>();
        
        // Itera sobre os objetos Input e coleta os dados
        for (Input input : inputs) {
            data.add(input.getInput());
        }
        
        return data;
    }
}
