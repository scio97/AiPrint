package it.unimib.aiprint.util;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import it.unimib.aiprint.repository.implementations.Like.LikeRepositoryWithLiveData;
import it.unimib.aiprint.repository.implementations.image.ImageRepositoryWithLiveData;
import it.unimib.aiprint.repository.implementations.post.IPostRepositoryWithLiveData;
import it.unimib.aiprint.repository.implementations.post.PostRepositoryWithLiveData;
import it.unimib.aiprint.repository.implementations.post.PostRoomDatabase;
import it.unimib.aiprint.repository.implementations.shop.ShopRepositoryWithLiveData;
import it.unimib.aiprint.repository.implementations.user.UserRepository;
import it.unimib.aiprint.repository.interfaces.image.IImageRepositoryWithLiveData;
import it.unimib.aiprint.repository.interfaces.like.ILikeRepositoryWithLiveData;
import it.unimib.aiprint.repository.interfaces.shop.IShopRepositoryWithLiveData;
import it.unimib.aiprint.repository.interfaces.user.IUserRepository;
import it.unimib.aiprint.service.ImageApiService;
import it.unimib.aiprint.service.ShopApiService;
import it.unimib.aiprint.source.implementations.Like.BaseLikeDataRemoteDataSource;
import it.unimib.aiprint.source.implementations.Like.LikeDataRemoteDataSource;
import it.unimib.aiprint.source.implementations.image.BaseImageRemoteDataSource;
import it.unimib.aiprint.source.implementations.image.ImageRemoteDataSource;
import it.unimib.aiprint.source.implementations.post.BasePostDataRemoteDataSource;
import it.unimib.aiprint.source.implementations.post.BasePostLocalDataSource;
import it.unimib.aiprint.source.implementations.post.PostDataRemoteDataSource;
import it.unimib.aiprint.source.implementations.post.PostLocalDataSource;
import it.unimib.aiprint.source.implementations.shop.BaseShopRemoteDataSource;
import it.unimib.aiprint.source.implementations.shop.ShopRemoteDataSource;
import it.unimib.aiprint.source.implementations.user.BaseUserAuthenticationRemoteDataSource;
import it.unimib.aiprint.source.implementations.user.BaseUserDataRemoteDataSource;
import it.unimib.aiprint.source.implementations.user.UserAuthenticationRemoteDataSource;
import it.unimib.aiprint.source.implementations.user.UserDataRemoteDataSource;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {
    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }
    public ImageApiService getImageApiService() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(150, TimeUnit.SECONDS);
        client.readTimeout(150, TimeUnit.SECONDS);
        client.writeTimeout(150, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.IMAGE_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).client(client.build()).build();
        return retrofit.create(ImageApiService.class);
    }

    public ShopApiService getShopApiService() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(150, TimeUnit.SECONDS);
        client.readTimeout(150, TimeUnit.SECONDS);
        client.writeTimeout(150, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.IMAGE_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).client(client.build()).build();
        return retrofit.create(ShopApiService.class);
    }

    public IImageRepositoryWithLiveData getNewsRepository(Application application, boolean debugMode) {
        BaseImageRemoteDataSource postRemoteDataSource;
          postRemoteDataSource = new ImageRemoteDataSource("quickstart-QUdJIGlzIGNvbWluZy4uLi4K");
          //newsRemoteDataSource = new ImageMockRemoteDataSource(new JSONParserUtil(application));
        return new ImageRepositoryWithLiveData(postRemoteDataSource);
    }

    public IShopRepositoryWithLiveData getShopRepository(Application application, boolean debugMode) {
        BaseShopRemoteDataSource newsRemoteDataSource;
         newsRemoteDataSource = new ShopRemoteDataSource();
        //newsRemoteDataSource = new ImageMockRemoteDataSource(new JSONParserUtil(application));
        return new ShopRepositoryWithLiveData(newsRemoteDataSource);
    }

    public IPostRepositoryWithLiveData getPostRepository(Application application, boolean debugMode) {
        BasePostDataRemoteDataSource postDataRemoteDataSource;
        postDataRemoteDataSource = new PostDataRemoteDataSource();
        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);
        BasePostLocalDataSource postLocalDataSource;
        postLocalDataSource = new PostLocalDataSource(getPostsDao(application), dataEncryptionUtil);
        //newsRemoteDataSource = new ImageMockRemoteDataSource(new JSONParserUtil(application));
        return new PostRepositoryWithLiveData(postDataRemoteDataSource, postLocalDataSource);
    }

    public PostRoomDatabase getPostsDao(Application application) {
        return PostRoomDatabase.getDatabase(application);
    }

    public IUserRepository getUserRepository(Application application) {
        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource =
                new UserAuthenticationRemoteDataSource();

        BaseUserDataRemoteDataSource userDataRemoteDataSource =
                new UserDataRemoteDataSource();

        BasePostDataRemoteDataSource newsLocalDataSource =
                new PostDataRemoteDataSource();

        return new UserRepository(userRemoteAuthenticationDataSource,
                userDataRemoteDataSource, newsLocalDataSource);
    }
    public ILikeRepositoryWithLiveData getLikeRepository(Application application, boolean debugMode) {
        BaseLikeDataRemoteDataSource likeDataRemoteDataSource;
            likeDataRemoteDataSource = new LikeDataRemoteDataSource();
        //newsRemoteDataSource = new ImageMockRemoteDataSource(new JSONParserUtil(application));
        return new LikeRepositoryWithLiveData(likeDataRemoteDataSource);
    }
}
