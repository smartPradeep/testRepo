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

5. Add Following capability inside your application
```swift
Push Notification
Keychain
Background Mode -> Remote Notification
```

6. Open App.xcworkspace and build.

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
Background Mode -> Remote Notification
App Groups -> Create new group with name “group.com.Smartech.com”
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
let netCore_AppID = "your App Id which you get from Netcore smartech admin panel"
// Set up NetCore  Application Id
NetCoreSharedManager.sharedInstance().handleApplicationLaunchEvent(launchOptions, forApplicationId: netCore_AppID)

//set up push delegate
NetCorePushTaskManager.sharedInstance().delegate = self
```
3. Register Device With NetCore SDK (AppDelegate file)
```swift
func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {

//Identity must be “”(blank) or as per Primary key which defined on smartech Panel
NetCoreInstallation.sharedInstance().netCorePushRegisteration(Identity, withDeviceToken: deviceToken) { (status) in
        }
}
```

4. Handle Push/Local Notification Delegate Events (AppDelegate file)
```swift
func application ( _ application : UIApplication, didReceiveRemoteNotification userInfo : [ AnyHashable : Any ]) {
    // perform notification received/click action as per third party SDK as per their document
    NetCorePushTaskManager.sharedInstance().didReceiveRemoteNotification(userInfo)
}

func application (_ application : UIApplication , didReceive notification : UILocalNotification ){
    NetCorePushTaskManager.sharedInstance().didReceiveLocalNotification(notification.userInfo)
}
```
```swift
 // called when application is open when user click on notification
extension AppDelegate: UNUserNotificationCenterDelegate {
   
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
```
```swift
//For Handling deep link
extension AppDelegate : NetCorePushTaskManagerDelegate {
func handleNotificationOpenAction(_ userInfo: [AnyHashable : Any]!, deepLinkType strType: String!) {
    if strType .lowercased().contains ("your app deep link"){
        // handle deep link here
     }
   }
}
```
6. Handle Interactive buttons
```swift
func application(_ application: UIApplication, handleActionWithIdentifier identifier:
String?, forRemoteNotification userInfo: [AnyHashable : Any], withResponseInfo
responseInfo: [AnyHashable : Any], completionHandler: @escaping () -> Void) {
 NetCorePushTaskManager.sharedInstance().handleAction(withIdentifier:
 identifier, forRemoteNotification: userInfo, withResponseInfo: responseInfo)
 completionHandler()
}
```
7. Login with NetCore
```swift
// Identity must be “”(blank) or as per Primary key which defined on smartech Panel
NetCoreInstallation.sharedInstance().netCorePushLogin(Identity) {(statusCode:Int) in }
```
8. Logout
```swift
NetCoreInstallation.sharedInstance().netCorePushLogout { (statusCode:Int) in }
```
9. Profile Push
```swift
// Identity must be “”(blank) or as per Primary key which defined on smartech Panel
let info = ["name":"Tester", "age":"23", "mobile":"9898948849"]

NetCoreInstallation.sharedInstance().netCoreProfilePush(Identity, payload: ino, block: nil)
```
10. Events Tracking:
Following is the list of tracking events
```swift
tracking_PageBrowse = 1,
tracking_AddToCart = 2,
tracking_CheckOut = 3,
tracking_CartExpiry = 4,
tracking_RemoveFromCart = 5,
```
You can use this events following ways

11. Track normal event
```swift
// for sending application launch event
NetCoreAppTracking.sharedInstance().sendEvent(Int(UInt32(tracking_AppLaunch.rawValue)), block: nil)
```
12. Track event with custom payload
```swift
//add To cart event with custom array of data
NetCoreAppTracking.sharedInstance().sendEvent(withCustomPayload:Int(UInt32(tracking_PageBrowse.rawValue)), payload: arrayAddToCart , block: nil)#
```
13. To fetch delivered push notifications
```swift
let notificationArray : Array = NetCoreSharedManager.sharedInstance().getNotifications()
```
14. Integrating Rich Push Notifications into App
```swift
i) Add “Notification Service Extension” to your app. File->New->Target- >Notification Service Extension.
<For details refer SDK-Integration-Steps.pdf>
       
ii) Click Next and when asked to “Activate”, Click Activate.

iii) Add “App Groups” to your apps Capabilities(Add one group with name “group.com.Smartech.com”).

iv) Enable App groups in Service Extension too and select group with name “group.com.Smartech.com”.
<For details refer SDK-Integration-Steps.pdf>
```
15. Implementing Rich Push Notifications into App

Remove all the code written in “NotificationService” implementation part.

i) Import NetCore Framework into Extension
```swift
import NetCorePush
```
ii) Handle Notification Request
```swift
override func didReceive(_ request: UNNotificationRequest, withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void) {
NetCoreNotificationService.sharedInstance().didReceive(request) { (contentToDeliver:UNNotificationContent) in
contentHandler(contentToDeliver) }
}
```
iii) Handle Notification Service Time Expire
```swift
override func serviceExtensionTimeWillExpire() { 
NetCoreNotificationService.sharedInstance().serviceExtensionTimeWillExpire() 
}
```
<For details refer SDK-Integration-Steps.pdf>

```
### Deployment Over Apple Store
Add Following runscript in your application target ,when you are deploying application
over apple store,this run script use remove unused architecture in release mode
```swift
APP_PATH="${TARGET_BUILD_DIR}/${WRAPPER_NAME}"

# This script loops through the frameworks embedded in the application and
# removes unused architectures.
find "$APP_PATH" -name '*.framework' -type d | while read -r FRAMEWORK
do
    FRAMEWORK_EXECUTABLE_NAME=$(defaults read "$FRAMEWORK/Info.plist" CFBundleExecutable)
    FRAMEWORK_EXECUTABLE_PATH="$FRAMEWORK/$FRAMEWORK_EXECUTABLE_NAME"
    echo "Executable is $FRAMEWORK_EXECUTABLE_PATH"

    EXTRACTED_ARCHS=()

    for ARCH in $ARCHS
    do
        echo "Extracting $ARCH from $FRAMEWORK_EXECUTABLE_NAME"
        lipo -extract "$ARCH" "$FRAMEWORK_EXECUTABLE_PATH" -o "$FRAMEWORK_EXECUTABLE_PATH-$ARCH"
        EXTRACTED_ARCHS+=("$FRAMEWORK_EXECUTABLE_PATH-$ARCH")
    done

    echo "Merging extracted architectures: ${ARCHS}"
    lipo -o "$FRAMEWORK_EXECUTABLE_PATH-merged" -create "${EXTRACTED_ARCHS[@]}"
    rm "${EXTRACTED_ARCHS[@]}"

    echo "Replacing original executable with thinned version"
    rm "$FRAMEWORK_EXECUTABLE_PATH"
    mv "$FRAMEWORK_EXECUTABLE_PATH-merged" "$FRAMEWORK_EXECUTABLE_PATH"

done
```
