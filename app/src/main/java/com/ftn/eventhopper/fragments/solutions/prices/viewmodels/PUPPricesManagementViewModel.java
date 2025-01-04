package com.ftn.eventhopper.fragments.solutions.prices.viewmodels;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.prices.PriceManagementDTO;
import com.ftn.eventhopper.shared.dtos.prices.UpdatePriceDTO;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PUPPricesManagementViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<PriceManagementDTO>> pricesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<PriceManagementDTO>> getPrices() {
        return pricesLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchPrices() {
        Call<ArrayList<PriceManagementDTO>> call = ClientUtils.productService.getPricesForManagement();
        call.enqueue(new Callback<ArrayList<PriceManagementDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<PriceManagementDTO>> call, Response<ArrayList<PriceManagementDTO>> response) {
                if (response.isSuccessful()) {
                    pricesLiveData.postValue(response.body());
                    errorMessage.postValue(null);

                } else {
                    errorMessage.postValue("Failed to fetch prices for management. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PriceManagementDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void editPrice(UUID productId, double basePrice, double discount) {
        UpdatePriceDTO updatePriceDTO = new UpdatePriceDTO();
        updatePriceDTO.setBasePrice(basePrice);
        updatePriceDTO.setDiscount(discount);

        Call<Void> call = ClientUtils.productService.updateProductsPrice(productId, updatePriceDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchPrices();
                    errorMessage.postValue(null);
                } else {
                    errorMessage.postValue("Failed to edit price. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void exportPricesToPDF(Context context) {
        createPdf(context);
    }

    private void createPdf(Context context) {
        try {
            String fileName = "Prices.pdf";

            // Create the PDF using MediaStore
            Uri pdfUri = savePdfToMediaStore(fileName, context);

            if (pdfUri != null) {
                // Open an OutputStream using the URI
                OutputStream outputStream = context.getContentResolver().openOutputStream(pdfUri);

                // Initialize PdfWriter and PdfDocument
                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument);

                // Create a table with 3 columns
                float[] columnWidths = {100f, 200f, 200f, 200f, 200f};
                Table table = new Table(columnWidths);

                Color headerColor = new DeviceRgb(229, 249, 255);
                Color headerTextColor = new DeviceRgb(0, 0, 0);

                // Add header cells
                Cell headerNo = new Cell()
                        .add(new Paragraph("No."))
                        .setBackgroundColor(headerColor)
                        .setFontColor(headerTextColor)
                        .setTextAlignment(TextAlignment.LEFT);
                Cell headerProductName = new Cell()
                        .add(new Paragraph("Product"))
                        .setBackgroundColor(headerColor)
                        .setFontColor(headerTextColor)
                        .setTextAlignment(TextAlignment.CENTER);
                Cell headerBasePrice = new Cell()
                        .add(new Paragraph("Base Price"))
                        .setBackgroundColor(headerColor)
                        .setFontColor(headerTextColor)
                        .setTextAlignment(TextAlignment.CENTER);
                Cell headerDiscount = new Cell()
                        .add(new Paragraph("Discount"))
                        .setBackgroundColor(headerColor)
                        .setFontColor(headerTextColor)
                        .setTextAlignment(TextAlignment.CENTER);
                Cell headerFinalPrice = new Cell()
                        .add(new Paragraph("Final Price"))
                        .setBackgroundColor(headerColor)
                        .setFontColor(headerTextColor)
                        .setTextAlignment(TextAlignment.CENTER);

                table.addHeaderCell(headerNo);
                table.addHeaderCell(headerProductName);
                table.addHeaderCell(headerBasePrice);
                table.addHeaderCell(headerDiscount);
                table.addHeaderCell(headerFinalPrice);

                // Add rows dynamically
                ArrayList<String[]> rows = new ArrayList<>();
                for (int i = 0; i < pricesLiveData.getValue().size(); i++) {
                    PriceManagementDTO price = pricesLiveData.getValue().get(i);
                    String[] row = {
                            String.format("%d.", i + 1),
                            price.getProductName(),
                            String.format("%.2f€", price.getBasePrice()),
                            String.format("%.2f%%", price.getDiscount()),
                            String.format("%.2f€", price.getFinalPrice())
                    };
                    rows.add(row);
                }

                for (String[] row : rows) {
                    table.addCell(new Paragraph(row[0]));
                    table.addCell(new Paragraph(row[1]));
                    table.addCell(new Paragraph(row[2]));
                    table.addCell(new Paragraph(row[3]));
                    table.addCell(new Paragraph(row[4]));
                }

                // Add table to the document
                document.add(table);

                // Close the document
                document.close();

                // Present to the user
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(pdfUri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            } else {
                errorMessage.postValue("Failed to create PDF file.");
            }

        } catch (Exception e) {
            errorMessage.postValue("Failed to create PDF file.");
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
            errorMessage.postValue("Failed to create PDF file.");

        }

        return pdfUri;
    }
}
