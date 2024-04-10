package it.unimib.aiprint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FavouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Initialize navigation host
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().
                findFragmentById(R.id.nav_host_fragment_collection);
        NavController navController = navHostFragment.getNavController();

//        RecyclerView recyclerView = view.findViewById(R.id.favouriteListView);
 //       GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
  //      recyclerView.setLayoutManager(gridLayoutManager);

        //Go to saved post fragment
        Button savedButton = (Button) view.findViewById(R.id.BTN_saved_post);
        savedButton.setOnClickListener(v-> {
            navController.navigate(R.id.fragment_saved_post);
            //Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_registerUserFragment);
        });
        //Go to favourite post fragment
        Button favouriteButton = (Button) view.findViewById(R.id.BTN_favourite_post);
        favouriteButton.setOnClickListener(v-> {
            navController.navigate(R.id.fragment_favourite_post);
            //Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_registerUserFragment);
        });

    }
}