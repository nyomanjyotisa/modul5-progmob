package com.example.praktikum.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.praktikum.R;
import com.example.praktikum.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public ProfileFragment(){
        // require a empty public constructor
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        return inflater.inflate(R.layout.fragment_profile, container, false);
        //
//        final TextView textView = binding.textNotifications;
//        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return binding.getRoot();
    }

//@Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
}