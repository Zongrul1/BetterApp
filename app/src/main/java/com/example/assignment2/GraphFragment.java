package com.example.assignment2;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.assignment2.Adapter.CalendarPagerAdapter;
import com.example.assignment2.Utils.DateUtil;
import com.example.assignment2.Utils.DisplayUtil;
import com.example.assignment2.Utils.LogUtil;
import com.example.assignment2.View.GraphView;
import com.example.assignment2.db.DayStatusDao;

import java.util.Calendar;
import java.util.Date;

public class GraphFragment extends Fragment {
    private Context context;
    private View mainLayout;
    private TextView headerText;
    private ViewPager viewPager;
    private TextView evaluationText;
    private Calendar calendar;
    private GraphView[] graphViews=new GraphView[3];
    private boolean isChanged=false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LogUtil.e("onAttach");
        this.context=context;
        initParams();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainLayout=inflater.inflate(R.layout.fragment_graph,container,false);
        initViews();
        return mainLayout;
    }
    private void initParams(){
        calendar= Calendar.getInstance();
    }
    private void initViews(){
        Typeface rl = ResourcesCompat.getFont(getActivity(),R.font.rl);
        headerText=mainLayout.findViewById(R.id.graph_year_month);
        viewPager=mainLayout.findViewById(R.id.graph_viewPager);
        evaluationText=mainLayout.findViewById(R.id.graph_evaluation);
        headerText.setTypeface(rl);
        evaluationText.setTypeface(rl);
        initHeaderText(calendar.getTime());
        initGraphViews();
        initViewPager();
    }
    private void initHeaderText(Date date){
        headerText.setText(DateUtil.getYearAndMonth(date));
    }

    private void initGraphViews(){
        for(int i=0;i<3;i++){
            graphViews[i]=new GraphView(context);
        }
        setPercent();
    }
    private void setPercent(){
        for(int i=0;i<3;i++){
            calendar.add(Calendar.MONTH,i-1);
            int[]monthParams= DayStatusDao.queryMonthStatus(calendar.getTime());
            float goodPercent=monthParams[2]*1.0f/monthParams[0];
            float ordinaryPercent=monthParams[3]*1.0f/monthParams[0];
            float badPercent=monthParams[4]*1.0f/monthParams[0];
            float notwellPercent=monthParams[6]*1.0f/monthParams[0];
            float noRecordPercent=monthParams[5]*1.0f/monthParams[0];
            graphViews[i].setPercent(goodPercent,ordinaryPercent,badPercent,notwellPercent,noRecordPercent);
            if(i==1){
                String evaluation0= String.format(getString(R.string.graph_evaluation0),monthParams[0],monthParams[1],monthParams[5]);
                String evaluation1= String.format(getString(R.string.graph_evaluation1),monthParams[2]);
                String evaluation2= String.format(getString(R.string.graph_evaluation2),monthParams[3]);
                String evaluation3= String.format(getString(R.string.graph_evaluation3),monthParams[4]);
                String evaluation4= String.format(getString(R.string.graph_evaluation4),monthParams[6]);
                if(monthParams[5]==monthParams[0]){
                    String text=evaluation0+evaluation1+evaluation2+evaluation3+evaluation4;
                    evaluationText.setText(text);
                }else{
                    int month=calendar.get(Calendar.MONTH)+1;
                    String text=evaluation0+evaluation1+evaluation2+evaluation3+evaluation4;
                    evaluationText.setText(text);
                }
            }
            calendar.add(Calendar.MONTH,1-i);
        }
    }

    private void initViewPager(){
        int height=(int)(DisplayUtil.getScreenWidth()*0.5f)+ DisplayUtil.dp2px(20);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        viewPager.setLayoutParams(params);
        viewPager.setAdapter(new CalendarPagerAdapter(graphViews));
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    calendar.add(Calendar.MONTH,-1);
                }else if(position==2){
                    calendar.add(Calendar.MONTH,1);
                }
                initHeaderText(calendar.getTime());
                isChanged=true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state==ViewPager.SCROLL_STATE_IDLE&&isChanged){
                    setPercent();
                    viewPager.setCurrentItem(1,false);
                    isChanged=false;
                }
            }
        });
    }
}
