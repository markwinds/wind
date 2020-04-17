package com.bangtong.wind.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bangtong.wind.R
import com.bangtong.wind.model.BoxIfo
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.fragment_chart.*
import kotlinx.android.synthetic.main.marker_view.*
import kotlin.random.Random


/**
 * A simple [Fragment] subclass.
 */
class ChartFragment : Fragment() {

    var list:List<BoxIfo> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // chart config
        chart.setBackgroundColor(Color.WHITE)
        chart.description.isEnabled = false // 描述
        chart.setTouchEnabled(true)
        //chart.setOnChartValueSelectedListener(this)
        chart.setDrawGridBackground(false) // 网格
        val mv = MyMarkerView(requireContext(), R.layout.marker_view) // 标记
        mv.chartView = chart
        chart.marker = mv
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)

        val xAxis = chart.xAxis
        xAxis.enableGridDashedLine(10f, 10f, 0f)
        val yAxis = chart.axisLeft
        chart.axisRight.isEnabled = false // 隐藏右边竖直坐标
        yAxis.enableGridDashedLine(10f, 10f, 0f)
        yAxis.axisMaximum = 100f
        yAxis.axisMinimum = -50f
        //setData(10)
        chart.animateXY(1500,1500) // 动画速度
        val l = chart.legend
        l.form = LegendForm.LINE
        setData()
    }

    fun setData() {
//        val values: ArrayList<Entry> = ArrayList()
//        for (i in 0 until count){
//            var num = Random(i).nextFloat()*50
//            values.add(Entry(i.toFloat(),num, resources.getDrawable(R.drawable.arrow)))
//        }
        val values: ArrayList<Entry> = ArrayList()
        for (i in list.indices){
            values.add(Entry(i.toFloat(),list[i].temperature.toFloat(), resources.getDrawable(R.drawable.arrow)))
        }
        val set1: LineDataSet
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = LineDataSet(values, "temperature")
            set1.setDrawIcons(false)
            set1.enableDashedLine(10f, 5f, 0f)
            set1.color = Color.BLACK
            set1.setCircleColor(Color.BLACK)
            set1.lineWidth = 1f
            set1.circleRadius = 3f
            set1.setDrawCircleHole(false)
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            set1.valueTextSize = 9f
            set1.enableDashedHighlightLine(10f, 5f, 0f)
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { _, _ -> chart.axisLeft.axisMinimum }
            if (Utils.getSDKInt() >= 18) {
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_red)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }
            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(set1) // add the data sets
            val data = LineData(dataSets)
            chart.data = data
        }
    }

}

@SuppressLint("ViewConstructor")
class MyMarkerView(context: Context,layoutResource:Int): MarkerView(context,layoutResource){
    private var tvContent:TextView = findViewById(R.id.tvContent)
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e is CandleEntry) {
            tvContent.text = Utils.formatNumber(
                e.high,
                0,
                true
            )
        } else {
            tvContent.text = Utils.formatNumber(
                e!!.y,
                0,
                true
            )
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}


