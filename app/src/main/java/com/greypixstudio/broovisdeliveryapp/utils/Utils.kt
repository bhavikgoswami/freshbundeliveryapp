package com.greypixstudio.broovisdeliveryapp.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.NonNull
import com.greypixstudio.broovisdeliveryapp.R
import io.github.rupinderjeet.kprogresshud.KProgressHUD
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

open class Utils {
    companion object {

        private var hud: KProgressHUD? = null
//        private var progressDialog: SpotsDialog? = null

        fun vibratePhone(context: Context) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        200,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(200)
            }
        }

        /**+
         * show Progress dialog
         */
        fun showProgress(context: Context) {
            hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(context.getColor(R.color.color_theme))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .show()
//            progressDialog = SpotsDialog(context, R.style.Custom)
//            progressDialog?.show()
        }

        fun isProgressShowing(): Boolean {
            return if (hud != null) {
                hud?.isShowing!!
            } else {
                false
            }
        }

        fun isValidLicenceNumber(inputString: String): Boolean {
            var isValid: Boolean = false

            val regex =
                Pattern.compile(
                    "^[A-Z]{2}[0-9]{2}\\s[0-9]{11}"
                    /*"^(([A-Z]{2}[0-9]{2})"HR-0619850034761
                            + "( )|([A-Z]{2}-[0-9]"
                            + "{2}))((19|20)[0-9]"
                            + "[0-9])[0-9]{7}$"*/
                )


            if (regex.matcher(inputString).find()) {
                isValid = true
            }
            return isValid
        }


        fun isValidVerifyIFSC(ifscCode: String): Boolean {
            var isValidIFSCCode = false
            try{
                val pattern: Pattern =
                    Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}\$")
                val matcher: Matcher = pattern.matcher(ifscCode)
                isValidIFSCCode = matcher.matches()
            }catch (e:Exception){
                e.printStackTrace()
            }
            return isValidIFSCCode
        }

        fun isValidAadhaarCardNumber(adhaarNumber: String?): Boolean {

            var isValid  = false
            try {
                val regex = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$"
                val p = Pattern.compile(regex)

                if (adhaarNumber == null) {
                    return false
                }
                val m = p.matcher(adhaarNumber)
                isValid =  m.matches()

            }catch (e : Exception){
                e.printStackTrace()
            }
            return isValid
        }


        fun isValidPanrCardNumber(inputString: String): Boolean {
            var isValid: Boolean = false

            val regex =
                Pattern.compile(
                    "^[A-Z]{5}[0-9]{4}[A-Z]{1}"
                )

            if (regex.matcher(inputString).find()) {
                isValid = true
            }
            return isValid
        }

        /**+
         * hide Progress dialog
         */
        fun hideProgress() {
            hud?.dismiss()
        }

        /**
         * Show toast msg in center of the screen
         */
        fun showToast(context: Context, msg: String) {
            val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            toast.show()
        }

        /**
         * This function check Email validation
         */
        fun verifyEmail(phoneNumber: String?): Boolean {
            val pattern: Pattern =
                Pattern.compile(
                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                            "\\@" +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                            "(" +
                            "\\." +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                            ")+"
                )
            val matcher: Matcher = pattern.matcher(phoneNumber)
            return matcher.matches()
        }

        /**
         * This function check Pin Code Number validation
         */
        fun verifyPhoneNumber(phoneNumber: String): Boolean {
            val pattern: Pattern =
                Pattern.compile("^[1-9][0-9]{9}\$")
            val matcher: Matcher = pattern.matcher(phoneNumber)
            return matcher.matches()
        }

        /**
         * get Address from latitude longitude
         */


        fun getAddressFromLocation(
            context: Context,
            latitude: Double,
            longitude: Double
        ): List<Address?> {
//        https://stackoverflow.com/a/9409229
            var addresses: List<Address?>
            var geocoder = Geocoder(context, Locale.getDefault())
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            var strAddress = addresses[0]?.getAddressLine(0)
            var strCity = addresses[0]?.locality
            var strState = addresses[0]?.adminArea
            var strPostalCode = addresses[0]?.postalCode
            var strKnownName = addresses[0]?.featureName
            return addresses
//            binding.addressTxtView.setText(strAddress.plus(" ").plus(strCity).plus(" ").plus(strState).plus(" ").plus(strKnownName).plus(" ").plus(strPostalCode))
        }

        //check Internet connection
        fun checkConnection(@NonNull context: Context): Boolean {
            return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo != null
        }
    }

}