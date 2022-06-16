package com.seneau.ServiceTask;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class rejectionMail implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("------------------------------------------- Rejection  mail ---------------------------------------------------");

    }
}
