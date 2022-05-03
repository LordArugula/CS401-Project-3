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

/**
 * Displays lists of the project users.
 */
public class ProjectUserAdapter extends RecyclerView.Adapter<ProjectUserAdapter.ViewHolder> {

    /**
     * The project id.
     */
    private String projectId;
    /**
     * The users in the project.
     */
    private List<ProjectUser> projectUsers;
    /**
     * User repository.
     */
    private UserRepository userRepository;

    /**
     * Creates the project user adapter.
     *
     * @param projectId    the project id.
     * @param projectUsers the project users.
     */
    public ProjectUserAdapter(String projectId, List<ProjectUser> projectUsers) {
        this.projectId = projectId;
        this.projectUsers = projectUsers;
        userRepository = new FirestoreUserRepository();
    }

    /**
     * Creates the ViewHolder.
     *
     * @param parent   the viewHolder parent.
     * @param viewType the type of view.
     * @return the created ViewHolder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_project_user, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the user to the ViewHolder.
     *
     * @param holder   the ViewHolder.
     * @param position the index of the user.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProjectUser projectUser = projectUsers.get(position);
        userRepository.getUser(projectUser.getUserId())
                .addOnSuccessListener(user -> {
                    holder.label_role.setText(projectUser.getRole());
                    holder.label_email.setText(projectUser.getEmail());

                    // Sets the remove button based on user's access level
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

    /**
     * Returns the number of items in the adapter's internal list.
     *
     * @return the number of items in the adapter's internal list.
     */
    @Override
    public int getItemCount() {
        return projectUsers.size();
    }

    /**
     * The Project user ViewHolder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The label for the user email address.
         */
        private final TextView label_email;
        /**
         * The label for the user role.
         */
        private final TextView label_role;
        /**
         * The remove button.
         */
        private final Button button_remove;

        /**
         * Creates the ViewHolder
         *
         * @param itemView the project user view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            label_email = itemView.findViewById(R.id.item_project_user_label_email);
            label_role = itemView.findViewById(R.id.item_project_user_label_role);
            button_remove = itemView.findViewById(R.id.item_project_user_button_remove);
        }
    }
}
