package it.unimib.aiprint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterProfile extends RecyclerView.Adapter<AdapterProfile.PostViewHolder>{

    String [] description, shopImageBase64, shopUrl, userId, userImage;
    int [] price, like;

    public AdapterProfile(PostTest postTest){
        this.description = postTest.description;
        this.shopImageBase64 = postTest.shopImageBase64;
        this.shopUrl = postTest.shopUrl;
        this.userId = postTest.userId;
        this.userImage = postTest.userImage;
        this.price = postTest.price;
        this.like = postTest.like;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.profile_grid_view, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        String test[] = new String[6];
        test[0] = description[position];
        test[1] = userId[position];
        test[2] = "£ " + price[position];
        test[3] = "" + like[position];
        test[4] = shopImageBase64[position];
        test[5] = userImage[position];
        holder.bind(test);
        //holder.bind(description[position]);
        //holder.bind(userId[position]);
        //holder.bind("" + price[position]);
        //holder.bind("" + like[position]);
    }

    @Override
    public int getItemCount() {
        if(description != null) {
            return description.length;
        }
        return 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{

        TextView textViewDescription, textViewUserId, textViewPrice, textViewLike;
        ImageView imageViewShopImageBase64, imageViewUserImage;

        public PostViewHolder (@NonNull View itemView){
            super(itemView);
            //textViewDescription = itemView.findViewById(R.id.gridTextView);
            //textViewUserId = itemView.findViewById(R.id.gridTextView);
            //textViewPrice = itemView.findViewById(R.id.TXTV_price);
            //textViewLike = itemView.findViewById(R.id.TXTV_likes_post);
            imageViewShopImageBase64 = itemView.findViewById(R.id.gridImageView);
            //imageViewUserImage = itemView.findViewById(R.id.IMGV_user_image);


        }

        public void bind(String[] s){

            //textViewDescription.setText(s[0]);
            //textViewUserId.setText(s[1]);
            //textViewPrice.setText(s[2]);
            //textViewLike.setText(s[3]);
            try{
                final byte[] decodedBytes = Base64.decode(s[4], Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                //ImageView imageViewShopImageBase64 = (ImageView) itemView.findViewById(R.id.IMGV_image_post);
                imageViewShopImageBase64.setImageBitmap(bitmap);
            }catch(Exception e){
                Log.e("TAG", "Error " + e);
            }
            /*try{
                final byte[] decodedBytes = Base64.decode(s[5], Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                //ImageView imageViewShopImageBase64 = (ImageView) itemView.findViewById(R.id.IMGV_image_post);
                imageViewUserImage.setImageBitmap(bitmap);
            }catch(Exception e){
                Log.e("TAG", "Error " + e);
            }*/

        }
    }

}
