package org.dbutils.db.converter;

import android.database.Cursor;

import org.dbutils.db.sqlite.ColumnDbType;

/**
 * 字段转化
 * @param <T>
 */
public interface ColumnConverter<T> {

    T getFieldValue(final Cursor cursor, int index);

    Object fieldValue2DbValue(T fieldValue);

    ColumnDbType getColumnDbType();
}
