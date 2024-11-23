package com.mycompany.motherbrain;

public class Neuron {
    private double[] weights; // Pesos
    private double bias; // Bias
    private double learningRate; // Taxa de aprendizado
    private String activationFunctionType; // Tipo de função de ativação
    private ActivationFunction activationFunction;

    // Construtor para inicializar os pesos e bias
    public Neuron(int inputSize, double learningRate, String activationFunctionType) {
        this.weights = new double[inputSize];
        this.learningRate = learningRate;
        this.activationFunctionType = activationFunctionType;
        this.activationFunction = createActivationFunction(activationFunctionType);
        weightInitialization();
    }

    // Inicializa os pesos e o bias aleatoriamente
    private void weightInitialization() {
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random() * 2 - 1; // Peso entre -1 e 1
        }
        bias = Math.random() * 2 - 1; // Bias entre -1 e 1
    }

    // Função de soma ponderada das entradas
    private double sumFunction(double[] inputs) {
        double sum = bias; // Incluindo o bias
        for (int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * weights[i];
        }
        return sum;
    }

    // Função de ativação (sigmoid ou linear)
    private ActivationFunction createActivationFunction(String type) {
        switch (type) {
            case "sigmoid":
                return new SigmoidFunction();
            default:
                return new LinearFunction();
        }
    }

    // Função para prever a saída
    public double predict(double[] inputs) {
        return activationFunction.output(sumFunction(inputs));
    }

    // Treinamento da rede
    public void train(double[][] inputs, double[] expectedOutputs, int maxEpochs, double targetAccuracy) {
        for (int epoch = 0; epoch < maxEpochs; epoch++) {
            double totalError = 0;
            for (int i = 0; i < inputs.length; i++) {
                double output = predict(inputs[i]);
                double error = expectedOutputs[i] - output;
                totalError += Math.pow(error, 2); // Calculando erro quadrático médio

                // Atualizando os pesos
                for (int j = 0; j < weights.length; j++) {
                    weights[j] += learningRate * error * inputs[i][j];
                }
                // Atualizando o bias
                bias += learningRate * error;
            }
            // Imprime o erro médio a cada 1000 épocas
            if (epoch % 1000 == 0) {
                System.out.println("Epoch " + epoch + ": Error = " + totalError / inputs.length);
            }
            // Se o erro médio for abaixo do limiar, pare o treinamento
            if (totalError / inputs.length <= targetAccuracy) {
                System.out.println("Treinamento completo após " + epoch + " épocas.");
                break;
            }
        }
    }

    // Interface para as funções de ativação
    interface ActivationFunction {
        double output(double x);
    }

    // Função de ativação Sigmoid
    static class SigmoidFunction implements ActivationFunction {
        public double output(double x) {
            return 1 / (1 + Math.exp(-x));
        }
    }

    // Função de ativação Linear
    static class LinearFunction implements ActivationFunction {
        public double output(double x) {
            return x;
        }
    }
}
