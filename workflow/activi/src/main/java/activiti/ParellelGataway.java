package activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * 本例用于测试并行网关。
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/8/3
 * @since : 1.5
 *
 */
public class ParellelGataway {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    @Test
    public void deploy(){
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("并行网关").addClasspathResource("parallel.bpmn").addClasspathResource("parallel.png").deploy();
        System.out.println(deployment.getId());
    }
    @Test
    public void start(){
        String processInstanceKey = "parallel";
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(processInstanceKey);
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessDefinitionId());
    }

//    查看我的任务

    @Test
    public void search(){
        String taskassignee = "员工1";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(taskassignee).list();
        if (list!=null&&list.size()>0){
            for (Task task:list){
                System.out.println(task.getId());
                System.out.println(task.getCreateTime());
                System.out.println(task.getAssignee());
            }
        }
    }
    @Test
    public void search1(){
        String taskassignee = "员工2";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(taskassignee).list();
        if (list!=null&&list.size()>0){
            for (Task task:list){
                System.out.println(task.getId());
                System.out.println(task.getCreateTime());
                System.out.println(task.getAssignee());
            }
        }
    }
    @Test
    public void search2(){
        String taskassignee = "主管";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(taskassignee).list();
        if (list!=null&&list.size()>0){
            for (Task task:list){
                System.out.println(task.getId());
                System.out.println(task.getCreateTime());
                System.out.println(task.getAssignee());
            }
        }
    }
    @Test
    public void search3(){
        String taskassignee = "部门经理";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(taskassignee).list();
        if (list!=null&&list.size()>0){
            for (Task task:list){
                System.out.println(task.getId());
                System.out.println(task.getCreateTime());
                System.out.println(task.getAssignee());
            }
        }
    }
    @Test
    public void search4(){
        String taskassignee = "老板";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(taskassignee).list();
        if (list!=null&&list.size()>0){
            for (Task task:list){
                System.out.println(task.getId());
                System.out.println(task.getCreateTime());
                System.out.println(task.getAssignee());
            }
        }
    }

//    完成任务
    @Test
    public  void complete(){
        String taskid = "73ca39ab-96e5-11e8-ac2d-107b44a45166";
        processEngine.getTaskService().complete(taskid);
        System.out.println("完成任务：");
    }
    @Test
    public  void complete2(){
        String taskid = "73ca60c1-96e5-11e8-ac2d-107b44a45166";
        processEngine.getTaskService().complete(taskid);
        System.out.println("完成任务：");
    }
@Test
    public  void complete3(){
        String taskid = "73ca60be-96e5-11e8-ac2d-107b44a45166";
        processEngine.getTaskService().complete(taskid);
        System.out.println("完成任务：");
    }
@Test
    public  void complete4(){
        String taskid = "d21c6d37-96e7-11e8-b41d-107b44a45166";
        processEngine.getTaskService().complete(taskid);
        System.out.println("完成任务：");
    }

    @Test
    public  void complete5(){
        String taskid = "1c2a187a-96e8-11e8-a67b-107b44a45166";
        processEngine.getTaskService().complete(taskid);
        System.out.println("完成任务：");

    }
}
