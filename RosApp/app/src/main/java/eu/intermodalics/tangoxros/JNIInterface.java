/*
 * Copyright 2016 Intermodalics All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.intermodalics.tangoxros;

import android.app.Activity;
import android.os.IBinder;
import android.util.Log;

/**
 * Interfaces between native C++ code and Java code.
 */
public class JNIInterface {
    static {
        if (TangoInitializationHelper.loadTangoSharedLibrary() ==
                TangoInitializationHelper.ARCH_ERROR) {
            Log.e("TangoJNINative", "ERROR! Unable to load libtango_client_api.so!");
        }
        System.loadLibrary("tango_ros_android");
    }

    /**
     * Initializes ROS given the ros master URI and the device IP address.
     * @param masterUri URI of the ROS master, format is __master:=http://local-desktop:11311.
     * @param ipAddress IP address of the device, format is __ip:=192.168.168.185.
     * @return true is initialization was successful.
     */
    public static native boolean initRos(String masterUri, String ipAddress);

    /**
     * @return true if ROS is OK, false if it has shut down.
     */
    public static native boolean isRosOk();

    /**
     * Initializes the tango-ros node. Specifically it:
     *   * creates the node and its publishers.
     *   * check that the tango version is correct.
     * initRos should always be called before.
     * @param callerActivity the caller activity of this function.
     */
    public static native boolean initNode(Activity callerActivity, PublisherConfiguration publisherConfiguration);

    /**
     * Called when the Tango service is connected successfully.
     *
     * @param nativeTangoServiceBinder The native binder object.
     */
    public static native boolean onTangoServiceConnected(IBinder nativeTangoServiceBinder);

    /**
     * Disconnects from Tango.
     */
    public static native void tangoDisconnect();

    /**
     * Publishes the available tango data (device pose, point cloud, images).
     */
    public static native void publish();
}