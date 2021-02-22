package com.example.health

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.huawei.hihealth.error.HiHealthError
import com.huawei.hihealth.listener.ResultCallback
import com.huawei.hihealthkit.HiHealthDataQuery
import com.huawei.hihealthkit.HiHealthDataQueryOption
import com.huawei.hihealthkit.auth.HiHealthAuth
import com.huawei.hihealthkit.auth.HiHealthOpenPermissionType
import com.huawei.hihealthkit.data.HiHealthPointData
import com.huawei.hihealthkit.data.HiHealthSetData
import com.huawei.hihealthkit.data.store.HiHealthDataStore
import com.huawei.hihealthkit.data.store.HiRealTimeListener
import com.huawei.hihealthkit.data.type.HiHealthPointType
import com.huawei.hihealthkit.data.type.HiHealthSetType
import kotlinx.android.synthetic.main.fragment_excersice.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ExcersiceFragment : Fragment() {
    companion object {
        const val TAG = "MainActivity"
    }

    private var btn11: Button? = null
    private var btn13: Button? = null
    private lateinit var textCountStepNumber: TextView
    private lateinit var queryCountStepNumber: TextView
    private lateinit var stepCountImageView: ImageView
    private lateinit var weightCountImageView: ImageView
    private lateinit var weightCountTextView: TextView
    private lateinit var qeuryImageView: ImageView
    private lateinit var calorieImageView: ImageView
    private lateinit var textCalorieTextView: TextView
    private lateinit var sleepImageView: ImageView
    private lateinit var sleepTextView: TextView
    private lateinit var spo2ImageView: ImageView
    private lateinit var spo2TextView: TextView
    private lateinit var heartbeatImageView: ImageView
    private lateinit var heartTextView: TextView
    private lateinit var button_start: Button
    private lateinit var button_stop: Button
    private lateinit var requestDataButton: Button
    private lateinit var uploadDataButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_excersice, container, false)



        btn11 = view.findViewById(R.id.btn_click_11)
        stepCountImageView = view.findViewById(R.id.steps)
        textCountStepNumber = view.findViewById(R.id.txt_steps)
        queryCountStepNumber = view.findViewById(R.id.txt_query_steps)
        weightCountImageView = view.findViewById(R.id.weight)
        weightCountTextView = view.findViewById(R.id.txt_weight)
        qeuryImageView = view.findViewById(R.id.query)
        calorieImageView = view.findViewById(R.id.calorie)
        textCalorieTextView = view.findViewById(R.id.txt_calorie)
        sleepImageView = view.findViewById(R.id.sleep)
        sleepTextView = view.findViewById(R.id.txt_sleep)
        spo2TextView = view.findViewById(R.id.txt_spo2)
        spo2ImageView = view.findViewById(R.id.spo2)
        heartbeatImageView = view.findViewById(R.id.iv_heart)
        heartTextView = view.findViewById(R.id.txt_heart_rate)
        button_start = view.findViewById(R.id.btn_start)
        button_stop = view.findViewById(R.id.btn_stop)
        requestDataButton = view.findViewById(R.id.btn_data)
        uploadDataButton = view.findViewById(R.id.btn_upload)


        requestDataButton.setOnClickListener {


            val button_start = view.findViewById<View>(R.id.btn_start)
            button_start.performClick()

            val textCounter = view.findViewById<View>(R.id.steps)
            textCounter.performClick()

            val queryCounter = view.findViewById<View>(R.id.query)
            queryCounter.performClick()

            val calorieCounter = view.findViewById<View>(R.id.calorie)
            calorieCounter.performClick()

            val sleepCounter = view.findViewById<View>(R.id.sleep)
            sleepCounter.performClick()

            val spo2couter = view.findViewById<View>(R.id.spo2)
            spo2couter.performClick()

            val weightCounter = view.findViewById<View>(R.id.weight)
            weightCounter.performClick()

            uploadDataButton.visibility = Button.VISIBLE

            val uploadButton = view.findViewById<View>(R.id.btn_upload)
            uploadButton.performClick()


        }

        uploadDataButton.setOnClickListener {
            val result =
                "Your Hear Rate is " + heartTextView.text.toString() +
                        " Your Spo2 is " + spo2TextView.text.toString() +
                        " Your Sleep is " + sleepTextView.text.toString() +
                        " Your Calorie Burn is " + textCalorieTextView.text.toString() +
                        " Your Query and Count Steps Are" + textCountStepNumber.text.toString() + " & " + queryCountStepNumber.text.toString()

            // Use the Kotlin extension in the fragment-ktx artifact
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        btn11!!.setOnClickListener {
            val userAllowTypesToRead = intArrayOf(
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_USER_PROFILE_INFORMATION,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_USER_PROFILE_FEATURE,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_POINT_STEP_SUM,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_SET_RUN_METADATA,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_SET_WEIGHT,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_REALTIME_HEARTRATE,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_REAL_TIME_SPORT,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_WRITE_DATA_SET_WEIGHT,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_POINT_CALORIES_SUM,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_SET_CORE_SLEEP,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_LAST_OXYGEN_SATURATION
            )
            val userAllowTypesToWrite =
                intArrayOf(HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_WRITE_DATA_SET_WEIGHT)
            HiHealthAuth.requestAuthorization(
                context, userAllowTypesToWrite, userAllowTypesToRead
            ) { resultCode, `object` ->
                Log.i(
                    TAG,
                    "requestAuthorization onResult:$resultCode"
                )
                if (resultCode == HiHealthError.SUCCESS) {
                    Log.i(
                        TAG,
                        "requestAuthorization success resultContent:$`object`"
                    )
                }

            }
        }

        qeuryImageView.setOnClickListener {


            qeuryImageView.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.step_query_move
                )
            )
            val timeout = 0
            val endTime = System.currentTimeMillis()
            val startTime = endTime - 1000 * 60 * 60 * 24 * 30L
            val hiHealthDataQuery = HiHealthDataQuery(
                HiHealthPointType.DATA_POINT_STEP_SUM,
                startTime, endTime, HiHealthDataQueryOption()
            )
            HiHealthDataStore.execQuery(
                context, hiHealthDataQuery, timeout
            ) { resultCode, data ->
                Log.i(
                    TAG,
                    "query steps resultCode: $resultCode"
                )
                var result = ""
                if (resultCode == HiHealthError.SUCCESS) {
                    val dataList: List<*> = data as ArrayList<*>
                    if (dataList.size >= 1) {
                        val pointData = dataList[dataList.size - 1] as HiHealthPointData
                        result = result + pointData.value.toString()
                    }
                }
                queryCountStepNumber.text = result


            }
        }

        stepCountImageView.setOnClickListener {

            stepCountImageView.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.count_steps_move
                )
            )

            val endTime = System.currentTimeMillis()
            val startTime = endTime - 1000L * 60 * 60 * 24 * 30L // Check Data of the latest 30 days
            val hiHealthDataQuery = HiHealthDataQuery(
                HiHealthPointType.DATA_POINT_STEP_SUM,
                startTime, endTime, HiHealthDataQueryOption()
            )
            HiHealthDataStore.getCount(
                context, hiHealthDataQuery
            ) { resultCode, data ->
                textCountStepNumber.text = data.toString()
                if (resultCode == HiHealthError.SUCCESS) {
                    val count = data as Int
                    Log.i(
                        TAG,
                        "walk track number: $count"
                    )
                }
            }


        }

        weightCountImageView.setOnClickListener {
            weightCountImageView.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.weight_animation
                )
            )

            HiHealthDataStore.getWeight(
                context
            ) { resultCode, weight ->
                weightCountTextView.text = weight.toString() + "KG"

                if (resultCode == HiHealthError.SUCCESS) {


                }
            }
        }

        calorieImageView.setOnClickListener {


            calorieImageView.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.calorie_animation
                )
            )


            val timeout = 0
            val endTime = System.currentTimeMillis()
            val startTime = endTime - 1000 * 60 * 60 * 24L
            val hiHealthDataQuery = HiHealthDataQuery(
                HiHealthPointType.DATA_POINT_CALORIES_SUM,
                startTime, endTime, HiHealthDataQueryOption()
            )
            HiHealthDataStore.execQuery(
                context, hiHealthDataQuery, timeout
            ) { resultCode, data ->
                Log.i(
                    TAG,
                    "query steps resultCode: $resultCode"
                )

                if (resultCode == HiHealthError.SUCCESS) {
                    val dataList: List<*> = data as ArrayList<*>
                    if (dataList.size >= 1) {
                        val pointData = dataList[dataList.size - 1] as HiHealthPointData

                        textCalorieTextView.text = pointData.value.toString() + "cal"
                    }
                }

            }


        }
        sleepImageView.setOnClickListener {


            sleepImageView.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.sleep_animation
                )
            )

            val timeout = 0

            // Set the start time and end time for the query.
            // Set the start time and end time for the query.
            val endTime = System.currentTimeMillis()
            val startTime = endTime - 86400000L * 30
            val hiHealthDataQuery = HiHealthDataQuery(
                HiHealthSetType.DATA_SET_CORE_SLEEP,
                startTime, endTime, HiHealthDataQueryOption()
            )
            HiHealthDataStore.execQuery(
                context,
                hiHealthDataQuery,
                timeout,
                object : ResultCallback {
                    val sb = StringBuilder()
                    override fun onResult(resultCode: Int, data: Any) {
                        if (resultCode == HiHealthError.SUCCESS) {
                            Log.i(TAG, "query not null,enter set data")
                            val dataList = data as List<*>
                            if (dataList.size >= 1) {
                                val hiHealthData =
                                    dataList[dataList.size - 1] as HiHealthSetData

                                val map = hiHealthData.map
                                sb.append("" + map[HiHealthSetType.CONTENT_SLEEP_WHOLE_DAY_AMOUNT])
                            }
                            sleepTextView.text = sb.toString() + "min"
                        }
                    }
                })
        }

        spo2ImageView.setOnClickListener {

            spo2ImageView.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.spotwoanimation
                )
            )

            val timeout = 0
            val endTime = System.currentTimeMillis()
            val startTime = endTime - 1000 * 60 * 60 * 24 * 30L
            val hiHealthDataQuery = HiHealthDataQuery(
                HiHealthPointType.DATA_POINT_LAST_OXYGEN_SATURATION,
                startTime, endTime, HiHealthDataQueryOption()
            )
            HiHealthDataStore.execQuery(
                context, hiHealthDataQuery, timeout
            ) { resultCode, data ->
                Log.i(
                    TAG,
                    "query steps resultCode: $resultCode"
                )
                var result = ""
                if (resultCode == HiHealthError.SUCCESS) {
                    val dataList: List<*> = data as ArrayList<*>
                    if (dataList.size >= 1) {
                        val pointData = dataList[dataList.size - 1] as HiHealthPointData
                        result = result + pointData.value.toString()
                    }
                }
                spo2TextView.text = result.toString()
            }

        }
        var heartCallback: HiRealTimeListener = object : HiRealTimeListener {
            override fun onResult(state: Int) {
                Log.i(TAG, " onResult state:$state")

            }

            override fun onChange(resultCode: Int, value: String) {
                val sb = StringBuilder()
                Log.i(
                    TAG,
                    " onChange resultCode: $resultCode value: $value"
                )
                if (resultCode == HiHealthError.SUCCESS) {
                    try {
                        val jsonObject = JSONObject(value)
                        sb.append("" + jsonObject.getInt("hr_info"))

                    } catch (e: JSONException) {
                        Log.e(TAG, "JSONException e" + e.message)
                    }
                }
                heartTextView.text = sb.toString() + "BPM"

            }
        }
        button_start.setOnClickListener {

            heartbeatImageView.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.heartbeat
                )
            )
            HiHealthDataStore.startReadingHeartRate(
                context,
                heartCallback
            )


        }
        button_stop.setOnClickListener {

            HiHealthDataStore.stopReadingHeartRate(
                context,
                heartCallback
            )
        }


        return view

    }


}
