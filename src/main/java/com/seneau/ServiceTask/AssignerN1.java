package com.seneau.ServiceTask;

import com.seneau.repository.CongeRepository;
import com.seneau.service.CongeService;
import lombok.val;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class AssignerN1 implements JavaDelegate {

     @Autowired
     CongeService congeService ;

     @Autowired
     public CongeRepository congeRepository ;

     /*
    public AssignerN1(CongeService congeService, CongeRepository congeRepository) {
      //  this.congeService = congeService;
        this.congeRepository = congeRepository;
    }

      */

    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("-------------------------------------------  Assigner N1 ---------------------------------------------------");
        System.out.println("------------------------------------------- "+ execution.getVariable("niveau") +" ---------------------------------------------------");
        System.out.println("------------------------------------------- "+ execution.getProcessInstanceId() +" ---------------------------------------------------");

        val conge = congeRepository.findAll();

        System.out.println("------------------------------------------- "+ conge +" ---------------------------------------------------");

    }

}