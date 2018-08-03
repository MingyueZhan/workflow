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
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/8/2
 * @since : 1.5
 */
public class ActivitiTest4 {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    //部署流程，并启动

    @Test
    public void deploy(){
        Deployment fenzhi  = processEngine.getRepositoryService().createDeployment().name("分支流程").addClasspathResource("ss.bpmn").addClasspathResource("ss.png").deploy();
        System.out.println(fenzhi.getId());
        System.out.println(fenzhi.getKey());
        System.out.println(fenzhi.getName());
    }


    @Test
    public void start(){
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("sequence");
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessDefinitionId());
    }

    /**
     * 查看我的个人任务
     */
@Test
    public void query(){
        String assignee ="赵总";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).list();
        if (list!=null&& list.size()>0){
            for (Task task:list){
                System.out.println(task.getId());
                System.out.println(task.getAssignee());
                System.out.println(task.getCreateTime());
            }
        }
    }

    /**
     * 接下来是完成任务
     */
    @Test
    public void completeTask(){
        String taskid = "a65a86cf-962f-11e8-983d-107b44a45166";
//        接下来设置流程变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("message","重要");
        processEngine.getTaskService().complete(taskid, variables);
        System.out.println("完成任务！");
    }

    /**
     * 查看李总的任务
     */
    @Test
    public void selecetTask(){
        String assignee ="张总";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).list();
        if (list!=null&&list.size()>0){
            for (Task task1:list){
                System.out.println(task1.getId());
                System.out.println(task1.getAssignee());
                System.out.println(task1.getCreateTime());
            }
        }
    }
}
