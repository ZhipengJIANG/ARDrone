/*
 * Copyright 2010 Cliff L. Biffle.  All Rights Reserved.
 * Use of this source code is governed by a BSD-style license that can be found
 * in the LICENSE file.
 */
package lib.ardrone.listeners;

public interface AttitudeListener {

    void attitudeUpdated(float pitch, float roll, float yaw, int altitude);
}
