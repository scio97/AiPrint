package it.unimib.aiprint;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

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
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerViewAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IPostRepositoryWithLiveData postRepositoryWithLiveData =
                ServiceLocator.getInstance().getPostRepository(
                        requireActivity().getApplication(),
                        false);

        postViewModel = new ViewModelProvider(
                requireActivity(),
                new AiPrintGeneratorFragment.PostViewModelFactory(postRepositoryWithLiveData)).get(PostViewModel.class);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ILikeRepositoryWithLiveData likeRepositoryWithLiveData =
                ServiceLocator.getInstance().getLikeRepository(
                        requireActivity().getApplication(),
                        false);

        IUserRepository userRepository =
                ServiceLocator.getInstance().getUserRepository(
                        requireActivity().getApplication());

        likeViewModel = new ViewModelProvider(requireActivity(),
                new AiPrintGeneratorFragment.LikeViewModelFactory(likeRepositoryWithLiveData)).get(LikeViewModel.class);


        userViewModel = new ViewModelProvider(
                requireActivity(),
                new WelcomeFragment.UserViewModelFactory(userRepository)).get(UserViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // SwipeRefreshLayout
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

        postList = new ArrayList<>();

        RecyclerView recyclerView = rootView.findViewById(R.id.customListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        Context context = getActivity().getApplicationContext();

        adapter = new RecyclerViewAdapter(postList, requireActivity().getApplication(), new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onPostItemClick(Post post) {
                Snackbar.make(rootView, post.getId(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public boolean onSaveButtonPressed(Post post) {
                postViewModel.isSavedPostInLocal(post.getId()).observe(getViewLifecycleOwner(),result -> {
                    if (((PostResponse) ((Result.Success) result).getData()).getPost() != null){
                        postList.add(((PostResponse) ((Result.Success) result).getData()).getPost());
                        adapter.notifyDataSetChanged();
                    }
                });

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
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getShopUrl()));
                startActivity(browserIntent);
            }

            @Override
            public void onFavoriteButtonPressed(int position, Post post) {
                updateLikeCounter(post, position);
            }
        },0);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void loadRecyclerViewData() {
        mSwipeRefreshLayout.setRefreshing(true);
        postViewModel.getLatestRandomPost(this.userViewModel.getLoggedUser().getEmail()).observe(getViewLifecycleOwner(), result ->
        {
            if (result != null && ((PostResponse) ((Result.Success) result).getData()).getPostList() != null) {
                postList.clear();
                postList.addAll(((PostResponse) ((Result.Success) result).getData()).getPostList());
                adapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }

    public void updateLikeCounter(Post post, int position){
        int likeCounter = post.getLike_count();
        if (!post.getIsLiked()){
            Like like = new Like(ThreadLocalRandom.current().nextInt(1, 10000 + 1), post.getId(), userViewModel.getLoggedUser().getEmail());
            postViewModel.addLike(post.getId());
            likeViewModel.saveLike(like);
            likeCounter++;
        } else {
            postViewModel.removeLike(post.getId());
            likeViewModel.deleteLike(post.getId());
            likeCounter--;
        }
        post.setLike_count(likeCounter);
        postList.set(position, post);
        adapter.notifyItemChanged(position);
    }
}
