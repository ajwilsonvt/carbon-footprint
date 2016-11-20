package com.example.app.carbonfootprint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
    int selection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        String[] list = getResources().getStringArray(R.array.activities);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        dropdown.setAdapter(adapter);

        final Button calc = (Button)findViewById(R.id.button);
        calc.setVisibility(View.INVISIBLE);

        final EditText userNum = (EditText)findViewById(R.id.editText);
        final TextView showResult = (TextView)findViewById(R.id.textView2);
        final TextView status = (TextView)findViewById(R.id.textView3);
        final TextView question = (TextView)findViewById(R.id.textView4);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                userNum.setText("");
                showResult.setText("");
                status.setText("");

                selection = position;
                if (selection > 0) {
                    Log.i("spinner", "selection made");
                    calc.setVisibility(View.VISIBLE);
                }

                switch (selection) {
                    case 0: //empty
                        question.setText("");
                        calc.setVisibility(View.INVISIBLE);
                        break;
                    case 1: //driving
                        question.setText("How many miles did you drive?");
                        break;
                    case 2: //bus
                    case 3: //subway or train
                    case 4: //flight
                        question.setText("How many miles did you travel?");
                        break;
                    case 5: //electricity uses
                        question.setText("How many kWh did you use?");
                        break;
                    case 6: //eating beef, pork, or lamb
                    case 7: //eating fruits or vegetables
                        question.setText("How many ounces?");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        calc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.i("calc", "button clicked");

                int number = -1;
                if (!isEmpty(userNum)) {
                    number = Integer.parseInt(userNum.getText().toString());
                }

                if (0 <= number && number <= 4000) {
                    int result = 0;
                    String info = "grams of CO2 equivalents emitted";

                    //diff calc for each case
                    switch (selection) {
                        case 1: //driving
                            result = calculate(number, 444);
                            info = "grams of CO2 equivalents emitted, assuming 20 MPG";
                            break;
                        case 2: //bus
                            result = calculate(number, 107);
                            break;
                        case 3: //subway or train
                            result = calculate(number, 163);
                            break;
                        case 4: //flight
                            if (number < 400) {
                                result = calculate(number, 254);
                            }
                            else if (number < 1500) {
                                result = calculate(number, 204);
                            }
                            else if (number < 3000) {
                                result = calculate(number, 181);
                            }
                            else {
                                result = calculate(number, 172);
                            }
                            break;
                        case 5: //electricity uses
                            result = calculate(number, 835);
                            info = "grams of CO2 equivalents emitted, and the average person in the US uses 12 kWh per day";
                            break;
                        case 6: //eating beef, pork, or lamb
                            result = calculate(number, 445);
                            break;
                        case 7: //eating fruits or vegetables
                            result = calculate(number, 50);
                            break;
                    }
                    showResult.setText(getString(R.string.showResult, (int) result));
                    status.setText(info);
                }
                else {
                    showResult.setText(getString(R.string.empty));
                    status.setText("Type a number [0, 4000]");
                }
            }
        });
    }

    private int calculate(int input, int multiplier) {
        int result = 0;
        result = input * multiplier;
        return result;
    }

    private boolean isEmpty(EditText e) {
        return e.getText().toString().trim().length() == 0;
    }
}
