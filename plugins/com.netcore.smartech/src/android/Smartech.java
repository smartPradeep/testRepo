package com.netcore.plugin;

import android.content.Context;

import in.netcore.smartechgcm.NetcoreSDK;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Smartech extends CordovaPlugin {
   String identity = "";
   JSONObject newPayload, profileDetail;
   String senderId = "";

   @Override
   public boolean execute(String action, JSONArray data, final CallbackContext callbackContext) throws JSONException {
      final Context context = this.cordova.getActivity().getApplicationContext();
      if (action.equals("track") && (data.getJSONObject(0) != null)) {
         JSONObject newData = data.getJSONObject(0);
         int eventId = Integer.parseInt(newData.getString("eventId"));
         if(newData.has("identity")){
            identity = newData.getString("identity");
         }
         NetcoreSDK.setCurrentActivity1(cordova.getActivity());
         switch(eventId) {
             case 0 :
                if(newData.has("profile")){
                   profileDetail = newData.getJSONObject("profile");
                }
                NetcoreSDK.profile(context, identity, profileDetail);
                break;
             case 22 :
                NetcoreSDK.login( context, identity);
                break;
             case 23 :
                NetcoreSDK.logout( context, identity);
                break;
            case 25 :
               if(newData.has("applicationId")){
                  if(newData.has("senderId")){
                     senderId = newData.getString("senderId");
                  }
                  String applicationId = newData.getString("applicationId");
                  NetcoreSDK.register(cordova.getActivity().getApplication(), applicationId, senderId, identity);
               }
               break;
            default:
                if(newData.has("payload")){
                   newPayload = newData.getJSONObject("payload");
                }
                NetcoreSDK.track(context, identity, eventId, newPayload.toString());
                break;
         }
         String message = "Tracking done for Activity Id "+eventId;
         callbackContext.success(message);
         return true;
      } else {
         String message = "Activity Id is not passed";
         callbackContext.error(message);
         return false;
      }
   }
}
