package com.ftn.eventhopper.fragments.events;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.events.viewmodels.EventStatsViewModel;
import com.ftn.eventhopper.shared.dtos.events.GraphDataDTO;
import com.ftn.eventhopper.shared.dtos.events.RatingTimeSeriesDTO;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.itextpdf.kernel.pdf.PdfDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EventStatsFragment extends Fragment {

    private EventStatsViewModel viewModel;
    private LineChart lineChart;
    private PieChart pieChart;
    private BarChart barChart;
    private Button btnDownloadPdf;
    private LinearLayout statsContainer;

    private NavController navController;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.view_stats, true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_stats, container, false);
        navController = NavHostFragment.findNavController(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        lineChart = view.findViewById(R.id.lineChart);
        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);
        btnDownloadPdf = view.findViewById(R.id.btnDownloadPdf);
        statsContainer = view.findViewById(R.id.statsContainer);

        String eventId = getArguments().getString("eventId");
        viewModel = new ViewModelProvider(this).get(EventStatsViewModel.class);
        viewModel.fetchStats(eventId);

        viewModel.getStats().observe(getViewLifecycleOwner(), stats -> {
            if (stats != null) {
                showLineChart(stats.getRatings());
                showPieChart(stats);
                showBarChart(stats);
            }
        });

        btnDownloadPdf.setOnClickListener(v -> generatePdf());
    }

    private void showLineChart(List<RatingTimeSeriesDTO> ratings) {
        List<Entry> entries = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        for (int i = 0; i < ratings.size(); i++) {
            RatingTimeSeriesDTO r = ratings.get(i);
            entries.add(new Entry(i, (float) r.getAverageRating()));
            dates.add(String.valueOf(r.getTimestamp().toLocalDate())); // just date
        }

        LineDataSet dataSet = new LineDataSet(entries, "Average Rating");
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dates));
        lineChart.invalidate();
    }

    private void showPieChart(GraphDataDTO stats) {
        List<PieEntry> entries = new ArrayList<>();
        if (stats.getNumAcceptedInvitations() > 0)
            entries.add(new PieEntry(stats.getNumAcceptedInvitations(), "Accepted"));
        if (stats.getNumPendingInvitations() > 0)
            entries.add(new PieEntry(stats.getNumPendingInvitations(), "Pending"));
        if (stats.getNumDeclinesInvitations() > 0)
            entries.add(new PieEntry(stats.getNumDeclinesInvitations(), "Declined"));

        PieDataSet dataSet = new PieDataSet(entries, "Invitations");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void showBarChart(GraphDataDTO stats) {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, stats.getMaxAttendance()));
        entries.add(new BarEntry(1, stats.getNumAcceptedInvitations()));

        BarDataSet dataSet = new BarDataSet(entries, "Attendance");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(Arrays.asList("Max", "Accepted")));
        barChart.invalidate();
    }

    private void generatePdf() {

    }
}
