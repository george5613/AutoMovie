package com.george5613.test.helper;

import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

public class GestureHelper {

    /*
        locus: the center of the touch gesture, the point that fingers are pinching away from or towards. They won't actually touch this point though
        startRadius: distance from center that fingers begin at
        endRadius: distance from center that fingers end at
        pinchAngle: at what angle the fingers pinch around the locus, in degrees. 0 for vertical pinch, 90 for horizontal pinch
        duration: the total amount of time the pinch gesture will take
    */
    private static Collection<Sequence> zoom(Point locus, int startRadius, int endRadius, int pinchAngle, Duration duration) {
        // convert degree angle into radians. 0/360 is top (12 O'clock).
        double angle = Math.PI / 2 - (2 * Math.PI / 360 * pinchAngle);

        // create the gesture for one finger
        Sequence fingerAPath = zoomSingleFinger("fingerA", locus, startRadius, endRadius, angle, duration);

        // flip the angle around to the other side of the locus and get the gesture for the second finger
        angle = angle + Math.PI;
        Sequence fingerBPath = zoomSingleFinger("fingerB", locus, startRadius, endRadius, angle, duration);

        return Arrays.asList(fingerAPath, fingerBPath);
    }

    /*
        Used by the `zoom` method, for creating one half of a zooming pinch gesture.
        This will return the tough gesture for a single finger, to be put together with
        another finger action to complete the gesture.
        fingerName: name of this input finger for the gesture. Used by automation system to tell inputs apart
        locus: the center of the touch gesture, the point that fingers are pinching away from or towards. They won't actually touch this point though
        startRadius: distance from center that fingers begin at
        endRadius: distance from center that fingers end at
        angle: at what angle the fingers pinch around the locus, in radians.
        duration: the total amount of time the pinch gesture will take
     */
    private static Sequence zoomSingleFinger(String fingerName, Point locus, int startRadius, int endRadius, double angle, Duration duration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, fingerName);
        Sequence fingerPath = new Sequence(finger, 0);

        double midpointRadius = startRadius + (endRadius > startRadius ? 1 : -1) * 20;

        // find coordinates for starting point of action (converting from polar coordinates to cartesian)
        int fingerStartx = (int) Math.floor(locus.x + startRadius * Math.cos(angle));
        int fingerStarty = (int) Math.floor(locus.y - startRadius * Math.sin(angle));

        // find coordinates for first point that pingers move quickly to
        int fingerMidx = (int) Math.floor(locus.x + (midpointRadius * Math.cos(angle)));
        int fingerMidy = (int) Math.floor(locus.y - (midpointRadius * Math.sin(angle)));

        // find coordinates for ending point of action (converting from polar coordinates to cartesian)
        int fingerEndx = (int) Math.floor(locus.x + endRadius * Math.cos(angle));
        int fingerEndy = (int) Math.floor(locus.y - endRadius * Math.sin(angle));

        // move finger into start position
        fingerPath.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), fingerStartx, fingerStarty));
        // finger comes down into contact with screen
        fingerPath.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        // finger moves a small amount very quickly
        fingerPath.addAction(finger.createPointerMove(Duration.ofMillis(1), PointerInput.Origin.viewport(), fingerMidx, fingerMidy));
        // pause for a little bit
        fingerPath.addAction(new Pause(finger, Duration.ofMillis(100)));
        // finger moves to end position
        fingerPath.addAction(finger.createPointerMove(duration, PointerInput.Origin.viewport(), fingerEndx, fingerEndy));
        // finger lets up, off the screen
        fingerPath.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        return fingerPath;
    }

    /*
    Simplified method for zooming in.
    Defaults to a 45 degree angle for the pinch gesture.
    Defaults to a duration of 25ms
    Fingers start 200px from locus
    locus: the center of the pinch action, fingers move away from here
    distance: how far fingers move outwards, starting 100px from the locus
     */
    public static Collection<Sequence> zoomIn(Point locus, int distance) {
        return zoom(locus, 200, 200 + distance, 45, Duration.ofMillis(25));
    }

    /*
    Simplified method for zooming out.
    Defaults to a 45 degree angle for the pinch gesture.
    Defaults to a duration of 25ms
    Fingers finish 200px from locus
    locus: the center of the pinch action, fingers move towards here
    distance: how far fingers move inwards, they will end 100px from the locus
     */
    public static Collection<Sequence> zoomOut(Point locus, int distance) {
        return zoom(locus, 200 + distance, 200, 45, Duration.ofMillis(25));
    }

    public static Point getCenter(Rectangle rect) {
        return new Point(rect.x + rect.getWidth() / 2, rect.y + rect.getHeight() / 2);
    }
}
