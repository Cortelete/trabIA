package com.mycompany.motherbrain;

import java.util.ArrayList;
import java.util.Objects;
import org.decimal4j.util.DoubleRounder;

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

    public Neuron(InputLayer il, double learningRate, double minimumAcuracy, ArrayList<Double> y, String activationFunction, int bias) {
        this.inputLayer = il;
        this.learningRate = learningRate;
        this.weight = new ArrayList<Double>();
        this.weightInitialization();
        this.activationInitialization(activationFunction);
        this.minimumAcuracy = minimumAcuracy;
        this.y = y;
        this.bias = bias;
    }

    private void weightInitialization() {
        for (Object il : this.inputLayer.getInputs()) {
            this.weight.add(1.0);
        }
    }

    public double Training() {
        double acuracy = 0.0;
        ArrayList<Double> outPuts;

        int iteration = 0;

        while (true) {
            iteration++;
            outPuts = new ArrayList<>();

            // Calcula a saída para cada amostra de entrada
            for (int i = 0; i < this.inputLayer.getInputs().get(0).getInput().size(); i++) {
                ArrayList<Double> row = new ArrayList<>();
                for (int j = 0; j < this.inputLayer.getInputs().size(); j++) {
                    row.add(this.inputLayer.getInputs().get(j).getInput().get(i));
                }

                // Calcula a soma ponderada e a saída da função de ativação
                double sum = this.sumFunction(row);
                double output = this.function.output(sum);

                // Adiciona a saída calculada à lista
                outPuts.add(output);
            }

            // Calcula a acurácia
            acuracy = this.calculateAcuracy(outPuts);

            System.out.println("Iteração: " + iteration);
            System.out.println("Acurácia atual: " + acuracy + "%");

            // Condição de parada
            if (acuracy >= this.minimumAcuracy) {
                System.out.println("Treinamento concluído com precisão de " + acuracy + "%");
                return DoubleRounder.round(this.weight.get(0), 3);
            }

            // Calcula o erro médio
            double totalError = 0;
            for (int i = 0; i < outPuts.size(); i++) {
                double output = outPuts.get(i);
                double target = this.y.get(i);
                totalError += target - output; // Acumula os erros
            }
            double meanError = totalError / outPuts.size(); // Calcula o erro médio

            System.out.println("Erro médio: " + meanError);

            // Atualiza os pesos
            for (int j = 0; j < this.weight.size(); j++) {
                double inputSum = 0;

                // Calcula a média dos inputs para este peso específico
                for (int i = 0; i < outPuts.size(); i++) {
                    double input = this.inputLayer.getInputs().get(j).getInput().get(i);
                    inputSum += input;
                }
                double meanInput = inputSum / outPuts.size();

                // Verifica se há valores válidos antes de atualizar os pesos
                if (Double.isNaN(meanInput) || Double.isNaN(meanError) || Double.isNaN(this.weight.get(j))) {
                    System.err.println("Erro: valor NaN detectado. Debug:");
                    System.err.println("Input Médio: " + meanInput + ", Erro Médio: " + meanError + ", Peso Atual: " + this.weight.get(j));
                    return Double.MIN_VALUE;
                }

                // Atualiza o peso
                double newWeight = this.weight.get(j) + (this.learningRate * meanInput * meanError);
                this.weight.set(j, newWeight);
            }
        }
    }

    private double sumFunction(ArrayList<Double> inputs) {
        double outPut = 0;
        for (int i = 0; i < inputs.size(); i++) {
            outPut += this.bias + (inputs.get(i) * this.weight.get(i));
        }
        return outPut;
    }

    private void activationInitialization(String activationFunction) {
        switch (activationFunction) {
            case LINEAR -> {
                this.function = new LinearFunction();
            }
            case STEP -> {
                this.function = new StepFunction();
            }
            default -> {
                System.out.println("Por favor, selecione uma função de ativação válida.");
                System.exit(0);
            }
        }
    }

    private double calculateAcuracy(ArrayList<Double> outPuts) {
        double accuracy = 0.0;
        for (int i = 0; i < outPuts.size(); i++) {
            // Arredonda ambos os valores para 4 casas decimais
            double targetRounded = DoubleRounder.round(this.y.get(i), 4);
            double outputRounded = DoubleRounder.round(outPuts.get(i), 4);

            // Incrementa a contagem de acertos se forem iguais
            if (Objects.equals(targetRounded, outputRounded)) {
                accuracy++;
            }
        }
        return (accuracy / outPuts.size()) * 100;
    }
}
