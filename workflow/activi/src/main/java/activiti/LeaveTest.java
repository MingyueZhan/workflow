package activiti;

import entity.User;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/8/1
 * @since : 1.5
 */
public class LeaveTest {
    //    初始化工作流引擎
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    //    第一步 部署流程并定义
    @Test
    public void deployProcess() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("test.bpmn");
        InputStream inputStreamPng = this.getClass().getClassLoader().getResourceAsStream("test.png");
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("审批流程").addInputStream("test.bpmn", inputStream).addInputStream("test.png", inputStreamPng).deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    //    启动流程实例
    @Test
    public void startProcess() {
        String processDefinationKey = "testvariable";
//        在启动的时候来设置流程变量
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinationKey);
        System.out.println(processInstance.getProcessDefinitionId());
    }

//    设置流程变量

    /**
     * 使用 taskservice设置和获取流程变量。
     * 设置流程变量：通常使用taskId taskId的值主要是：在act_ru_task 表里面查询。
     * <p>
     * 设置流程变量的时候，向act_ru_variable表中添加数据。
     */
    @Test
    public void setVariable() {
//        获取与任务有关的service
        TaskService taskService = processEngine.getTaskService();
        String taskId = "21666175-952b-11e8-a96a-107b44a45166";
        //设置流程变量
        taskService.setVariable(taskId, "请假天数", 4);
        taskService.setVariable(taskId, "请假日期", new Date());
        taskService.setVariable(taskId, "请假原因", "回家探亲");
    }

    //    接下来使用javabean来设置流程变量

    /**
     * 在这里面要注意的是两点：①：流程变量的作用域是流程实例，只需要设置，不用管在哪个阶段设置。
     * ②：当javabean用作设置流程变量时。这个Javabean必须实现Serializable接口
     */
    @Test
    public void setVariableByBean() {
        TaskService taskService = processEngine.getTaskService();
        String taskId = "21666175-952b-11e8-a96a-107b44a45166";
//        实例化javabean
        User user = new User();
        user.setId("1");
        user.setName("小王");
        taskService.setVariableLocal(taskId, "用户信息", user);//与任务id绑定的流程变量。
    }

//    获取流程变量
    /**
     *
     *  流程便量的获取针对流程实例，（1个流程）每个流程实例获取的流程变量是不同的。
     *  使用基本类型获取流程变量，在taskService 里面使用taskId ,与流程变量的name值即可获取。
     *  当使用javabean来获取流程便量的时候，除了需要Javabean需要实现序列化接口以外，还要求，流程变量
     *  对象的属性不能发生变化，否则会抛出异常。解决方案，就是使用序列化id。
     */

    /**
     * 获取流程变量
     */
    @Test
    public void getVariable() {
//        第一步获取与任务相关的service
        TaskService taskService = processEngine.getTaskService();

//        给定任务id
        String taskId = "21666175-952b-11e8-a96a-107b44a45166";
//      获取流程便量的值
        Integer day = (Integer) taskService.getVariable(taskId, "请假天数");
        Date date = (Date) taskService.getVariable(taskId, "请假日期");
        String reason = (String) taskService.getVariable(taskId, "请假原因");
        System.out.println("请假日期：" + date + ",请假天数：" + day + ",请假原因：" + reason);
    }

    /**
     * 根据javabean来获取流程变量。
     */
    @Test
    public void getVariableByBean() {
        TaskService taskService = processEngine.getTaskService();
        String taskId = "21666175-952b-11e8-a96a-107b44a45166";
//        获取流程变量的值
        User user = (User) taskService.getVariable(taskId, "用户信息");
        System.out.println("用户id :" + user.getId() + ",用户姓名：" + user.getName());
    }


    /**
     * 使用runtimeservice 来设置和获取流程变量。
     * <p>
     * 此时的流程变量的值依然是存储在act_ru_variable表中。
     */
    @Test
    public void setVariableByRuntimeService() {
//        毫无疑问，首先需要获取runtimeservice
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        给定一个执行id
        String executionId = "15001";
        runtimeService.setVariable(executionId, "days", 4);
        runtimeService.setVariable(executionId, "date", new Date());
        runtimeService.setVariable(executionId, "reason", "回家看看");
    }

    //    使用javabean来设置 路程变量
    @Test
    public void setVariableWithBeanByRuntimeService() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String executionId = "15001";
//        实例化javabean
        User user = new User();
        user.setId("2");
        user.setName("李四");
        runtimeService.setVariable(executionId, "user", user);
    }


//    接下来是获取流程变量

    /**
     * runtimeservice
     */
    @Test
    public void getVariableByRuntimeService() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String executionId = "15001";
        Integer days = (Integer) runtimeService.getVariableLocal(executionId, "days");
        Date date = (Date) runtimeService.getVariable(executionId, "date");
        String reason = (String) runtimeService.getVariable(executionId, "reason");
        System.out.println("请假天数：" + days + ",请假日期" + date + ",请假原因：" + reason);
        System.out.println("-------------------------------");

        System.out.println("根据Javabean获取流程变量");
        User user = (User) runtimeService.getVariable(executionId, "user");
        System.out.println("请假对象：" + user.getId() + "," + user.getName());
    }


    /**
     * 在启动流程的时候，就设置流程变量。
     */
    @Test
    public void RuntimeServiceStart() {
        User user = new User();
        user.setId("3");
        user.setName("小伙子");
        Map<String, Object> variable = new HashMap<>();
        variable.put("days", 66);
        variable.put("date", new Date());
        variable.put("reason", "回家过年");
        variable.put("user", user);
//        获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testvariable", variable);
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessDefinitionId());
    }


//    在完成任务的时候设置流程变量，这个借口比较常用，比如说像请假的第一个接口，我们一般会在申请任务结束时设置申请的内容。

    @Test
    public void completeTasksVariable() {
        User user = new User();
        user.setId("1");
        user.setName("zhangsan");
        Map<String, Object> variable = new HashMap<>();
        variable.put("days", 223);
        variable.put("date", new Date());
        variable.put("reason", "出去郊游");
        variable.put("user", user);
        processEngine.getTaskService().complete("35005", variable);
    }

    //    查看历史流程变量
    @Test
    public void findHistoryProcessVariable() {
        List<HistoricVariableInstance> list =
                processEngine.getHistoryService().createHistoricVariableInstanceQuery().variableName("请假天数").list();

        if (list != null && list.size() > 0) {
            for (HistoricVariableInstance historicVariableInstance : list) {
                System.out.println("流程变量名称：" + historicVariableInstance.getVariableName() + ", 流程变量值：" + historicVariableInstance.getValue());
            }
        }
    }


//    接下来需要了解一下流程变量所需要支持的数据类型。
    /**
     * string ,integer,short,long,double,boolean,date,binary,serializable
     */

    /**
     * 流程变量的总结：
     * 流程变量：在流程执行或者任务执行的过程中，用于设置和获取变量，使用流程变量在流程传递的过程中传递业务参数！
     * 数据库中对应的有：act_ru_variable 和 act_ru_varinst
     *
     * setVariable和setVariableLocal的区别。
     *  setVariable 在设置流程变量的时候，流程变量名称相同的时候，后一次的值会替换前一次的值。
     *  而且可以看到task_id 的字段不会存储任务id
     *
     *
     * 接下来再来说：setVariableLocal :设置流程变量的时候，针对当前活动的节点设置流程变量，如果一个流程中存在2个活动节点。
     * 对每个活动节点都设置流程变量，及时流程变量的名称相同，后一次的版本也不会替换前一个版本的值，
     * 因为他会使用不同的任务id作为标识，存放2个流程变量值，而且可以看出task_id 会存放任务id的值。
     *
     * 使用setVariableLocal说明流程变量绑定了当前的任务，当流程继续执行时，下个任务获取不到这个流程变量（因为正在执行的流程变量中没有这个数据）
     * 所有查询正在执行的任务时不能查询到我们需要的数据，此时需要查询历史的流程变量。
     */


    /**
     * 流程执行历史记录，
     *
     */

    /**
     * 查询历史流程实例
     */
//   查找按照某个流程定义的规则一共执行了多少次流程。
    @Test
    public void getHistoryProcessInstance() {
//        获取历史流程实例，返回历史流程实例的集合
        List<HistoricProcessInstance> list = processEngine.getHistoryService().createHistoricProcessInstanceQuery().processDefinitionKey("myProcess_1").orderByProcessInstanceStartTime().asc().list();
//        遍历查看结果集
        for (HistoricProcessInstance historicProcessInstance : list) {
            System.out.println(historicProcessInstance.getId());
            System.out.println(historicProcessInstance.getProcessDefinitionId());
            System.out.println(historicProcessInstance.getStartTime());
            System.out.println(historicProcessInstance.getEndTime());
            System.out.println(historicProcessInstance.getDurationInMillis());
        }
    }


//    查询历史活动：某一次流程的执行一共经历了多少个活动

    /**
     * 查看某一次流程的执行经历的多少步
     */
    @Test
    public void queryHistoryActivityInstance() {
        String processInstanceId = "35001";  //act_ru_execution 表里面的id.
        List<HistoricActivityInstance> list =
                processEngine.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceEndTime().asc().list();
        for (HistoricActivityInstance historicActivityInstance : list) {
            System.out.println(historicActivityInstance.getActivityId());
            System.out.println("name：" + historicActivityInstance.getActivityName());
            System.out.println("type：" + historicActivityInstance.getActivityType());
            System.out.println("pid：" + historicActivityInstance.getProcessInstanceId());
            System.out.println("assignee: " + historicActivityInstance.getAssignee());
            System.out.println("startTime; " + historicActivityInstance.getStartTime());
            System.out.println("endTime; " + historicActivityInstance.getEndTime());
            System.out.println("duration: " + historicActivityInstance.getDurationInMillis());
        }
    }

    /**
     * 查询历史任务
     */

//    某一次流程的执行一共经历了多少个任务
    @Test
    public void queryHistoricTask() {
        String processInstanceId = "215c7661-952b-11e8-a96a-107b44a45166";
        List<HistoricTaskInstance> list =
                processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceStartTime().asc().list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("taskId: " + historicTaskInstance.getId());
            System.out.println("name: " + historicTaskInstance.getName());
            System.out.println("pdid: " + historicTaskInstance.getProcessDefinitionId());
            System.out.println("pid: " + historicTaskInstance.getProcessInstanceId());
            System.out.println("assignee: " + historicTaskInstance.getAssignee());
            System.out.println("startTime: " + historicTaskInstance.getStartTime());
            System.out.println("endTime：" + historicTaskInstance.getEndTime());
            System.out.println("duration: " + historicTaskInstance.getDurationInMillis());
        }
    }

    /**
     * 查询历史流程变量
     */
    @Test
    public void queryHistoricVariables() {
        String processInstanceId = "215c7661-952b-11e8-a96a-107b44a45166";
        List<HistoricVariableInstance> list = processEngine.getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).orderByVariableName().asc().list();
        if (list != null && list.size() > 0) {
            for (HistoricVariableInstance historicVariableInstance : list) {
                System.out.println("pid: " + historicVariableInstance.getProcessInstanceId());
                System.out.println("variableName: " + historicVariableInstance.getVariableName());
                System.out.println("variablevalue: " + historicVariableInstance.getValue());
            }
        }
    }
}


