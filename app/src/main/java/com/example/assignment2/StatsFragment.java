package com.example.assignment2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.assignment2.Utils.FocusHelper;
import com.example.assignment2.Utils.DayAxisValueFormatter;
import com.example.assignment2.Utils.TimeTrackerPrefHandler;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatsFragment extends Fragment {
    private BarChart barChart;

    private TextView startTime;
    private TextView endTime;

    private View rootView;

    private final int DAILY = FocusHelper.DAILY_STATS;
    private final int YESTERDAY = FocusHelper.YESTERDAY_STATS;
    private final int WEEKLY = FocusHelper.WEEKLY_STATS;
    private final int MONTHLY = FocusHelper.MONTHLY_STATS;
    private final int NETWORK_MODE = FocusHelper.NETWORK_MODE;

    private int selectedPeriod = 0;
    private int mode = 0;
    private List<String> appList = null;
    private List<String> appNameList = null;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        Bundle args = getArguments();
        selectedPeriod = args.getInt("period", 0);
        Typeface rl = ResourcesCompat.getFont(getActivity(),R.font.rl);
        barChart = (BarChart) rootView.findViewById(R.id.chart1);
        barChart.setNoDataText(getResources().getString(R.string.no_data));
        startTime = (TextView) rootView.findViewById(R.id.tvStartTime);
        endTime = (TextView) rootView.findViewById(R.id.tvEndTime);
        startTime.setTypeface(rl);
        endTime.setTypeface(rl);
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        mode = TimeTrackerPrefHandler.INSTANCE.getMode(context);
        String serialized = TimeTrackerPrefHandler.INSTANCE.getPkgList(context);
        if (serialized != null) {
            appList = null;
            appNameList = null;
            appList = new LinkedList<String>(Arrays.asList(TextUtils.
                    split(serialized, ",")));
            appNameList = new LinkedList<String>();
        }
        if (mode == 1 && (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)) {
            showAlert();
        }
        getStatsInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mode = TimeTrackerPrefHandler.INSTANCE.getMode(context);
        String serialized = TimeTrackerPrefHandler.INSTANCE.getPkgList(context);
        //Log.d("STATSINFO", serialized);
        if (serialized != null) {
            appList = null;
            appNameList = null;
            appList = new LinkedList<String>(Arrays.asList(TextUtils.
                    split(serialized, ",")));
            appNameList = new LinkedList<String>();
        }
        if (mode == 1 && (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)) {
            showAlert();
        }
        getStatsInfo();
    }

    private void showAlert() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.network_stats)
                .setMessage(R.string.networks_stats_na)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getStatsInfo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startMillis = 0;
        long endMillis = System.currentTimeMillis();

        Date endresultdate = new Date(System.currentTimeMillis());
        switch (selectedPeriod) {
            case DAILY:
                startMillis = calendar.getTimeInMillis();
                break;
            case YESTERDAY:
                calendar.set(Calendar.HOUR_OF_DAY, -24);
                startMillis = calendar.getTimeInMillis();
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 99);
                endMillis = calendar.getTimeInMillis();
                endresultdate = calendar.getTime();
                break;
            case WEEKLY:
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                startMillis = calendar.getTimeInMillis();
                break;
            case MONTHLY:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startMillis = calendar.getTimeInMillis();
                break;
            default:
                break;
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date startresultdate = new Date(startMillis);
        startTime.setText("From " + sdf.format(startresultdate));
        endTime.setText("To " + sdf.format(endresultdate));

        setUsageInfo(startMillis, endMillis);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUsageInfo(long startMillis, long endMillis) {
        PackageManager packageManager = context.getPackageManager();
        ArrayList<Float> values = new ArrayList<Float>();
        HashMap<Float, String> valueMap = fetchAppStatsInfo(startMillis, endMillis);
        TreeMap<Float, String> valueTree = new TreeMap<>(valueMap);
        int count = 0;
        String appname = null;
        for (Map.Entry<Float, String> entry : valueTree.entrySet()) {
            try {
                values.add(entry.getKey());
                appname = (String) packageManager.getApplicationLabel(packageManager.
                        getApplicationInfo(entry.getValue(), PackageManager.GET_META_DATA));
                appNameList.add(appname);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            count += 1;
            if (count > 5){break;}
        }

        saveAppPreference();
        if (values.size() != 0) {
            setChart(values);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private HashMap<Float, String> fetchAppStatsInfo(long startMillis, long endMillis){//, String appPkg) {
        Map<String, UsageStats> lUsageStatsMap = FocusHelper.getUsageStatsManager().
                queryAndAggregateUsageStats(startMillis, endMillis);
        //for (String k : lUsageStatsMap.keySet()) {
        //    Log.d("STATSINFO", k);
        //}
        //Log.d("STATSINFO", "~~~");
        float total = 0.0f;
        HashMap<Float, String> totalMap = new HashMap<>();
        for (String appPkg : lUsageStatsMap.keySet()){
        //if (lUsageStatsMap.containsKey(appPkg)) {
            total = (FocusHelper.getMinutes(lUsageStatsMap.get(appPkg).getTotalTimeInForeground()));
            totalMap.put(total, appPkg);
        }
        return totalMap;
    }

    private float fetchNetworkStatsInfo(long startMillis, long endMillis, int uid) {
        NetworkStatsManager networkStatsManager;
        float total = 0.0f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            float receivedWifi = 0;
            float sentWifi = 0;
            float receivedMobData = 0;
            float sentMobData = 0;

            networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
            NetworkStats nwStatsWifi = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, null,
                    startMillis, endMillis, uid);
            NetworkStats.Bucket bucketWifi = new NetworkStats.Bucket();
            while (nwStatsWifi.hasNextBucket()) {
                nwStatsWifi.getNextBucket(bucketWifi);
                receivedWifi = receivedWifi + bucketWifi.getRxBytes();
                sentWifi = sentWifi + bucketWifi.getTxBytes();
            }

            NetworkStats nwStatsMobData = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, null,
                    startMillis, endMillis, uid);
            NetworkStats.Bucket bucketMobData = new NetworkStats.Bucket();
            while (nwStatsMobData.hasNextBucket()) {
                nwStatsMobData.getNextBucket(bucketMobData);
                receivedMobData = receivedMobData + bucketMobData.getRxBytes();
                sentMobData = sentMobData + bucketMobData.getTxBytes();
            }

            if (selectedPeriod == DAILY || selectedPeriod == YESTERDAY) {
                total = (receivedWifi + sentWifi + receivedMobData + sentMobData) / (1024 * 1024);
            } else {
                total = (receivedWifi + sentWifi + receivedMobData + sentMobData) / (1024 * 1024 * 1024);
            }
        }
        return total;
    }

    private void saveAppPreference() {
        TimeTrackerPrefHandler.INSTANCE.savePkgList
                (TextUtils.join(",", appList), context);
    }

    private void setChart(ArrayList<Float> values) {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
//        barChart.setMaxVisibleValueCount(60);
        barChart.setVisibleXRangeMaximum(6);
        barChart.moveViewToX(10);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

        barChart.setDrawGridBackground(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        // barChart.setDrawYLabels(false);
        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart, appNameList);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(appNameList.size());
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(10, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
//        l.setXEntrySpace(4f);

        setData(values);
    }

    private void setData(ArrayList<Float> values) {
        try {
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

            for (int i = 0; i < values.size(); i++) {
                float val = values.get(i);
                yVals1.add(new BarEntry(i, val));
            }

            BarDataSet set1;

            if (barChart.getData() != null &&
                    barChart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
                set1.setValues(yVals1);
                barChart.getData().notifyDataChanged();
                barChart.notifyDataSetChanged();
            } else {
                set1 = new BarDataSet(yVals1, ((mode == 0) ? "App" : "Network") + " usage in " + ((mode == 0) ? "Minutes" : "MB"));

                set1.setDrawIcons(false);
                set1.setColors(ColorTemplate.LIBERTY_COLORS);
                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);
                BarData data = new BarData(dataSets);
                data.setValueTextSize(10f);
                data.setBarWidth(0.2f);
                try {
                    barChart.setData(data);
                    barChart.setVisibleXRangeMaximum(4.0f);
                }catch (Exception e) {
                    Log.d("STATSERRl1", dataSets.toString());
                    Log.d("STATSERRl2", dataSets.get(0).getEntryForIndex(0).toString());
                }
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
