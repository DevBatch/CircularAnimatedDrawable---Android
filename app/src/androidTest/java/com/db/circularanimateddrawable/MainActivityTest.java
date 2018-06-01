package com.db.circularanimateddrawable;

import android.test.ActivityInstrumentationTestCase2;

import com.db.circularanimateddrawable.widgets.CircularProgressButton;

/**
 * Instrumentation Testing of the CircularProgressButton using JUnit
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mTestActivity;
    private CircularProgressButton circularProgressButton;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mTestActivity = this.getActivity();
        circularProgressButton = mTestActivity.findViewById(R.id.progressButton);

    }

    public void testPreconditions() {
        assertNotNull("mPath is null", circularProgressButton);
    }
}