package fu.mi.fitting.controllers;


import javafx.stage.Stage;

/**
 * Created by shang on 5/1/2016.
 * This is a singleton class, put all controllers here,
 * they can communicate.
 */
public class Controllers {
    private static final Controllers INSTANCE = new Controllers();
    public MainController mainController;
    public ParameterController parameterController;
    public ChartsController chartsController;
    public SampleController sampleController;
    public ConfigurationController confController;
    public MenuController menuController;
    public Stage stage;

    private Controllers() {
    }

    public static Controllers getInstance() {
        return INSTANCE;
    }
}
