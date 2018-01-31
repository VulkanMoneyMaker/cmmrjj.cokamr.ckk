package cmmrjj.cokamr.ckk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cmmrjj.cokamr.ckk.custom.src.kankan.wheel.widget.adapters.AbstractWheelAdapter;
import cmmrjj.cokamr.ckk.model.Post;

import java.util.ArrayList;


public class AdapterForGameItems extends AbstractWheelAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Integer> mListImages;
    private Context mContext;

    private Post post = new Post();

    @SuppressLint("WrongConstant")
    public AdapterForGameItems(Context mContext, ArrayList<Integer> list) {
        this.mContext = mContext;
        this.mListImages = list;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService("layout_inflater");

        post.starCount = 0;
        if (post.author == null) {
            Log.d("ADAPTER", "null");
        }
    }

    @Override
    public int getItemsCount() {
        if(post.starCount > 0) {
            return post.starCount;
        }
        return mListImages == null ? 0 : mListImages.size();
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        View view = this.mLayoutInflater.inflate(R.layout.item_list, parent, false);
        ((ImageView) view.findViewById(R.id.image)).setImageDrawable(
                ContextCompat.getDrawable(this.mContext, this.mListImages.get(index))
        );
        return view;
    }
}
