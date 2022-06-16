package com.seneau.ServiceTask;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CamundaStartService {
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
/*
    public void startProcessByMessage(Person person) {
        runtimeService.createMessageCorrelation("msg-s-1")
                .setVariable("springname", person.getName())
                .setVariable("gender", person.getGender())
                .correlate();
    }

    public void startProcessByMessage2(Person person) throws Exception {
        String name = person.getName();
        String gender = person.getGender();

        ObjectMapper obj = new ObjectMapper();
        String json = obj.writeValueAsString(person);

        ObjectValue typedPersonValue = Variables.objectValue(person).serializationDataFormat("application/json").create(); //application json lets you all class function...
//        JsonValue jsonValue = (JsonValue) SpinValues.jsonValue(person.toString()).create();
        runtimeService.createMessageCorrelation("msg-s-2")
                .setVariable("name", name)
                .setVariable("customSerialized", typedPersonValue)
                .setVariable("customJson", json)
                .correlate();


    }

 */

    public ProcessInstance startProcessByKey() {
        /*
        String name = person.getName();
        String gender = person.getGender();

        ObjectMapper obj = new ObjectMapper();
        String json = obj.writeValueAsString(person);
*/
//        ObjectValue typedPersonValue = Variables.objectValue(person).serializationDataFormat("application/json").create(); //application json lets you all class function...
//        JsonValue jsonValue = (JsonValue) SpinValues.jsonValue(person.toString()).create();
       // runtimeService.createMessageCorrelation("msg-s-3")

                //.setVariable("name", name)
                //.setVariable("customSerialized", typedPersonValue)
                //.setVariable("customJson", json)
        ProcessInstance process =  runtimeService.startProcessInstanceByKey("MycongeWorkflow");
              //  .correlate();
        return process ;
    }

    public void completeTask(String taskInstanceId) {
        taskService.complete(taskInstanceId);
    }

//    for deserializing: String customerJson = customer.getValueSerialized();

//    https://docs.camunda.org/manual/latest/user-guide/data-formats/json/#native-json-variable-value
//    https://docs.camunda.org/manual/7.8/user-guide/data-formats/configuring-spin-integration/ - for dependencies
//    xml manipulations: https://docs.camunda.org/manual/7.5/user-guide/data-formats/#json-native-json-variable-value
//    play with json: https://docs.camunda.org/manual/7.7/reference/spin/json/01-reading-json/
}

