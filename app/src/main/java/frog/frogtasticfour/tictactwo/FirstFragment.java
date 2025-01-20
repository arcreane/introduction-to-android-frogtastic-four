package frog.frogtasticfour.tictactwo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import frog.frogtasticfour.tictactwo.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    public FirstFragment() {
    }
    private FragmentFirstBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        Button buttonFirst = view.findViewById(R.id.next_two);
        buttonFirst.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new SecondFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

}