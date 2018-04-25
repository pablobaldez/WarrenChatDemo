package com.github.pablo.warrenchatdemo.views.widgets

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.Double.parseDouble
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.util.*

class CurrencyTextWatcher(private val editText: EditText) : TextWatcher {

    private val groupDivider: Char
    private val monetaryDivider: Char
    private val numberFormat: DecimalFormat
    private val fractionDigit: Int
    private val currencySymbol: String

    init {
        val locale = getDefaultLocale()
        val df = DecimalFormat.getCurrencyInstance(locale) as DecimalFormat
        val symbols = DecimalFormatSymbols.getInstance(locale)
        groupDivider = symbols.groupingSeparator
        monetaryDivider = symbols.monetaryDecimalSeparator
        currencySymbol = symbols.currencySymbol
        numberFormat = DecimalFormat(df.toPattern(), symbols)
        fractionDigit = Currency.getInstance(locale).defaultFractionDigits
    }

    @Suppress("DEPRECATION")
    private fun getDefaultLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            editText.resources.configuration.locales.get(0)
        else
            editText.resources.configuration.locale
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.isEmpty())
            return

        editText.removeTextChangedListener(this)

        /***
         * Clear input to get clean text before format
         * '\u0020' is empty character
         */
        var text = s.toString()
        text = text.replace(groupDivider, '\u0020').replace(monetaryDivider, '\u0020')
                .replace(".", "").replace(" ", "")
                .replace(currencySymbol, "").trim({ it <= ' ' })
        try {
            text = format(text)
            editText.setText(text)
            editText.setSelection(text.length)
            editText.addTextChangedListener(this)
        }
        catch (e: ParseException) { }
        catch (e: NumberFormatException) {}
    }

    override fun afterTextChanged(s: Editable) {}

    @Throws(ParseException::class, NumberFormatException::class)
    private fun format(text: String): String {
        return numberFormat.format(parseDouble(text) / Math.pow(10.0, fractionDigit.toDouble()))
    }

}