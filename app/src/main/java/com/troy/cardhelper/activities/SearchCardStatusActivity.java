package com.troy.cardhelper.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.troy.cardhelper.R;
import com.troy.cardhelper.bean.CardInfo;
import com.troy.cardhelper.managers.Constant;
import com.troy.cardhelper.presenter.ISearchCardStatusPresenter;
import com.troy.cardhelper.presenter.impl.SearchCardStatusPresenterImpl;
import com.troy.cardhelper.views.SearchCardStatusView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlongfei on 16/2/1.
 */
public class SearchCardStatusActivity extends AppCompatActivity implements SearchCardStatusView {
    private ListView mListView;
    private TextView mTips;
    private Button mClearCard;
    private Button mAddCard;
    private Button mQuery;
    private View mClearCardView;
    private View mAddCardView;
    private View mQueryCardView;
    private ProgressDialog mProgressDialog;
    private ISearchCardStatusPresenter mPresenter;
    private CardInfoAdapter mAdapter;
    private List<CardInfo> mCardInfos = new ArrayList<CardInfo>();
    private static final int REQUESTCODE_CARD_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_card_status);
        mPresenter = new SearchCardStatusPresenterImpl(this, this);
        initViews();
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.list_card);
        mAdapter = new CardInfoAdapter();
        mListView.setAdapter(mAdapter);

        mTips = (TextView) findViewById(R.id.tv_add_card_tips);
        mTips.setVisibility(View.GONE);

        mClearCard = (Button) findViewById(R.id.btn_clear_card);
        mClearCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardInfos.clear();
                mAdapter.notifyDataSetChanged();
                mClearCardView.setVisibility(View.GONE);
                mQueryCardView.setVisibility(View.GONE);
            }
        });

        mAddCard = (Button) findViewById(R.id.btn_add_card);
        mAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchCardStatusActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUESTCODE_CARD_ID, null);
            }
        });

        mQuery = (Button) findViewById(R.id.btn_search_card);
        mQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.searchCardStatus(mCardInfos);
            }
        });

        mQueryCardView = (View) findViewById(R.id.layout_search_card);
        mQueryCardView.setVisibility(View.GONE);

        mClearCardView = (View) findViewById(R.id.layout_clear_card);
        mClearCardView.setVisibility(View.GONE);

        mAddCardView = (View) findViewById(R.id.layout_add_card);

        mProgressDialog = new ProgressDialog(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_CARD_ID) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    String cardId = bundle.getString(Constant.KEY_EXTRA_CARD_ID);
                    boolean find = false;
                    for (CardInfo cardInfo : mCardInfos) {
                        if (cardInfo.getCardId().equals(cardId)) {
                            find = true;
                            Toast.makeText(SearchCardStatusActivity.this, "请勿重复添加", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    if (!find) {
                        CardInfo newCardInfo = new CardInfo();
                        newCardInfo.setCardId(cardId);
                        newCardInfo.setStatus(getString(R.string.card_status_default));
                        mCardInfos.add(newCardInfo);
                        mAdapter.notifyDataSetChanged();
                        mQueryCardView.setVisibility(View.VISIBLE);
                        mClearCardView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void querySuccess(Map<String, String> result) {
        for (CardInfo cardInfo : mCardInfos) {
            if (result.containsKey(cardInfo.getCardId())) {
                cardInfo.setStatus(result.get(cardInfo.getCardId()));
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void queryEnd() {
        mClearCardView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoading(String tips) {
        mProgressDialog.setMessage(tips);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String tips) {
        Toast.makeText(this, tips, Toast.LENGTH_SHORT).show();
    }


    private class CardInfoAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mCardInfos == null ? 0 : mCardInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mCardInfos == null ? null : mCardInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(SearchCardStatusActivity.this).inflate(R.layout.item_card_info, null);
                viewHolder.mCardId = (TextView) convertView.findViewById(R.id.tv_card_id);
                viewHolder.mStatus = (TextView) convertView.findViewById(R.id.tv_card_status);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mCardId.setText(mCardInfos.get(position).getCardId());
            viewHolder.mStatus.setText(mCardInfos.get(position).getStatus());
            return convertView;
        }
    }


    private class ViewHolder {
        private TextView mCardId;
        private TextView mStatus;
    }
}
