package fh.server.helpers;

import fh.server.entity.Page;
import fh.server.entity.widget.Widget;
import fh.server.entity.widget.WidgetComponent;

public class Operation extends Context {

    private final String operation;
    private final Object value;
    private final Object previous;

    protected Operation(Context context, String operation, Object value, Object previous) {
        super(context);
        this.operation = operation;
        this.value = value;
        this.previous = previous;
    }

    public String getOperation() {
        return operation;
    }

    public String getValue() {
        return value.toString();
    }

    public String getPrevious() {
        return previous.toString();
    }

    public Integer valueAsInteger() {
        return (Integer) value;
    }

    public Page valueAsPage() {
        return (Page) value;
    }

    public Widget valueAsWidget() {
        return (Widget) value;
    }

    public WidgetComponent valueAsComponent() {
        return (WidgetComponent) value;
    }

}
