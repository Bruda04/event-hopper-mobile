package com.ftn.eventhopper.fragments.events.viewmodels;

import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.events.CreateAgendaActivityDTO;
import com.ftn.eventhopper.shared.dtos.events.GetEventAgendasDTO;
import com.ftn.eventhopper.shared.dtos.events.SinglePageEventDTO;
import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;
import com.ftn.eventhopper.shared.models.events.EventPrivacyType;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.BorderRadius;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPageViewModel extends ViewModel {
    @Getter
    private final MutableLiveData<SinglePageEventDTO> eventLiveData = new MutableLiveData<>();

    @Getter
    @Setter
    private boolean isFavorited;

    public void loadEventById(String eventId) {
        UUID id = UUID.fromString(eventId);

        ClientUtils.eventService.getEvent(id).enqueue(new Callback<SinglePageEventDTO>() {
            @Override
            public void onResponse(Call<SinglePageEventDTO> call, Response<SinglePageEventDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventLiveData.setValue(response.body());
                    setFavorited(response.body().isFavorite());
                }
            }
            @Override
            public void onFailure(Call<SinglePageEventDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public LiveData<SinglePageEventDTO> getEvent() {
        return eventLiveData;
    }

    public void toggleFavorite() {
        SinglePageEventDTO currentEvent = eventLiveData.getValue();
        isFavorited = !isFavorited;
        if (currentEvent != null) {
            currentEvent.setFavorite(!currentEvent.isFavorite());
            eventLiveData.setValue(currentEvent);

            if(currentEvent.isFavorite()){
                Call<Void> call = ClientUtils.profileService.addEventToFavorites(currentEvent.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("Adding favorite event", "SUCCESS");
                        } else {
                            Log.d("Adding favorite event", "FAILURE");
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("Adding favorite event", "ERROR");
                    }
                });
            }else{
                Call<Void> call = ClientUtils.profileService.removeEventFromFavorites(currentEvent.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("Removing favorite event", "SUCCESS");
                        } else {
                            Log.d("Removing favorite event", "FAILURE");
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("Removing favorite event", "ERROR");
                    }
                });
            }

        }
    }

    public void generateGuestList(Context context){
        SinglePageEventDTO currentEvent = eventLiveData.getValue();
        Call<ArrayList<SimpleAccountDTO>> call = ClientUtils.eventService.getGuestList(currentEvent.getId());
        call.enqueue(new Callback<ArrayList<SimpleAccountDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<SimpleAccountDTO>> call, Response<ArrayList<SimpleAccountDTO>> response) {
                if (response.isSuccessful()) {
                    Log.d("Fetching guest list", "SUCCESS");
                    generateGuestListPdf(response.body(), context);
                } else {
                    Log.d("Fetching guest list", "FAILURE");
                }
            }
            @Override
            public void onFailure(Call<ArrayList<SimpleAccountDTO>> call, Throwable t) {
                Log.d("Fetching guest list", "ERROR");
            }
        });
    }

    private void generateGuestListPdf(ArrayList<SimpleAccountDTO> accounts, Context context) {
        try {
            SinglePageEventDTO eventDetails = eventLiveData.getValue();
            String fileName = eventDetails.getName() + "_Guest_List.pdf";

            // Save using MediaStore (your working logic)
            Uri pdfUri = savePdfToMediaStore(fileName, context);
            if (pdfUri == null) {
                Log.e("PDF", "Failed to create PDF URI.");
                return;
            }

            OutputStream outputStream = context.getContentResolver().openOutputStream(pdfUri);
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);

            float pageWidth = pdfDoc.getDefaultPageSize().getWidth();

            // HEADER BAR (Dark Blue)
            Rectangle header = new Rectangle(0, pdfDoc.getDefaultPageSize().getTop() - 50, pageWidth, 50);
            // Force first page creation by adding an invisible element
            doc.add(new Paragraph(" ").setFontSize(1));

            // Now it's safe to draw on the first page
            PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
            canvas.setFillColor(new DeviceRgb(41, 128, 185))
                    .rectangle(header)
                    .fill();

            doc.add(new Paragraph("Guest List")
                    .setFontSize(20)
                    .setFontColor(ColorConstants.WHITE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(0, pdfDoc.getDefaultPageSize().getTop() - 35, pageWidth));

            doc.add(new Paragraph("\n\n\n")); // spacing under header

            // EVENT INFO CARD
            Div eventInfo = new Div()
                    .setBackgroundColor(new DeviceRgb(240, 240, 240))
                    .setPadding(10)
                    .setMarginBottom(20)
                    .setBorderRadius(new BorderRadius(5));

            eventInfo.add(new Paragraph("Event: " + eventDetails.getName()).setFontSize(13));
            eventInfo.add(new Paragraph("Description:")
                    .setBold().setFontSize(11));
            eventInfo.add(new Paragraph(eventDetails.getDescription())
                    .setFontSize(11).setFontColor(new DeviceRgb(80, 80, 80)));

            eventInfo.add(new Paragraph("Date & Time: " + eventDetails.getTime().toString()).setFontSize(11));
            eventInfo.add(new Paragraph("Location: " + eventDetails.getLocation().getAddress()
                    + ", " + eventDetails.getLocation().getCity()).setFontSize(11));

            doc.add(eventInfo);

            // ATTENDEES TITLE
            doc.add(new Paragraph("Attendees")
                    .setFontSize(14)
                    .setFontColor(new DeviceRgb(41, 128, 185)));

            // LINE BELOW TITLE
            canvas.setStrokeColor(new DeviceRgb(200, 200, 200))
                    .moveTo(36, pdfDoc.getDefaultPageSize().getTop() - 270)
                    .lineTo(pageWidth - 36, pdfDoc.getDefaultPageSize().getTop() - 270)
                    .stroke();

            doc.add(new Paragraph("\n"));

            if (accounts.isEmpty()) {
                doc.add(new Paragraph("No guests found.")
                        .setFontSize(12)
                        .setFontColor(ColorConstants.GRAY));
            } else {
                for (int i = 0; i < accounts.size(); i++) {
                    SimpleAccountDTO account = accounts.get(i);
                    String fullName = account.getPerson().getName() + " " + account.getPerson().getSurname();

                    Div card = new Div()
                            .setBackgroundColor(new DeviceRgb(248, 248, 248))
                            .setBorderRadius(new BorderRadius(5))
                            .setPadding(8)
                            .setMarginBottom(10);

                    card.add(new Paragraph(fullName)
                            .setFontSize(11)
                            .setBold());

                    card.add(new Paragraph(account.getEmail())
                            .setFontSize(10)
                            .setFontColor(new DeviceRgb(100, 100, 100)));

                    doc.add(card);
                }
            }

            // FOOTER
            doc.add(new Paragraph("Generated by EventHopper")
                    .setFontSize(10)
                    .setFontColor(new DeviceRgb(150, 150, 150))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30));

            // Close and Open (your original working logic)
            doc.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);

        } catch (Exception e) {
            Log.e("PDF", "Error generating guest list PDF", e);
        }
    }

    public void exportToPDF(Context context){
        SinglePageEventDTO currentEvent = eventLiveData.getValue();
        Call<GetEventAgendasDTO> call = ClientUtils.eventService.getAgendaForEvent(currentEvent.getId());
        call.enqueue(new Callback<GetEventAgendasDTO>() {
            @Override
            public void onResponse(Call<GetEventAgendasDTO> call, Response<GetEventAgendasDTO> response) {
                if (response.isSuccessful()) {
                    Log.d("Fetching agenda", "SUCCESS");
                    generatePdf(response.body(), context);
                } else {
                    Log.d("Fetching agenda", "FAILURE");
                }
            }
            @Override
            public void onFailure(Call<GetEventAgendasDTO> call, Throwable t) {
                Log.d("Fetching agenda", "ERROR");
            }
        });
    }

    private void generatePdf(GetEventAgendasDTO eventAgendasDTO, Context context){
        try {
            SinglePageEventDTO eventDetails = eventLiveData.getValue();

            String fileName = eventDetails.getName() + ".pdf";

            // Save the PDF using MediaStore
            Uri pdfUri = savePdfToMediaStore(fileName, context);
            if (pdfUri == null) {
                Log.e("PDF", "Failed to create PDF URI.");
                return;
            }

            // Open OutputStream using the URI
            OutputStream outputStream = context.getContentResolver().openOutputStream(pdfUri);
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Title
            document.add(new Paragraph(eventDetails.getName())
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(new DeviceRgb(0, 51, 102))); // Dark Blue

            // Description
            document.add(new Paragraph(eventDetails.getDescription())
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.LEFT));

            // Line Separator
            document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(10));

            // Location & Time
            document.add(new Paragraph("Location: " + eventDetails.getLocation().getAddress() + ", " + eventDetails.getLocation().getCity())
                    .setFontSize(14)
                    .setBold()
                    .setFontColor(new DeviceRgb(7, 59, 76)));

            LocalDateTime eventTimeValue = eventDetails.getTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            document.add(new Paragraph("Time: " + eventTimeValue.format(formatter))
                    .setFontSize(12));

            // Privacy
            document.add(new Paragraph("Privacy: " +
                    (eventDetails.getPrivacy() == EventPrivacyType.PUBLIC ? "Public" :
                            eventDetails.getPrivacy() == EventPrivacyType.PRIVATE ? "Private" : "Unknown"))
                    .setFontSize(12)
                    .setBold());

            // Sort Agendas by Start Time
            List<CreateAgendaActivityDTO> sortedAgendas = eventAgendasDTO.getAgendas();
            Collections.sort(sortedAgendas, Comparator.comparing(CreateAgendaActivityDTO::getStartTime));

            // Agenda Table
            float[] columnWidths = {100f, 200f, 200f, 200f};
            Table table = new Table(columnWidths);

            // Header Styling
            Color headerColor = new DeviceRgb(229, 249, 255);
            Color headerTextColor = new DeviceRgb(0, 0, 0);

            table.addHeaderCell(new Cell().add(new Paragraph("Time"))
                    .setBackgroundColor(headerColor)
                    .setFontColor(headerTextColor)
                    .setTextAlignment(TextAlignment.LEFT));
            table.addHeaderCell(new Cell().add(new Paragraph("Activity Name"))
                    .setBackgroundColor(headerColor)
                    .setFontColor(headerTextColor)
                    .setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Description"))
                    .setBackgroundColor(headerColor)
                    .setFontColor(headerTextColor)
                    .setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Location"))
                    .setBackgroundColor(headerColor)
                    .setFontColor(headerTextColor)
                    .setTextAlignment(TextAlignment.CENTER));

            // Add agenda items
            for (CreateAgendaActivityDTO agenda : sortedAgendas) {
                String startTime = formatTime(agenda.getStartTime());
                String endTime = formatTime(agenda.getEndTime());
                table.addCell(new Paragraph(startTime + " - " + endTime));
                table.addCell(new Paragraph(agenda.getName()));
                table.addCell(new Paragraph(agenda.getDescription()));
                table.addCell(new Paragraph(agenda.getLocationName()));
            }

            if(!sortedAgendas.isEmpty()){
                document.add(table);
            }

            // Footer
            document.add(new Paragraph("Generated by EventHopper")
                    .setFontSize(10)
                    .setFontColor(new DeviceRgb(150, 150, 150))
                    .setTextAlignment(TextAlignment.CENTER));

            // Close Document
            document.close();

            // Open the PDF after generation
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);

        } catch (Exception e) {
            Log.e("PDF", "Error generating PDF", e);
        }

    }

    private Uri savePdfToMediaStore(String fileName, Context context) {
        Uri pdfUri = null;

        try {
            // Define content values for the MediaStore entry
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");

            // Save to the Documents directory
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);
            }

            // Insert the content values into MediaStore
            pdfUri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);

        } catch (Exception e) {
            Log.d("Saving pdf", "ERROR");
        }

        return pdfUri;
    }

    private String formatTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(formatter);
    }

}
