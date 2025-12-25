package com.example.mlbbtutorial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlbbtutorial.R;
import com.example.mlbbtutorial.model.SkillModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder> {

    // Pastikan URL ini sama seperti yang kamu pakai di DetailHeroes
    private static final String SKILL_IMAGE =
            "https://raw.githubusercontent.com/rzkyfhrzi21/mlbb-tutorial-api/refs/heads/master/assets/skills/";
    private static final String DEFAULT_SKILL_IMAGE =
            SKILL_IMAGE + "default_skill.webp";

    private final Context context;
    private final ArrayList<SkillModel> skillList;

    public SkillAdapter(Context context, ArrayList<SkillModel> skillList) {
        this.context = context;
        this.skillList = (skillList != null) ? skillList : new ArrayList<>();
    }

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_skill, parent, false);
        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder h, int position) {
        SkillModel s = skillList.get(position);

        String skillName = safe(s.skillName, "-");
        String skillType = safe(s.type, "-");
        String skillDesc = safe(s.description, "-");
        String cooldown = safe(s.cooldown, "-");
        String manacost = safe(s.manacost, "-");
        String unique = safe(s.unique, "-");

        h.TV_SkillName.setText(skillName);
        h.TV_SkillType.setText(skillType.toUpperCase());
        h.TV_SkillDesc.setText(skillDesc);

        h.TV_SkillCD.setText("CD: " + cooldown);
        h.TV_SkillMana.setText("Mana: " + manacost);
        h.TV_SkillUnique.setText(unique);

        // Icon skill
        String iconFile = safe(s.skillIcon, "default_skill.webp");
        String iconUrl = SKILL_IMAGE + iconFile;

        Picasso.get()
                .load(iconUrl)
                .placeholder(R.drawable.default_skill)
                .into(h.IV_SkillIcon, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(DEFAULT_SKILL_IMAGE)
                                .placeholder(R.drawable.default_skill)
                                .error(R.drawable.default_skill)
                                .into(h.IV_SkillIcon);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return skillList.size();
    }

    // Kalau kamu mau update data tanpa bikin adapter baru
    public void setData(ArrayList<SkillModel> newData) {
        skillList.clear();
        if (newData != null) skillList.addAll(newData);
        notifyDataSetChanged();
    }

    static class SkillViewHolder extends RecyclerView.ViewHolder {

        // Prefix sesuai aturan kamu:
        // IV_ untuk ImageView, TV_ untuk TextView, dst
        ImageView IV_SkillIcon;
        TextView TV_SkillName, TV_SkillType, TV_SkillDesc, TV_SkillCD, TV_SkillMana, TV_SkillUnique;

        SkillViewHolder(@NonNull View v) {
            super(v);

            IV_SkillIcon = v.findViewById(R.id.IV_Skill);

            TV_SkillName = v.findViewById(R.id.TV_SkillName);
            TV_SkillType = v.findViewById(R.id.TV_SkillType);
            TV_SkillDesc = v.findViewById(R.id.TV_SkillDesc);

            TV_SkillCD = v.findViewById(R.id.TV_SkillCD);
            TV_SkillMana = v.findViewById(R.id.TV_SkillMana);
            TV_SkillUnique = v.findViewById(R.id.TV_SkillUnique);
        }
    }

    private String safe(String s, String def) {
        if (s == null) return def;
        s = s.trim();
        return s.isEmpty() ? def : s;
    }
}
