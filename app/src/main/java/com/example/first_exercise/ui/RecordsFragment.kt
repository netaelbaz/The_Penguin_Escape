package com.example.first_exercise.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import com.example.first_exercise.R
import com.example.first_exercise.logic.RecordsManager
import com.google.android.material.textview.MaterialTextView

class RecordsFragment : Fragment() {

    private lateinit var scoreRows: List<TableRow>
    private lateinit var scoreText: List<MaterialTextView>
    private var recordsManager = RecordsManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(R.layout.fragment_records, container, false)
        findViews(view)
        initViews(view)
        return view
    }

    private fun findViews(view: View) {
        scoreRows = listOf(
            view.findViewById(R.id.records_ROW_row1),
            view.findViewById(R.id.records_ROW_row2),
            view.findViewById(R.id.records_ROW_row3),
            view.findViewById(R.id.records_ROW_row4),
            view.findViewById(R.id.records_ROW_row5),
            view.findViewById(R.id.records_ROW_row6),
            view.findViewById(R.id.records_ROW_row7),
            view.findViewById(R.id.records_ROW_row8),
            view.findViewById(R.id.records_ROW_row9),
            view.findViewById(R.id.records_ROW_row10)
        )
        scoreText = listOf(
            view.findViewById(R.id.records_LBL_score1),
            view.findViewById(R.id.records_LBL_score2),
            view.findViewById(R.id.records_LBL_score3),
            view.findViewById(R.id.records_LBL_score4),
            view.findViewById(R.id.records_LBL_score5),
            view.findViewById(R.id.records_LBL_score6),
            view.findViewById(R.id.records_LBL_score7),
            view.findViewById(R.id.records_LBL_score8),
            view.findViewById(R.id.records_LBL_score9),
            view.findViewById(R.id.records_LBL_score10)
        )
    }

    private fun initViews(view: View) {
        val topScores = recordsManager.loadScores().topScores
        for (i in scoreRows.indices) {
            val row = scoreRows[i]
            if (i < topScores.size) {
                row.visibility = View.VISIBLE
                scoreText[i].text = topScores[i].score.toString()
            }
            else {
                row.visibility = View.INVISIBLE
            }
        }

    }
}