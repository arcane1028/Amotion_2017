package com.amotion.amotion_2017.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.amotion.amotion_2017.MainActivity;
import com.amotion.amotion_2017.R;
import com.amotion.amotion_2017.asynctask.ScheduleAsyncTask;
import com.amotion.amotion_2017.asynctask.SubjectAsyncTask;
import com.amotion.amotion_2017.asynctask.SubjectSubmenuAsyncTask;
import com.amotion.amotion_2017.data.AsyncData;
import com.amotion.amotion_2017.data.Schedule;
import com.amotion.amotion_2017.View.ScheduleView;
import com.amotion.amotion_2017.data.Subject;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    private View rootView;
    private ArrayList<Schedule> scheduleArrayList = new ArrayList<>();
    private ScheduleAdapter adapter;
    private ListView listView;
    public FragmentHome() {
    }

    @Nullable
    //내부화면 관리
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_home, null);
        listView=rootView.findViewById(R.id.scheduleList);
        listView.setAdapter(adapter);
        if(adapter.getCount()==0){
            for(int i=0;i<scheduleArrayList.size();i++){
                adapter.addItem(scheduleArrayList.get(i));
            }
        }
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!getAssignmentList()){
            System.out.print("로드 실패");
        }
        adapter = new ScheduleAdapter();
    }

    boolean getAssignmentList(){
        ArrayList<Subject> subjects = null;
        AsyncData asyncData;
        try {
            subjects = new SubjectAsyncTask().execute(MainActivity.loginCookie).get();
            asyncData = new AsyncData(MainActivity.loginCookie, subjects);
            //System.out.println(subjects);
            //TODO 스케쥴 들임
            scheduleArrayList = new ScheduleAsyncTask().execute(asyncData).get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    class ScheduleAdapter extends BaseAdapter {
        ArrayList<Schedule> items = new ArrayList<Schedule>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Schedule item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View converView, ViewGroup viewGroup) {
            ScheduleView view = new ScheduleView(getContext());
            Schedule item = items.get(position);
            view.setCourse(item.getCourse());
            view.setContent(item.getTitle());
            view.setStartDate(item.getStart());
            view.setEndDate(item.getEnd());
            return view;
        }
    }
}