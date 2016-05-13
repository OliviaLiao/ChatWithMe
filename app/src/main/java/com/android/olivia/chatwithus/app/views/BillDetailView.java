package com.android.olivia.chatwithus.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.olivia.chatwithus.R;

/**
 * Created by olivia on 5/10/16.
 */
public class BillDetailView extends LinearLayout {
    private final Context mContext;
    private TextView mNumber;
    private TextView mPrice;
    private TextView mFees;

    public BillDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        final View mRootView = LayoutInflater.from(context).inflate(R.layout.bill_detail, this);
        mNumber = (TextView) mRootView.findViewById(R.id.number);
        mPrice = (TextView) mRootView.findViewById(R.id.price);
        mFees = (TextView) mRootView.findViewById(R.id.fee);
    }

    public void setBillDetail(String number, String price, String fees) {
        mNumber.setText(number);
        mPrice.setText(price);
        mFees.setText(fees);
    }
}
