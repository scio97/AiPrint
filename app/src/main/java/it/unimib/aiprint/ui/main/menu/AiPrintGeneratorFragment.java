package it.unimib.aiprint.ui.main.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import it.unimib.aiprint.R;
import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.model.image.ImageResponse;
import it.unimib.aiprint.model.post.Post;
import it.unimib.aiprint.model.shop.ShopResponse;
import it.unimib.aiprint.repository.implementations.post.IPostRepositoryWithLiveData;
import it.unimib.aiprint.repository.interfaces.image.IImageRepositoryWithLiveData;
import it.unimib.aiprint.repository.interfaces.like.ILikeRepositoryWithLiveData;
import it.unimib.aiprint.repository.interfaces.shop.IShopRepositoryWithLiveData;
import it.unimib.aiprint.repository.interfaces.user.IUserRepository;
import it.unimib.aiprint.ui.welcome.WelcomeFragment;
import it.unimib.aiprint.util.ServiceLocator;
import it.unimib.aiprint.viewModel.ImageViewModel;
import it.unimib.aiprint.viewModel.LikeViewModel;
import it.unimib.aiprint.viewModel.PostViewModel;
import it.unimib.aiprint.viewModel.ShopViewModel;
import it.unimib.aiprint.viewModel.UserViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AiPrintGeneratorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AiPrintGeneratorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageViewModel imageViewModel;
    private ShopViewModel shopViewModel;
    private PostViewModel postViewModel;
    private LikeViewModel likeViewModel;
    private UserViewModel userViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView ivBasicImage;
    private ShopResponse shopResponse;
    private ImageResponse imageResponse;
    private ImageView imageAIResponse;
    private Button generateAiImage;
    private Button savePost;
    private Button openShop;
    private EditText descriptionAiImage;
    private ProgressBar circularProgressBar;
    private Post newPost;


    public AiPrintGeneratorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AiPrintGeneratorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AiPrintGeneratorFragment newInstance(String param1, String param2) {
        AiPrintGeneratorFragment fragment = new AiPrintGeneratorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        IImageRepositoryWithLiveData newsRepositoryWithLiveData =
                ServiceLocator.getInstance().getNewsRepository(
                        requireActivity().getApplication(),
                        false);

        IShopRepositoryWithLiveData shopRepositoryWithLiveData =
                ServiceLocator.getInstance().getShopRepository(
                        requireActivity().getApplication(),
                        false);

        IPostRepositoryWithLiveData postRepositoryWithLiveData =
                ServiceLocator.getInstance().getPostRepository(
                        requireActivity().getApplication(),
                        false);

        ILikeRepositoryWithLiveData likeRepositoryWithLiveData =
                ServiceLocator.getInstance().getLikeRepository(
                        requireActivity().getApplication(),
                        false);

        IUserRepository userRepository =
                ServiceLocator.getInstance().getUserRepository(
                        requireActivity().getApplication());


        imageViewModel = new ViewModelProvider(
                requireActivity(),
                new ImageViewModelFactory(newsRepositoryWithLiveData)).get(ImageViewModel.class);

        shopViewModel = new ViewModelProvider(
                requireActivity(),
                new ShopViewModelFactory(shopRepositoryWithLiveData)).get(ShopViewModel.class);

        postViewModel = new ViewModelProvider(
                requireActivity(),
                new PostViewModelFactory(postRepositoryWithLiveData)).get(PostViewModel.class);

        likeViewModel = new ViewModelProvider(
                requireActivity(),
                new LikeViewModelFactory(likeRepositoryWithLiveData)).get(LikeViewModel.class);

        userViewModel = new ViewModelProvider(
                requireActivity(),
                new WelcomeFragment.UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ai_print_generator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageAIResponse = (ImageView) view.findViewById(R.id.image_view);
        generateAiImage = (Button) view.findViewById(R.id.BTN_generate);
        descriptionAiImage = (EditText) view.findViewById(R.id.edit_text);
        ivBasicImage = (ImageView) view.findViewById(R.id.immagineRemota);
        savePost = (Button) view.findViewById(R.id.BTN_savePost);
        circularProgressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
        openShop = (Button) view.findViewById(R.id.BTN_buy);

        newPost = new Post();

        openShop.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newPost.getShopUrl()));
            startActivity(browserIntent);
        });


        generateAiImage.setOnClickListener(v -> {
            circularProgressBar.setVisibility(view.VISIBLE);
            imageViewModel.getAiImage(descriptionAiImage.getText().toString()).observe(getViewLifecycleOwner(), result -> {
                if (result != null) {
                    ImageResponse imageResponse = (ImageResponse) ((Result.Success) result).getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL((imageResponse.getOutput_url())).getContent());

                        getShopImage(imageResponse.getOutput_url());

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();

                        imageAIResponse.setImageBitmap(bitmap);
                        circularProgressBar.setVisibility(view.GONE);
                        newPost.setAiImageBase64(Base64.encodeToString(b, Base64.DEFAULT));

                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        });

        savePost.setOnClickListener(v->{
            newPost.setId(String.valueOf(ThreadLocalRandom.current().nextInt(1, 10000 + 1)));
            newPost.setUserId(userViewModel.getLoggedUser().getEmail());
            newPost.setDescription(descriptionAiImage.getText().toString());
            newPost.setPrice(34);
            newPost.setLike_count(0);
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                newPost.setDate(LocalDate.now().format(formatter));
            }
            newPost.setUserName(userViewModel.getLoggedUser().getEmail());
            postViewModel.savePost(newPost).observe(getViewLifecycleOwner(), result -> {
                if (result != null) {
                    Context context = requireActivity().getApplication();
                    CharSequence text = "Post Created!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
        });
    }

    public void getShopImage(String urlImage){
        shopViewModel.getShopImage(urlImage).observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                shopResponse = (ShopResponse) ((Result.Success) result).getData();
                try {
                    ivBasicImage.setImageBitmap(BitmapFactory.decodeStream((InputStream)new URL((shopResponse.getImage())).getContent()));

                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL((shopResponse.getImage())).getContent());

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    newPost.setShopImageBase64(Base64.encodeToString(b, Base64.DEFAULT));

                    newPost.setShopUrl(shopResponse.getUrl());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public class ImageViewModelFactory implements ViewModelProvider.Factory {

        private final IImageRepositoryWithLiveData iNewsRepositoryWithLiveData;

        public ImageViewModelFactory(IImageRepositoryWithLiveData iNewsRepositoryWithLiveData) {
            this.iNewsRepositoryWithLiveData = iNewsRepositoryWithLiveData;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ImageViewModel(iNewsRepositoryWithLiveData);
        }
    }


    public class ShopViewModelFactory implements ViewModelProvider.Factory {

        private final IShopRepositoryWithLiveData iShopRepositoryWithLiveData;

        public ShopViewModelFactory(IShopRepositoryWithLiveData iShopRepositoryWithLiveData) {
            this.iShopRepositoryWithLiveData = iShopRepositoryWithLiveData;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ShopViewModel(iShopRepositoryWithLiveData);
        }
    }

    public static class PostViewModelFactory implements ViewModelProvider.Factory {

        private final IPostRepositoryWithLiveData iPostRepositoryWithLiveData;

        public PostViewModelFactory(IPostRepositoryWithLiveData iPostRepositoryWithLiveData) {
            this.iPostRepositoryWithLiveData = iPostRepositoryWithLiveData;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PostViewModel(iPostRepositoryWithLiveData);
        }
    }


    public static class LikeViewModelFactory implements ViewModelProvider.Factory {

        private final ILikeRepositoryWithLiveData iLikeRepositoryWithLiveData;

        public LikeViewModelFactory(ILikeRepositoryWithLiveData iLikeRepositoryWithLiveData) {
            this.iLikeRepositoryWithLiveData = iLikeRepositoryWithLiveData;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new LikeViewModel(iLikeRepositoryWithLiveData);
        }
    }
}
