package state;

public class NetworkResults {
    double x_axis;
    double y_axis;
    int group_id;
    int received_command;

    NetworkResults(double ls, double rs, int group_id, int rc) {
        this.x_axis = ls;
        this.y_axis = rs;
        this.group_id = group_id;
        this.received_command = rc;
    }

    NetworkResults() {
        this.x_axis = 0;
        this.y_axis = 0;
        this.group_id = 0;
        this.received_command = 0;
    }

    public void print()
    {
    	System.out.println("x: " + x_axis + "- y: " + y_axis + " - group: " + group_id + " - received: " + received_command);
    }
}
