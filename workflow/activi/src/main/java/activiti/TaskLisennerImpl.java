package activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/7/30
 * @since : 1.5
 *
 *
 * 本类用来指定个人任务的办理人。
 */
public class TaskLisennerImpl implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
//        通过类来查询数据库，将下一个任务的办理人查询获取
//        根据业务查询，比如查询到了。。。
        delegateTask.setAssignee("小林子");



    }
}
