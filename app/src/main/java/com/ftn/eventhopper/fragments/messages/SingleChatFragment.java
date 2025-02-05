package com.ftn.eventhopper.fragments.messages;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ChatMessagesAdapter;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.fragments.messages.viewmodels.SingleChatViewModel;
import com.ftn.eventhopper.fragments.reports.viewmodels.ReportsViewModel;
import com.ftn.eventhopper.shared.dtos.messages.ChatMessageDTO;
import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class SingleChatFragment extends Fragment {
    NavController navController;
    private static final String ARG_USERNAME = "username";
    private static final String ARG_NAME = "name";
    private static final String ARG_SURNAME = "surname";
    private static final String ARG_PROFILE_PICTURE = "profilePicture";

    private String username;
    private String name;
    private String surname;
    private String profilePicture;
    private SingleChatViewModel viewModel;

    private TextView receiverName;
    private TextView receiverUsername;
    private ImageView receiverProfilePicture;
    private RecyclerView messagesRecyclerView;
    private ImageButton sendMessageButton;
    private ImageButton blockUserButton;
    private ImageButton reportUserButton;
    private TextInputLayout messageInput;
    private boolean initializedHistory = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            name = getArguments().getString(ARG_NAME);
            surname = getArguments().getString(ARG_SURNAME);
            profilePicture = getArguments().getString(ARG_PROFILE_PICTURE);
        }

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewModel.disconnectChat();
                navController.popBackStack(R.id.single_chat_fragment, true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_chat, container, false);
        viewModel = new ViewModelProvider(this).get(SingleChatViewModel.class);
        viewModel.setReceiverUsername(username);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        
        receiverName = view.findViewById(R.id.name);
        receiverUsername = view.findViewById(R.id.username);
        receiverProfilePicture = view.findViewById(R.id.profile_picture);
        messagesRecyclerView = view.findViewById(R.id.chat_messages);
        sendMessageButton = view.findViewById(R.id.send_button);
        blockUserButton = view.findViewById(R.id.block_user_button);
        reportUserButton = view.findViewById(R.id.report_user_button);
        messageInput = view.findViewById(R.id.message_input_layout);

        blockUserButton.setOnClickListener(v -> {


        });

        reportUserButton.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_report_user, null);
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext());
            dialog.setTitle("Report user");
            dialog.setView(dialogView);
            dialog.setPositiveButton("Report", (dialogInterface, i) -> {
            });
            dialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            androidx.appcompat.app.AlertDialog alertDialog = dialog.create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                if (reportUser(dialogView)) {
                    alertDialog.dismiss();
                }
            });

        });


        viewModel.getHistory().observe(getViewLifecycleOwner(), messages -> {
            if (!initializedHistory) {
                initializedHistory = true;
                setHistory(messages);
            } else {
                Log.d("SingleChatFragment", "New message arrived");
                messageArrived(messages);
            }
        });

        sendMessageButton.setOnClickListener(v -> {
            String message = messageInput.getEditText().getText().toString().trim();
            if (message.isEmpty()) {
                return;
            } else if (message.length() > 255) {
                messageInput.setError("Message is too long");
                return;
            }

            messageInput.setError(null);
            messageInput.getEditText().setText(null);
            viewModel.sendMessage(message);
        });

        Objects.requireNonNull(messageInput.getEditText()).setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessageButton.performClick();
                return true;
            }
            return false;
        });

        viewModel.fetchHistory();

        setReceiverFields();

        viewModel.connectToChat();

        return view;
    }

    private boolean reportUser(View dialogView) {
        boolean isValid = true;

        TextInputLayout reasonLayout = dialogView.findViewById(R.id.reason_layout);
        String reason = reasonLayout != null ? reasonLayout.getEditText().getText().toString().trim() : "";


        if (reasonLayout != null && reason.isEmpty()) {
            reasonLayout.setError("Reason for reporting is required");
            isValid = false;
        } else {
            reasonLayout.setError(null);
        }

        if (!isValid) {
            return false;
        }

        viewModel.fetchReceiverByEmail(username);

        viewModel.getReceiverLiveData().observe((LifecycleOwner) dialogView.getContext(), receiver ->{

            if (receiver != null) {
                viewModel.createReport(reason, receiver);
            } else {
                Toast.makeText(dialogView.getContext(), "Failed to fetch user.", Toast.LENGTH_SHORT).show();
            }
        });


        return true;
    }

    private void messageArrived(ArrayList<ChatMessageDTO> messages) {
        ChatMessagesAdapter adapter = (ChatMessagesAdapter) messagesRecyclerView.getAdapter();
        if (adapter == null ) {
            setHistory(messages);
            return;
        }
        adapter.notifyItemInserted(messages.size() - 1);
        if (messages.get(messages.size() - 1).isSentByMe()) {
            messagesRecyclerView.post(() -> {
                messagesRecyclerView.smoothScrollToPosition(Objects.requireNonNull(messagesRecyclerView.getAdapter()).getItemCount() - 1);
            });
        }
    }


    private void setHistory(ArrayList<ChatMessageDTO> messages) {
        ChatMessagesAdapter adapter = new ChatMessagesAdapter(messages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        messagesRecyclerView.setLayoutManager(layoutManager);
        messagesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        messagesRecyclerView.setAdapter(adapter);
        if (!messages.isEmpty()) {
             messagesRecyclerView.scrollToPosition(messages.size() - 1);
        }
    }

    private void setReceiverFields() {
        receiverName.setText(String.format("%s %s", name, surname));
        receiverUsername.setText(username);
        if (profilePicture == null || profilePicture.isEmpty()) {
            receiverProfilePicture.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.profile_pic_temp));
        } else {
            Glide.with(this)
                    .load(String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, profilePicture))
                    .circleCrop()
                    .placeholder(R.drawable.baseline_image_placeholder_24)
                    .error(R.drawable.baseline_image_not_supported_24)
                    .into(receiverProfilePicture);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (initializedHistory) {
            initializedHistory = false;
            viewModel.fetchHistory();
        }
        viewModel.connectToChat();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.disconnectChat();
    }


}