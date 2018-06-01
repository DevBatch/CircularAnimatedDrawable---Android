package com.db.circularanimateddrawable;

import com.db.circularanimateddrawable.widgets.CircularProgressButton;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


/**
 * Unit Testing of the CircularProgressButton using Mockito Framework
 */
@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testCircularProgressButton() throws Exception {
        final MainActivity activity = Mockito.spy(new MainActivity());
        final CircularProgressButton circularProgressButton = Mockito.mock(CircularProgressButton.class);

        assertNotNull(activity);
        assertNotNull("Circular Progress Button", circularProgressButton);

    }

}