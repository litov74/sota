package com.development.sota.scooter.ui.doc

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import kotlinx.android.synthetic.main.item_agreement.view.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException


class LoginUserAgreementAdapter :
    RecyclerView.Adapter<LoginUserAgreementAdapter.LoginUserAgreementViewHolder>() {
    private val data = arrayListOf(
        Pair(R.string.privacy_policy_label, R.raw.privacy_policy_data),
        Pair(R.string.user_agreement_label, R.raw.user_agreement_data),
        Pair(R.string.accession_agreement_label, R.raw.accession_agreement_data),
        Pair(R.string.refund_conditions_label, R.raw.refund_conditions_data)
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LoginUserAgreementViewHolder = LoginUserAgreementViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_agreement, parent, false)
    )


    private fun getStringFromRawRes(rawRes: Int, context: Context): String? {
        val inputStream: InputStream
        inputStream = try { context.getResources().openRawResource(rawRes)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            return null
        }
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        try {
            while (inputStream.read(buffer).also { length = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, length)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                inputStream.close()
                byteArrayOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val resultString: String
        resultString = try {
            byteArrayOutputStream.toString("UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }
        return resultString
    }

    override fun onBindViewHolder(holder: LoginUserAgreementViewHolder, position: Int) =
        holder.run {
            holder.itemView.textViewLoginAgreementLabel.setText(data[position].first)
            holder.itemView.textViewLoginAgreementData.setText(getStringFromRawRes(data[position].second, holder.itemView.context))
        }

    override fun getItemCount(): Int = data.size


    inner class LoginUserAgreementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}