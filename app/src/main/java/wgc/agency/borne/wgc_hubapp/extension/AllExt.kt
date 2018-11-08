package wgc.agency.borne.wgc_hubapp.extension

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.File
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.io.FileOutputStream
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Natasa on 11/8/18.
 */

//================================================================================
// Activity
//================================================================================


fun Activity.hideKeyboard() {
    currentFocus?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

fun Activity.screenSize(): Point {
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

fun Activity.contentLayout(): ViewGroup {
    return findViewById(android.R.id.content)
}

fun Activity.setFullScreenMode(lightStatusBar: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && lightStatusBar)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    else
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}


//================================================================================
// Button
//================================================================================


fun Button.enable(color: Int? = null, backgroundRes: Int? = null) {
    color?.let { setTextColor(color) }
    backgroundRes?.let { setBackgroundResource(backgroundRes) }
    isClickable = true
}

fun Button.disable(color: Int? = null, backgroundRes: Int? = null) {
    color?.let { setTextColor(color) }
    backgroundRes?.let { setBackgroundResource(backgroundRes) }
    isClickable = false
}


//================================================================================
// Collections
//================================================================================


fun List<Int>.toIntArray(): Array<Int> {
    return Array(size) { i -> this[i] }
}


//================================================================================
// Context
//================================================================================


fun Context.screenSize(): Point {
    val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

fun Context.showKeyboard(editText: EditText) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.hideKeyboard(editText: EditText) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(editText.windowToken, 0)
}

fun Context.color(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.startEmailIntent(email: String) {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
    startActivity(intent)
}

fun Context.startDialIntent(phone: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phone")
    startActivity(intent)
}

fun Context.startWebIntent(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(if (url.contains("http", true)) url else "https://$url")
    startActivity(intent)
}

//fun Context.startAppSettingsIntent() {
//    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID))
//    startActivity(intent)
//}

fun Context.cameraOrExternalStoragePermissionIsGranted(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
}

fun Context.shortToast(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.shortToast(text: String) {
    if (text.isNotBlank()) Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
}

fun Context.longToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun round(num: Double?): String? {

    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING

    var roundNum=df.format(num)
    return roundNum
}

fun Context.clearEmptyFiles() {
    val imageStorageDir = Environment.getExternalStoragePublicDirectory("Unifi/Unifi Images")
    if (imageStorageDir.exists()) {
        val files = imageStorageDir.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (file.length() == 0L) file.delete()
            }
        }
    }

    val videoStorageDir = Environment.getExternalStoragePublicDirectory("Unifi/Unifi Video")
    if (videoStorageDir.exists()) {
        val files = videoStorageDir.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (file.length() == 0L) file.delete()
            }
        }
    }
}

fun Context.galleryAddMedia(mediaUri: Uri) {
    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    mediaScanIntent.data = mediaUri
    sendBroadcast(mediaScanIntent)
}

fun Context.imageFile(): File? {
    val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
    val imageFileName = "gowithYamo$timeStamp"
    val storageDir = Environment.getExternalStoragePublicDirectory("gowithYamo/gowithYamo Images")
    storageDir.mkdirs()
    return File.createTempFile(imageFileName,  ".jpg", storageDir)
}

fun Context.videoFile(): File? {
    val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
    val imageFileName = "gowithYamo$timeStamp"
    val storageDir = Environment.getExternalStoragePublicDirectory("gowithYamo/gowithYamo Video")
    storageDir.mkdirs()
    return File.createTempFile(imageFileName,  ".mp4", storageDir)
}

fun Context.saveBitmap(bitmap: Bitmap?, imageFile: File?) {
    var outputStream: FileOutputStream? = null
    try {
        outputStream = FileOutputStream(imageFile)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
    } catch (ex: Exception) {
        ex.printStackTrace()
    } finally {
        outputStream?.let { outputStream.close() }
    }
}

//fun Context.logOut() {
//    OneSignal.sendTag("id", "")
//    OneSignal.sendTag("email", "")
//    LoginManager.getInstance().logOut()
//    SharedPrefManager.default.clearAuthorizationData()
//    startActivity(LoginActivity.intent(this))
//}


//================================================================================
// DateTime
//================================================================================


fun DateTime.stringForPattern(pattern: String): String {
    val dateTimeFormatter = DateTimeFormat.forPattern(pattern)
    return dateTimeFormatter.print(this)
}

fun DateTime.formattedDateSocials(): String {
    return "${dayOfMonth.formattedDay()} ${stringForPattern("MMMM yyyy")}"
}


//================================================================================
// EditText
//================================================================================


fun EditText.stringText(): String {
    return text.toString()
}


//================================================================================
// Float
//================================================================================


fun Float.dpFromPx(context: Context): Int {
    return (this / context.resources.displayMetrics.density).toInt()
}

fun Float.pxFromDp(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}

fun Float.miles(): String {
    return "${"%.1f".format(this)} miles"
}


//================================================================================
// Integer
//================================================================================


fun Int.formattedDay(): String {
    return when {
        this != 11 && this % 10 == 1 -> "${this}st"
        this != 12 && this % 10 == 2 -> "${this}nd"
        this != 13 && this % 10 == 3 -> "${this}rd"
        else -> "${this}th"
    }
}


//================================================================================
// Long
//================================================================================


fun Long.formattedDuration(): String {
    val dateTimeFormatter = DateTimeFormat.forPattern("mm:ss")
    return dateTimeFormatter.print(this)
}


//================================================================================
// SpannableString
//================================================================================


fun SpannableString.withClickableSpan(context: Context, clickablePartRes: Int, onClickListener: () -> Unit, color:Int): SpannableString {
    val clickableSpan = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint?) {
            ds?.color = context.color(color)
            ds?.typeface = Typeface.DEFAULT_BOLD
            ds?.isUnderlineText = false
        }
        override fun onClick(widget: View?) = onClickListener.invoke()
    }
    val clickablePart = context.getString(clickablePartRes)
    val clickablePartStart = indexOf(clickablePart)
    setSpan(clickableSpan, clickablePartStart, clickablePartStart + clickablePart.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun SpannableString.withBoldPart(boldPart: String?): SpannableString {
    if (boldPart != null ) setSpan(StyleSpan(android.graphics.Typeface.BOLD), indexOf(boldPart), boldPart.length, 0);
    return this
}

fun SpannableString.withClickableSpans(context: Context, clickablePartRes1: Int, onClickListener1: () -> Unit, clickablePartRes2: Int, onClickListener2: () -> Unit, color:Int): SpannableString {
    val clickableSpan1 = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint?) {
            ds?.color = context.color(color)
            ds?.typeface = Typeface.DEFAULT_BOLD
            ds?.isUnderlineText = false
        }
        override fun onClick(widget: View?) = onClickListener1.invoke()
    }
    val clickablePart1 = context.getString(clickablePartRes1)
    val clickablePartStart1 = indexOf(clickablePart1)
    setSpan(clickableSpan1, clickablePartStart1, clickablePartStart1 + clickablePart1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    val clickableSpan2 = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint?) {
            ds?.color = context.color(color)
            ds?.typeface = Typeface.DEFAULT_BOLD
            ds?.isUnderlineText = false
        }
        override fun onClick(widget: View?) = onClickListener2.invoke()
    }
    val clickablePart2 = context.getString(clickablePartRes2)
    val clickablePartStart2 = indexOf(clickablePart2)
    setSpan(clickableSpan2, clickablePartStart2, clickablePartStart2 + clickablePart2.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return this
}


//================================================================================
// String
//================================================================================


fun String.emailValid(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun String.phoneValid(): Boolean {
    return android.util.Patterns.PHONE.matcher(this).matches()
}

fun String.shortToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun String.longToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}

fun String.dateTimeForPattern(pattern: String): DateTime {
    val dateTimeFormatter = DateTimeFormat.forPattern(pattern)
    return dateTimeFormatter.parseDateTime(this)
}

fun String.urlPath(): String {
    return this.replace("\\u0020", "%20").replace(" ", "%20")
}

fun String.urlMedia(): String {
    return when {
        this.contains("http") || this.contains("file://") -> this
        else -> "https://$this"
    }
}

fun String.formattedPrice(): String {
    return if (this.isBlank()) "" else "%.2f".format(this.toDouble())
}


//================================================================================
// TextView
//================================================================================


fun TextView.enable(color: Int? = null, backgroundRes: Int? = null) {
    color?.let { setTextColor(color) }
    backgroundRes?.let { setBackgroundResource(backgroundRes) }
    isClickable = true
}

fun TextView.disable(color: Int? = null, backgroundRes: Int? = null) {
    color?.let { setTextColor(color) }
    backgroundRes?.let { setBackgroundResource(backgroundRes) }
    isClickable = false
}


//================================================================================
// View
//================================================================================


fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.activity(): Activity? {
    var context = context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}


//================================================================================
// ViewGroup
//================================================================================


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}