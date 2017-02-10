package com.zyj.mvptest;

import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import com.zyj.mvptest.ui.VideoListActivity;

import org.junit.runner.RunWith;


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
class TestSubject extends InstrumentationTestCase {

    public void testPublishSubject() {

        launchActivity("com.zyj.mvptest", VideoListActivity.class, null);
    }

}
