package com.fixit.app.ui.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.app.ui.adapters.ProfessionRecyclerAdapter.ProfessionViewHolder;
import com.fixit.core.data.Profession;
import com.fixit.core.ui.adapters.CommonRecyclerAdapter;
import com.fixit.core.ui.adapters.animations.RecyclerItemScaleAnimation;
import com.squareup.picasso.Picasso;

/**
 * Created by konstantin on 4/2/2017.
 */

public class ProfessionRecyclerAdapter extends CommonRecyclerAdapter<Profession, ProfessionViewHolder> {

    public ProfessionRecyclerAdapter(Profession[] items, CommonRecyclerViewInteractionListener<Profession> listener) {
        super(new RecyclerItemScaleAnimation(600), items, listener);
    }

    @Override
    public ProfessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_profession, parent, false);
        ProfessionViewHolder pvh = new ProfessionViewHolder(v);

        attachItemClickListener(v, pvh);

        return pvh;
    }

    static class ProfessionViewHolder extends CommonViewHolder<Profession> {

        private final ImageView ivBackground;
        private final TextView tvName;

        ProfessionViewHolder(View itemView) {
            super(itemView);

            this.ivBackground = (ImageView) itemView.findViewById(R.id.iv_profession_bg);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_profession_name);
        }

        @Override
        public void populate(Profession entity) {
            tvName.setText(entity.getName());

            String imgUrl = entity.getImageUrl();
            if(!TextUtils.isEmpty(imgUrl)) {
                Picasso.with(itemView.getContext()).load(imgUrl).centerCrop().into(ivBackground);
            }
        }
    }

}
