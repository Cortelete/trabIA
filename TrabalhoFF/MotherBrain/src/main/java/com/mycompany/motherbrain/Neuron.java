package com.mycompany.motherbrain;

import java.util.Arrays;
import org.decimal4j.util.DoubleRounder;

public class Neuron {
    private double[] weights;
    private double learningRate;
    private ActivationFunction activationFunction;

    public Neuron(int inputSize, double learningRate, String activationType) {
        this.weights = new double[inputSize];
        this.learningRate = learningRate;
        this.weightInitialization();
        this.activationInitialization(activationType);
    }

    private void weightInitialization() {
        // Inicialização aleatória dos pesos entre -1 e 1
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random() * 2 - 1;
        }
    }

    private void activationInitialization(String type) {
        switch (type.toLowerCase()) {
            case "linear":
                this.activationFunction = new LinearFunction();
                break;
            case "step":
                this.activationFunction = new StepFunction();
                break;
            default:
                throw new IllegalArgumentException("Função de ativação desconhecida: " + type);
        }
    }

    public double[] predict(double[][] inputs) {
        double[] outputs = new double[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            outputs[i] = activationFunction.output(sumFunction(inputs[i]));
        }
        return outputs;
    }

    private double sumFunction(double[] inputs) {
        if (inputs.length != weights.length) {
            throw new IllegalArgumentException("Número de entradas não corresponde ao número de pesos.");
        }
        double sum = 0.0;
        for (int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * weights[i];
        }
        return sum;
    }

    public void train(double[][] inputs, double[] expectedOutputs, int maxEpochs, double targetAccuracy) {
        int epoch = 0;
        double accuracy = 0.0;

        while (epoch < maxEpochs && accuracy < targetAccuracy) {
            for (int i = 0; i < inputs.length; i++) {
                double output = activationFunction.output(sumFunction(inputs[i]));
                double error = expectedOutputs[i] - output;

                // Atualização dos pesos usando gradiente descendente
                for (int j = 0; j < weights.length; j++) {
                    weights[j] += learningRate * error * inputs[i][j];
                }
            }
            accuracy = calculateAccuracy(inputs, expectedOutputs);
            epoch++;

            if (Double.isNaN(accuracy)) {
                System.out.println("Erro inesperado: acurácia resultou em NaN. Verifique os dados de entrada.");
                break;
            }
            System.out.println("Época: " + epoch + ", Acurácia: " + DoubleRounder.round(accuracy * 100, 2) + "%");
        }
    }

    private double calculateAccuracy(double[][] inputs, double[] expectedOutputs) {
        int correctCount = 0;
        for (int i = 0; i < inputs.length; i++) {
            double predicted = activationFunction.output(sumFunction(inputs[i]));
            if (Math.abs(predicted - expectedOutputs[i]) < 0.01) {
                correctCount++;
            }
        }
        return (double) correctCount / inputs.length;
    }

    // Função de ativação linear
    static class LinearFunction implements ActivationFunction {
        public double output(double x) {
            return x;
        }
    }

    // Função de ativação step (exemplo)
    static class StepFunction implements ActivationFunction {
        public double output(double x) {
            return x >= 0 ? 1 : 0;
        }
    }

    // Interface para funções de ativação
    interface ActivationFunction {
        double output(double x);
    }

    public static void main(String[] args) {
        // Dados de treinamento: Fahrenheit para Kelvin
        double[][] trainingData = {
            {32}, {68}, {104}, {50}, {77}
        };
        double[] expectedOutputs = {
            273.15, 293.15, 313.15, 283.15, 298.15
        };

        // Normalização dos dados para evitar problemas de escala
        for (int i = 0; i < trainingData.length; i++) {
            trainingData[i][0] = (trainingData[i][0] - 32) * 5 / 9; // Conversão para Celsius
        }

        Neuron neuron = new Neuron(1, 0.01, "linear");
        neuron.train(trainingData, expectedOutputs, 10000, 0.99);
    }
}
