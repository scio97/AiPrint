package it.unimib.aiprint;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.aiprint.model.Like.Like;
import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.model.post.Post;
import it.unimib.aiprint.model.post.PostResponse;
import it.unimib.aiprint.repository.implementations.post.IPostRepositoryWithLiveData;
import it.unimib.aiprint.repository.interfaces.like.ILikeRepositoryWithLiveData;
import it.unimib.aiprint.repository.interfaces.user.IUserRepository;
import it.unimib.aiprint.ui.main.menu.AiPrintGeneratorFragment;
import it.unimib.aiprint.ui.welcome.WelcomeFragment;
import it.unimib.aiprint.util.ServiceLocator;
import it.unimib.aiprint.viewModel.LikeViewModel;
import it.unimib.aiprint.viewModel.PostViewModel;
import it.unimib.aiprint.viewModel.UserViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_favourite_post#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_favourite_post extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PostViewModel postViewModel;
    private LikeViewModel likeViewModel;
    private UserViewModel userViewModel;
    private List<Post> postList;
    RecyclerViewAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public fragment_favourite_post() {}

    public static fragment_favourite_post newInstance(String param1, String param2) {
        fragment_favourite_post fragment = new fragment_favourite_post();
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

        IPostRepositoryWithLiveData postRepositoryWithLiveData =
                ServiceLocator.getInstance().getPostRepository(
                        requireActivity().getApplication(),
                        false);

        postViewModel = new ViewModelProvider(
                requireActivity(),
                new AiPrintGeneratorFragment.PostViewModelFactory(postRepositoryWithLiveData)).get(PostViewModel.class);

        ILikeRepositoryWithLiveData likeRepositoryWithLiveData =
                ServiceLocator.getInstance().getLikeRepository(
                        requireActivity().getApplication(),
                        false);

        likeViewModel = new ViewModelProvider(requireActivity(),
                new AiPrintGeneratorFragment.LikeViewModelFactory(likeRepositoryWithLiveData)).get(LikeViewModel.class);


        IUserRepository userRepository =
                ServiceLocator.getInstance().getUserRepository(
                        requireActivity().getApplication());


        userViewModel = new ViewModelProvider(
                requireActivity(),
                new WelcomeFragment.UserViewModelFactory(userRepository)).get(UserViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        postList = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_favourite_post, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadRecyclerViewData();
            }
        });

        RecyclerView recyclerView = rootView.findViewById(R.id.favouriteListView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new RecyclerViewAdapter(postList, requireActivity().getApplication(), new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onPostItemClick(Post post) {
                Snackbar.make(rootView, post.getId(), Snackbar.LENGTH_LONG).show();
                postViewModel.removeLike(post.getId());
                likeViewModel.deleteLike(post.getId());
            }

            @Override
            public boolean onSaveButtonPressed(Post post) {
                postViewModel.isSavedPostInLocal(post.getId());
                Log.d("myTag", postList.toString());
                if (true){
                    postViewModel.removePostInLocal(post);
                    return false;
                }else{
                    postViewModel.savePostInLocal(post);
                    return true;
                }
            }

            @Override
            public void onShopButtonPressed(Post post) {

            }

            @Override
            public void onFavoriteButtonPressed(int position, Post post) {
                updateLikeCounter(post, position);
            }
        }, 1);
        recyclerView.setAdapter(adapter);
        loadRecyclerViewData();


        return rootView;
    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }


    private void loadRecyclerViewData() {
        likeViewModel.getAllLikesByUserId(this.userViewModel.getLoggedUser().getEmail()).observe(getViewLifecycleOwner(), result ->
        {
            if (result != null) {
                ArrayList<String> e = new ArrayList<>();
                for (Like post: (List<Like>)((Result.Success) result).getData()
                     ) {
                    e.add(post.getPost_id());
                }
                if(e.size()==0){
                    this.postList.clear();
                    mSwipeRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }else{
                    postViewModel.getAllPostByIds(e).observe(getViewLifecycleOwner(), result2 -> {
                        if(((PostResponse) ((Result.Success) result2).getData()).getPostList() != null){
                            this.postList.clear();
                            postList.addAll(((PostResponse) ((Result.Success) result2).getData()).getPostList());
                            mSwipeRefreshLayout.setRefreshing(false);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void updateLikeCounter(Post post, int position){
        postViewModel.removeLike(post.getId());
        likeViewModel.deleteLike(post.getId());
        postList.remove(post);
        adapter.notifyItemChanged(position);
    }
}