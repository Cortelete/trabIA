/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motherbrain;

/**
 *
 * @author willi
 */
public class StepFunction extends ActivationFunction{

    @Override
    protected double output(double result) {
        if(result >= 1){
            return 1;
        }
        return 0;
    }
    
}
