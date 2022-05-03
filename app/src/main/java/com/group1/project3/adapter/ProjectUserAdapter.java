package com.group1.project3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group1.project3.R;
import com.group1.project3.model.ProjectUser;
import com.group1.project3.model.Role;
import com.group1.project3.repository.FirestoreUserRepository;
import com.group1.project3.repository.UserRepository;

import java.util.List;

public class ProjectUserAdapter extends RecyclerView.Adapter<ProjectUserAdapter.ViewHolder> {

    private String projectId;
    private List<ProjectUser> projectUsers;
    private UserRepository userRepository;

    public ProjectUserAdapter(String projectId, List<ProjectUser> projectUsers) {
        this.projectId = projectId;
        this.projectUsers = projectUsers;
        userRepository = new FirestoreUserRepository();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_project_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProjectUser projectUser = projectUsers.get(position);
        userRepository.getUser(projectUser.getUserId())
                .addOnSuccessListener(user -> {
                    holder.label_role.setText(projectUser.getRole());
                    holder.label_email.setText(projectUser.getEmail());

                    if (Role.Admin.equals(Role.valueOf(projectUser.getRole()))) {
                        holder.button_remove.setVisibility(View.INVISIBLE);
                    } else {
                        holder.button_remove.setOnClickListener(view -> {
                            projectUsers.remove(position);
                            notifyItemRemoved(position);

                            user.removeProject(projectId);
                        });
                    }
                });
    }

    @Override
    public int getItemCount() {
        return projectUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView label_email;
        private final TextView label_role;
        private final Button button_remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            label_email = itemView.findViewById(R.id.item_project_user_label_email);
            label_role = itemView.findViewById(R.id.item_project_user_label_role);
            button_remove = itemView.findViewById(R.id.item_project_user_button_remove);
        }
    }
}
