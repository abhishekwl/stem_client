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
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import io.github.abhishekwl.stemclient.Activities.MainActivity;
import io.github.abhishekwl.stemclient.Models.Test;
import io.github.abhishekwl.stemclient.R;
import java.util.ArrayList;

public class TestsRecyclerViewAdapter extends RecyclerView.Adapter<TestsRecyclerViewAdapter.TestViewHolder> {

    private ArrayList<Test> testItemArrayList;
    private String currencyCode;
    private LayoutInflater layoutInflater;

    public TestsRecyclerViewAdapter(Context context, ArrayList<Test> testItemArrayList) {
        this.testItemArrayList = testItemArrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.currencyCode = MainActivity.currency == null ? "\u20b9" : MainActivity.currency.toString();
    }

    @NonNull
    @Override
    public TestsRecyclerViewAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.test_list_item, parent, false);
        return new TestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestsRecyclerViewAdapter.TestViewHolder holder, int position) {
        Test testItem = testItemArrayList.get(position);
        holder.bind(testItem);
    }

    @Override
    public int getItemCount() {
        return testItemArrayList.size();
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

        void renderTestAddButton(Test testItem) {
            if (testItem.isTestSelected()) {
                testAddButton.setText("- Remove test");
                testAddButton.setBackground(primaryRoundedDrawable);
            } else {
                testAddButton.setText("+ Add item to cart");
                testAddButton.setBackground(accentRoundedDrawable);
            }
        }

        void bind(Test testItem) {
            if (TextUtils.isEmpty(testItem.getTestHospital().getHospitalImageUrl())) Glide.with(testNameTextView.getContext()).load(R.drawable.logo).into(hospitalImageView);
            else Glide.with(testNameTextView.getContext()).load(testItem.getTestHospital().getHospitalImageUrl()).into(hospitalImageView);
            testNameTextView.setText(Character.toUpperCase(testItem.getTestName().charAt(0))+testItem.getTestName().substring(1));
            hospitalNameTextView.setText(testItem.getTestHospital().getHospitalName());
            testPriceTextView.setText(currencyCode + " " + Double.toString(testItem.getTestPrice()));
            renderTestAddButton(testItem);
            testAddButton.setOnClickListener(v -> {
                testItem.setTestSelected(!testItem.isTestSelected());
                renderTestAddButton(testItem);
            });
        }
    }
}
