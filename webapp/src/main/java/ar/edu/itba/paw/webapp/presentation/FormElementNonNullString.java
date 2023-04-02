package ar.edu.itba.paw.webapp.presentation;

public class FormElementNonNullString extends FormElementImpl{

    public FormElementNonNullString(String label, String inputName) {
        super(label, "text", inputName, "", null);
    }

    @Override
    public boolean isValidInput(String input) {
        return input != null;
    }
}