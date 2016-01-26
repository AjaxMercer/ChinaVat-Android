package org.netutils.ex;

import org.dbutils.db.ex.BaseException;

public class FileLockedException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileLockedException(String detailMessage) {
        super(detailMessage);
    }
}
