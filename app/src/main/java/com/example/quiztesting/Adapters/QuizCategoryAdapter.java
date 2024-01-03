package com.example.quiztesting.Adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiztesting.Models.QuestionModel;
import com.example.quiztesting.Models.QuizModel;
import com.example.quiztesting.Models.TaskStatus;
import com.example.quiztesting.Models.TaskToDo;
import com.example.quiztesting.QuizStudentDoQuizActivity;
import com.example.quiztesting.QuizStudentLeaderboardActivity;
import com.example.quiztesting.QuizStudentSetActivity;
import com.example.quiztesting.R;
import com.example.quiztesting.RecyViewInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizCategoryAdapter extends RecyclerView.Adapter<QuizCategoryAdapter.viewHolder> implements RecyQuizInterface {
    Context context;
    ArrayList<String> list;
    HashMap<String, ArrayList<QuizModel>>  quizModelsList;
    HashMap<String, QuizAdapter> quizAdapterList;
    FirebaseDatabase database;
    String uid;

    public QuizCategoryAdapter(Context context, ArrayList<String> list, String uid) {
        this.context = context;
        this.list = list;
        this.quizModelsList = new HashMap<>();
        this.quizAdapterList = new HashMap<>();
        this.database = FirebaseDatabase.getInstance();
        this.uid = uid;
    }

    public void addQuizModel(QuizModel quizModel, String ctg) {
        ArrayList<QuizModel> quizModels = new ArrayList<>();
        if(quizModelsList.get(ctg) != null) {
            quizModels = quizModelsList.get(ctg);
        }
        quizModels.add(quizModel);
        quizModelsList.put(ctg, quizModels);

        QuizAdapter adapter = quizAdapterList.get(ctg);
        if (adapter != null) {
            adapter.notifyItemInserted(quizModels.size()-1);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_category, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        String ctgKey = list.get(position);
        ArrayList<QuizModel> quizModels = new ArrayList<>();
        quizModelsList.put(ctgKey, quizModels);

        database.getReference().child("Categories").child(ctgKey).child("categoryName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.category.setText(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recyQuizToDo.setLayoutManager(layoutManager);
        QuizAdapter adapter = new QuizAdapter(this, context, quizModelsList.get(ctgKey));
        holder.recyQuizToDo.setAdapter(adapter);
        quizAdapterList.put(ctgKey, adapter);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onItemClick(int position, String ctgKey) {
        QuizModel quizModel = quizModelsList.get(ctgKey).get(position);

        ArrayList<QuestionModel> questions = new ArrayList<>();
        ArrayList<String> keyQuestionList = new ArrayList<>();

        DatabaseReference referenceSet = database.getReference().child("Categories").child(ctgKey).child("Sets").child(quizModel.getSetKey());

        referenceSet.child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long ttlQues = snapshot.getChildrenCount();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        QuestionModel questionModel = dataSnapshot.getValue(QuestionModel.class);
                        questions.add(questionModel);
                        keyQuestionList.add(questionModel.getKeyQuestion());

                        if (keyQuestionList.size() == ttlQues) {
                            referenceSet.child("setName").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String setName = snapshot.getValue(String.class);
                                        if (quizModel.getStatus().equals("In progress")) {
                                            Intent intent = new Intent(context, QuizStudentDoQuizActivity.class);
                                            intent.putExtra("uid", uid);
                                            intent.putExtra("setName", setName);
                                            intent.putExtra("keyCtg", ctgKey);
                                            intent.putExtra("keySet", quizModel.getSetKey());
                                            intent.putParcelableArrayListExtra("questions", questions);
                                            intent.putExtra("queIndex", quizModel.getProgress());
                                            context.startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(context, QuizStudentDoQuizActivity.class);
                                            intent.putExtra("uid", uid);
                                            intent.putExtra("setName", setName);
                                            intent.putExtra("keyCtg", ctgKey);
                                            intent.putExtra("keySet", quizModel.getSetKey());
                                            intent.putParcelableArrayListExtra("questions", questions);
                                            context.startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView category;
        RecyclerView recyQuizToDo;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.setQuizCategory);
            recyQuizToDo = itemView.findViewById(R.id.recyQuizSetToDo);
        }
    }
}
