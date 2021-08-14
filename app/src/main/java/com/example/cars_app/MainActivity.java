package com.example.cars_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.provider.CarViewModel;
import com.example.provider.Car_entity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity<mCarViewModel> extends AppCompatActivity implements View.OnTouchListener {

    //@ Week1 #1. declaration of the variable
    String activity_check = "activity_check";
    //EdiTtext
    private EditText etMaker;
    private EditText etModel;
    private EditText etYear;
    private EditText etColor;
    private EditText etSeats;
    private EditText etPrice;
    //Button
    Button btnClearItems;
//@ Week2 #1. declaration for SharedPreference

    SharedPreferences sP;
    SharedPreferences.Editor editor;
    String FILENAME = "FILENAME";

    //Strings to store editText
    String Maker;
    String Color;
    int Seats;
    int Year;
    String Model;
    double Price = 0;

    //SP Keys
    String Maker_KEY = "Maker_KEY";
    String Color_KEY = "Color_KEY";
    String Seats_KEY = "Seats_KEY";
    String Year_KEY = "Year_KEY";
    String Model_KEY = "Model_KEY";
    String Price_KEY = "Price_KEY";

    //@ Week5 #1. declaration for layout
    DrawerLayout drawerlayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Context self;
    ListView listView;
    ArrayList<String> datasource;
    ArrayAdapter<String> adapter;

    //@ Week5 option menu
    Button clear_fields;
    FloatingActionButton fab;
    //extratask
    //@ Week2 Q1#1. Create the callbacks using ( ctrl + O )
    //@ Week3 #1
//@ Week6
    ArrayList<String> itemList;

    //@ Week6 RoomDB
    private CarViewModel mCarViewModel;
    Car_entity newItem;
    //@ Week8 FireBase
    FirebaseDatabase database;
    DatabaseReference myRef;
    public String firebasePath;

    //@week 11
    private GestureDetectorCompat mDetector;

    View drawyerLayout;
    View listview;

//---------------------------------------------- end of declaration ----------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        self = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        initaialiseLayoutComponents();
        Log.d(activity_check, "onCreate");

        //@ Week3 #1 : Make a permission (with the permission declared in the manifest)
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        //@ Week3 #2.1
        //declare instance of The BroadcastReceiver that listens for
        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        //@ Week3 #2.2 : register the mBroadCastReceiver
        //@ Week3 #3 : SMSReceiver class
        /*
         * Register the broadcast handler with the intent filter that is declared in
         * class SMSReceiver @line 11
         * */
        //Which broacast receiver, what type of message that are interested
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

        setUpDatabase();
        iniFirebase();
        touchLayout();
        createDector();
    }

    private void touchLayout() {
        drawyerLayout.setOnTouchListener(this);
        listView.setOnTouchListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        restoreSharePreference();

        String Maker1 = Maker;
        String Model1 = Model;
        String Year1 = String.valueOf(Year);
        String Color1 = Color;
        String Seats1 = String.valueOf(Seats);
        String Price1 = String.valueOf(Price);
        Log.d(activity_check, "onStart -- restore data of : " + Maker1 + Model1 + Year1 + Color1 + Seats1 + Price1);

    }

    @Override
    //@ Week2 Q1#2.2
    //being re-initialized from a previously saved state, given here in savedInstanceState.
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(activity_check, "onRestoreInstanceState : ");

    }


    @Override
    //@ Week2 Q1#2.1
    //must call super to let Android save View-state data in the Bundle Object before pausing the activity
    //invoked when the activity may be temporarily destroyed,
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putInt("key1", 1);
        super.onSaveInstanceState(outState);
        Log.d(activity_check, "onSaveInstanceState : When the device rotate, view data was saved in the Bundle object");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(activity_check, "onRestart");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(activity_check, "onDestroy");

    }
//---------------------------------------------- end of Activity ----------------------------------------------


    //@ Week1 #2. initiate the layout controls
    public void initaialiseLayoutComponents() {
        //edittext
        etMaker = findViewById(R.id.etMaker);
        etYear = findViewById(R.id.etYear);
        etModel = findViewById(R.id.etModel);
        etSeats = findViewById(R.id.etSeats);
        etPrice = findViewById(R.id.etPrice);
        etColor = findViewById(R.id.etColor);


        //@Week5
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerlayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());
        fab = findViewById(R.id.fab);
        dataSourceToListview();

        //week 6
        itemList = new ArrayList<>();
        //week 11
        drawyerLayout = findViewById(R.id.drawer_layout);
        listview = findViewById(R.id.listview);
    }

    //@ Week11 #1. create gesture detector
    public void createDector() {
        mDetector = new GestureDetectorCompat(this, (GestureDetector.OnGestureListener) new MyGestureListener());
        mDetector.setOnDoubleTapListener((GestureDetector.OnDoubleTapListener) new MyGestureListener());
    }

    //@ Week1 #3. onClick function for view -- button( add new car)
    //The view is a parameter -- because it is to specify which view (which is button in this case) to trigger this method

    public void clear(View view) {
        etMaker = findViewById(R.id.etMaker);
        etMaker.setText("");

        etYear = findViewById(R.id.etYear);
        etYear.setText("0");

        etModel = findViewById(R.id.etModel);
        etModel.setText("");

        etColor = findViewById(R.id.etColor);
        etColor.setText("");

        etColor = findViewById(R.id.etModel);
        etColor.setText("");

        etSeats = findViewById(R.id.etSeats);
        etSeats.setText("0");

        etPrice = findViewById(R.id.etPrice);
        etPrice.setText("0.0");

        clearSavedData();

    }

    //@ Week2 #Function
    public void saveSharePreference() {
        //declare global variables
        //@ Week2 Q2#1.1 Access the sharePreference by declaring an object
        sP = getSharedPreferences(FILENAME, 0);
        //@ Week2 Q2#1.2 open the file for editing
        editor = sP.edit();

        //@ Week2 Q2#1.3 Retrieve the data from editText which are saved as Strings
        Maker = etMaker.getText().toString();
        Color = etColor.getText().toString();
        Seats = Integer.parseInt(etSeats.getText().toString());
        Year = Integer.parseInt((etYear.getText().toString()));
        Model = etModel.getText().toString();
        Price = Double.parseDouble(etPrice.getText().toString());
        Log.d("SP", "save " + Maker);
        //@ Week2 Q2#1.4 Put them into the editor
        //Para = key's name and the payload
        editor.putString(Maker_KEY, Maker);
        editor.putString(Color_KEY, Color);
        editor.putString(Seats_KEY, String.valueOf(Seats));
        editor.putString(Year_KEY, String.valueOf(Year));
        editor.putString(Model_KEY, Model);
        editor.putString(Price_KEY, String.valueOf(Price));

        //@ Week2 Q2#1.5 Save the edition
        editor.apply();
    }

    public void restoreSharePreference() {
        // sp is like a buffer place
        //@ Week2 Q2#2.1 Access the sharePreference by declaring an object
        sP = getSharedPreferences(FILENAME, 0);
        //@ Week2 Q2#2.2 take the data from Sp
        try {
            Seats = Integer.parseInt(sP.getString(Seats_KEY, ""));
            Year = Integer.parseInt(sP.getString(Year_KEY, ""));
            Price = Double.parseDouble(sP.getString(Price_KEY, ""));
            Maker = sP.getString(Maker_KEY,"");
            Model = sP.getString(Model_KEY,"");
            Color = sP.getString(Color_KEY,"");
        } catch (NumberFormatException ex) { // handle your exception

        }


        //@ Week2 Q2#2.3 put the data into the editText
        etMaker.setText(Maker);
        ;
        etModel.setText(Model);
        ;
        etYear.setText(String.valueOf(Year));
        ;
        etColor.setText(Color);
        ;
        etSeats.setText(String.valueOf(Seats));
        ;
        etPrice.setText(String.valueOf((int) Price));
        ;
    }

    public void clearSavedData() {
        //@ Week2 clearSharePreference
        //@ Week2 Q3#1.1 open the file for editing
        editor = sP.edit();
        //@ Week2 Q3#1.2 clear all states (data) in the sP
        editor.clear();
        //@ Week2 Q3#1.3 save the action
        editor.apply();

        //@ Week7 clearRoomDB
        deleteAllItem();

    }

    //@week5 Datasource
    public void dataSourceToListview() {

        datasource = new ArrayList<>();
        listView = findViewById(R.id.listview);

        /*What is android.R.layout.simple_list_item_1?
         * The resource ID for the layout file containing a layout to use when instantiating views.
         * */
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datasource);
        listView.setAdapter(adapter);

    }

    public void addItemToListView(View view) {
        addItemToListView();
        saveCarToDatabase();
        addValueToFirebase();
    }

    //@ Week11 2. implement onTouch Listener
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mDetector.onTouchEvent(event);
        return false;
    }


//---------------------------------------------- MyBroadCastReceiver ----------------------------------------------

    //@ Week3
    class MyBroadCastReceiver extends BroadcastReceiver {

        public void updateSMStoViews(String msg) {

            /*
             * String Tokenizer is used to parse the incoming message
             * The protocol is to have the account holder name and account number separate by a semicolon
             * */
            StringTokenizer sT = new StringTokenizer(msg, ";");
            String tag = "tag";
            Log.d(tag, msg);
            //Strings to store the tokenised Strings
            String Maker = sT.nextToken();
            String Model = sT.nextToken();
            String Year = sT.nextToken();
            String Color = sT.nextToken();
            String Seats = sT.nextToken();
            String Price = sT.nextToken();

            /*
             * Now, its time to update the UI
             * */
            etMaker.setText(Maker);
            etModel.setText(Model);
            etYear.setText(Year);
            etColor.setText(Color);
            etSeats.setText(Seats);
            etPrice.setText(Price);

        }


        //         @ Week3 #1 * This method 'onReceive' will get executed every time class SMSReceive sends a broadcast
        @Override
        public void onReceive(Context context, Intent intent) {
            /*
             * Retrieve the message from the intent
             * */
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            updateSMStoViews(msg);

        }
    }

//@ Week 5
//---------------------------------------------- Layout ----------------------------------------------

    //option menu
    //This method will be invoked to inflate the menu list.
    /*It accepts as input a reference to the menu object that should be passed to
    the inflator as shown below.*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Here you inflate the option menu
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    /*
        This callback method will be invoked each time an option item gets clicked.
        This callback gets as input a reference to the menu item that gets the click.
        What does if statement do?
             1. You have to use to extract the menu item.
             2. select the appropriate behaviour, as shown below.
    */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear_fields) {
            clear(clear_fields);
        } else if (id == R.id.remove_all_car_in_listview) {
            ClearItemsBtn();
        }
        return true;
    }

    // add items to listView
    private void getItemInfo() {
        Model = etModel.getText().toString();
        Maker = etMaker.getText().toString();
        Color = etColor.getText().toString();
        Seats = Integer.parseInt(etSeats.getText().toString());
        Year = Integer.parseInt(etYear.getText().toString());
        Price = Double.parseDouble(etPrice.getText().toString());
    }

    private void adpter2ListView() {
        saveSharePreference();

        saveCarsForCardViews();

        datasource.add(Maker + " | " + Model);
        adapter.notifyDataSetChanged();
    }

    private void addItemToListView() {
        getItemInfo();
        Toast.makeText(this, "We added a new car (" + Maker + ")", Toast.LENGTH_LONG).show();
        adpter2ListView();
    }

    //Remove All  Car
    public void ClearItemsBtn() {
        deleteAllItem();
        itemList.clear();
        datasource.clear();
        clearFirebaseValues();
        adapter.notifyDataSetChanged();
    }

    //Remove Last Car
    public void ClearLastItemsBtn() {

        if (datasource.size() > 0) {
            deleteLastCar();
            datasource.remove(datasource.size() - 1);
            adapter.notifyDataSetChanged();
        }
        if (itemList.size() > 0) {
            itemList.remove(itemList.size() - 1);
            adapter.notifyDataSetChanged();
        }

    }

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the nav_menuID of the selected item
            int nav_menuID = item.getItemId();

            if (nav_menuID == R.id.add_car) {
                addItemToListView();

            } else if (nav_menuID == R.id.remove_all_car) {
                ClearItemsBtn();
            } else if (nav_menuID == R.id.remove_last_car) {
                ClearLastItemsBtn();
            } else if (nav_menuID == R.id.list_all_items) {
                ////@ Week 6 :fetch the data from the SharePreference and pass it to the adapter
//                listAll();
                ////@ Week 7 :go to CardActivity (fetch the data from the database and pass it to the adapter.)
                listAllCarFromDatabase();
            }

            // close the drawer
            drawerlayout.closeDrawers();
            // tell the OS
            return true;
        }
    }

    //@ Week 6
    //list all cars into recycler containers presented in card view
    private void listAll() {
        //notify Android that there is an intent to go to  CardActivity.class
        Intent intent = new Intent(this, CardActivity.class);
        intent.putStringArrayListExtra("itemList", itemList);
        startActivity(intent);
    }

    private void saveCarsForCardViews() {
        itemList.add(String.format("%s,%s,%s,%s,%s,%s", Maker, Model, Price, Year, Seats, Color));
        Log.d("SP", "When I click this, the Strings of Cars will be put to Arraylist itemList " + itemList);

    }

    //@ Week 7
    public void setUpDatabase() {
        mCarViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        CarRecyclerAdapter adapter = new CarRecyclerAdapter();
        mCarViewModel.getAllCars().observe(this, newData -> {
            adapter.setmCars(newData);
            adapter.notifyDataSetChanged();
            Log.d("DEBUG", Integer.toString(adapter.getItemCount()));
        });

    }

    private void saveCarToDatabase() {
        newItem = new Car_entity(Maker, Model, Year, Color, Seats, Price);
        mCarViewModel.insert(newItem);
    }

    private void listAllCarFromDatabase() {
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);
    }

    private void deleteAllItem() {
        mCarViewModel.deleteAll();
    }

    private void deleteLastCar() {
        mCarViewModel.deleteLastCar();
    }

    //@ Week 8
    private void iniFirebase() {
        //@ Week8 firebase
        firebasePath = "car";
        //create an instance of a firebase
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(firebasePath);

        //interface to receive events about changes in the child locations of a given DatabaseReference myRef.
        myRef.addChildEventListener(new ChildEventListener() {
            //method is triggered when a new child (car) is added to the location to which this listener was added.
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                itemList.add(snapshot.child(firebasePath).getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            //method is triggered when a child is removed from the location to which this listener was added.
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addValueToFirebase() {
        myRef.push().setValue(newItem);
    }

    private void clearFirebaseValues() {
        myRef.getRef().removeValue();
    }


    //@ Week11 method for data manupulation
    private void increaseSeatBy1() {
        Seats = Seats + 1;
        etSeats.setText(String.valueOf(Seats));
    }

    private void loadDefaultValues() {

        Maker = "BMW";
        etMaker.setText(Maker);

        Model = "X7";
        etModel.setText(Model);

        Year = 2021;
        etYear.setText(String.valueOf(Year));

        Color = "Black";
        etColor.setText(Color);

        Seats = 7;
        etSeats.setText(String.valueOf(Seats));

        Price = 1500;
        etPrice.setText(String.valueOf(Price));
    }

    //@ Week11 MyGestureListener
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        //Q1
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d("gesture", "Single Tap");
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            increaseSeatBy1();
            return super.onSingleTapConfirmed(e);
        }

        //Q2
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d("gesture", "Double Tap");
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            loadDefaultValues();
            return super.onDoubleTapEvent(e);
        }

        //Q3

        //Q5

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            Log.d("gesture", String.valueOf(velocityX));

            if (Math.abs(velocityX) > 700){
                moveTaskToBack(true);
                Log.d("gesture","Fling haha");
            }
            Log.d("gesture", "Fling");

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        //Q6
        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("gesture", "Long press");
            clear(clear_fields);
            super.onLongPress(e);
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int diffY = (int) e2.getY();
            int diffX = (int) e2.getX();
            int SWIPE_THREASHOLD = 100;
            int startX = 0, endX = 0;

            if(e1.getActionMasked() == MotionEvent.ACTION_DOWN){startX = (int) e1.getX();}
            if(e1.getActionMasked() == MotionEvent.ACTION_UP){endX = (int) e1.getX();}


                if ((endX <startX)) {
                    //swipe right
                    Price = Price + distanceX;
                    Log.d("gesture", "Swipe Right");
                    if (Price <= 0 ) {
                        Price = 0;
                        etPrice.setText(String.valueOf((int) Price));}
                    else{
                        etPrice.setText(String.valueOf((int) Price));
                    }

                }
                 if(endX >startX) {
                    Log.d("gesture", "Swipe Left");
                    //swipe left
                     Price = Price - distanceX;
                    if (Price >0) {
                        etPrice.setText(String.valueOf((int) Price));
                    }else{
                        Price = 0;
                        etPrice.setText(String.valueOf((int) Price));
                    }





                Log.d("gesture", "Swipe");
            }
            return super.onScroll(e1, e2, distanceX, distanceY);

        }

}}




