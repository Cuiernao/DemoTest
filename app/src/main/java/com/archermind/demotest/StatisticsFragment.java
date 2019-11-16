package com.archermind.demotest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.archermind.demotest.view.BarChartView;
import com.archermind.demotest.view.BarChartView.BarData;

import java.util.ArrayList;

/**
 * Created by bingye.tang on 2019/1/7.
 */
public class StatisticsFragment extends Fragment implements View.OnClickListener {
    private BarChartView mMonthChart;
    private BarChartView mYearChart;
    private TextView monthBtn;
    private TextView yearBtn;

    ArrayList<BarChartView.BarData> monthData = new ArrayList<>();
    ArrayList<BarChartView.BarData> yearData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_layout, container, false);
        mMonthChart = view.findViewById(R.id.month_chart);
        mYearChart = view.findViewById(R.id.year_chart);

        monthBtn = view.findViewById(R.id.month_data);
        yearBtn = view.findViewById(R.id.year_data);

        monthBtn.setOnClickListener(this);
        yearBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        monthData.add(new BarData(10, "1"));
        monthData.add(new BarData(15, "2"));
        monthData.add(new BarData(13, "3"));
        monthData.add(new BarData(15, "4"));
        monthData.add(new BarData(23, "5"));
        monthData.add(new BarData(12, "6"));
        monthData.add(new BarData(14, "7"));
        monthData.add(new BarData(4, "8"));
        monthData.add(new BarData(5, "9"));
        monthData.add(new BarData(1, "10"));
        monthData.add(new BarData(0, "11"));
        monthData.add(new BarData(7, "12"));
        monthData.add(new BarData(13, "13"));
        monthData.add(new BarData(23, "14"));
        monthData.add(new BarData(16, "15"));
        monthData.add(new BarData(20, "16"));
        monthData.add(new BarData(16, "17"));
        monthData.add(new BarData(12, "18"));
        monthData.add(new BarData(12, "19"));
        monthData.add(new BarData(15, "20"));
        monthData.add(new BarData(17, "21"));
        monthData.add(new BarData(20, "22"));
        monthData.add(new BarData(20, "23"));
        monthData.add(new BarData(15, "24"));
        monthData.add(new BarData(18, "25"));
        monthData.add(new BarData(10, "26"));
        monthData.add(new BarData(19, "27"));
        monthData.add(new BarData(18, "28"));
        monthData.add(new BarData(11, "29"));
        monthData.add(new BarData(22, "30"));
        monthData.add(new BarData(15, "31"));


        yearData.add(new BarData(12, "1"));
        yearData.add(new BarData(12, "1"));
        yearData.add(new BarData(15, "1"));
        yearData.add(new BarData(17, "1"));
        yearData.add(new BarData(20, "2"));
        yearData.add(new BarData(22, "2"));
        yearData.add(new BarData(15, "2"));
        yearData.add(new BarData(18, "2"));
        yearData.add(new BarData(10, "3"));
        yearData.add(new BarData(19, "3"));
        yearData.add(new BarData(18, "3"));
        yearData.add(new BarData(11, "3"));
        yearData.add(new BarData(22, "4"));
        yearData.add(new BarData(20, "4"));
        yearData.add(new BarData(16, "4"));
        yearData.add(new BarData(20, "4"));
        yearData.add(new BarData(16, "5"));
        yearData.add(new BarData(12, "5"));
        yearData.add(new BarData(12, "5"));
        yearData.add(new BarData(15, "5"));
        yearData.add(new BarData(17, "5"));
        yearData.add(new BarData(20, "6"));
        yearData.add(new BarData(20, "6"));
        yearData.add(new BarData(15, "6"));
        yearData.add(new BarData(18, "6"));
        yearData.add(new BarData(10, "7"));
        yearData.add(new BarData(19, "7"));
        yearData.add(new BarData(18, "7"));
        yearData.add(new BarData(11, "7"));
        yearData.add(new BarData(22, "8"));
        yearData.add(new BarData(15, "8"));
        yearData.add(new BarData(20, "8"));
        yearData.add(new BarData(22, "8"));
        yearData.add(new BarData(15, "9"));
        yearData.add(new BarData(18, "9"));
        yearData.add(new BarData(10, "9"));
        yearData.add(new BarData(19, "9"));
        yearData.add(new BarData(18, "10"));
        yearData.add(new BarData(11, "10"));
        yearData.add(new BarData(20, "10"));

        yearData.add(new BarData(20, "10"));
        yearData.add(new BarData(20, "11"));
        yearData.add(new BarData(15, "11"));
        yearData.add(new BarData(18, "11"));
        yearData.add(new BarData(10, "11"));
        yearData.add(new BarData(19, "11"));
        yearData.add(new BarData(18, "12"));
        yearData.add(new BarData(11, "12"));
        yearData.add(new BarData(22, "12"));
        yearData.add(new BarData(15, "12"));
        yearData.add(new BarData(20, "12"));

        mMonthChart.setBarChartData(monthData, getAvcValue(monthData), true);
        mYearChart.setBarChartData(yearData, getAvcValue(yearData), false);
    }


    private float getAvcValue(ArrayList<BarData> innerData) {
        float avc = 0f;
        float total = 0f;
        if (!innerData.isEmpty()) {
            for (int i = 0; i < innerData.size(); i++) {
                total += innerData.get(i).getValue();
            }
            avc = total / (innerData.size());
        }
        return avc;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.month_data:
                mMonthChart.setBarChartData(monthData, getAvcValue(monthData), true);

//                mMonthChart.setVisibility(View.VISIBLE);
//                mYearChart.setVisibility(View.GONE);


                yearBtn.setBackgroundResource(android.R.color.transparent);
                break;
            case R.id.year_data:
                mMonthChart.setBarChartData(yearData, getAvcValue(yearData), false);
//
//                mYearChart.setVisibility(View.VISIBLE);
//                mMonthChart.setVisibility(View.GONE);


                monthBtn.setBackgroundResource(android.R.color.transparent);
                break;
        }
    }
}