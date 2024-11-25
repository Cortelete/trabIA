package com.mycompany.motherbrain;

import java.util.ArrayList;
import java.util.Objects;
import org.decimal4j.util.DoubleRounder;

/**
 * A classe Neuron realiza regressão linear para converter Fahrenheit em Kelvin.
 */
public class Neuron {

    private InputLayer inputLayer;
    private ArrayList<Double> weight;
    private ActivationFunction function;
    public final static String LINEAR = "LINEAR";
    private double learningRate;
    private double minimumErrorThreshold;
    private ArrayList<Double> y;
    private int bias;

    public Neuron(InputLayer il, double learningRate, double minimumErrorThreshold, ArrayList<Double> y, String activationFunction, int bias) {
        this.inputLayer = il;
        this.learningRate = learningRate;
        this.weight = new ArrayList<>();
        this.weightInitialization();
        this.activationInitialization(activationFunction);
        this.minimumErrorThreshold = minimumErrorThreshold;
        this.y = y;
        this.bias = bias;
    }

    // Inicialização dos pesos com valores aleatórios entre -0.05 e 0.05
    private void weightInitialization() {
        for (Object il : this.inputLayer.getInputs()) {
            this.weight.add(Math.random() * 0.1 - 0.05); // Pesos entre -0.05 e 0.05
        }
    }

    /**
     * Método de treinamento utilizando Erro Médio Quadrático como critério.
     */
    public double Training() {
        ArrayList<Double> outPuts;
        double meanSquaredError = Double.MAX_VALUE;
        int iterationCount = 0;
        final int MAX_ITERATIONS = 10000;

        while (iterationCount < MAX_ITERATIONS && meanSquaredError > this.minimumErrorThreshold) {
            outPuts = new ArrayList<>();
            meanSquaredError = 0;

            // Calcula a saída para cada linha de entrada
            for (int i = 0; i < this.inputLayer.getInputs().get(0).getInput().size(); i++) {
                ArrayList<Double> row = new ArrayList<>();
                for (int j = 0; j < this.inputLayer.getInputs().size(); j++) {
                    row.add(this.inputLayer.getInputs().get(j).getInput().get(i));
                }

                double sum = this.sumFunction(row);
                double output = this.function.output(sum);
                outPuts.add(output);
            }

            // Calcula o Erro Médio Quadrático
            for (int i = 0; i < outPuts.size(); i++) {
                double error = this.y.get(i) - outPuts.get(i);
                meanSquaredError += error * error;
            }
            meanSquaredError /= outPuts.size();

            // Exibe os resultados da iteração atual
            System.out.println("Iteração: " + (iterationCount + 1) + " - Erro Médio Quadrático: " + meanSquaredError);

            // Atualiza os pesos com base no gradiente
            for (int j = 0; j < this.weight.size(); j++) {
                double gradientSum = 0;
                for (int i = 0; i < outPuts.size(); i++) {
                    double error = this.y.get(i) - outPuts.get(i);
                    double input = this.inputLayer.getInputs().get(j).getInput().get(i);
                    gradientSum += error * input;
                }
                double gradient = gradientSum / outPuts.size();
                this.weight.set(j, this.weight.get(j) + this.learningRate * gradient);
            }

            iterationCount++;
        }

        if (iterationCount == MAX_ITERATIONS) {
            System.out.println("Treinamento não convergiu após " + MAX_ITERATIONS + " iterações.");
        } else {
            System.out.println("Treinamento concluído em " + iterationCount + " iterações com erro de " + meanSquaredError);
        }

        return DoubleRounder.round(this.weight.get(0), 3);
    }

    // Função de soma ponderada
    private double sumFunction(ArrayList<Double> inputs) {
        double output = 0;
        for (int i = 0; i < inputs.size(); i++) {
            output += (inputs.get(i) * this.weight.get(i));
        }
        return output + this.bias;
    }

    // Inicializa a função de ativação linear
    private void activationInitialization(String activationFunction) {
        if (Objects.equals(activationFunction, LINEAR)) {
            this.function = new LinearFunction();
        } else {
            throw new IllegalArgumentException("Função de ativação inválida. Use LINEAR.");
        }
    }
}
