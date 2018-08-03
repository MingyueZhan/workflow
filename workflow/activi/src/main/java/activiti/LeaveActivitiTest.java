package activiti;

import bean.LeaveBill;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/7/26
 * @since : 1.5
 *
 * ProcessDefination  解析bpmn文件，得到流程定义的规则信息，工作流系统按照流程定义的规则执行的。
 * ProcessInstance：流程实例包括了所有的运行节点，可以利用此对象来了解当前流程实例的进度信息。
 */
public class LeaveActivitiTest {
    //最简单的获取工作流引擎的方式。  此方法默认会自动加载classpath路径下的名为activiti.cfg.xml文件。
    //    第一步创建工作流引擎  初始化工作流引擎。
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();     //ProcessEngines 是线程安全的。

    //  获取processEngine对象的方式1。（通过手动配置的方式）
    @Test
    public void testCreateProcessEngine() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        cfg.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti_1110");
        cfg.setJdbcDriver("com.mysql.jdbc.Driver");
        cfg.setJdbcUsername("root");
        cfg.setJdbcPassword("1234");
//        配置建表策略
        cfg.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine engine = cfg.buildProcessEngine();
    }

    //    根据配置文件的方式创建ProcessEngine  （通过配置文件的方式）
    @Test
    public void testCreatedProcessEngineByCfgXml() {
        ProcessEngineConfiguration xmlcfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
//        获取流程引擎对象。
        ProcessEngine processEngine = xmlcfg.buildProcessEngine();
    }

    /**
     * 流程部署方式1  ：首先需要将我们的业务画成图，然后利用工作流引擎将我们的业务图信息放到数据库中。

     * 要注意的是部署可以包含多个bpmn文件和任何的其他资源。
     * 一个process definition 定义了流程的定义了流程的不同步骤的结构和行为。。流程定义是流程定义的一次执行。
     *
     * 对于每个流程定义，通常会有多个实例同时运行。

     * 这里面最重要的方法是从classpath路径下读取流程定义文件。要注意的是一次只能加载一个文件。
     *
     * 使用到的方法。 addClasspathResource(资源文件名);
     */
    @Test
    public void deploy1Process(){
           Deployment deployment = processEngine.getRepositoryService().createDeployment().name("leaveBill").addClasspathResource("leave.bpmn").addClasspathResource("leave.png").deploy();
            System.out.println(deployment.getId());
             System.out.println(deployment.getName());
    }

    /**
     * 部署流程定义2；这种方法是通过读取zip文件的方式来创建的。
     */
    @Test
    public void deployProcessByZip(){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("resources.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = processEngine.getRepositoryService()  //第一步获取与部署流程和定义流程相关的service。
                .createDeployment()//接下来创建部署对象。
                .name("流程定义")//给流程定义取名字。
                .addZipInputStream(zipInputStream)//以指定的zip格式的文件来完成部署。
                .deploy();//完成部署。
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
        System.out.println(deployment.getCategory());
        System.out.println(deployment.getDeploymentTime());
        System.out.println(deployment.getKey());
        System.out.println(deployment.getTenantId());
    }

    /**
     * 使用inputStream来部署流程定义。
     */
    @Test
    public void   deployProcessByInputStream(){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("leave.bpmn");
        InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream("leave.png");
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("请假流程").addInputStream("leave.bpmn",inputStream).addInputStream("leave.png",inputStream1).deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }



//    查询流程定义

    /**
     * ProcessDefinition  : 流程定义类，可以获取静态的资源文件，得到流程的相关定义信息。
     */

    @Test
    public void selectProcessDefine(){
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().orderByProcessDefinitionVersion().asc().list();//创建一个流程定义的查询

        if (list!=null&&list.size()>0){
            for (ProcessDefinition processDefinition:list){
//                此时会查询act_re_procdef表的数据
                System.out.println("流程定义id:"+processDefinition.getId());
                System.out.println("流程定义名称:"+processDefinition.getName());//对应的是bpmn文件里面的name属性值。
                System.out.println(processDefinition.getCategory());
                System.out.println(processDefinition.getDeploymentId());
                System.out.println(processDefinition.getDescription());
                System.out.println(processDefinition.getEngineVersion());
                System.out.println(processDefinition.getKey());  //key 的值是bpmn文件里面的process节点的id属性的值。 版本升级的时候，比较的就是这个值。默认版本号加一
                System.out.println(processDefinition.getVersion());

                System.out.println("********************************");
            }
        }
    }

    /**
     * 删除流程定义
     */
    @Test
    public void deleleProcessDefination(){
//        首先要明白的是流程定义的部署是属于仓库服务的，所以还是应该先得到RepositoryService
//        查看源码得知此方法需要一个流程id
        String deploymentId = "1";  // 部署id 在 act_re_deployment 中查找
        processEngine.getRepositoryService().deleteDeployment(deploymentId);
    }

    /**
     *  获取流程定义的文档资源 （此资源可以供用户查看）
     * @throws IOException
     */
    @Test
    public void seeProcess() throws IOException {
//        将生成的图片放到文件夹下
//        先定义部署id
        String deploymentId = "80001";

//       获取图片的资源名称

        String resourceName = "";
        List<String> deploymentResourceNames = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);//查源码可知，此方法需要传入部署id
        if (deploymentResourceNames!=null&&deploymentResourceNames.size()>0){
            for (String resource:deploymentResourceNames){
                if (resource.indexOf(".png")>0){
                    resourceName = resource;
                }
            }
        }
//        获取图片输入流，
        InputStream inputStream = processEngine.getRepositoryService().getResourceAsStream(deploymentId,resourceName);

//        将图片生成到d盘目录下
        File file = new File("e:/"+resourceName);
        FileUtils.copyInputStreamToFile(inputStream,file);
    }

    /**
     * 查看最新版本的流程定义 (这里是多个流程定义)
     */
    @Test
    public void findNewVesionProcessDefination(){
//        算法  第一步：获取所有的流程定义对象
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().orderByProcessDefinitionVersion().asc().list();
//            第二步: 利用map集合的特点：当key相同时，后一次的值将会替换前一次的值。
//        实例化一个map集合
        Map<String,ProcessDefinition> map = new LinkedHashMap<>();
        if(list!=null&&list.size()>0){
            for (ProcessDefinition processDefinition:list){
                    map.put(processDefinition.getKey(),processDefinition);
            }
        }

        List<ProcessDefinition> processDefinitionList = new ArrayList<>();
        if (list!=null&&list.size()>0){
            for (ProcessDefinition processDefinition:processDefinitionList){
                System.out.println("流程定义："+processDefinition.getId());
                System.out.println("流程定义名称："+processDefinition.getName());
            }
        }

    }

    /**
     * 删除key相同的所有不同版本的流程定义.
     */
    public void deleteProcessDefination(){
        String processDefinationKey = "";
//      使用流程定义的key查出所有版本的流程定义
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(processDefinationKey).list();

//        遍历，获取每一个流程定义的部署id;  这个id 是从act_re_deployment里面的id
        if (list!=null&&list.size()>0){
            for (ProcessDefinition processDefinition:list){
//                获取部署id,
                String deploymentId = processDefinition.getDeploymentId();
//                根据部署id来删除定义id
                processEngine.getRepositoryService().deleteDeployment(deploymentId);
            }
        }
    }


//    启动流程定义

    /**
     *      在这里操作的是数据库里面的act_ru_execution表。如果当前操作的是用户节点（userTask节点） 那么同时会在act_ru_task添加一条记录。
     *      如果是单例流程，那么在act_ru_execution表中，id和proc_inst_id 值是相同的。
     *      所谓的单例流程：意思是说流程图里面是没有分支和聚合的。
     *      流程实例只有一个，但执行对象却可以有多个。
     *
     *     ProcessInstance：流程实例包括了所有的运行节点，可以利用此对象来了解当前流程实例的进度信息。
     *
     */
    @Test
    public void startProcessEngine(){
//        获取流程定义的key  这个是在流程定义文件里面的process节点的id值
        String processDefinationKey = "myProcess_1";
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinationKey);
        System.out.println("流程实例id:"+processInstance.getId());   //获取的是act_ru_execution表里面的id.
        System.out.println("流程定义id:"+processInstance.getProcessDefinitionId());   //获取的是act_re_procdef表里面的id;

    }

//    查询个人任务

    /**
     * 查看我的个人任务  人用assignee来指定。
     */
    @Test
    public void findMyTask(){
        String assignee = "fanbingbing";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).list();
        if(list!=null&&list.size()>0){
            for (Task task:list){
//                此时查询的是act_ru_task表
                System.out.println("任务id："+task.getId());
                System.out.println("任务名称："+task.getName());
                System.out.println("任务创建时间："+task.getCreateTime());
                System.out.println("任务实例id："+task.getProcessInstanceId()); //这个id是execution表里面的id.
                System.out.println("任务执行对象id："+task.getExecutionId()); //这个id是execution表里面的proc_inst_id.
                System.out.println("流程定义id："+task.getProcessDefinitionId());//这个是procdef表里面的id.
                System.out.println("--------------------");
            }
        }
    }
//    最后来看办理任务

    /**
     * 在这里相当于提交请假单
     */
public void  finishMyTask(){
//    任务id
    String taskId = "";
    processEngine.getTaskService().complete(taskId);
    System.out.println("完成任务，任务id："+taskId);
}

//查询流程状态  可判断流程是正在进行，还是结束.

    /**
     * 因为是查询流程实例，所以首先要获取的就是runtimeService
     * 创建流程实例查询对象，设置实例id过滤参数。
     * 由于一个流程实例ID只能对应一个流程实例，所以结果使用singleResult返回结果，如果返回结果是大于1的话的将会抛出异常。
     * 判断流程实例是否为空,如果结果为空，则代表流程结束，实例在正在进行的执行对象表中已被删除，转换成历史数据。
     */
    @Test
public void queryProcessState(){
        String processInstanceId = "35001";
    ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    if (processInstance!=null){
        System.out.println("当前流程在："+processInstance.getId());
    }else{
        System.out.println("流程结束");
    }
}

























//    第二步：启动流程
@Test
    public void startProcess(){
//        获取将要被运行的流程
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("myProcess_1");
    }

//    第三步我们来设置流程变量

@Test
    public void setProcessVar(){
        TaskService taskService = processEngine.getTaskService();
        String processInstenceId = "myProcess_1:11:80004";
//        创建任务
        Task task = taskService.createTaskQuery().processDefinitionId(processInstenceId).singleResult();
//        实例化javabean
        LeaveBill leaveBill = new LeaveBill();
        leaveBill.setDay(3);
        leaveBill.setLeavereason("回去寄存档案");
        leaveBill.setLeavestarttime(new Date());
        taskService.setVariable(task.getId(),"请假天数",leaveBill.getDay());
        taskService.setVariable(task.getId(),"请假日期",leaveBill.getLeavestarttime());
        taskService.setVariable(task.getId(),"请假原因",leaveBill.getLeavereason());
        //如何查看流程变量
    }


//    第三步+ 如何获取流程变量。
    @Test
    public void getProcessVariable(){
        String processInstenceId = "myProcess_1:11:80004";
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionId(processInstenceId).singleResult();
//        接下来获取流程变量。
        int day =(int)taskService.getVariable(task.getId(),"请假天数");
        Date startTime = (Date) taskService.getVariable(task.getId(),"请假日期");
        String reason = (String) taskService.getVariable(task.getId(),"请假原因");
        System.out.println(day+"----"+startTime+"-----"+reason);
    }

//    第四步 查看任务 通过。taskService对象里面的list()方法获取委托人所有的task
    @Test
    public void getAllTask(){
        System.out.println("---------查看当前委托人所有的任务");
        TaskService taskService = processEngine.getTaskService();

//        接下来设置代理人
        String assigner = "emp";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assigner).list();
        int size = list.size();
        for (int i=0;i<size;i++){
            Task task = list.get(i);
        }
        for (Task task:list){
            System.out.println(task.getId()+"taskName:"+task.getName()+",Assignment"+task.getAssignee());
        }

    }


//    第五步 完成任务
    public void handlerTask(){
        TaskService taskService = processEngine.getTaskService();
        String taskId = "35005";
        taskService.complete(taskId);

    }




//    第二步我们需要的是设置流程变量

    /**
     * 启动流程   用于申请任务  流程创建的时候添加act_ru_execution中的businiess_key   并且是需要在创建流程的时候来创建这个属性的值得。
     * RuntimeService    用来检索和存储服务流程变量的。这是特定于给定流程实例的数据。 说到底，Runtimeservice事实上就相当于是之指向流程实例的当前位置的指针。
     * @param
     */
    @Test
    public void setProcessVariable(){
//        获取正在运行的流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        获取正在执行的任务。
        TaskService taskService = processEngine.getTaskService();
//        接下来可以设置流程变量了
//        先设置流程id
        String processInstenceId = "myProcess_1:10:32504";
        String assignee = "组长";
//        创建任务
        Task task = taskService.createTaskQuery().processDefinitionId(processInstenceId).singleResult();
//        实例化Javabean
            LeaveBill leaveBill = new LeaveBill();
        leaveBill.setDay(3);
        leaveBill.setLeavereason("回去寄存档案");
        leaveBill.setLeavestarttime(new Date());
//        接下来设置流程变量
        taskService.setVariable(task.getId(),"请假天数",leaveBill.getDay());
        taskService.setVariable(task.getId(),"请假日期",leaveBill.getLeavestarttime());
        taskService.setVariable(task.getId(),"请假原因",leaveBill.getLeavereason());
//        第二种: 使用javabean 但是这个javabean必须要实现序列化
//        runtimeService.startProcessInstanceByKey("流程定义");
    }




    @Test
    public void  start(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("myProcess_1");
    }

    /**
     * 查看任务
     * TaskService
     */
    @Test
    public void queryTask(){
        TaskService taskService = processEngine.getTaskService();
//        这一块是比较重要的。assigne 是代理人
        String assigne = "emp";
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assigne).list();
        int size = tasks.size();
        for (int i=0; i<size;i++){
            Task task = tasks.get(i);
        }
        for (Task task:tasks){
            System.out.println("taskId:"+task.getId()+",taskName:"+task.getName()+",assignee:"+task.getAssignee()+",CreateTime:"+task.getCreateTime());
            System.out.println(task.getId());
        }
    }


    /**
     * 办理任务
     * TaskService 的完成任务的方法。complete（）；
     */
    @Test
    public void handlerTask1(){
            TaskService taskService = processEngine.getTaskService();
//            根据上一步生成的taskId执行任务
            String taskId = "15005";
            taskService.complete(taskId);
    }


}
