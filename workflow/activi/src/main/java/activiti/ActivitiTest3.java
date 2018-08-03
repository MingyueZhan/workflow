package activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/8/2
 * @since : 1.5
 */
public class ActivitiTest3 {
//    第一步 获取processEngine
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    //接下来部署流程定义以及启动流程实例
    /**
     * 部署流程定义： 首先要定义流程也就是要画流程图
     */
    @Test
    public void deployProcessDefination(){
        //第一步 获取repositoryService
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("分支流程").addClasspathResource("fenzhi.bpmn").addClasspathResource("fenzhi.png").deploy();

        //此时操作的是act_re_deployment表。
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
        System.out.println(deployment.getKey());
        System.out.println(deployment.getDeploymentTime());
        System.out.println(deployment.getCategory());

    }
    //启动流程实例

    /**
     * 启动流程，现在要看的是
     */
    @Test
    public void startProcess(){
        String processInstancekey = "fenzhi";
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(processInstancekey);
        System.out.println(processInstance.getId());  //流程实例的id是来自ack_ru_execution
        System.out.println(processInstance.getProcessDefinitionId());  //这个id是ack_re_procdef表里面的
    }

    /**
     * 查询我的个人任务列表
     */
    @Test
    public void findMyTask(){
        String userId = "小峰";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(userId).list();
//        遍历集合
        if (list!=null&&list.size()>0){
//            此时查看的是act_ru_task表的信息。
            for (Task task:list){
                System.out.println("id :"+task.getId());
                System.out.println("name :"+task.getName());
                System.out.println("assignee :"+task.getAssignee());
                System.out.println("createTime :"+task.getCreateTime());
                System.out.println("executionId :"+task.getExecutionId());
                System.out.println("------------------------");
            }
        }
    }

}
