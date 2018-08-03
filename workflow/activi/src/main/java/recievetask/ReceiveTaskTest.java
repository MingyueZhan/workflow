package recievetask;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 接受活动，receive task 即就是等待活动。
 *  接受任务是一个简单的任务，他会等待对应消息的到达，
 *
 *  主要具体表现在：当流程创建以后，流程会进入等待状态，直接引擎接受了一个特定的消息，这会触发流程流程穿过接受任务继续执行。
 *
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/8/3
 * @since : 1.5
 */
public class ReceiveTaskTest {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
@Test
    public void deploy(){
        Deployment receive  = processEngine.getRepositoryService().createDeployment().name("接受活动").addClasspathResource("receive.bpmn").addClasspathResource("receive.png").deploy();
    }

    @Test
    public void start(){
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("myProcess_1");
        System.out.println(processInstance.getId());

        String pid = processInstance.getId();

//        查询是否有一个执行对象在描述“汇总当日销售额”
        Execution execution = processEngine.getRuntimeService().createExecutionQuery().processInstanceId(pid).activityId("_3").singleResult();
//        执行逻辑，设置流程变量。
        Map<String,Object> variable = new HashMap<>();
        variable.put("当日销售额",10000);
//        5 流程向后执行一步，往后推移execution，使用signal给流程引擎信号，告诉他当前任务已经完成了，可以往后执行。
        processEngine.getRuntimeService().signalEventReceived(execution.getId(),variable);

//        接下来判断当前流程是否在“给老板发短信”
        Execution execution1 = processEngine.getRuntimeService().createExecutionQuery().processInstanceId("7bf59c8d-9703-11e8-bcd6-107b44a45166").activityId("给总经理发短信").singleResult();
        System.out.println(execution1);
        Integer money = (Integer) processEngine.getRuntimeService().getVariable(execution1.getId(), "当日销售额");
        System.out.println("今天，今天赚了"+money+"元钱");

//        向后执行一步：任务完成，往后推移“给老板发短信”任务。

         processEngine.getRuntimeService().signalEventReceived(execution1.getId());

//         查询流程状态
        ProcessInstance processInstance1 = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(pid).singleResult();
        if (processInstance1==null){
            System.out.println("流程正常执行！！！，已经结束了");
        }

    }




}
