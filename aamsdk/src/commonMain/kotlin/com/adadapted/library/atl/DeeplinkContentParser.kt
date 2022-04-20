package com.adadapted.library.atl

import android.net.Uri
import android.util.Base64
import com.adadapted.library.constants.AddToListTypes
import com.adadapted.library.constants.DeeplinkFields
import com.adadapted.library.constants.EventStrings
import io.ktor.utils.io.core.String
import org.json.JSONException
import org.json.JSONObject

class DeeplinkContentParser {
    @Throws(Exception::class)
    fun parse(uri: Uri?): AddItContent {
        //AppEventClient.getInstance().trackError(EventStrings.ADDIT_NO_DEEPLINK_RECEIVED, NO_DEEPLINK_URL)
        if (uri == null) throw Exception(NO_DEEPLINK_URL)
        val data = uri.getQueryParameter("data")
        val decodedData = Base64.decode(data, Base64.DEFAULT)
        val jsonString = String(decodedData)
        try {
            val atlItems: MutableList<AddToListItem> = ArrayList()
            val jsonObject = JSONObject(jsonString)
            val payloadId = if (jsonObject.has(DeeplinkFields.PayloadId)) jsonObject.getString(DeeplinkFields.PayloadId) else ""
            val message = if (jsonObject.has(DeeplinkFields.PayloadMessage)) jsonObject.getString(DeeplinkFields.PayloadMessage) else ""
            val image = if (jsonObject.has(DeeplinkFields.PayloadImage)) jsonObject.getString(DeeplinkFields.PayloadImage) else ""
            val urlPath = uri.path

            when {
                urlPath != null && urlPath.endsWith("addit_add_list_items") -> {
                    val detailListItems = jsonObject.getJSONArray(DeeplinkFields.DetailedListItems)
                    for (i in 0 until detailListItems.length()) {
                        val item = detailListItems[i] as JSONObject
                        atlItems.add(parseItem(item))
                    }
                    return createDeeplinkContent(payloadId, message, image, AddToListTypes.ADD_TO_LIST_ITEMS, atlItems)
                }
                urlPath != null && urlPath.endsWith("addit_add_list_item") -> {
                    val detailListItem = jsonObject.getJSONObject(DeeplinkFields.DetailedListItem)
                    atlItems.add(parseItem(detailListItem))
                    return createDeeplinkContent(payloadId, message, image, AddToListTypes.ADD_TO_LIST_ITEM, atlItems)
                }
                //getInstance().trackError(EventStrings.ADDIT_UNKNOWN_PAYLOAD_TYPE, "Unknown payload type: " + uri.path, errorParams)
                else -> {
                    val errorParams: MutableMap<String, String> = HashMap()
                    errorParams["url"] = uri.toString()
                    //getInstance().trackError(EventStrings.ADDIT_UNKNOWN_PAYLOAD_TYPE, "Unknown payload type: " + uri.path, errorParams)
                    throw Exception("Unknown payload type")
                }
            }

        } catch (ex: JSONException) {
            val errorParams: MutableMap<String, String> = HashMap()
            errorParams["payload"] = "{\"raw\":\"$data\", \"parsed\":\"$jsonString\"}"
            ex.message?.let { errorParams.put(EventStrings.EXCEPTION_MESSAGE, it) }
            //getInstance().trackError(EventStrings.ADDIT_PAYLOAD_PARSE_FAILED, "Problem parsing Deeplink JSON input", errorParams)
            throw Exception("Problem parsing content payload")
        }
    }

    private fun parseItem(itemJson: JSONObject): AddToListItem {
        return AddToListItem(
            parseField(itemJson, DeeplinkFields.TrackingId),
            parseField(itemJson, DeeplinkFields.ProductTitle),
            parseField(itemJson, DeeplinkFields.ProductBrand),
            parseField(itemJson, DeeplinkFields.ProductCategory),
            parseField(itemJson, DeeplinkFields.ProductBarCode),
            parseField(itemJson, DeeplinkFields.ProductSku),
            parseField(itemJson, DeeplinkFields.ProductDiscount), //discount to ID temp swap
            parseField(itemJson, DeeplinkFields.ProductImage)
        )
    }

    private fun parseField(itemJson: JSONObject, fieldName: String): String {
        return try {
            itemJson.getString(fieldName)
        } catch (ex: JSONException) {
            val errorParams: MutableMap<String, String> = HashMap()
            ex.message?.let { errorParams.put(EventStrings.EXCEPTION_MESSAGE, it) }
            errorParams["field_name"] = fieldName
            //getInstance().trackError(EventStrings.ADDIT_PAYLOAD_FIELD_PARSE_FAILED, "Problem parsing Deeplink JSON input field $fieldName", errorParams)
            ""
        }
    }

    private fun createDeeplinkContent(
        payloadId: String,
        message: String,
        image: String,
        type: Int,
        items: List<AddToListItem>
    ): AddItContent {
        return AddItContent(
            payloadId,
            message,
            image,
            type,
            AddToListContent.Sources.OUT_OF_APP,
            AddItContent.AddItSources.DEEPLINK,
            items
        )
    }

    companion object {
        private const val NO_DEEPLINK_URL = "Did not receive a deeplink url."
    }
}