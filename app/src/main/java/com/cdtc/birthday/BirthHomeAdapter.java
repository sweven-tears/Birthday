package com.cdtc.birthday;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cdtc.birthday.data.BirthBean;
import com.cdtc.birthday.data.BornDay;
import com.cdtc.birthday.utils.ToastUtil;
import com.cdtc.birthday.view.BirthDetailActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Sweven on 2018/9/23.
 * Email:sweventears@Foxmail.com
 */
public class BirthHomeAdapter extends RecyclerView.Adapter<BirthHomeAdapter.BirthHomeViewHolder> {

    private List<BirthBean> birthBeanArrayList;
    private LayoutInflater inflater;
    private Context context;

    private static final int backgroundImage[] = {
            R.drawable.home_background_01, R.drawable.home_background_02,
            R.drawable.home_background_03, R.drawable.home_background_04,
            R.drawable.home_background_05, R.drawable.home_background_06,
            R.drawable.home_background_07, R.drawable.home_background_08};

    private static final int foregroundImage[] = {
            R.drawable.home_foreground_01, R.drawable.home_foreground_02,
            R.drawable.home_foreground_03, R.drawable.home_foreground_04,
            R.drawable.home_foreground_05, R.drawable.home_foreground_06,
            R.drawable.home_foreground_07, R.drawable.home_foreground_08};

    private static final int fontColor[] = {
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE};

    BirthHomeAdapter(Context context, List<BirthBean> birthDate) {
        this.context = context;
        this.birthBeanArrayList = birthDate;

        // 处理第一张卡片的异常
        birthBeanArrayList.add(0, new BirthBean());
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BirthHomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.panel_home_item, viewGroup, false);
        return new BirthHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BirthHomeViewHolder holder, int position) {
        BirthBean birthBean = birthBeanArrayList.get(position);
        int imageStyle = birthBean.getImageStyle();

        if (position == 0) {
            holder.setVisibility(false);
        } else {
            if (position % 5 == 0) {
                holder.setVisibility(true);
            }
            initTextView(holder, imageStyle);
            initData(holder, birthBean);
            holder.HomePanelItemLayout.setBackgroundResource(backgroundImage[imageStyle]);
            holder.birthLayout.setBackgroundResource(foregroundImage[imageStyle]);
        }

    }

    @Override
    public int getItemCount() {
        return birthBeanArrayList.size();
    }

    /**
     * [初始化数据]
     *
     * @param holder    .
     * @param birthBean bean
     */
    @SuppressLint("SetTextI18n")
    private void initData(BirthHomeViewHolder holder, BirthBean birthBean) {
        int imageStyle = birthBean.getImageStyle();
        holder.birthCountDownText.setTextColor(fontColor[imageStyle]);
        holder.birthYearMonthText.setTextColor(fontColor[imageStyle]);

        holder.birthCountDownText.setText(birthBean.getTipText());

        int nextYear = birthBean.getNextBirthYear();
        int nextMonth = birthBean.getNextBirthMonth();
        int nextDate = birthBean.getNextBirthDate();
        holder.birthYearMonthText.setText(nextYear + "年" + nextMonth + "月");
        holder.birthDateText.setText(String.valueOf(nextDate));

        String week = DealHomeBirthDate.getWeekOfDate(nextYear, nextMonth, nextDate);
        holder.birthWeekText.setText(week);

        // Extra Information
        Calendar current = Calendar.getInstance();
        current.set(nextYear, nextMonth - 1, nextDate);
        CalendarUtil util = new CalendarUtil(current);

        String lunarYear = util.cyclical();
        String animalSign = util.animalsYear();
        holder.birthLunarYearText.setText(lunarYear + "年[" + animalSign + "年]");

        String lunarMonthDate = CalendarUtil.getCurrentDay(current);
        lunarMonthDate = lunarMonthDate.substring(13, lunarMonthDate.length() - 2);
        holder.birthLunarMonthDateText.setText(lunarMonthDate);


        String constellation = DealHomeBirthDate.constellation(nextMonth, nextDate);
        holder.birthConstellationText.setText(constellation);

    }

    /**
     * [代码添加新TextView]
     *
     * @param holder     View
     * @param imageStyle 文字颜色
     */
    private void initTextView(BirthHomeViewHolder holder, int imageStyle) {
        holder.birthWeekText = addTextView(imageStyle);
        holder.birthLunarYearText = addTextView(imageStyle);
        holder.birthLunarMonthDateText = addTextView(imageStyle);
        holder.birthConstellationText = addTextView(imageStyle);

        holder.birthWeekText.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);

        holder.birthLayout.removeAllViews();
        holder.birthLayout.addView(holder.birthYearMonthText);
        holder.birthLayout.addView(holder.birthDateText);
        holder.birthLayout.addView(holder.birthWeekText);
        holder.birthLayout.addView(holder.birthLunarYearText);
        holder.birthLayout.addView(holder.birthLunarMonthDateText);
        holder.birthLayout.addView(holder.birthConstellationText);

    }

    /**
     * [新建TextView]
     *
     * @param imageStyle 文字颜色
     * @return .
     */
    private TextView addTextView(int imageStyle) {
        TextView textView = new TextView(context);
        textView.setTextColor(fontColor[imageStyle]);
        textView.setTextSize(18);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        textView.setLayoutParams(lp);

        return textView;
    }

    class BirthHomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView birthCountDownText, birthYearMonthText, birthDateText;
        private TextView birthWeekText, birthLunarYearText,
                birthLunarMonthDateText, birthConstellationText;

        private LinearLayout birthLayout;
        private LinearLayout HomePanelItemLayout;

        BirthHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            birthLayout = itemView.findViewById(R.id.birth_layout);
            birthCountDownText = itemView.findViewById(R.id.birth_count_down);
            birthYearMonthText = itemView.findViewById(R.id.birth_year_month);
            birthDateText = itemView.findViewById(R.id.birth_day);

            HomePanelItemLayout = itemView.findViewById(R.id.home_panel_item_layout);

            birthLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.birth_layout) {
                BirthBean bean = birthBeanArrayList.get(getAdapterPosition());
                if (!bean.getName().equals("无记录")) {
                    BornDay day = bean.getBirthday();
                    Intent intent = new Intent(context, BirthDetailActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("name", bean.getName());
                    bundle.putIntArray("birthday", new int[]{day.year, day.month, day.date});
                    bundle.putIntArray("nextBirth", new int[]{bean.getNextBirthYear(), bean.getNextBirthMonth(), bean.getNextBirthDate()});
                    bundle.putInt("age", bean.getAge());
                    bundle.putBoolean("isLunarBirth", bean.isLunarBirth());
                    bundle.putBoolean("isLockScreen", bean.isLockScreen());
                    bundle.putIntArray("clockTime", bean.getTipTime());

                    intent.putExtra("allMessage", bundle);
                    context.startActivity(intent);
                }
            }
        }

        /**
         * 设置item的可见性
         *
         * @param isVisible .
         */
        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }
}
