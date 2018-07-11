package org.sssj.com.dogecoin;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class Help extends AppCompatActivity {

    private View parent_view;

    private NestedScrollView nested_scroll_view;
    private ImageButton btn_toggle_help, btn_toggle_claim, btn_toggle_withdraw, btn_toggle_fees, btn_toggle_payoutday;
    private Button btn_hide_help, btn_hide_claim, btn_hide_withdraw, btn_hide_fees, btn_hide_payoutday;
    private View lyt_expand_help, lyt_expand_claim, lyt_expand_withdraw, lyt_expand_fees, lyt_expand_payoutday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        parent_view = findViewById(android.R.id.content);


        initComponent();
    }


    private void initComponent() {

        // Help section
        btn_toggle_help = (ImageButton) findViewById(R.id.btn_toggle_help);
        btn_hide_help = (Button) findViewById(R.id.btn_hide_help);
        lyt_expand_help = (View) findViewById(R.id.lyt_expand_help);
        lyt_expand_help.setVisibility(View.GONE);

        btn_toggle_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText2(btn_toggle_help, lyt_expand_help);
            }
        });

        btn_hide_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText2(btn_toggle_help, lyt_expand_help);
            }
        });


        // Claim section
        btn_toggle_claim = (ImageButton) findViewById(R.id.btn_toggle_claim);
        btn_hide_claim = (Button) findViewById(R.id.btn_hide_claim);
        lyt_expand_claim = (View) findViewById(R.id.lyt_expand_claim);
        lyt_expand_claim.setVisibility(View.GONE);

        btn_toggle_claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText2(btn_toggle_claim,lyt_expand_claim);
            }
        });

        btn_hide_claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText2(btn_toggle_claim,lyt_expand_claim);
            }
        });

        // Withdraw section
        btn_toggle_withdraw = (ImageButton) findViewById(R.id.btn_toggle_withdraw);
        btn_hide_withdraw = (Button) findViewById(R.id.btn_hide_withdraw);
        lyt_expand_withdraw = (View) findViewById(R.id.lyt_expand_withdraw);
        lyt_expand_withdraw.setVisibility(View.GONE);

        btn_toggle_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText2(btn_toggle_withdraw,lyt_expand_withdraw);
            }
        });

        btn_hide_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText2(btn_toggle_withdraw,lyt_expand_withdraw);
            }
        });

        // Fees section
        btn_toggle_fees = (ImageButton) findViewById(R.id.btn_toggle_fees);
        btn_hide_fees = (Button) findViewById(R.id.btn_hide_fees);
        lyt_expand_fees = (View) findViewById(R.id.lyt_expand_fees);
        lyt_expand_fees.setVisibility(View.GONE);

        btn_toggle_fees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText2(btn_toggle_fees,lyt_expand_fees);
            }
        });

        btn_hide_fees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText2(btn_toggle_fees,lyt_expand_fees);
            }
        });

        // PayoutDay section
        btn_toggle_payoutday = (ImageButton) findViewById(R.id.btn_toggle_payoutday);
        btn_hide_payoutday = (Button) findViewById(R.id.btn_hide_payoutday);
        lyt_expand_payoutday = (View) findViewById(R.id.lyt_expand_payoutday);
        lyt_expand_payoutday.setVisibility(View.GONE);

        btn_toggle_payoutday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText2(btn_toggle_payoutday,lyt_expand_payoutday);
            }
        });

        btn_hide_payoutday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText2(btn_toggle_payoutday,lyt_expand_payoutday);
            }
        });


        // nested scrollview
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);
    }

    private void toggleSectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_help, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_help);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_help);
        }
    }


    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    private void toggleSectionText2(View view, View v2) {
        final View lyt_v2 = v2;
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_v2, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_v2);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_v2);
        }
    }


}
