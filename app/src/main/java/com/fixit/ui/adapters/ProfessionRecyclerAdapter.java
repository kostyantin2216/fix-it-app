package com.fixit.ui.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.data.Profession;
import com.fixit.ui.adapters.animations.RecyclerItemScaleAnimation;
import com.fixit.ui.adapters.ProfessionRecyclerAdapter.ProfessionViewHolder;
import com.squareup.picasso.Picasso;
import com.fixit.ui.adapters.CommonRecyclerAdapter.CommonViewHolder;

/**
 * Created by konstantin on 4/2/2017.
 */

public class ProfessionRecyclerAdapter extends CommonRecyclerAdapter<Profession, ProfessionViewHolder> {

    public ProfessionRecyclerAdapter(Profession[] items, CommonRecyclerViewInteractionListener<Profession> listener) {
        super(new RecyclerItemScaleAnimation(600), items, listener, Profession.NAME_COMPARATOR);
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
        private final TextView tvDesc;

        ProfessionViewHolder(View itemView) {
            super(itemView);

            this.ivBackground = (ImageView) itemView.findViewById(R.id.iv_profession_bg);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_profession_name);
            this.tvDesc = (TextView) itemView.findViewById(R.id.tv_profession_desc);
        }

        @Override
        public void populate(Profession entity) {
            tvName.setText(entity.getName());
            tvDesc.setText(entity.getDescription());

            String imgUrl = entity.getImageUrl();
            if(!TextUtils.isEmpty(imgUrl)) {
                Picasso.with(itemView.getContext()).load(imgUrl).fit().into(ivBackground);
            } else {
                Picasso.with(itemView.getContext()).load(R.drawable.bg_plumber).fit().into(ivBackground);
            }
        }
    }

}
