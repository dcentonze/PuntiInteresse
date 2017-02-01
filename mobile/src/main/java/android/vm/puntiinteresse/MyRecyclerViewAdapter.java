package android.vm.puntiinteresse;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by acbes on 01/02/2017.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder>{

    private ArrayList<BitmapDrawable> photos;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    public OnItemClickListener getOnItemClickListener(){
        return onItemClickListener;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public MyRecyclerViewAdapter(ArrayList<BitmapDrawable> photos, Context mContext) {
        this.photos = photos;
        this.mContext = mContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row,null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        BitmapDrawable bitmapDrawable = photos.get(position);
        holder.imageView.setImageDrawable(bitmapDrawable);
    }

    @Override
    public int getItemCount() {
        return (null!=photos?photos.size():0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;


        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.image_view);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}

