package io.github.abhishekwl.stemclient.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar.BaseCallback;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.firebase.auth.FirebaseAuth;
import io.github.abhishekwl.stemclient.Models.Test;
import io.github.abhishekwl.stemclient.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CheckoutActivity extends AppCompatActivity {

  private static final int LOAD_PAYMENT_REQUEST_CODE = 897;
  @BindView(R.id.checkoutRecyclerView)
  RecyclerView checkoutRecyclerView;
  @BindView(R.id.checkoutPayButton)
  Button checkoutPayButton;

  private Unbinder unbinder;
  private ArrayList<Test> testArrayList;
  private FirebaseAuth firebaseAuth;
  private PaymentsClient paymentsClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_checkout);

    unbinder = ButterKnife.bind(CheckoutActivity.this);
    initializeViews();
  }

  private void initializeViews() {
    testArrayList = getIntent().getParcelableArrayListExtra("TESTS");
    if (testArrayList==null || testArrayList.isEmpty()) {
      Snackbar.make(checkoutRecyclerView, "Please select tests to be added to cart", Snackbar.LENGTH_SHORT)
          .addCallback(new BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
              super.onDismissed(transientBottomBar, event);
              finish();
            }
          });
    }
    else {
      firebaseAuth = FirebaseAuth.getInstance();
      paymentsClient = Wallet.getPaymentsClient(CheckoutActivity.this, new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build());
    }
  }

  private void isReadyToPay(double totalCost) {
    IsReadyToPayRequest request =
        IsReadyToPayRequest.newBuilder()
            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
            .build();
    Task<Boolean> task = paymentsClient.isReadyToPay(request);
    task.addOnCompleteListener(
        task1 -> {
          try {
            boolean result = task1.getResult(ApiException.class);
            if (result) {
              PaymentDataRequest paymentDataRequest = createPaymentDataRequest(totalCost);
              if (paymentDataRequest!=null) {
                AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(paymentDataRequest), CheckoutActivity.this, LOAD_PAYMENT_REQUEST_CODE);
              }
            } else {
              notifyMessage("Google cannot be used as a payment option :(");
            }
          } catch (ApiException exception) {
            notifyMessage(exception.getMessage());
          }
        });
  }

  private void notifyMessage(String message) {
    Snackbar.make(checkoutRecyclerView, message, Snackbar.LENGTH_SHORT).show();
  }

  private PaymentDataRequest createPaymentDataRequest(double totalCost) {
    PaymentDataRequest.Builder request =
        PaymentDataRequest.newBuilder()
            .setTransactionInfo(
                TransactionInfo.newBuilder()
                    .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                    .setTotalPrice(Double.toString(totalCost))
                    .setCurrencyCode("INR")
                    .build())
            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
            .setCardRequirements(
                CardRequirements.newBuilder()
                    .addAllowedCardNetworks(
                        Arrays.asList(
                            WalletConstants.CARD_NETWORK_OTHER,
                            WalletConstants.CARD_CLASS_DEBIT,
                            WalletConstants.CARD_CLASS_CREDIT,
                            WalletConstants.CARD_NETWORK_VISA,
                            WalletConstants.CARD_NETWORK_MASTERCARD))
                    .build());

    PaymentMethodTokenizationParameters params =
        PaymentMethodTokenizationParameters.newBuilder()
            .setPaymentMethodTokenizationType(
                WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
            .addParameter("gateway", "example")
            .addParameter("gatewayMerchantId", "exampleGatewayMerchantId")
            .build();

    request.setPaymentMethodTokenizationParameters(params);
    return request.build();
  }

  @OnClick(R.id.checkoutPayButton)
  public void onPayButtonPressed() {
    double totalCost = 0;
    for (Test test: testArrayList) totalCost+=test.getTestPrice();
    isReadyToPay(totalCost);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case LOAD_PAYMENT_REQUEST_CODE:
        switch (resultCode) {
          case RESULT_OK:
            PaymentData paymentData = PaymentData.getFromIntent(data);
            String token = Objects.requireNonNull(Objects.requireNonNull(paymentData).getPaymentMethodToken()).getToken();
            break;
          case RESULT_CANCELED:
            notifyMessage("Operation cancelled by user");
            break;
          case AutoResolveHelper.RESULT_ERROR:
            Status status = AutoResolveHelper.getStatusFromIntent(data);
            break;
        }
    }
  }

  @Override
  protected void onDestroy() {
    unbinder.unbind();
    super.onDestroy();
  }
}
