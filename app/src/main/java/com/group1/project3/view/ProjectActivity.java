package com.group1.project3.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group1.project3.R;
import com.group1.project3.adapter.PipelineAdapter;
import com.group1.project3.model.Card;
import com.group1.project3.model.Pipeline;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivity extends AppCompatActivity {

    private TextView text_projectName;

    private RecyclerView pipelineRecyclerView;
    private PipelineAdapter pipelineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        text_projectName = findViewById(R.id.project_name);

        List<Pipeline> pipelines = new ArrayList<>();
        Pipeline backlogPipeline = new Pipeline("Backlog");
        backlogPipeline.addCard(new Card("122", "Hello"));
        backlogPipeline.addCard(new Card("123", "World"));
        pipelines.add(backlogPipeline);
        Pipeline bugsPipeline = new Pipeline("Bugs");
        bugsPipeline.addCard(new Card("124", "Bug A"));
        bugsPipeline.addCard(new Card("125", "Bug B"));
        pipelines.add(bugsPipeline);
        Pipeline inProgressPipeline = new Pipeline("In Progress");
        inProgressPipeline.addCard(new Card("126", "Finish this project\nI need to sleep."));
        pipelines.add(inProgressPipeline);
        Pipeline donePipeline = new Pipeline("Done");
        pipelines.add(donePipeline);
        pipelineAdapter = new PipelineAdapter(pipelines);

        pipelineRecyclerView = findViewById(R.id.project_pipeline_recyclerView);
        pipelineRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pipelineRecyclerView.setAdapter(pipelineAdapter);
    }
}