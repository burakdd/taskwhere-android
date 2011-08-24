package com.taskwhere.android.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.taskwhere.android.adapter.TaskListDbAdapter;
import com.taskwhere.android.model.Task;

public class TaskWhereActivity extends Activity {
    
	private final static String TW = "TaskWhere";
	private ArrayList<Task> taskList;
	private ListView taskListView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main);
        setContentView(R.layout.main);
        taskList = new ArrayList<Task>();
        
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setHomeAction(new IntentAction(this, createIntent(this),R.drawable.home));
        
        final Action infoAction = new IntentAction(this, InfoActivity.createIntent(this), R.drawable.info);
        actionBar.addAction(infoAction);
        final Action addAction = new IntentAction(this, AddTaskActivity.createIntent(this), R.drawable.add_item);
        actionBar.addAction(addAction);
        
        showSavedProfiles();
        
        taskListView = (ListView) findViewById(R.id.taskList);
        taskListView.setAdapter(new TaskListAdapter(getApplicationContext(), R.layout.task_item, taskList));
    }
    
    public void showSavedProfiles(){
    	
    	TaskListDbAdapter dbAdapter = new TaskListDbAdapter(getApplicationContext());
    	dbAdapter.open();
    	
    	dbAdapter.insertNewTask(new Task("do app test", "here", 40.82345,29.454656,23424));
    	dbAdapter.insertNewTask(new Task("do app test2", "there", 20.83453636,19.453636,577878));
    	
    	Cursor taskCursor = dbAdapter.getAllTasks();
    	startManagingCursor(taskCursor);
    	
    	Log.d(TW, "Cursor count : " + taskCursor.getCount());
    	
    	if(taskCursor.getCount() > 0){
    		
    		taskCursor.moveToFirst();
    		do{
        		taskList.add(new Task(taskCursor.getString(0), taskCursor.getString(1),
        				taskCursor.getDouble(2), taskCursor.getDouble(3), taskCursor.getInt(4)));
        		taskCursor.moveToNext();
    		}while(taskCursor.moveToNext());
    	}
    }
    
    public static Intent createIntent(Context context) {
		
    	Intent i = new Intent(context, TaskWhereActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
    
    private class TaskListAdapter extends ArrayAdapter<Task>{

		public TaskListAdapter(Context context, int textViewResourceId,
				ArrayList<Task> objects) {
			super(context, textViewResourceId, objects);
			context = getContext();
			taskList = objects;
		}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		View v = convertView;
			
			if(v == null){
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.task_item, null);
			}
			
			Task task = taskList.get(position);
			
			if(task != null){
				
				TextView trackData = (TextView) v.findViewById(R.id.taskText);
				trackData.setText(task.getTaskText() + "-" + task.getTaskLoc());
			}
			
			return v;
    	}
    }
}
