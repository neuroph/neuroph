package org.neuroph.training;

/**
 *
 * @author zoran
 */
public class IfTask extends Task {
    BooleanExpression condition;
    String jumpToTaskName;
    

    public IfTask(String name) {
        super(name);
    }

    public IfTask(BooleanExpression condition, String jumpToTaskName) {
        super("IfTask");
        this.condition = condition;
        this.jumpToTaskName = jumpToTaskName;
    }    
    
    
    @Override
    public void execute() {
        if (condition.isTrue())
            parentProcess.jumpToTask(jumpToTaskName);
    }
    
}
