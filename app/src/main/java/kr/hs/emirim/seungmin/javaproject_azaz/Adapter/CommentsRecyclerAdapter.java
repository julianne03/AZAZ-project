package kr.hs.emirim.seungmin.javaproject_azaz.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import kr.hs.emirim.seungmin.javaproject_azaz.Model.Comments;
import kr.hs.emirim.seungmin.javaproject_azaz.Model.User;
import kr.hs.emirim.seungmin.javaproject_azaz.R;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    public List<Comments> commentsList;
    public Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String review_id;

    public CommentsRecyclerAdapter(List<Comments> commentsList, String review_id) {
        this.commentsList = commentsList;
        this.review_id = review_id;

    }

    @NonNull
    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentsRecyclerAdapter.ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        String commentMessage = commentsList.get(position).getMessage();
        final String comment_user_id = commentsList.get(position).getUser_id();
        final String comment_id = commentsList.get(position).getComment_id();

        holder.setComment_message(commentMessage);

        final String current_user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Users").document(comment_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        final String name = task.getResult().getString("name");
                        final String image = task.getResult().getString("image");

                        holder.setUserContext(name, image);

                    }
                }
            }
        });

        if (current_user_id.equals(comment_user_id)) {
            holder.comment_delete_btn.setVisibility(View.VISIBLE);
        }

        holder.comment_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("댓글 삭제").setMessage("정말 댓글을 삭제하시겠습니까?");

                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (current_user_id.equals(comment_user_id)) {
                            firebaseFirestore.collection("Reviews/" + review_id + "/Comments").document(comment_id).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // notifyDataSetChanged();
                                            commentsList.remove(position);
                                            notifyItemRemoved(position);
                                            notifyDataSetChanged();

                                            Toast.makeText(context, "댓글이 삭제되었습니다!", Toast.LENGTH_LONG).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "자신이 작성한 댓글만 삭제됩니다!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                });

                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                final AlertDialog alertDialog = builder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                    }
                });
                alertDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {

        if (commentsList!=null) {
            return commentsList.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView comment_message;

        private TextView comment_user_name;
        private CircularImageView comment_user_image;

        private ImageView comment_delete_btn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            comment_delete_btn = mView.findViewById(R.id.comment_delete_btn);
        }

        public void setComment_message(String message) {
            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }

        public void setUserContext(String username, String downloadUri) {

            comment_user_name = mView.findViewById(R.id.comment_username);
            comment_user_image = mView.findViewById(R.id.comment_image);

            comment_user_name.setText(username);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.profile);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail().into(comment_user_image);
        }
    }
}
