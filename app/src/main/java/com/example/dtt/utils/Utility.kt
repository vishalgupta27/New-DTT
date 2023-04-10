package com.example.albums.utils


import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.albums.utils.Constants.Companion.SERVER_TIMEOUT_TIME
import com.example.dtt.R

import java.lang.reflect.Method

import java.util.regex.Matcher
import java.util.regex.Pattern


object Utility {

    private const val TAG = "UtilTAG"
    fun postapi(
        context: Context?,
        url: String?,
        param: Map<String, String>,
        volleyCallback: VolleyCallback
    ) {
        val queue = Volley.newRequestQueue(context)
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            com.android.volley.Response.Listener { response -> volleyCallback.onSuccess(response) },
            com.android.volley.Response.ErrorListener { error -> Log.e(TAG, error.toString()) }) {
            override fun getParams(): Map<String, String>? {
                return param
            }
        }
        request.retryPolicy =
            DefaultRetryPolicy(
                SERVER_TIMEOUT_TIME,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        queue.add(request)
    }

    fun getAPI(
        context: Context?,
        url: String?,
        param: Map<String, String>,
        volleyCallback: VolleyCallback
    ) {
        val queue = Volley.newRequestQueue(context)
        val request: StringRequest = object : StringRequest(
            Method.GET, url,
            com.android.volley.Response.Listener { response -> volleyCallback.onSuccess(response) },
            com.android.volley.Response.ErrorListener { error -> Log.e(TAG, error.toString()) }) {
            override fun getParams(): Map<String, String>? {
                return param
            }
        }
        request.retryPolicy =
            DefaultRetryPolicy(
                SERVER_TIMEOUT_TIME,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        queue.add(request)
    }

    fun isValidEmail(emailInput: String?): Boolean {
        val emailPatterns = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(emailPatterns)
        val matcher = pattern.matcher(emailInput)
        return matcher.matches()
    }

    fun isValidPassword(password: String?): Boolean {
        val passwordPatterns = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        val pattern = Pattern.compile(passwordPatterns)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }


    fun showClosedMessage(messageToDisplay: String?, activity: Activity?) {
        val builder1 = AlertDialog.Builder(activity)
        builder1.setMessage(messageToDisplay)
        builder1.setCancelable(true)
        builder1.setPositiveButton(
            "Ok"
        ) { dialog, id -> dialog.cancel() }
        val dialog = builder1.create()
        dialog.show()
        val buttonbackground1 = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        buttonbackground1.setTextColor(Color.RED)
    }

     fun showAlertDialog(context: Context) {
        val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Alert Dialog")
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setMessage("This is an example of AlertDialog in Kotlin.")
        alertDialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Do something when OK button is clicked
            Toast.makeText(context, "Exit", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        })
        alertDialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            // Do something when Cancel button is clicked
            dialog.dismiss()
        })
        alertDialogBuilder.show()
    }




    fun getPathFromUri(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

// custom adapter for recyclerview



}
