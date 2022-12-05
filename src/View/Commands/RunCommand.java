package View.Commands;

import Controller.Controller;

public class RunCommand extends Command {
    private final Controller controller;

    public RunCommand(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            if (controller.stepByStep) {
                controller.stepByStepExecution();
            } else {
                controller.executeAllSteps();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Result: " + controller.getRepository().getCurrentProgramState().outToString());
    }
}