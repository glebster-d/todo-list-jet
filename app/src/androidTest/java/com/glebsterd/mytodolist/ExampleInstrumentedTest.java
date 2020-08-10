package com.glebsterd.mytodolist;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

//import static org.junit.jupiter.api.Assertions.assertEquals;



/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleInstrumentedTest {
    //@Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        //assertEquals("com.glebsterd.mytodolist", appContext.getPackageName());
    }
}
