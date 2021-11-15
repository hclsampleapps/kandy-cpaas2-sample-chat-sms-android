package com.hcl.kandy.cpass;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hcl.kandy.cpass.activities.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


  // @Rule
    //public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    private String url = "domain.com";
	private String uName = "abc@gmail.com";
	private String pwd = "Test";
	private String client = "PUB-abc.dfg.dghfh";
	private String destination = "abc@gmail.com";


    @Test
    public void verifyMessageSentToMessageActivity() {

        ActivityScenario.launch(LoginActivity.class);
        // Types a message into a EditText element.
        onView(withId(R.id.et_url))
                .perform(typeText(url), closeSoftKeyboard());
        onView(withId(R.id.et_user_name))
                .perform(typeText(uName), closeSoftKeyboard());
        onView(withId(R.id.et_user_password))
                .perform(typeText(pwd), closeSoftKeyboard());
        onView(withId(R.id.et_user_client))
                .perform(typeText(client), closeSoftKeyboard());

        // Clicks a button to send the message to another
        // activity through an explicit intent.
        onView(withId(R.id.button_login)).perform(click());

        // Verifies that the DisplayMessageActivity received an intent
        // with the correct package name and message.

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.container)).check(matches(isDisplayed()));


        onView(withId(R.id.etDestainationAddress))
                .perform(typeText(destination), closeSoftKeyboard());
        onView(withId(R.id.btnFetchChat)).perform(click());

        onView(withId(R.id.etMessage))
                .perform(typeText("hi"), closeSoftKeyboard());
        onView(withId(R.id.btnStartChat)).perform(click());

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open()); // Open Drawer


        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sms));

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /*

    @Rule
    public IntentsTestRule<HomeActivity> intentsTestRule =
            new IntentsTestRule<>(HomeActivity.class);

    @Test
    public void verifyHome(){
        Intents.init();
        intended(allOf(
                hasComponent(hasShortClassName(".HomeActivity")),
                toPackage("com.hcl.kandy.cpass.activities"),
                hasExtraWithKey(LoginActivity.id_token)));

    }*/

}
