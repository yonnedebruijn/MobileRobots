package state;

import view.LoggingPanel;

/**
 * Created by Yonne on 05/12/13.
 */
public class JungleException extends Exception {

    /**
	 * 	Eclipse <3 serialversionuid
	 */
	private static final long serialVersionUID = 578842235930241374L;
	int pool_size;
    int group_id;

    public JungleException(LoggingPanel debug, int pool_size, int group_id) {
        this.pool_size = pool_size;
        this.group_id = group_id;
        debug.write_error("ERROR: JungleException: " + "Pool Size: " + pool_size + "Group ID: " + group_id);
    }
}
