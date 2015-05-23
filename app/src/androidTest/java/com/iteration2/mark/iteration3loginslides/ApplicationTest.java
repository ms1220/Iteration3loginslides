/*

Claritas: ‘Clarity through innovation’

Unit Test Script

Project: SocBox

Module: Login Slides

Test Script Name: ApplicationTest.java

Associated Code File Name: MainActivity.java

Description: This class tests the functionality of the login slides Module to match the

requirements found in the User Stories and Design Specification.

Initial Authors: Mark Stonehouse

Change History:

Version: 0.1

Author: Mark Stonehouse

Change: Created original version

Date: 12/05/2015

User Story Traceabilty:

Tag(s): D/LPF – Entire Module


Requirements in not covered by test script:

D/LPF/02-15/01:

Justification: Not able to be unit tested. HMI testing complete

*/
package com.iteration2.mark.iteration3loginslides;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public Activity activity;

    public ApplicationTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        activity = getActivity();
    }

    @UiThreadTest
    public void testPanel() {
        assertEquals(true, activity instanceof Activity);
    }

     protected void tearDown() throws Exception{super.tearDown();}
}
