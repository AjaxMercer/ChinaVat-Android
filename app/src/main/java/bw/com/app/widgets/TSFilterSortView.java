package bw.com.app.widgets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import bw.com.app.R;
import bw.com.app.model.bean.InvoiceItem;

/**
 * Created by codingsky on 15/12/22.
 */
public class TSFilterSortView extends RelativeLayout {
    private static final String TAG = "TSFilterView";
    private static final String NORMAL_INVOICE = "380";
    private static final String RED_INVOICE = "381";

    static final int LIST_VIEW_FIRST_ITEM_INDEX = 0;
    static final int FILTER_SELECTED_NOW = 0;
    static final int SORT_SELECTED_NOW = 1;
    static final int TIME_SORT_SELECTED_NOW = 2;
    static final int MONEY_AMOUNT_SORT_SELECTED_NOW = 3;
    static final int CLEAR_FILTER_SORT_NOW = 4;

    static final int ALPHABET_UP_SORT = 0;
    static final int ALPHABET_DOWN_SORT = 1;

    static final int MORE_SORT_UNLIMITED = 0;
    static final int MORE_SORT_DEAL_TYPE = 1;
    static final int MORE_SORT_DEAL_ADDRESS = 2;
    static final int MORE_SORT_INVOICE_STATUS = 3;
    static final int MORE_SORT_PHONE_NUMBER = 4;

    static final int FILTER_DEAL_DATE_INDEX = 0;
    static final int FILTER_DEAL_TYPE_INDEX = 1;
    static final int FILTER_PHONE_INDEX = 2;
    static final int FILTER_DEAL_ADDRESS_INDEX = 3;
    static final int FILTER_INVOICE_STATUS_INDEX = 4;


    static List<FilterItem> dealTypeList = new ArrayList<>();
    static List<FilterItem> phoneList = new ArrayList<>();
    static List<FilterItem> dealAddressList = new ArrayList<>();
    static List<FilterItem> invoiceStatusList = new ArrayList<>();
    static List<String> filterTargetsList = new ArrayList<>();
    static List<InvoiceItem> filterSortResultList = new ArrayList<>();
    static int sortWaysIndexSelected = 0;
    static int sortWaysSubIndexSelected = 0;

    private Context mContext;
    private Button cancelBtn, emptyBtn, sureBtn;
    private TextView filterTv, timeSortTv, moneySortTv, moreSortTv;
    private TextView dealDateTv, dealTypeTv, invoiceTypeTv, dealAddTv, phoneTv;
    private TextView dealDateStartTv, dealDateEndTv;
    private TextView alphaDownTv, alphaUpTv;
    private boolean firstInit = true;

    private ImageView unlimitedIv, dealTypeIv, invoiceStatusIv, dealAddressIv, telPhoneIv;
    private RelativeLayout sortUnlimitedRl, sortDealTypeRl, sortDealAddRl, sortInvoiceStatusRl, sortPhoneRl;

    private ListView subFilterLv;
    private Adapter mAdapter;
    private RelativeLayout homeRl, menuRl;
    private LinearLayout tabLl, filterLl, dealDateLl, sortLl;
    private View lastSelectedFilterView, lastSelectedSortView;
    private int lastSelectedFilterViewIndex = FILTER_DEAL_DATE_INDEX;
    private OnSureClickListener onSureClickListener;

    //how to convert field when sorting
    private static final int STRING2LONG = 0;
    private static final int STRING2INTEGER = 1;
    private static final int STRING2DATE = 2;
    private static final int FLOAT2FLOAT = 3;

    public TSFilterSortView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public void resetTabViewDisplay() {
        filterTv.setText(mContext.getString(R.string.filter));
        timeSortTv.setText(mContext.getString(R.string.time_sort));
        moneySortTv.setText(mContext.getString(R.string.money_sort));
        moreSortTv.setText(mContext.getString(R.string.more_sort));

        filterTv.setSelected(false);
        timeSortTv.setSelected(false);
        moneySortTv.setSelected(false);
        moreSortTv.setSelected(false);
        dealDateStartTv.setSelected(false);
        dealDateEndTv.setSelected(false);

        firstInit = true;
    }

    public void setOnSureClickListener(OnSureClickListener listener) {
        this.onSureClickListener = listener;
    }

    private void initFilterList() {
        dealTypeList.clear();
        phoneList.clear();
        dealAddressList.clear();
        invoiceStatusList.clear();

        FilterItem dealTypeItem = new FilterItem();
        dealTypeItem.value = getResources().getString(R.string.all_filter_sort);
        dealTypeItem.isSelected = true;
        dealTypeList.add(dealTypeItem);
        FilterItem phoneItem = new FilterItem();
        phoneItem.value = getResources().getString(R.string.all_filter_sort);
        phoneItem.isSelected = true;
        phoneList.add(phoneItem);
        FilterItem invoiceStatusItem = new FilterItem();
        invoiceStatusItem.value = getResources().getString(R.string.all_filter_sort);
        invoiceStatusItem.isSelected = true;
        invoiceStatusList.add(invoiceStatusItem);
        FilterItem dealAddressItem = new FilterItem();
        dealAddressItem.value = getResources().getString(R.string.all_filter_sort);
        dealAddressItem.isSelected = true;
        dealAddressList.add(dealAddressItem);


        FilterItem sampleAddress1 = new FilterItem();
        FilterItem sampleAddress2 = new FilterItem();
        sampleAddress1.value = getResources().getString(R.string.filter_address_1);
        sampleAddress2.value = getResources().getString(R.string.filter_address_2);
        dealAddressList.add(sampleAddress1);
        dealAddressList.add(sampleAddress2);

        FilterItem sampleStatus1 = new FilterItem();
        FilterItem sampleStatus2 = new FilterItem();
        sampleStatus1.value = getResources().getString(R.string.filter_status_1);
        sampleStatus2.value = getResources().getString(R.string.filter_status_2);


        invoiceStatusList.add(sampleStatus1);
        invoiceStatusList.add(sampleStatus2);

//        for (InvoiceItem a : ApiGetInvoiceList.documentList) {
//            FilterItem itemDealType = new FilterItem();
//            FilterItem itemPhone = new FilterItem();
//            FilterItem itemInvoiceStatus = new FilterItem();
//            FilterItem itemDealAddress = new FilterItem();
//
//            itemDealType.value = a.invoiceType;
//            itemDealType.isSelected = false;
//
//            itemPhone.value = a.buyerPhone;
//            itemPhone.isSelected = false;
//
////            itemInvoiceStatus = a.
//
//            if (isNotInclude(itemDealType.value, dealTypeList)) dealTypeList.add(itemDealType);
//            if (isNotInclude(itemPhone.value, phoneList)) phoneList.add(itemPhone);
//        }


    }

    private boolean isNotInclude(String value, List<FilterItem> list) {
        for (FilterItem filterItem : list) {
            if (filterItem.value.equals(value)) return false;
        }
        return true;
    }


    private void changeFilterListValue(int index, int position, boolean value) {
        switch (index) {
            case FILTER_DEAL_TYPE_INDEX:
                dealTypeList.get(position).isSelected = value;
                break;
            case FILTER_PHONE_INDEX:
                phoneList.get(position).isSelected = value;
                break;
            case FILTER_INVOICE_STATUS_INDEX:
                invoiceStatusList.get(position).isSelected = value;
                break;
            case FILTER_DEAL_ADDRESS_INDEX:
                dealAddressList.get(position).isSelected = value;
                break;
        }

    }

    private int getFilterListSelectedCount(int filterIndex) {
        int count = 0;
        switch (filterIndex) {
            case FILTER_DEAL_TYPE_INDEX:
                for (FilterItem item : dealTypeList) {
                    if (item.isSelected) count++;
                    if (count > 0) break;
                }
                break;
            case FILTER_PHONE_INDEX:
                for (FilterItem item : phoneList) {
                    if (item.isSelected) count++;
                    if (count > 0) break;
                }
                break;
            case FILTER_INVOICE_STATUS_INDEX:
                for (FilterItem item : invoiceStatusList) {
                    if (item.isSelected) count++;
                    if (count > 0) break;
                }
                break;
            case FILTER_DEAL_ADDRESS_INDEX:
                for (FilterItem item : dealAddressList) {
                    if (item.isSelected) count++;
                    if (count > 0) break;
                }
                break;
        }
        return count;
    }

    private void setFilterTargetsList() {

        filterTargetsList.clear();

        if (!dealTypeList.get(0).isSelected) {
            for (FilterItem item : dealTypeList) {
                if (item.isSelected) {
                    filterTargetsList.add(item.value);
                }
            }
        }

        if (!phoneList.get(0).isSelected) {
            for (FilterItem item : phoneList) {
                if (item.isSelected) {
                    filterTargetsList.add(item.value);
                }
            }
        }

    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.filter_tv:
                    view.setSelected(true);
                    tabLl.setVisibility(GONE);
                    menuRl.setVisibility(View.VISIBLE);
                    filterLl.setVisibility(VISIBLE);
                    sortLl.setVisibility(GONE);
                    if (firstInit) {
                        initFilterList();
                        firstInit = false;
                    }
                    updateFilterView(lastSelectedFilterViewIndex);
                    break;
                case R.id.time_sort_tv:
                    view.setSelected(true);
                    if (timeSortTv.getText().toString().equals(getContext().getString(R.string.time_sort)) || timeSortTv.getText().toString().equals(getContext().getString(R.string.time_sort_2)))
                        timeSortTv.setText(getContext().getString(R.string.time_sort_1));
                    else timeSortTv.setText(getContext().getString(R.string.time_sort_2));
                    moneySortTv.setSelected(false);
                    moneySortTv.setText(getContext().getString(R.string.money_sort));
                    moreSortTv.setSelected(false);
//                    getResultList(TIME_SORT_SELECTED_NOW);
                    break;
                case R.id.money_sort_tv:
                    view.setSelected(true);
                    if (moneySortTv.getText().toString().equals(getContext().getString(R.string.money_sort)) || moneySortTv.getText().toString().equals(getContext().getString(R.string.money_sort_2)))
                        moneySortTv.setText(getContext().getString(R.string.money_sort_1));
                    else moneySortTv.setText(getContext().getString(R.string.money_sort_2));
                    timeSortTv.setSelected(false);
                    timeSortTv.setText(getContext().getString(R.string.time_sort));
                    moreSortTv.setSelected(false);
//                    getResultList(MONEY_AMOUNT_SORT_SELECTED_NOW);
                    break;
                case R.id.more_sort_tv:
                    view.setSelected(true);
                    moneySortTv.setSelected(false);
                    moneySortTv.setText(getContext().getString(R.string.money_sort));
                    timeSortTv.setSelected(false);
                    timeSortTv.setText(getContext().getString(R.string.time_sort));
                    tabLl.setVisibility(GONE);
                    menuRl.setVisibility(View.VISIBLE);
                    filterLl.setVisibility(GONE);
                    sortLl.setVisibility(VISIBLE);
                    break;
                case R.id.filter_cancel_btn:
                case R.id.home_rl:
                    tabLl.setVisibility(VISIBLE);
                    menuRl.setVisibility(GONE);
                    break;
                case R.id.filter_sort_ok_btn:
                    tabLl.setVisibility(VISIBLE);
                    menuRl.setVisibility(GONE);
//                    if (filterLl.getVisibility() == VISIBLE) {
//                        if (noFilterSelected()) getResultList(CLEAR_FILTER_SORT_NOW);
//                        else getResultList(FILTER_SELECTED_NOW);
//                    } else getResultList(SORT_SELECTED_NOW);
//                    break;
                case R.id.clear_filter_sort_btn:
                    if (filterLl.getVisibility() == VISIBLE) {
                        initFilterList();
//                        getResultList(CLEAR_FILTER_SORT_NOW);
                        updateFilterView(lastSelectedFilterViewIndex);
                    } else {
                        updateMoreSortViewDisplay(MORE_SORT_UNLIMITED);
                    }
                    break;
            }

        }
    };

    private void updateFilterView(int lastSelectedFilterViewIndex) {
        switch (lastSelectedFilterViewIndex) {
            case FILTER_DEAL_DATE_INDEX:
                dealDateLl.setVisibility(VISIBLE);
                subFilterLv.setVisibility(GONE);
                break;
            case FILTER_DEAL_TYPE_INDEX:
                dealDateLl.setVisibility(GONE);
                subFilterLv.setVisibility(VISIBLE);
                mAdapter.swapItems(dealTypeList, FILTER_DEAL_TYPE_INDEX);
                break;
            case FILTER_PHONE_INDEX:
                dealDateLl.setVisibility(GONE);
                subFilterLv.setVisibility(VISIBLE);
                mAdapter.swapItems(phoneList, FILTER_PHONE_INDEX);
                break;
            case FILTER_DEAL_ADDRESS_INDEX:
                dealDateLl.setVisibility(GONE);
                subFilterLv.setVisibility(VISIBLE);
                mAdapter.swapItems(dealAddressList, FILTER_DEAL_ADDRESS_INDEX);
                break;
            case FILTER_INVOICE_STATUS_INDEX:
                dealDateLl.setVisibility(GONE);
                subFilterLv.setVisibility(VISIBLE);
                mAdapter.swapItems(invoiceStatusList, FILTER_INVOICE_STATUS_INDEX);
                break;
        }


    }

    private boolean noFilterSelected() {
        return dealTypeList.get(0).isSelected && phoneList.get(0).isSelected && !dealDateStartTv.isSelected() && dealDateEndTv.isSelected();
    }

    private boolean notInclude(InvoiceItem item) {
        for (int i = 0; i < filterSortResultList.size(); i++) {
            if (filterSortResultList.get(i).equals(item))
                return false;
        }
        return true;
    }

//    private void getResultList(int index) {
//        if (index == FILTER_SELECTED_NOW) {
//            filterSortResultList.clear();
//            setFilterTargetsList();
//            InvoiceItem item;
//            for (int i = 0; i < ApiGetInvoiceList.documentList.size(); i++) {
//                item = ApiGetInvoiceList.documentList.get(i);
//                for (String s : filterTargetsList) {
//                    if (item.isInclude(s)) {
//                        if (notInclude(item)) filterSortResultList.add(item);
//                    }
//                }
//            }
//
//            if (dealDateStartTv.isSelected() && dealDateEndTv.isSelected()) {
//                if (filterTargetsList.size() == 0)
//                    filterSortResultList.addAll(ApiGetInvoiceList.documentList);
//                List<InvoiceItem> tmpList = new ArrayList<>();
//                try {
//                    Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(dealDateStartTv.getText().toString());
//                    Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(dealDateEndTv.getText().toString());
//                    for (int i = 0; i < filterSortResultList.size(); i++) {
//                        Date dest = new SimpleDateFormat("yyyy-MM-dd").parse(filterSortResultList.get(i).Created);
//                        if (dest.after(startDate) && dest.before(endDate)) {
//                            tmpList.add(filterSortResultList.get(i));
//                        }
//
//                    }
//                    filterSortResultList.clear();
//                    filterSortResultList.addAll(tmpList);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (timeSortTv.isSelected()) getResultList(TIME_SORT_SELECTED_NOW);
//            else if (moneySortTv.isSelected()) getResultList(MONEY_AMOUNT_SORT_SELECTED_NOW);
//            else if (moreSortTv.isSelected()) getResultList(SORT_SELECTED_NOW);
//        } else if (index == SORT_SELECTED_NOW) {
//            if (filterSortResultList.size() == 0) {
//                filterSortResultList.addAll(ApiGetInvoiceList.documentList);
//            }
//            if (sortWaysIndexSelected == ALPHABET_UP_SORT) {
//
//                switch (sortWaysSubIndexSelected) {
//                    case MORE_SORT_DEAL_TYPE:
//                        sortInvoiceItems(filterSortResultList, "invoiceType", STRING2INTEGER, false);
//                        break;
//                    case MORE_SORT_PHONE_NUMBER:
//                        sortInvoiceItems(filterSortResultList, "buyerPhone", STRING2LONG, false);
//                        break;
//                    case MORE_SORT_DEAL_ADDRESS:
//
//                        break;
//                    case MORE_SORT_INVOICE_STATUS:
//
//                        break;
//                }
//
//            } else if (sortWaysIndexSelected == ALPHABET_DOWN_SORT) {
//                switch (sortWaysSubIndexSelected) {
//                    case MORE_SORT_DEAL_TYPE:
//                        sortInvoiceItems(filterSortResultList, "invoiceType", STRING2INTEGER, true);
//                        break;
//                    case MORE_SORT_PHONE_NUMBER:
//                        sortInvoiceItems(filterSortResultList, "buyerPhone", STRING2LONG, true);
//                        break;
//                    case MORE_SORT_DEAL_ADDRESS:
//
//                        break;
//                    case MORE_SORT_INVOICE_STATUS:
//
//                        break;
//                }
//
//            }
//
//        } else if (index == MONEY_AMOUNT_SORT_SELECTED_NOW) {
//            if (filterSortResultList.size() == 0) {
//                filterSortResultList.addAll(ApiGetInvoiceList.documentList);
//            }
//            if (moneySortTv.getText().toString().equals(mContext.getString(R.string.money_sort_1))) {
//                sortInvoiceItems(filterSortResultList, "totalIncTax", FLOAT2FLOAT, false);
//            } else {
//                sortInvoiceItems(filterSortResultList, "totalIncTax", FLOAT2FLOAT, true);
//            }
//        } else if (index == TIME_SORT_SELECTED_NOW) {
//            if (filterSortResultList.size() == 0) {
//                filterSortResultList.addAll(ApiGetInvoiceList.documentList);
//            }
//            if (timeSortTv.getText().toString().equals(mContext.getString(R.string.time_sort_1))) {
//                sortInvoiceItems(filterSortResultList, "Created", STRING2DATE, false);
//            } else {
//                sortInvoiceItems(filterSortResultList, "Created", STRING2DATE, true);
//            }
//        } else if (index == CLEAR_FILTER_SORT_NOW) {
//            dealDateStartTv.setSelected(false);
//            dealDateEndTv.setSelected(false);
//            filterSortResultList.clear();
//            filterSortResultList.addAll(ApiGetInvoiceList.documentList);
//        }
//        onSureClickListener.onclick(filterSortResultList);
//    }


    //sort the invoiceitem list according to the designated property of invoiceItem
    private static void sortInvoiceItems(List<InvoiceItem> invoiceItems, String porperty, int convertType, boolean isASC) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        InvoiceItem swap;
        Field field;
        try {
            field = InvoiceItem.class.getDeclaredField(porperty);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }
        for (int i = 0; i < invoiceItems.size() - 1; i++) {
            boolean isSorted = true;
            for (int j = 1; j < invoiceItems.size() - i; j++) {
                boolean needSwap = false;
                try {
                    switch (convertType) {
                        case STRING2INTEGER:
                            needSwap = Integer.valueOf((String) field.get(invoiceItems.get(j - 1))) > Integer.valueOf((String) field.get
                                    (invoiceItems.get(j)));
                            break;
                        case STRING2LONG:
                            needSwap = Long.valueOf((String) field.get(invoiceItems.get(j - 1))) > Long.valueOf((String) field.get(invoiceItems.get
                                    (j)));
                            break;
                        case STRING2DATE:
                            needSwap = formater.parse((String) field.get(invoiceItems.get(j - 1))).after(formater.parse((String) field.get
                                    (invoiceItems.get(j))));
                            break;
                        case FLOAT2FLOAT:
                            needSwap = (float) field.get(invoiceItems.get(j - 1)) > (float) field.get(invoiceItems.get(j));
                            break;
                        default:
                            break;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
                needSwap = isASC ? needSwap : !needSwap;
                if (needSwap) {
                    swap = invoiceItems.get(j - 1);
                    invoiceItems.set(j - 1, invoiceItems.get(j));
                    invoiceItems.set(j, swap);
                    isSorted = false;
                }
            }
            if (isSorted) break;
        }
    }

    private OnClickListener mFilterListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            lastSelectedFilterView.setSelected(false);
            view.setSelected(true);
            lastSelectedFilterView = view;
            switch (view.getId()) {
                case R.id.filter_deal_date_tv:
                    dealDateLl.setVisibility(VISIBLE);
                    subFilterLv.setVisibility(GONE);
                    lastSelectedFilterViewIndex = FILTER_DEAL_DATE_INDEX;
                    break;
                case R.id.filter_deal_type_tv:
                    dealDateLl.setVisibility(GONE);
                    subFilterLv.setVisibility(VISIBLE);
                    mAdapter.swapItems(dealTypeList, FILTER_DEAL_TYPE_INDEX);
                    lastSelectedFilterViewIndex = FILTER_DEAL_TYPE_INDEX;
                    break;
                case R.id.filter_phone_type_tv:
                    dealDateLl.setVisibility(GONE);
                    subFilterLv.setVisibility(VISIBLE);
                    mAdapter.swapItems(phoneList, FILTER_PHONE_INDEX);
                    lastSelectedFilterViewIndex = FILTER_PHONE_INDEX;
                    break;
                case R.id.filter_deal_add_tv:
                    dealDateLl.setVisibility(GONE);
                    subFilterLv.setVisibility(VISIBLE);
                    mAdapter.swapItems(dealAddressList, FILTER_DEAL_ADDRESS_INDEX);
                    lastSelectedFilterViewIndex = FILTER_DEAL_ADDRESS_INDEX;
                    break;
                case R.id.filter_invoice_status_tv:
                    dealDateLl.setVisibility(GONE);
                    subFilterLv.setVisibility(VISIBLE);
                    mAdapter.swapItems(invoiceStatusList, FILTER_INVOICE_STATUS_INDEX);
                    lastSelectedFilterViewIndex = FILTER_INVOICE_STATUS_INDEX;
                    break;
            }
        }
    };

    private OnClickListener mDealDateListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            if (v.getId() == R.id.date_start_txt) {
                DatePickerDialog dpdStart = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dealDateStartTv.setText(getFormattedDate(year, (monthOfYear + 1), dayOfMonth));
                                dealDateStartTv.setSelected(true);
                            }
                        }, mYear, mMonth, mDay);
                dpdStart.show();
            } else if (v.getId() == R.id.date_end_txt) {
                DatePickerDialog dpdEnd = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                try {
                                    if (new SimpleDateFormat("yyyy-MM-dd").parse(dealDateStartTv.getText().toString()).after(new SimpleDateFormat("yyyy-MM-dd").parse(getFormattedDate(year, (monthOfYear + 1), dayOfMonth)))) {
                                        Toast.makeText(mContext, R.string.date_error_tip, Toast.LENGTH_SHORT).show();
                                        dealDateEndTv.setSelected(false);
                                    } else {
                                        dealDateEndTv.setText(getFormattedDate(year, (monthOfYear + 1), dayOfMonth));
                                        dealDateEndTv.setSelected(true);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                dpdEnd.show();
            }
        }
    };

    private OnClickListener mSortListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.sort_ll) {
                return;
            } else {
                lastSelectedSortView.setSelected(false);
                view.setSelected(true);
                lastSelectedSortView = view;
                switch (view.getId()) {
                    case R.id.sort_alpha_up_tv:
                        sortWaysIndexSelected = ALPHABET_UP_SORT;
                        break;
                    case R.id.sort_alpha_down_tv:
                        sortWaysSubIndexSelected = ALPHABET_DOWN_SORT;
                        break;
                }
                updateMoreSortViewDisplay(MORE_SORT_UNLIMITED);
            }
        }
    };


    private OnClickListener mSortSubListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (alphaUpTv.isSelected()) {
                sortWaysIndexSelected = ALPHABET_UP_SORT;
            } else {
                sortWaysIndexSelected = ALPHABET_DOWN_SORT;
            }
            switch (view.getId()) {
                case R.id.sort_sub_no_iv:
                case R.id.sub_no_rl:
                    sortWaysSubIndexSelected = MORE_SORT_UNLIMITED;
                    break;
                case R.id.sort_sub_del_type_iv:
                case R.id.sub_deal_type_rl:
                    sortWaysSubIndexSelected = MORE_SORT_DEAL_TYPE;
                    break;
                case R.id.sort_sub_deal_add_iv:
                case R.id.sub_deal_address_rl:
                    sortWaysSubIndexSelected = MORE_SORT_DEAL_ADDRESS;
                    break;
                case R.id.sort_sub_invoice_status_iv:
                case R.id.sub_invoice_status_rl:
                    sortWaysSubIndexSelected = MORE_SORT_INVOICE_STATUS;
                    break;
                case R.id.sort_sub_phone_iv:
                case R.id.sub_phone_rl:
                    sortWaysSubIndexSelected = MORE_SORT_PHONE_NUMBER;
                    break;
            }
            updateMoreSortViewDisplay(sortWaysSubIndexSelected);
        }
    };


    public View getViewByPosition(int position, ListView listView) {
        final int firstVisibleListItemPosition = listView.getFirstVisiblePosition();
        final int lastVisibleListItemPosition = firstVisibleListItemPosition + listView.getChildCount() - 1;
        if (position < firstVisibleListItemPosition || position > lastVisibleListItemPosition) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int index = position - firstVisibleListItemPosition;
            return listView.getChildAt(index);
        }
    }


    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.filter_layout, this);

        mContext = context;
        tabLl = (LinearLayout) findViewById(R.id.tab_bottom_ll);
        homeRl = (RelativeLayout) findViewById(R.id.home_rl);
        menuRl = (RelativeLayout) findViewById(R.id.filter_sort_rl);
        filterLl = (LinearLayout) findViewById(R.id.filter_ll);
        sortLl = (LinearLayout) findViewById(R.id.sort_ll);

        //init tab view
        filterTv = (TextView) findViewById(R.id.filter_tv);
        timeSortTv = (TextView) findViewById(R.id.time_sort_tv);
        moneySortTv = (TextView) findViewById(R.id.money_sort_tv);
        moreSortTv = (TextView) findViewById(R.id.more_sort_tv);

        //init filter-sort header view
        cancelBtn = (Button) findViewById(R.id.filter_cancel_btn);
        sureBtn = (Button) findViewById(R.id.filter_sort_ok_btn);
        emptyBtn = (Button) findViewById(R.id.clear_filter_sort_btn);

        //init filter view
        dealDateTv = (TextView) findViewById(R.id.filter_deal_date_tv);
        dealTypeTv = (TextView) findViewById(R.id.filter_deal_type_tv);
        invoiceTypeTv = (TextView) findViewById(R.id.filter_invoice_status_tv);
        dealAddTv = (TextView) findViewById(R.id.filter_deal_add_tv);
        phoneTv = (TextView) findViewById(R.id.filter_phone_type_tv);

        //init deal date ll
        dealDateLl = (LinearLayout) findViewById(R.id.deal_date_ll);
        dealDateStartTv = (TextView) findViewById(R.id.date_start_txt);
        dealDateEndTv = (TextView) findViewById(R.id.date_end_txt);

        //get filter list
        subFilterLv = (ListView) findViewById(R.id.sub_filter_lv);
        mAdapter = new Adapter(dealTypeList, FILTER_DEAL_TYPE_INDEX);
        subFilterLv.setAdapter(mAdapter);

        ////init sort view
        alphaDownTv = (TextView) findViewById(R.id.sort_alpha_down_tv);
        alphaUpTv = (TextView) findViewById(R.id.sort_alpha_up_tv);

        sortUnlimitedRl = (RelativeLayout) findViewById(R.id.sub_no_rl);
        sortDealTypeRl = (RelativeLayout) findViewById(R.id.sub_deal_type_rl);
        sortDealAddRl = (RelativeLayout) findViewById(R.id.sub_deal_address_rl);
        sortInvoiceStatusRl = (RelativeLayout) findViewById(R.id.sub_invoice_status_rl);
        sortPhoneRl = (RelativeLayout) findViewById(R.id.sub_phone_rl);

        //init sort subview cbx
        unlimitedIv = (ImageView) findViewById(R.id.sort_sub_no_iv);
        dealTypeIv = (ImageView) findViewById(R.id.sort_sub_del_type_iv);
        dealAddressIv = (ImageView) findViewById(R.id.sort_sub_deal_add_iv);
        invoiceStatusIv = (ImageView) findViewById(R.id.sort_sub_invoice_status_iv);
        telPhoneIv = (ImageView) findViewById(R.id.sort_sub_phone_iv);


        homeRl.setOnClickListener(mOnClickListener);
        filterTv.setOnClickListener(mOnClickListener);
        timeSortTv.setOnClickListener(mOnClickListener);
        moneySortTv.setOnClickListener(mOnClickListener);
        moreSortTv.setOnClickListener(mOnClickListener);
        cancelBtn.setOnClickListener(mOnClickListener);
        sureBtn.setOnClickListener(mOnClickListener);
        emptyBtn.setOnClickListener(mOnClickListener);

        menuRl.setVisibility(View.GONE);

        dealDateTv.setOnClickListener(mFilterListener);
        dealTypeTv.setOnClickListener(mFilterListener);
        dealAddTv.setOnClickListener(mFilterListener);
        invoiceTypeTv.setOnClickListener(mFilterListener);
        phoneTv.setOnClickListener(mFilterListener);

        dealDateStartTv.setOnClickListener(mDealDateListener);
        dealDateEndTv.setOnClickListener(mDealDateListener);

        alphaDownTv.setOnClickListener(mSortListener);
        alphaUpTv.setOnClickListener(mSortListener);
        sortLl.setOnClickListener(mSortListener);

        sortUnlimitedRl.setOnClickListener(mSortSubListener);
        sortDealTypeRl.setOnClickListener(mSortSubListener);
        sortDealAddRl.setOnClickListener(mSortSubListener);
        sortInvoiceStatusRl.setOnClickListener(mSortSubListener);
        sortPhoneRl.setOnClickListener(mSortSubListener);

        unlimitedIv.setOnClickListener(mSortSubListener);
        dealTypeIv.setOnClickListener(mSortSubListener);
        dealAddressIv.setOnClickListener(mSortSubListener);
        invoiceStatusIv.setOnClickListener(mSortSubListener);
        telPhoneIv.setOnClickListener(mSortSubListener);

        dealDateTv.setSelected(true);
        lastSelectedFilterView = dealDateTv;
        dealDateStartTv.setText(getFormattedDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        dealDateEndTv.setText(getFormattedDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, 30));


        alphaUpTv.setSelected(true);
        lastSelectedSortView = alphaUpTv;
        sortUnlimitedRl.setSelected(true);


    }

    private String getFormattedDate(int year, int month, int dayOfMonth) {

        try {
            return new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(String.format("%d-%d-%d", year, month, dayOfMonth)));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateMoreSortViewDisplay(int subIndex) {

        switch (subIndex) {
            case MORE_SORT_UNLIMITED:
                sortUnlimitedRl.setSelected(true);
                sortDealTypeRl.setSelected(false);
                sortDealAddRl.setSelected(false);
                sortInvoiceStatusRl.setSelected(false);
                sortPhoneRl.setSelected(false);
                break;
            case MORE_SORT_DEAL_TYPE:
                sortUnlimitedRl.setSelected(false);
                sortDealTypeRl.setSelected(true);
                sortDealAddRl.setSelected(false);
                sortInvoiceStatusRl.setSelected(false);
                sortPhoneRl.setSelected(false);
                break;
            case MORE_SORT_DEAL_ADDRESS:
                sortUnlimitedRl.setSelected(false);
                sortDealTypeRl.setSelected(false);
                sortDealAddRl.setSelected(true);
                sortInvoiceStatusRl.setSelected(false);
                sortPhoneRl.setSelected(false);
                break;
            case MORE_SORT_INVOICE_STATUS:
                sortUnlimitedRl.setSelected(false);
                sortDealTypeRl.setSelected(false);
                sortDealAddRl.setSelected(false);
                sortInvoiceStatusRl.setSelected(true);
                sortPhoneRl.setSelected(false);
                break;
            case MORE_SORT_PHONE_NUMBER:
                sortUnlimitedRl.setSelected(false);
                sortDealTypeRl.setSelected(false);
                sortDealAddRl.setSelected(false);
                sortInvoiceStatusRl.setSelected(false);
                sortPhoneRl.setSelected(true);
                break;
        }

    }


    class Adapter extends BaseAdapter {

        List<FilterItem> mList = new ArrayList<>();
        int filterIndex;

        public Adapter(List<FilterItem> list, int index) {
            this.mList.clear();
            this.mList.addAll(list);
            this.filterIndex = index;
        }

        @Override
        public int getCount() {
            return mList.size();
        }


        public void swapItems(List<FilterItem> list, int index) {
            this.mList.clear();
            this.mList.addAll(list);
            this.filterIndex = index;
            notifyDataSetChanged();
        }


        @Override
        public FilterItem getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_sub_filter, parent, false);
                viewHolder.subFilterContentTv = (TextView) convertView.findViewById(R.id.sub_filter_content_tv);
                viewHolder.selectedIv = (ImageView) convertView.findViewById(R.id.sub_filter_iv);
                convertView.setTag(viewHolder);
            } else viewHolder = (ViewHolder) convertView.getTag();

            /*if (position == LIST_VIEW_FIRST_ITEM_INDEX) {
                viewHolder.selectedIv.setBackgroundResource(R.drawable.background_unlimited_iv_selector);
            } else {
                viewHolder.selectedIv.setBackgroundResource(R.drawable.iv_selector);
            }*/

            if (filterIndex == FILTER_DEAL_TYPE_INDEX) {
                if (mList.get(position).value.equals(NORMAL_INVOICE))
                    viewHolder.subFilterContentTv.setText(mContext.getString(R.string.invoice_normal));
                else if (mList.get(position).value.equals(RED_INVOICE))
                    viewHolder.subFilterContentTv.setText(mContext.getString(R.string.invoice_red));
                else viewHolder.subFilterContentTv.setText(mList.get(position).value);

            } else viewHolder.subFilterContentTv.setText(mList.get(position).value);

            viewHolder.subFilterContentTv.setSelected(mList.get(position).isSelected);
            viewHolder.selectedIv.setSelected(mList.get(position).isSelected);


            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (position > LIST_VIEW_FIRST_ITEM_INDEX) {
                        if (view.findViewById(R.id.sub_filter_content_tv).isSelected()) {
                            view.findViewById(R.id.sub_filter_content_tv).setSelected(false);
                            view.findViewById(R.id.sub_filter_iv).setSelected(false);
                            changeFilterListValue(filterIndex, position, false);
                            if (!(getFilterListSelectedCount(filterIndex) > LIST_VIEW_FIRST_ITEM_INDEX)) {
                                getViewByPosition(LIST_VIEW_FIRST_ITEM_INDEX, subFilterLv).findViewById(R.id.sub_filter_content_tv).setSelected(true);
                                getViewByPosition(LIST_VIEW_FIRST_ITEM_INDEX, subFilterLv).findViewById(R.id.sub_filter_iv).setSelected(true);
                                changeFilterListValue(filterIndex, LIST_VIEW_FIRST_ITEM_INDEX, true);
                            }
                        } else {
                            view.findViewById(R.id.sub_filter_content_tv).setSelected(true);
                            view.findViewById(R.id.sub_filter_iv).setSelected(true);
                            changeFilterListValue(filterIndex, position, true);
                            if (getViewByPosition(LIST_VIEW_FIRST_ITEM_INDEX, subFilterLv).findViewById(R.id.sub_filter_content_tv).isSelected()) {
                                getViewByPosition(LIST_VIEW_FIRST_ITEM_INDEX, subFilterLv).findViewById(R.id.sub_filter_content_tv).setSelected(false);
                                getViewByPosition(LIST_VIEW_FIRST_ITEM_INDEX, subFilterLv).findViewById(R.id.sub_filter_iv).setSelected(false);
                                changeFilterListValue(filterIndex, LIST_VIEW_FIRST_ITEM_INDEX, false);
                            }
                        }
                    } else {
                        if (!view.findViewById(R.id.sub_filter_content_tv).isSelected()) {
                            for (FilterItem item : mList)
                                item.isSelected = false;
                            mList.get(0).isSelected = true;
                            notifyDataSetChanged();
                        }
                    }

                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView subFilterContentTv;
            ImageView selectedIv;
        }
    }

    class FilterItem {
        String value;
        boolean isSelected = false;
    }

    public interface OnSureClickListener {

        void onclick(List<InvoiceItem> list);

    }

}
