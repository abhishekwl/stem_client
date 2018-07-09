package io.github.abhishekwl.stemclient.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import io.github.abhishekwl.stemclient.Activities.MainActivity;
import io.github.abhishekwl.stemclient.Models.Test;
import io.github.abhishekwl.stemclient.R;
import java.util.ArrayList;

public class CheckoutRecyclerViewPagerAdapter extends RecyclerView.Adapter<CheckoutRecyclerViewPagerAdapter.CheckoutViewHolder> {

  private ArrayList<Test> testArrayList;
  private Context context;
  private String currencyCode;
  private LayoutInflater layoutInflater;

  public CheckoutRecyclerViewPagerAdapter(Context context, ArrayList<Test> testArrayList) {
    this.context = context;
    this.testArrayList = testArrayList;
    this.layoutInflater = LayoutInflater.from(context);
    this.currencyCode = MainActivity.currency == null ? "\u20b9" : MainActivity.currency.toString();
  }

  @NonNull
  @Override
  public CheckoutRecyclerViewPagerAdapter.CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = layoutInflater.inflate(R.layout.checkout_list_item, parent, false);
    return new CheckoutViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull CheckoutRecyclerViewPagerAdapter.CheckoutViewHolder holder, int position) {
    Test test = testArrayList.get(position);

    if (TextUtils.isEmpty(test.getTestHospital().getHospitalImageUrl()))
      Glide.with(holder.itemView.getContext()).load(R.drawable.logo).into(holder.hospitalImageView);
    else Glide.with(holder.itemView.getContext()).load(test.getTestHospital().getHospitalImageUrl()).into(holder.hospitalImageView);
    holder.itemNameTextView.setText(Character.toUpperCase(test.getTestName().charAt(0)) + test.getTestName().substring(1));
    holder.hospitalNameTextView.setText(test.getTestHospital().getHospitalName());
    holder.testPriceTextView.setText(currencyCode+" "+Double.toString(test.getTestPrice()));
  }

  @Override
  public int getItemCount() {
    return testArrayList.size();
  }

  class CheckoutViewHolder extends ViewHolder {

    @BindView(R.id.checkoutListItemTestName) TextView itemNameTextView;
    @BindView(R.id.checkoutListItemHospitalName) TextView hospitalNameTextView;
    @BindView(R.id.checkoutListItemTestPrice) TextView testPriceTextView;
    @BindView(R.id.checkoutListItemHospitalImageView) ImageView hospitalImageView;

    CheckoutViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
