package com.longthay.quicksqlite.model

import android.util.Log
import org.json.JSONObject


/**
 * Created by Long Thay on 03/01/2019.
 * Concung.com
 * long.thay@concung.com
 */
class QSModel {

    var id: Long = 0
    var title: String = ""
    var subtitle: String = ""

    companion object {

        val jsonScript = "{\n" +
                "          \"PFileID\": 3301,\n" +
                "          \"PostID\": 1813,\n" +
                "          \"FileName\": \"\",\n" +
                "          \"FilePath\": \"https://wfservice.concung.com/data/PostFeed/2018/12/1813/2948_98aa8641-2ba8-4ca4-973e-846d7f9ae9c7.jpg\",\n" +
                "          \"FileTypeID\": 1,\n" +
                "          \"Description\": \"\",\n" +
                "          \"CreatedUser\": 0,\n" +
                "          \"CreatedUserName\": null,\n" +
                "          \"ImagePath\": null,\n" +
                "          \"CreatedDate\": 0,\n" +
                "          \"IsDeleted\": false,\n" +
                "          \"LikeNumber\": 0,\n" +
                "          \"CommentNumber\": 0,\n" +
                "          \"Liked\": 0\n" +
                "        }"

        fun getNames() {
            val jsonObject = JSONObject(jsonScript)
            val keys = jsonObject.keys()

            for (key in keys) {
                Log.d("AAAAAA", key)

                if (jsonObject.get(key) == null) {
                    // String
                    Log.d("AAAAAA", "string null")
                } else {
                    if (jsonObject.get(key) is Long) {
                        Log.d("AAAAAA", "long - " + jsonObject.get(key).toString())
                    } else if (jsonObject.get(key) is Int) {
                        Log.d("AAAAAA", "int - " + jsonObject.get(key).toString())
                    } else if (jsonObject.get(key) is Boolean) {
                        Log.d("AAAAAA", "Boolean - " + jsonObject.get(key).toString())
                    } else if (jsonObject.get(key) is Float) {
                        Log.d("AAAAAA", "Float - " + jsonObject.get(key).toString())
                    } else if (jsonObject.get(key) is Double) {
                        Log.d("AAAAAA", "Double - " + jsonObject.get(key).toString())
                    } else {
                        Log.d("AAAAAA", "String - " + jsonObject.get(key).toString())
                    }
                }
            }
        }
    }

}