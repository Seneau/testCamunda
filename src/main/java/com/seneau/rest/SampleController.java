package com.seneau.rest;

import camundajar.impl.com.google.gson.Gson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seneau.ServiceTask.CamundaStartService;
import com.seneau.service.CompleteProcessFromJava;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class SampleController {

    @Autowired
    CamundaStartService camundaStartService;

    @Autowired
    CompleteProcessFromJava completeProcessFromJava;


    ProcessEngine processEngine;

    @RequestMapping("/get")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    /*
    @RequestMapping(value = "/testcasesampleflow", method = RequestMethod.POST)
    public void testcaseSample(@RequestBody TestCaseSampleDto obj) throws Exception {
        completeProcessFromJava.completeTestCaseSampleFlow(obj);
    }

    @RequestMapping(value = "/msgeventstart", method = RequestMethod.POST)
    public void persistPerson(@RequestBody Person obj) throws Exception {
        camundaStartService.startProcessByMessage2(obj);
    }

     */

    @RequestMapping(value = "/congestart", method = RequestMethod.POST)
    public String persistConge() throws Exception {
    ProcessInstance process = camundaStartService.startProcessByKey();
        return process.getProcessInstanceId();
    }

//    @RequestMapping(value = "/msgs2", method = RequestMethod.POST)
//    public void msgs2(@RequestBody String name) {
//        camundaStartService.startProcessByMessage2(name);
//    }

    @RequestMapping(value = "/getconnector/{name}", method = RequestMethod.GET)
    public int getConnector(@PathVariable String name) {
        return name.length();
    }

    /*

    @RequestMapping(value = "/postconnector", method = RequestMethod.POST)
    public int msgs2(@RequestBody Summers summers) {
        return summers.getNum1() + summers.getNum2() + summers.getNum3() + summers.getNum4();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/demonativejson")
    public void demoNativeJson(@RequestBody NativeJsonDemoRequestDto nativeJsonDemoRequestDto) {
        camundaRequestsService.callNativeJsonDemoSample(nativeJsonDemoRequestDto);
    }

     */

    @GetMapping("/tasks")
    public String getTask() throws JsonProcessingException {
        Gson gson = new Gson();
        return gson.toJson(processEngine.getTaskService().createTaskQuery()
                .taskId("e902b4b8-d6c6-11ec-bef5-7ee8319f04d6").singleResult());
    }


    @RequestMapping(value = "/completeTask/{taskId}", method = RequestMethod.POST)
    public void testcaseSample(@PathVariable String taskId) throws Exception {
        completeProcessFromJava.completeTask(taskId);
    }
}
