package com.example.calculator_cs460;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTV, solutionTV;
    MaterialButton buttonC, buttonBOpen, buttonBClose;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonMul, buttonDiv, buttonAdd, buttonSub, buttonEqual;
    MaterialButton buttonAC, buttonDot;

    /**
     * Called when the activity is first created.
     * Sets up the user interface, assigns click listeners to buttons, and initializes
     * the necessary text views for displaying input and results.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     *                           If the activity has never existed before, the value is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resultTV = findViewById(R.id.result_tv);
        solutionTV = findViewById(R.id.solution_tv);
        assignID(buttonDot, R.id.button_decimal);


        // number buttons
        assignID(button0, R.id.button_zero);
        assignID(button1, R.id.button_one);
        assignID(button2, R.id.button_two);
        assignID(button3, R.id.button_three);
        assignID(button4, R.id.button_four);
        assignID(button5, R.id.button_five);
        assignID(button6, R.id.button_six);
        assignID(button7, R.id.button_seven);
        assignID(button8, R.id.button_eight);
        assignID(button9, R.id.button_nine);

        // PEMDAS
        assignID(buttonMul, R.id.button_multiply);
        assignID(buttonDiv, R.id.button_divide);
        assignID(buttonAdd, R.id.button_add);
        assignID(buttonSub, R.id.button_subtract);
        assignID(buttonEqual, R.id.button_equal);
        assignID(buttonBOpen, R.id.button_open_bracket);
        assignID(buttonBClose, R.id.button_close_bracket);

        // clear and all clear
        assignID(buttonAC, R.id.button_all_clear);
        assignID(buttonC, R.id.button_c);



    }

    /**
     * Assigns a button's ID and sets a click listener to it.
     *
     * @param btn The MaterialButton instance representing the button.
     * @param id  The ID resource of the button being assigned.
     */
    void assignID(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);

    }
    /**
     * Handles the click events for the calculator buttons.
     * Depending on the button pressed, it updates the calculation display,
     * clears input, or calculates the result.
     *
     * @param view The view (button) that was clicked.
     */
    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTV.getText().toString();

        if (buttonText.equals("AC")) {
            solutionTV.setText("0");
            resultTV.setText("0");
            return;
        }

        if (buttonText.equals("=")) {
            solutionTV.setText(resultTV.getText());
            return;
        }

        if (buttonText.equals("C")) {
            // Prevent trying to remove a character from an empty string
            if (!dataToCalculate.isEmpty()) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);

            }
        } else {

            dataToCalculate = dataToCalculate + buttonText;
        }

        solutionTV.setText(dataToCalculate);

        String finalResult = getResults(dataToCalculate);
        if (!finalResult.equals("Err")) {
            resultTV.setText(finalResult);
        }
    }
    /**
     * Evaluates the mathematical expression entered by the user.
     * Uses Mozilla Rhino to interpret the string as JavaScript and return the result.
     *
     * @param data The mathematical expression to evaluate, in string format.
     * @return The result of the expression as a string, or "Err" if an exception occurs.
     */
    String getResults(String data) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();

            Object result = context.evaluateString(scriptable, data, "Javascript", 1, null);

            // Check if the result is undefined or null
            if (result == Context.getUndefinedValue()) {
                return "0"; // or any default value you want
            }

            return result.toString();

        } catch (Exception e) {
            return "Err"; // Handle errors gracefully
        } finally {
            Context.exit(); // Always exit the Rhino context
        }
    }


}

























