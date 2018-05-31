package io.github.abhishekwl.stemclient.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.abhishekwl.stemclient.Activities.MainActivity;
import io.github.abhishekwl.stemclient.Models.TestItem;
import io.github.abhishekwl.stemclient.R;

public class TestsRecyclerViewAdapter extends RecyclerView.Adapter<TestsRecyclerViewAdapter.TestViewHolder> {

    private ArrayList<TestItem> testItemArrayList;
    private ArrayList<TestItem> allItemsArrayList;
    private LayoutInflater layoutInflater;
    private String currencyCode;
    private CustomFilter customFilter;

    public TestsRecyclerViewAdapter(Context context, ArrayList<TestItem> testItemArrayList) {
        this.testItemArrayList = testItemArrayList;
        this.allItemsArrayList = testItemArrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.currencyCode = MainActivity.currency == null ? "\u20b9" : MainActivity.currency.toString();
        this.customFilter = new CustomFilter(TestsRecyclerViewAdapter.this);
    }

    @NonNull
    @Override
    public TestsRecyclerViewAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.test_list_item, parent, false);
        return new TestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestsRecyclerViewAdapter.TestViewHolder holder, int position) {
        TestItem testItem = testItemArrayList.get(position);

        if (TextUtils.isEmpty(testItem.getHospitalImageUrl()))
            Glide.with(holder.itemView.getContext()).load(R.drawable.logo).into(holder.hospitalImageView);
        else
            Glide.with(holder.itemView.getContext()).load(testItem.getHospitalImageUrl()).into(holder.hospitalImageView);
        holder.testNameTextView.setText(testItem.getTestName());
        holder.hospitalNameTextView.setText(testItem.getHospitalName());
        holder.testPriceTextView.setText(currencyCode + " " + Integer.toString(testItem.getTestPrice()));

        renderTestAddButton(testItem, holder);
        holder.testAddButton.setOnClickListener(v -> {
            testItem.setTestSelected(!testItem.isTestSelected());
            renderTestAddButton(testItem, holder);
        });
    }

    private void renderTestAddButton(TestItem testItem, TestViewHolder holder) {
        if (testItem.isTestSelected()) {
            holder.testAddButton.setText("- Remove test");
            holder.testAddButton.setBackground(holder.accentRoundedDrawable);
        } else {
            holder.testAddButton.setText("+ Add item to cart");
            holder.testAddButton.setBackground(holder.primaryRoundedDrawable);
        }
    }

    @Override
    public int getItemCount() {
        return testItemArrayList.size();
    }

    public CustomFilter getCustomFilter() {
        return customFilter;
    }

    public void setCustomFilter(CustomFilter customFilter) {
        this.customFilter = customFilter;
    }

    class TestViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.testListItemTestName)
        TextView testNameTextView;
        @BindView(R.id.testListItemHospitalName)
        TextView hospitalNameTextView;
        @BindView(R.id.testListItemHospitalImageView)
        ImageView hospitalImageView;
        @BindView(R.id.testListItemTestPrice)
        TextView testPriceTextView;
        @BindView(R.id.testListItemAddButton)
        Button testAddButton;
        @BindDrawable(R.drawable.border_accent_rounded)
        Drawable accentRoundedDrawable;
        @BindDrawable(R.drawable.border_primary_rounded)
        Drawable primaryRoundedDrawable;

        TestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public String toString() {
            return testNameTextView.getText().toString();
        }
    }

    public class CustomFilter extends Filter {

        private TestsRecyclerViewAdapter testsRecyclerViewAdapter;

        public CustomFilter(TestsRecyclerViewAdapter testsRecyclerViewAdapter) {
            super();
            this.testsRecyclerViewAdapter = testsRecyclerViewAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            testItemArrayList.clear();
            FilterResults filterResults = new FilterResults();
            if (constraint.length()==0) testItemArrayList.addAll(allItemsArrayList);
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TestItem testItem: allItemsArrayList) {
                    if (testItem.getTestName().toLowerCase().contains(filterPattern)) testItemArrayList.add(testItem);
                }
            }
            filterResults.values = testItemArrayList;
            filterResults.count = testItemArrayList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }
}
