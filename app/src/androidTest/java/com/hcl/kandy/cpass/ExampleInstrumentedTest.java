package com.hcl.kandy.cpass;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.provider.ContactsContract;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.hcl.kandy.cpass.activities.LoginActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);
    private String url = "abcd";

    @Test
    public void verifyMessageSentToMessageActivity() {

        // Types a message into a EditText element.
        onView(withId(R.id.et_url))
                .perform(typeText(url), closeSoftKeyboard());
        onView(withId(R.id.et_user_name))
                .perform(typeText(url), closeSoftKeyboard());
        onView(withId(R.id.et_user_password))
                .perform(typeText(url), closeSoftKeyboard());
        onView(withId(R.id.et_user_client))
                .perform(typeText(url), closeSoftKeyboard());

        // Clicks a button to send the message to another
        // activity through an explicit intent.
        onView(withId(R.id.button_login)).perform(click());

        // Verifies that the DisplayMessageActivity received an intent
        // with the correct package name and message.
       /* intended(allOf(
                hasComponent(hasShortClassName(".DisplayMessageActivity")),
                toPackage(PACKAGE_NAME),
                hasExtra(MainActivity.EXTRA_MESSAGE, MESSAGE)));*/

    }

}
