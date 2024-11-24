package com.mycompany.motherbrain;

import java.util.ArrayList;
import java.util.Objects;
import org.decimal4j.util.DoubleRounder;

/**
 *
 * @author will
 */
public class Neuron {

    private InputLayer inputLayer;
    private ArrayList<Double> weight;
    private ActivationFunction function;
    public final static String LINEAR = "LINEAR";
    public final static String STEP = "STEP";
    private double learningRate;
    private double minimumAcuracy;
    private ArrayList<Double> y;
    private int bias;

    // Construtor
    public Neuron(InputLayer il, double learningRate, double minimumAcuracy, ArrayList<Double> y, String activationFunction, int bias) {
        this.inputLayer = il;
        this.learningRate = learningRate;
        this.weight = new ArrayList<>();
        this.weightInitialization();
        this.activationInitialization(activationFunction);
        this.minimumAcuracy = minimumAcuracy;
        this.y = y;
        this.bias = bias;
    }

    // Inicializa os pesos com valor 1.0
    private void weightInitialization() {
        for (Object il : this.inputLayer.getInputs()) {
            this.weight.add(1.0);
        }
    }

    // Treinamento do neurônio
    public double Training() {
        double accuracy = 0.0;
        ArrayList<Double> outputs;

        int iteration = 0; // Contador de iterações
        while (true) {
            iteration++;
            System.out.println("Início da iteração: " + iteration);

            outputs = new ArrayList<>();
            for (int i = 0; i < this.inputLayer.getInputs().get(0).getInput().size(); i++) {
                ArrayList<Double> row = new ArrayList<>();
                for (int j = 0; j < this.inputLayer.getInputs().size(); j++) {
                    row.add(this.inputLayer.getInputs().get(j).getInput().get(i));
                }

                double sum = this.sumFunction(row);
                double output = this.function.output(sum);
                outputs.add(output);
            }

            // Calcula a precisão
            accuracy = this.calculateAccuracy(outputs);
            System.out.println("Precisão atual: " + accuracy + "%");

            if (accuracy >= this.minimumAcuracy) {
                System.out.println("Treinamento concluído após " + iteration + " iterações!");
                return DoubleRounder.round(this.weight.get(0), 3);
            }

            // Atualiza os pesos
            double totalError = 0.0;
            for (int i = 0; i < outputs.size(); i++) {
                double output = outputs.get(i);
                double target = this.y.get(i);
                totalError += target - output;
            }

            double meanError = totalError / outputs.size();
            System.out.println("Erro médio: " + meanError);

            for (int j = 0; j < this.weight.size(); j++) {
                double inputSum = 0.0;
                for (int i = 0; i < outputs.size(); i++) {
                    double input = this.inputLayer.getInputs().get(j).getInput().get(i);
                    inputSum += input;
                }

                double meanInput = inputSum / outputs.size();
                double newWeight = this.weight.get(j) + (this.learningRate * meanInput * meanError);
                System.out.println("Peso atualizado [" + j + "]: " + newWeight);
                this.weight.set(j, newWeight);
            }

            // Evitar loops infinitos para debug inicial
            if (iteration > 10000) {
                System.err.println("Treinamento interrompido por excesso de iterações.");
                break;
            }
        }

        return -1.0; // Retorno em caso de falha
    }

    // Função de soma
    private double sumFunction(ArrayList<Double> inputs) {
        double output = 0.0;
        for (int i = 0; i < inputs.size(); i++) {
            output += this.bias + (inputs.get(i) * this.weight.get(i));
        }
        return output;
    }

    // Inicializa a função de ativação
    private void activationInitialization(String activationFunction) {
        switch (activationFunction) {
            case LINEAR -> this.function = new LinearFunction();
            case STEP -> this.function = new StepFunction();
            default -> {
                System.out.println("Por favor, selecione uma função de ativação válida.");
                System.exit(0);
            }
        }
    }

    // Calcula a precisão
    private double calculateAccuracy(ArrayList<Double> outputs) {
        double correctPredictions = 0.0;
        for (int i = 0; i < outputs.size(); i++) {
            double targetRounded = DoubleRounder.round(this.y.get(i), 4);
            double outputRounded = DoubleRounder.round(outputs.get(i), 4);

            if (Objects.equals(targetRounded, outputRounded)) {
                correctPredictions++;
            }
        }
        return (correctPredictions / outputs.size()) * 100;
    }
}
