package startprocess;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/8/3
 * @since : 1.5
 */
public class Start {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    @Test
    public void deploy(){
        Deployment start = processEngine.getRepositoryService().createDeployment().name("开始活动节点").addClasspathResource("start.bpmn").addClasspathResource("start.png").deploy();
        System.out.println(start.getId());
    }
    @Test
    public void start(){
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("start");
        System.out.println(processInstance.getId());
    }


//    此时已经查不到流程实例了，流程结束以后，流程实例已经被删除了。
    @Test
    public void search(){
        ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId("ae76b9ff-96ea-11e8-bbda-107b44a45166").singleResult();
        System.out.println(processInstance);
    }

    /**
     * 可以使用历史的记录查询
     */
    @Test
    public void select(){
        HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId("ae76b9ff-96ea-11e8-bbda-107b44a45166").singleResult();
        System.out.println(historicProcessInstance.getId());
    }
}
