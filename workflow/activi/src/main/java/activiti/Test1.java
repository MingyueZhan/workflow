package activiti;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/8/2
 * @since : 1.5
 */
public class Test1 {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    @Test
    public void deploy(){
        Deployment fenahi_ = processEngine.getRepositoryService().createDeployment().name("fenahi ").addClasspathResource("test1.bpmn").addClasspathResource("test1.png").deploy();
        System.out.println(fenahi_.getId());
    }
    @Test
    public void start(){
        Map<String,Object> map = new HashMap<>();
        map.put("pass",6);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("test1",map);
        System.out.println(processInstance.getId());
    }

}
