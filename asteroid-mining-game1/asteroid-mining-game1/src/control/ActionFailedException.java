package control;

public class ActionFailedException extends Exception {
    public ActionFailedException(String errorMessage) {
        super(errorMessage);
    }
}
