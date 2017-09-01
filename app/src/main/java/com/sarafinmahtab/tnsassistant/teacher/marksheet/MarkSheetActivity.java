package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sarafinmahtab.tnsassistant.MySingleton;
import com.sarafinmahtab.tnsassistant.R;
import com.sarafinmahtab.tnsassistant.ServerAddress;
import com.sarafinmahtab.tnsassistant.teacher.examsetup.CourseCustomize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkSheetActivity extends AppCompatActivity {

    private String examDataLoadUrl = ServerAddress.getMyServerAddress().concat("custom_exam_data_loader.php");
    private String avgFunctionsUrl = ServerAddress.getMyServerAddress().concat("avg_func_loader.php");
    private String markSheetLoader = ServerAddress.getMyServerAddress().concat("mark_sheet_loader.php");

    private CourseCustomize courseCustomize;

    String courseID, teacherID, courseCode;

    RecyclerView markSheetRecyclerView;
    MarkSheetAdapter markSheetAdapter;
    List<MarkListItem> stdMarkList;

    SearchView markSheetSearchView;
    EditText markSheetSearchEditText;
    ImageView markSheetCloseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_sheet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mark_sheet_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle courseBundle = getIntent().getExtras();

        courseID = courseBundle.getString("course_id");
        teacherID = courseBundle.getString("teacher_id");
        courseCode = courseBundle.getString("course_code");

        toolbar.setTitleTextColor(0xFFFFFFFF);

        getSupportActionBar().setTitle(courseCode + " Result Sheet");

        courseCustomize = new CourseCustomize();

        viewCustomCourseData();

        markSheetSearchView = (SearchView) findViewById(R.id.frag_searchView_mark_update);
        markSheetSearchEditText = (EditText) findViewById(R.id.search_src_text);
        markSheetCloseButton = (ImageView) findViewById(R.id.search_close_btn);

        markSheetRecyclerView = (RecyclerView) findViewById(R.id.frag_recyclerView_mark_update);
        markSheetRecyclerView.setHasFixedSize(true);
        markSheetRecyclerView.setLayoutManager(new LinearLayoutManager(MarkSheetActivity.this));
        stdMarkList = new ArrayList<>();

        StringRequest stringRequestForStdMarkList = new StringRequest(Request.Method.POST, markSheetLoader, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("mark_sheet_loader");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        MarkListItem markListItem = new MarkListItem();

                        markListItem.setCourseRegID(obj.getString("course_reg_id"));
                        markListItem.setRegNo(obj.getString("registration_no"));
                        markListItem.setMarkSheetID(obj.getString("marksheet_id"));
                        markListItem.setTermTest1_Mark(obj.getString("term_test_1"));
                        markListItem.setTermTest2_Mark(obj.getString("term_test_2"));
                        markListItem.setAttendanceMark(obj.getString("attendance"));
                        markListItem.setVivaMark(obj.getString("viva"));
                        markListItem.setFinalExamMark(obj.getString("final_exam"));
                        markListItem.setMarksOutOf100(obj.getString("marks_out_of_100"));

                        stdMarkList.add(markListItem);
                    }

                    markSheetAdapter = new MarkSheetAdapter(stdMarkList, MarkSheetActivity.this, courseCustomize);
                    markSheetRecyclerView.setAdapter(markSheetAdapter);

                } catch (JSONException e) {
                    Toast.makeText(MarkSheetActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MarkSheetActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("course_id", courseID);

                return params;
            }
        };

        MySingleton.getMyInstance(MarkSheetActivity.this).addToRequestQueue(stringRequestForStdMarkList);

        markSheetSearchView.onActionViewExpanded();
        markSheetSearchView.setIconified(false);
        markSheetSearchView.setQueryHint("Search by Reg ID or Name");

        if(!markSheetSearchView.isFocused()) {
            markSheetSearchView.clearFocus();
        }

        markSheetSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                markSheetAdapter.checkQueryFromList(newText.toLowerCase());

                return true;
            }
        });

        markSheetCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear the text from EditText view
                markSheetSearchEditText.setText("");

                //Clear query
                markSheetSearchView.setQuery("", false);
                markSheetAdapter.notifyDataSetChanged();
                markSheetSearchView.clearFocus();
            }
        });
    }

    private void viewCustomCourseData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, examDataLoadUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("custom_exam_data_loader");
                    JSONObject obj = jsonArray.getJSONObject(0);

                    courseCustomize.setCustomTT1Name(obj.getString("custom_test1_name"));
                    courseCustomize.setCustomTT1Percent(obj.getString("custom_test1_percent"));

                    courseCustomize.setCustomTT2Name(obj.getString("custom_test2_name"));
                    courseCustomize.setCustomTT2Percent(obj.getString("custom_test2_percent"));

                    courseCustomize.setCustomAttendanceName(obj.getString("custom_attendance_name"));
                    courseCustomize.setCustomAttendancePercent(obj.getString("custom_attendance_percent"));

                    courseCustomize.setCustomVivaName(obj.getString("custom_viva_name"));
                    courseCustomize.setCustomVivaPercent(obj.getString("custom_viva_percent"));

                    courseCustomize.setCustomFinalName(obj.getString("custom_final_name"));
                    courseCustomize.setCustomFinalPercent(obj.getString("custom_final_percent"));

                } catch (JSONException e) {
                    Toast.makeText(MarkSheetActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MarkSheetActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("course_id", courseID);

                return params;
            }
        };

        MySingleton.getMyInstance(MarkSheetActivity.this).addToRequestQueue(stringRequest);

        StringRequest stringRequestForAvgFunc = new StringRequest(Request.Method.POST, avgFunctionsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("avg_func_loader");
                    JSONObject obj = jsonArray.getJSONObject(0);

                    boolean[] checkedAvgArray = new boolean[5];

                    checkedAvgArray[0] = obj.getString("custom_test1_avg_check").equals("1");
                    checkedAvgArray[1] = obj.getString("custom_test2_avg_check").equals("1");
                    checkedAvgArray[2] = obj.getString("custom_attendance_avg_check").equals("1");
                    checkedAvgArray[3] = obj.getString("custom_viva_avg_check").equals("1");
                    checkedAvgArray[4] = obj.getString("custom_final_avg_check").equals("1");

                    courseCustomize.setCheckedAvgArray(checkedAvgArray);

                } catch (JSONException e) {
                    Toast.makeText(MarkSheetActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MarkSheetActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("course_id", courseID);

                return params;
            }
        };

        MySingleton.getMyInstance(MarkSheetActivity.this).addToRequestQueue(stringRequestForAvgFunc);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewCustomCourseData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}