package it.unimib.aiprint;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.aiprint.model.post.Post;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int HOME_LIST = 0;
    private static final int F_LIST = 1;

    private int localViewType;

    /**
     * Interface to associate a click listener with
     * a RecyclerView item.
     */
    public interface OnItemClickListener {
        void onPostItemClick(Post post);
        void onFavoriteButtonPressed(int position, Post post);
        boolean onSaveButtonPressed(Post post);
        void onShopButtonPressed(Post post);
    }

    private final List<Post> postList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;


    public RecyclerViewAdapter(List<Post> postList, Application application, OnItemClickListener onItemClickListener, int type){
        this.postList = postList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
        this.localViewType = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(localViewType == HOME_LIST){
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.activity_custom_list_view, parent, false);
            return new PostViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.grid_view, parent, false);
            return new PostViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostViewHolder) {
            ((PostViewHolder) holder).bind(postList.get(position));
        } else if (holder instanceof LoadingPostViewHolder) {
            ((LoadingPostViewHolder) holder).activate();
        }
    }


    @Override
    public int getItemCount() {
        if (postList != null) {
            return postList.size();
        }
        return 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewDescription, textViewUserId, textViewPrice, textViewLike, textViewAuthName;
        ImageView imageViewShopImageBase64, imageViewUserImage, imageViewLike, imageShopFav,
                imageShopIcon, imageViewSave;


        public PostViewHolder (@NonNull View itemView){
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.TXTV_description_post);
            textViewUserId = itemView.findViewById(R.id.TXTV_username_post);
            textViewPrice = itemView.findViewById(R.id.TXTV_price);
            textViewLike = itemView.findViewById(R.id.TXTV_likes_post);
            imageViewShopImageBase64 = itemView.findViewById(R.id.IMGV_image_post);
            imageViewUserImage = itemView.findViewById(R.id.IMGV_user_image);
            imageViewLike = itemView.findViewById(R.id.IMGV_like);
            imageViewSave = itemView.findViewById(R.id.IMGV_save_post);
            imageShopIcon = itemView.findViewById(R.id.IMGV_buy);
            itemView.setOnClickListener(this);
            if(localViewType == 0){
                imageViewLike.setOnClickListener(this);
                imageViewSave.setOnClickListener(this);
                imageShopIcon.setOnClickListener(this);
            }

        }

        public void bind(Post post){
            if(localViewType == 1){
                textViewAuthName = itemView.findViewById(R.id.textViewUserId);
                imageShopFav = itemView.findViewById(R.id.imageViewShopImageBase64);
                textViewAuthName.setText(post.getUserId());
                try{
                    final byte[] decodedBytes = Base64.decode(post.getShopImageBase64(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    //ImageView imageViewShopImageBase64 = (ImageView) itemView.findViewById(R.id.IMGV_image_post);
                    imageShopFav.setImageBitmap(bitmap);
                }catch(Exception e){
                    Log.e("TAG", "Error " + e);
                }
            }else{
                textViewDescription.setText(post.getDescription());
                textViewUserId.setText(post.getUserId());
                textViewPrice.setText(String.valueOf(post.getPrice()));
                textViewLike.setText(String.valueOf(post.getLike_count()));
                setImageViewFavoriteNews(post.getIsLiked());

                try{
                    final byte[] decodedBytes = Base64.decode(post.getShopImageBase64(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    //ImageView imageViewShopImageBase64 = (ImageView) itemView.findViewById(R.id.IMGV_image_post);
                    imageViewShopImageBase64.setImageBitmap(bitmap);
                }catch(Exception e){
                    Log.e("TAG", "Error " + e);
                }
                try{
                    //final byte[] decodedBytes = Base64.decode(s[5], Base64.DEFAULT);
                    //Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    ////ImageView imageViewShopImageBase64 = (ImageView) itemView.findViewById(R.id.IMGV_image_post);
                    //imageViewUserImage.setImageBitmap(bitmap);
                }catch(Exception e){
                    Log.e("TAG", "Error " + e);
                }
            }
        }

        private void bindFavouriteLayout(Post post) {

        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.IMGV_like) {
                onItemClickListener.onFavoriteButtonPressed(getAdapterPosition(), postList.get(getAdapterPosition()));
                // todo: change the icon
                postList.get(getAdapterPosition()).setIsLiked(!postList.get(getAdapterPosition()).getIsLiked());
                setImageViewFavoriteNews(postList.get(getAdapterPosition()).getIsLiked());
            } else if (view.getId() == R.id.IMGV_save_post) {
                setImageViewSaveIcon(onItemClickListener.onSaveButtonPressed(postList.get(getAdapterPosition())));
            } else if (view.getId() == R.id.IMGV_buy) {
                onItemClickListener.onShopButtonPressed(postList.get(getAdapterPosition()));
            } else {
                onItemClickListener.onPostItemClick(postList.get(getAdapterPosition()));
            }
        }

        private void setImageViewFavoriteNews(boolean isFavorite) {
            if (isFavorite) {
                imageViewLike.setImageDrawable(
                        AppCompatResources.getDrawable(application,
                                R.drawable.ic_baseline_favorite_24));
            } else {
                imageViewLike.setImageDrawable(
                        AppCompatResources.getDrawable(application,
                                R.drawable.ic_baseline_favorite_border_24));
            }
        }

        private void setImageViewSaveIcon(boolean isSaved) {
            if (isSaved) {
                imageViewSave.setImageDrawable(
                        AppCompatResources.getDrawable(application,
                                R.drawable.ic_bookmark_filled));
            } else {
                imageViewSave.setImageDrawable(
                        AppCompatResources.getDrawable(application,
                                R.drawable.ic_baseline_bookmark_border_24));
            }
        }


    }

    public static class LoadingPostViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;

        LoadingPostViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar2);
        }

        public void activate() {
            progressBar.setIndeterminate(true);
        }
    }

}
