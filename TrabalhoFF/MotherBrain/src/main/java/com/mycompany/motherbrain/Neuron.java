package com.mycompany.motherbrain;

import java.util.ArrayList;
import org.decimal4j.util.DoubleRounder;

public class Neuron {

    private InputLayer inputLayer;
    private ArrayList<Double> weight;
    private ActivationFunction function;
    public final static String LINEAR = "LINEAR";
    public final static String STEP = "STEP";
    private double learningRate;
    private double minimumAccuracy;
    private ArrayList<Double> y;
    private int bias;

    public Neuron(InputLayer il, double learningRate, double minimumAccuracy, ArrayList<Double> y, String activationFunction, int bias) {
        this.inputLayer = il;
        this.learningRate = learningRate;
        this.weight = new ArrayList<>();
        this.weightInitialization();
        this.activationInitialization(activationFunction);
        this.minimumAccuracy = minimumAccuracy;
        this.y = y;
        this.bias = bias;
    }

    private void weightInitialization() {
        for (Object il : this.inputLayer.getInputs()) {
            this.weight.add(Math.random()); // Pesos iniciais aleatórios
        }
    }

    public double Training() {
        int maxIterations = 1000; // Limite máximo de iterações
        double accuracy = 0.0;

        for (int iteration = 1; iteration <= maxIterations; iteration++) {
            ArrayList<Double> outputs = new ArrayList<>();

            // Calcula as saídas para todas as amostras
            for (int i = 0; i < this.inputLayer.getInputs().get(0).getInput().size(); i++) {
                ArrayList<Double> row = new ArrayList<>();
                for (int j = 0; j < this.inputLayer.getInputs().size(); j++) {
                    row.add(this.inputLayer.getInputs().get(j).getInput().get(i));
                }

                double sum = this.sumFunction(row);
                double output = this.function.output(sum);
                outputs.add(output);
            }

            // Calcula a acurácia
            accuracy = this.calculateAccuracy(outputs, 0.01); // Tolerância de 0.01
            System.out.println("Iteração: " + iteration + ", Acurácia: " + accuracy + "%");

            if (accuracy >= this.minimumAccuracy) {
                System.out.println("Treinamento concluído com precisão de " + accuracy + "%");
                return accuracy;
            }

            // Atualiza os pesos para cada amostra
            for (int i = 0; i < this.inputLayer.getInputs().get(0).getInput().size(); i++) {
                ArrayList<Double> row = new ArrayList<>();
                for (int j = 0; j < this.inputLayer.getInputs().size(); j++) {
                    row.add(this.inputLayer.getInputs().get(j).getInput().get(i));
                }

                double sum = this.sumFunction(row);
                double output = this.function.output(sum);
                double error = this.y.get(i) - output; // Calcula o erro

                // Atualiza cada peso com base no erro
                for (int j = 0; j < this.weight.size(); j++) {
                    double newWeight = this.weight.get(j) + (this.learningRate * row.get(j) * error);
                    this.weight.set(j, newWeight);
                }
            }
        }
        System.out.println("Treinamento encerrado após atingir o máximo de iterações.");
        return accuracy;
    }

    private double sumFunction(ArrayList<Double> inputs) {
        double output = 0;
        for (int i = 0; i < inputs.size(); i++) {
            output += (inputs.get(i) * this.weight.get(i));
        }
        return output + this.bias;
    }

    private void activationInitialization(String activationFunction) {
        switch (activationFunction) {
            case LINEAR -> this.function = new LinearFunction();
            case STEP -> this.function = new StepFunction();
            default -> throw new IllegalArgumentException("Função de ativação inválida.");
        }
    }

    private double calculateAccuracy(ArrayList<Double> outputs, double tolerance) {
        int correct = 0;
        for (int i = 0; i < outputs.size(); i++) {
            if (Math.abs(outputs.get(i) - this.y.get(i)) <= tolerance) {
                correct++;
            }
        }
        return (correct / (double) outputs.size()) * 100;
    }
}
