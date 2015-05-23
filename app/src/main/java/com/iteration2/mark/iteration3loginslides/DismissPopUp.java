/*
Claritas: ‘Clarity through innovation’

Project: SocBox

Module: login slides

Code File Name: DismissPopUp.java

Primary Module Code File Name: MainActivity.java

Description: dismiss' popup background

Initial Authors: Mark Stonehouse

Change History:

Version: 0.1

Author: Mark Stonehouse

Change: Created original version

Date: 23/05/2015

Additional Information:

None

 Todo: Full screen functionality,  Orientation-based viewing.

*/
package com.iteration2.mark.iteration3loginslides;

import android.widget.PopupWindow;
/**
 * Created by Mark on 23/05/2015.
 */
public class DismissPopUp implements PopupWindow.OnDismissListener {
    protected android.widget.PopupWindow window2;

    public DismissPopUp (android.widget.PopupWindow pop2)
    {
        this.window2 = pop2;
    }
    public void onDismiss()
    {
        // Close both popups
        this.window2.dismiss();
    }

}
