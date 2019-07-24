package ru.xsobolx.currencyexchange.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ru.xsobolx.currencyexchange.R;
import ru.xsobolx.currencyexchange.di.DependencyInjection;
import ru.xsobolx.currencyexchange.domain.model.Currency;
import ru.xsobolx.currencyexchange.presenter.CurrencyPresenter;

public class CurrencyActivity extends AppCompatActivity implements CurrencyMvpView {

    private ProgressBar progressBar;
    private CurrencyPresenter presenter;
    private ArrayAdapter<Currency> topCurrenciesAdapter;
    private ArrayAdapter<Currency> bottomCurrenciesAdapter;
    private TextView courseText;
    private TextView bottomCurrencyText;
    private EditText topCurrencyEditText;
    private Spinner topCurrencySpinner;
    private Spinner bottomCurrencySpinner;
    private RelativeLayout contentView;
    private LinearLayout emptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        initViews();
        presenter = DependencyInjection.currencyPresenter(getApplicationContext(), this);
        presenter.onAttach();
    }

    private void initViews() {
        topCurrencyEditText = findViewById(R.id.top_currency_edit_text);
        topCurrencyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onAmountChanged(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        contentView = findViewById(R.id.content_view);
        contentView.setVisibility(View.VISIBLE);
        emptyView = findViewById(R.id.empty_view);
        emptyView.setVisibility(View.GONE);
        bottomCurrencyText = findViewById(R.id.bottom_currency_text);
        courseText = findViewById(R.id.course_value);
        topCurrenciesAdapter = new ArrayAdapter<>(this, R.layout.currency_dropdown_menu_item);
        topCurrencySpinner = findViewById(R.id.top_currency_drop_down);
        topCurrencySpinner.setAdapter(topCurrenciesAdapter);
        topCurrenciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                presenter.onFromCurrencySelected(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bottomCurrenciesAdapter = new ArrayAdapter<>(this, R.layout.currency_dropdown_menu_item);
        bottomCurrenciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bottomCurrencySpinner = findViewById(R.id.bottom_currency_drop_down);
        bottomCurrencySpinner.setAdapter(bottomCurrenciesAdapter);
        bottomCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                presenter.onToCurrencySelected(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        progressBar = findViewById(R.id.progress);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setCourse(String course) {
        courseText.setText(course);
    }

    @Override
    public void showError() {
        Toast.makeText(this, "Something went wrong, try again later", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyCurrencies() {
        emptyView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
    }

    @Override
    public void showCurrencies(List<Currency> response) {
        topCurrenciesAdapter.addAll(response);
        topCurrenciesAdapter.notifyDataSetChanged();
        bottomCurrenciesAdapter.addAll(response);
        bottomCurrenciesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCalculatedValue(String result) {
        bottomCurrencyText.setText(result);
    }
}
