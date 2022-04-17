package christinehicaro.a309demo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private double remainingBalance = 50.00;
    private HashMap<String, Integer> basket = new HashMap<>();
    private double currentBalance = 50.00;

    //TODO add prices for each food item
    private static final double CONST_PRICE = 1.50;
    private static final String[] FOOD_ITEMS = {
            "apple",
            "banana",
            "orange",
            "grapes",
            "carrot",
            "corn",
            "peas",
            "lettuce",
            "bread",
            "cereal",
            "oatmeal",
            "pasta",
            "beef",
            "chicken",
            "tuna",
            "eggs"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Grocery Shopping Simulator");

        // Sets remainingBalance to user's current balance.
        currentBalance = remainingBalance;

        TextView remainingBalanceTextView = (TextView) findViewById(R.id.remaining_balance);
        remainingBalanceTextView.setText("Your remaining balance: $"
                + String.format("%.2f", remainingBalance));

        insertItemsIntoEmptyBasket();

        // Set onClick listeners for fruits.
        findViewById(R.id.apple).setOnClickListener(this);
        findViewById(R.id.banana).setOnClickListener(this);
        findViewById(R.id.orange).setOnClickListener(this);
        findViewById(R.id.grapes).setOnClickListener(this);

        // Set onClick listeners for vegetables.
        findViewById(R.id.carrot).setOnClickListener(this);
        findViewById(R.id.lettuce).setOnClickListener(this);
        findViewById(R.id.peas).setOnClickListener(this);
        findViewById(R.id.corn).setOnClickListener(this);

        // Set onClick listeners for carbs.
        findViewById(R.id.bread).setOnClickListener(this);
        findViewById(R.id.cereal).setOnClickListener(this);
        findViewById(R.id.oatmeal).setOnClickListener(this);
        findViewById(R.id.pasta).setOnClickListener(this);

        // Set onClick listeners for protein.
        findViewById(R.id.beef).setOnClickListener(this);
        findViewById(R.id.chicken).setOnClickListener(this);
        findViewById(R.id.eggs).setOnClickListener(this);
        findViewById(R.id.tuna).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (remainingBalance >= CONST_PRICE) {
            remainingBalance -= CONST_PRICE;
            refreshBasket();
            switch(v.getId()) {
                case R.id.apple:
                    updateBasket("apple");
                    break;
                case R.id.banana:
                    updateBasket("banana");
                    break;
                case R.id.orange:
                    updateBasket("orange");
                    break;
                case R.id.grapes:
                    updateBasket("grapes");
                    break;
                case R.id.carrot:
                    updateBasket("carrot");
                    break;
                case R.id.lettuce:
                    updateBasket("lettuce");
                    break;
                case R.id.peas:
                    updateBasket("peas");
                    break;
                case R.id.corn:
                    updateBasket("corn");
                    break;
                case R.id.bread:
                    updateBasket("bread");
                    break;
                case R.id.cereal:
                    updateBasket("cereal");
                    break;
                case R.id.oatmeal:
                    updateBasket("oatmeal");
                    break;
                case R.id.pasta:
                    updateBasket("pasta");
                    break;
                case R.id.beef:
                    updateBasket("beef");
                    break;
                case R.id.chicken:
                    updateBasket("chicken");
                    break;
                case R.id.tuna:
                    updateBasket("tuna");
                    break;
                case R.id.eggs:
                    updateBasket("eggs");
                    break;
                default:
                    break;
            }
            TextView remainingBalanceTextView = (TextView) findViewById(R.id.remaining_balance);
            remainingBalanceTextView.setText("Your remaining balance: $"
                    + String.format("%.2f", remainingBalance));
            TextView basketTextView = (TextView) findViewById(R.id.basket);
            basketTextView.setText(basketResults());
        }
    }

    private void updateBasket(String item) {
        basket.put(item, basket.get(item) + 1);
    }

    private void refreshBasket() {
        TextView basketTextView = (TextView) findViewById(R.id.basket);
        String basketList = basketTextView.getText().toString();
        if (basketList != null && basketList != "") {
            String[] basketItems = basketList.split("\n");
            List<String> foodItems = new ArrayList<>();
            Collections.addAll(foodItems, FOOD_ITEMS);
            for (String item : basketItems) {
                String[] result = item.split(" ");
                Integer num = Integer.parseInt(result[0]);
                String name = result[1];
                if (!foodItems.contains(name)
                        ) {
                    // Removes the s if there is more than 1 of that item.
                    name = name.substring(0, name.length() - 1);
                }
                basket.put(name, num);
                foodItems.remove(name);
            }
            for (String remainingItem : foodItems) {
                basket.put(remainingItem, 0);
            }
        }
    }

    private String basketResults() {
        Iterator iterator = basket.entrySet().iterator();
        String result = "";
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            if (!pair.getValue().equals(0)) {
                result += pair.getValue();
                result += " " + pair.getKey().toString();
                if (!pair.getValue().equals(1) &&
                        pair.getKey().toString().charAt(pair.getKey().toString().length() - 1) != 's') {
                    result += "s";
                }
                result += "\n";
            }
            iterator.remove();
        }
        return result;
    }

    private void insertItemsIntoEmptyBasket() {
        //TODO eventually use API to retrieve suggestions for each category.
        for (String foodItem: FOOD_ITEMS) {
            basket.put(foodItem, 0);
        }
    }
}

