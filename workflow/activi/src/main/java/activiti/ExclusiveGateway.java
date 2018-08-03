package activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *  这里完成对排他网关的测试。
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/8/2
 * @since : 1.5
 */
public class ExclusiveGateway {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void deploy() {
        Deployment deploy = processEngine.getRepositoryService().createDeployment().addClasspathResource("excu.bpmn").addClasspathResource("excu.png").deploy();
        System.out.println(deploy.getId());
    }

    @Test
    public void deploy2() {
        Deployment deploy = processEngine.getRepositoryService().createDeployment().name("排他网关").addClasspathResource("excu.bpmn").addClasspathResource("excu.png").deploy();
        System.out.println(deploy.getId());
    }

    @Test
    public void start() {
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("exec");
        System.out.println(processInstance.getProcessDefinitionId());
    }


//    查看任务
    @Test
    public void query() {
        String taskAssignee = "bumen";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(taskAssignee).list();
        if (list!=null&&list.size()>0){
            for (Task task:list){
                System.out.println(task.getId());
                System.out.println(task.getAssignee());
                System.out.println(task.getCreateTime());
            }
        }
    }


//    完成任务 并设置流程便量
    @Test
    public void completeTask(){
        //给定taskId
        String taskId = "5c72b6c0-96c7-11e8-9044-107b44a45166";
        Map<String,Object> variable = new HashMap<>();
        variable.put("money",400);
        processEngine.getTaskService().complete(taskId,variable);
        System.out.println("完成任务 ,任务id："+taskId);
    }

@Test
    public void query1() {
        String taskAssignee = "caiwumanager";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(taskAssignee).list();
        if (list!=null&&list.size()>0){
            for (Task task:list){
                System.out.println(task.getId());
                System.out.println(task.getAssignee());
                System.out.println(task.getCreateTime());
            }
        }
    }


}
