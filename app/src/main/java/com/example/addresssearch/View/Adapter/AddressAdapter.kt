package com.example.addresssearch.View.Adapter

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.addresssearch.Data.Model.Address
import com.example.addresssearch.databinding.ItemAddressBinding
import java.text.Normalizer
import java.util.regex.Pattern

class AddressAdapter: RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    private var keyWord:String =""
    fun setKeyWord(kw:String){
        keyWord = kw.lowercase().trim()
    }
    private fun normalizeString(input: String): String {
        val normalized = Normalizer.normalize(input.lowercase(), Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(normalized).replaceAll("")
    }
    inner class AddressViewHolder(private val binding: ItemAddressBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(address: Address) {
            val spannable = SpannableString(address.display_name)
            if (keyWord.isNotEmpty()) {
                // Chuẩn hóa chuỗi và từ khóa để so sánh không dấu
                val normalizedText = normalizeString(address.display_name)
                val normalizedKeyWord = normalizeString(keyWord)

                // Tìm tất cả các vị trí của từ khóa trong chuỗi
                var startIndex = normalizedText.indexOf(normalizedKeyWord)
                while (startIndex >= 0) {
                    // Áp dụng highlight cho đoạn khớp
                    spannable.setSpan(
                        StyleSpan(Typeface.BOLD),
                        startIndex,
                        startIndex + keyWord.length,
                        0
                    )
                    // Tìm vị trí tiếp theo
                    startIndex = normalizedText.indexOf(normalizedKeyWord, startIndex + 1)
                }
                Log.e("checkHighlight", "Highlight positions processed for keyword: $keyWord")
            }

            binding.tvAddressName.text = spannable

        }
    }
    private val diffUtil = object : DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(
            oldItem: Address,
            newItem: Address
        ): Boolean {
            return oldItem.place_id == newItem.place_id
        }

        override fun areContentsTheSame(
            oldItem: Address,
            newItem: Address
        ): Boolean {
            return oldItem == newItem
        }

    }
    val diff = AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddressViewHolder {
        return AddressViewHolder(ItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(
        holder: AddressViewHolder,
        position: Int
    ) {
        val address = diff.currentList[position]
        holder.bind(address)
        holder.itemView.setOnClickListener {
            onClick?.invoke(address)
        }
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }
    var onClick:((Address)-> Unit)?=null

}