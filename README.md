[![Netcore Logo](https://netcore.in/wp-content/themes/netcore/img/Netcore-new-Logo.png)](http:www.netcore.in)

# Netcore iOS SDK
Smartech iOS SDK


## Integration using CocoaPod
1. Install CocoaPods on your computer.

2. Open your project and create pod file using below command
```swift
pod init
```
3. Add following line in your podfile
```swift
pod 'Netcore-Smartech-iOS-SDK'
```

4. Run following command in your project directory
```swift
pod install
```

5. Open App.xcworkspace and build.

## NetCore Manual Integration
1. Download iOS SDK and Unzip the file. Open Framework folder - inside it you will
see NetCorePush.framework file.
2. Open existing or create a new project in Xcode and drag drop or add framework
in Target > Embedded Binaries section
3. Add following frameworks inside your application if required
```swift
Security
CoreLocation
SystemConfiguration
JavaScriptCore 
```
4. Add Following capability inside your application
```swift
Push Notification
Keychain
Background Mode -> Background fetch
App Groups(Add one group with name “group.com.Smartech.com”)
```
5. Create Bridge file in existing swift project if required and add Following code inside file.
```objc
#import <NetCorePush/NetCorePush.h>
```

## NetCore SDK Initialization
1. Import following file in App Delegate File
```swift
import UserNotifications
import UserNotificationsUI
import NetCorePush
```
2. Add NetCore Application AppID in support in Finish Launching Methods
(AppDelegate file)
```swift
let netCore_AppID = "your App Id which you get from Netcore smartech admin
panel"

// Set up NetCore Application Id-------------------------------------
NetCoreSharedManager.sharedInstance().handleApplicationLaunchEvent(launchOptions, forApplicationId: netCore_AppID)

//set up push delegate
NetCorePushTaskManager.sharedInstance().delegate = self

// set up your third party framework initialization process as per their document
```
3. Register Device With NetCore SDK (AppDelegate file)
```swift
func application ( _ application : UIApplication,
didRegisterForRemoteNotificationsWithDeviceToken deviceToken : Data) {

    //strEmail = your application identity
    // Register User Device with NetCore
    NetCoreInstallation.sharedInstance().netCorePushRegisteration(strEmail as! String!, withDeviceToken: deviceToken) {     (status) in }

}
```

4. Handle Push/Local Notification Delegate Events (AppDelegate file)
```swift
func application ( _ application : UIApplication, didReceiveRemoteNotification
userInfo : [ AnyHashable : Any ]) {
    // perform notification received/click action as per third party SDK as per their document
    NetCorePushTaskManager.sharedInstance().didReceiveRemoteNotification(userInfo)
}

func application (_ application : UIApplication , didReceive notification :
UILocalNotification ){
    NetCorePushTaskManager.sharedInstance().didReceiveLocalNotification(notification.userInfo)
}

extension AppDelegate: UNUserNotificationCenterDelegate {
    // called when application is open when user click on notification
    @objc (userNotificationCenter: didReceiveNotificationResponse :withCompletionHandler:)
    @available ( iOS 10.0 , * )
    func userNotificationCenter ( _ center : UNUserNotificationCenter, didReceive
    response : UNNotificationResponse, withCompletionHandler completionHandler :
    @escaping () -> Void ) {
        // perform notification received/click action as per third party SDK as per their document
        NetCorePushTaskManager.sharedInstance().userNotificationdidReceive(response)
        }
    }
    
```
5. Handle Deep Linking
```swift
func application(_ application: UIApplication, open url: URL, sourceApplication: String?, annotation: Any) -> Bool {
    if url.absoluteString.lowercased().contains ("your app deep link") {
        // handle deep link here
    }
    return true
}

extension AppDelegate : NetCorePushTaskManagerDelegate {
func handleNotificationOpenAction(_ userInfo: [AnyHashable : Any]!, deepLinkType strType: String!) {
    if strType .lowercased().contains ("your app deep link"){
        // handle deep link here
     }
}
```
6. Login with NetCore
```swift
// strEmail = pass your device identity
NetCoreInstallation.sharedInstance().netCorePushLogin(strEmail) {(statusCode:Int) in }
```
7. Logout
```swift
// strEmail = pass your device identity
NetCoreInstallation.sharedInstance().netCorePushLogout { (statusCode:Int) in }
```
8. Profile Push
```swift
// strEmail = pass your device identity
let info = ["name":"Tester", "age":"23", "mobile":"9898948849"]

NetCoreInstallation.sharedInstance().netCoreProfilePush(strEmail, payload: ino, block: nil)
```
9. Events Tracking:
Following is the list of tracking events
```swift
tracking_PageBrowse = 1,
tracking_AddToCart = 2,
tracking_CheckOut = 3,
tracking_CartExpiry = 4,
tracking_RemoveFromCart = 5,
tracking_FirstLaunch = 20,
tracking_AppLaunch = 21
```
You can use this events following ways

10. Track normal event
```swift
// for sending application launch event
NetCoreAppTracking.sharedInstance().sendEvent(Int(UInt32(tracking_AppLaunch.rawValue)), block: nil)
```
11. Track event with custom payload
```swift
//add To cart event with custom array of data
NetCoreAppTracking.sharedInstance().sendEvent(withCustomPayload:Int(UInt32(tracking_PageBrowse.rawValue)), payload: arrayAddToCart , block: nil)#
```
12. To fetch delivered push notifications
```swift
let array : Array = netCoreSharedManager.sharedInstance().getNotifications()
```

### Deployment Over Apple Store
Add Following runscript in your application target ,when you are deploying application
over apple store,this run script use remove unused architecture in release mode
```swift
echo "Target architectures: $ARCHS"
APP_PATH="${TARGET_BUILD_DIR}/${WRAPPER_NAME}"

find "$APP_PATH" -name '*.framework' -type d | while read -r FRAMEWORK

do
FRAMEWORK_EXECUTABLE_NAME=$(defaults read "$FRAMEWORK/Info.plist" CFBundleExecutable)
FRAMEWORK_EXECUTABLE_PATH="$FRAMEWORK/$FRAMEWORK_EXECUTABLE_NAME"

echo "Executable is $FRAMEWORK_EXECUTABLE_PATH"
echo $(lipo -info $FRAMEWORK_EXECUTABLE_PATH)

FRAMEWORK_TMP_PATH="$FRAMEWORK_EXECUTABLE_PATH-tmp"

# remove simulator's archs if location is not simulator's directory
case "${TARGET_BUILD_DIR}" in
*"iphonesimulator")
echo "No need to remove archs"
;;
*)

if $(lipo $FRAMEWORK_EXECUTABLE_PATH -verify_arch "i386") ; then
lipo -output $FRAMEWORK_TMP_PATH -remove "i386"

$FRAMEWORK_EXECUTABLE_PATH
echo "i386 architecture removed"
rm $FRAMEWORK_EXECUTABLE_PATH
mv $FRAMEWORK_TMP_PATH $FRAMEWORK_EXECUTABLE_PATH

fi
if $(lipo $FRAMEWORK_EXECUTABLE_PATH -verify_arch "x86_64") ; then
lipo -output $FRAMEWORK_TMP_PATH -remove "x86_64"

$FRAMEWORK_EXECUTABLE_PATH
echo "x86_64 architecture removed"
rm $FRAMEWORK_EXECUTABLE_PATH
mv $FRAMEWORK_TMP_PATH $FRAMEWORK_EXECUTABLE_PATH
fi
;;
esac

echo "Completed for executable $FRAMEWORK_EXECUTABLE_PATH"
echo $(lipo -info $FRAMEWORK_EXECUTABLE_PATH)
done
```
