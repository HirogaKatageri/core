package dev.hirogakatageri.sandbox.ui.main.feature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.hirogakatageri.sandbox.databinding.ItemSampleFeatureBinding
import dev.hirogakatageri.sandbox.ui.main.feature.FeatureAdapter.SampleViewHolder

class FeatureAdapter(
    private val featureList: List<FeatureModel>
) : RecyclerView.Adapter<SampleViewHolder>() {

    var clickCallback: SampleItemClickCallback? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SampleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSampleFeatureBinding.inflate(layoutInflater, parent, false)
        val holder = SampleViewHolder(binding.root, clickCallback)
        holder.binding = binding
        return holder
    }

    override fun onBindViewHolder(
        holder: SampleViewHolder,
        position: Int
    ) {
        holder.bindData(featureList[position])
    }

    override fun onViewDetachedFromWindow(holder: SampleViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.binding = null
    }

    override fun getItemCount(): Int = featureList.size

    class SampleViewHolder(
        itemView: View,
        private val callback: SampleItemClickCallback?
    ) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var binding: ItemSampleFeatureBinding? = null

        private lateinit var _model: FeatureModel

        override fun onClick(v: View?) {
            callback?.onClick(_model)
        }

        fun bindData(model: FeatureModel) {
            _model = model
            binding?.button?.setText(_model.titleRes)
            binding?.button?.setOnClickListener(this)
        }
    }

    abstract class SampleItemClickCallback {
        abstract fun onClick(model: FeatureModel)
    }
}
