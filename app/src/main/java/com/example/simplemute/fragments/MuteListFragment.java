package com.example.simplemute.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simplemute.Mute;
import com.example.simplemute.R;
import com.example.simplemute.adapter.MuteRecylerViewAdapter;
import com.example.simplemute.databinding.FragmentMuteListBinding;
import com.example.simplemute.util.OnToggleMuteListener;
import com.example.simplemute.viewmodel.MuteListViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MuteListFragment extends Fragment implements OnToggleMuteListener{

    FragmentMuteListBinding fragmentMuteListBinding;

    FloatingActionButton gotoCrateMuteFragment;
    MuteRecylerViewAdapter muteRecylerViewAdapter;
    public static MuteListViewModel muteListViewModel;
    RecyclerView muteRecylerView;

    AudioManager audioManager;
    NotificationManager nm;
    public MuteListFragment(){
        //required empty constractor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        muteRecylerViewAdapter = new MuteRecylerViewAdapter((OnToggleMuteListener) this);
        muteListViewModel = new ViewModelProvider(this).get(MuteListViewModel.class);
        muteListViewModel.getAlarmsLiveData().observe(this, new Observer<List<Mute>>() {
            @Override
            public void onChanged(List<Mute> mutes) {
                if(mutes!=null){
                    muteRecylerViewAdapter.setMutes(mutes);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       fragmentMuteListBinding = FragmentMuteListBinding.inflate(inflater,container,false);
       View view = fragmentMuteListBinding.getRoot();
       audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        muteRecylerView= fragmentMuteListBinding.showMutelistRecylerView;
        muteRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        muteRecylerView.setAdapter(muteRecylerViewAdapter);
        NotificationManager nm  = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
       gotoCrateMuteFragment =view.findViewById(R.id.gotoCreateMute);


       gotoCrateMuteFragment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 && !nm.isNotificationPolicyAccessGranted()) {
                   startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
               }
               Navigation.findNavController(view).navigate(R.id.action_muteListFragment_to_createMuteFragment);
           }
       });

       return view;
    }


    @Override
    public void onToggle(Mute mute) {
        if (mute.isStarted()) {
            mute.cancelAlarm(getContext());
            muteListViewModel.update(mute);
        } else {
            mute.shedule(getContext());
            muteListViewModel.update(mute);
        }
    }

    @Override
    public void onDelete(Mute mute) {
        mute.cancelAlarm(getContext());
        muteListViewModel.delete(mute.getMuteId() );
    }

    @Override
    public void onItemClick(Mute mute, View view) {

        Log.d("mute is startred","mute "+mute.isStarted()+" requirment "+mute.isRecurring());
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.arg_alarm_obj),mute);
        Navigation.findNavController(view).navigate(R.id.action_muteListFragment_to_createMuteFragment,args);

    }
}