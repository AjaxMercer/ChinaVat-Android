package bw.com.app.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by zhaoyechao on 2016/1/20.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater inflater;
    protected Context context;
    protected List<T> datas;
    protected int itemLayoutId;

    public CommonAdapter(LayoutInflater inflater, Context context, List<T> datas, int itemLayoutId) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.datas = datas;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder commonViewHolder = getViewHolder(position, convertView, parent);
        convert(commonViewHolder, getItem(position), position);
        return commonViewHolder.getConvertView();
    }

    public abstract void convert(CommonViewHolder viewHolder, T item, int position);


    private CommonViewHolder getViewHolder(int position, View convertView,
                                           ViewGroup parent) {
        return CommonViewHolder.get(context, convertView, parent, itemLayoutId, position);
    }
}
